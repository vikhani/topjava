const mealAjaxUrl = "ui/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    filter: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            dataType: 'json',
            data: $('#filter').serialize(),
            //success: function (d){ ctx.datatableApi.clear().rows.add(d).draw(); }
            success: function (d){console.log(d);}
        });
    }
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "render": deleteButton,
                     "defaultContent": "",
                     "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});