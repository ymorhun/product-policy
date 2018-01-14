<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=utf-8" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/assortment.css">
    <link rel="stylesheet" type="text/css" href="/css/menu.css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script src="/js/menu.js" type="text/javascript"></script>
<title>Система розробки Товарної Політики</title>
</head>
<body>
<%@include file="menu.jsp" %>
    <header class="site-header push">${productPolicy.name}</header>
    <div id="container">
        <!-- Menu Button -->
        <button class="menu-btn">&#9776; Menu</button>
        <script>
        function addElementOnLevel(button, name) {
            var container = button.parentElement
            var input = document.createElement("INPUT");
            input.type = 'text';
            input.name = 'criteria_' + name;
            input.classList.add('hierarchy_element');
            container.appendChild(input);
        }
        </script>
        <form action="/hierarchy/assortment/products" method="POST">
            <input type="hidden" name="productPolicyId" value="${productPolicyId}"></input>
            <c:forEach items="${productGroups}" var="group">
                Продукти для критерія ${group.title}
                <div class="hierarchy_level_block">
                    <button type="button" onClick="return addElementOnLevel(this, ${group.id})">Додати продукт</button>
                </div>
            </c:forEach>
            <button type="submit">Створити</button>
        </form>
    </div>
<script src="/js/menu.min.js"></script>
</body>
</html>