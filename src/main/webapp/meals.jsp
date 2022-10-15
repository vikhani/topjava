<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
      table, th, td {
        padding: 5px;
        border: 1px solid black;
        border-collapse: collapse;
      }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meal</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <c:if test = "${meal.isExcess()}">
            <tr>
                <td><font color="green">${meal.getDateTimeView()}</font></td>
                <td><font color="green">${meal.getDescription()}</font></td>
                <td><font color="green">${meal.getCalories()}</font></td>
            </tr>
        </c:if>
        <c:if test = "${!meal.isExcess()}">
            <tr>
                <td><font color="red">${meal.getDateTimeView()}</font></td>
                <td><font color="red">${meal.getDescription()}</font></td>
                <td><font color="red">${meal.getCalories()}</font></td>
            </tr>
        </c:if>
    </c:forEach>
</table>
</body>
</html>