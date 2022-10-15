<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
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
<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <c:choose>
        <c:when test="${meal.isExcess()}">
            <c:set var = "colour" value = "red"/>
        </c:when>
        <c:otherwise>
            <c:set var = "colour" value = "green"/>
        </c:otherwise>
        </c:choose>
        <tr style="color:${colour}">
            <td>${meal.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>