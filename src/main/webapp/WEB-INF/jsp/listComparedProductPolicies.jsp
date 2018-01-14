<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=utf-8" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/list.css">
        <link rel="stylesheet" type="text/css" href="/css/menu.css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="/js/menu.js" type="text/javascript"></script>
        <title>Система розробки Товарної Політики</title>
    </head>
    <body>
        <%@include file="menu.jsp" %>
        <header class="site-header push">Перелік досліджень, для яких виконані парні порівняння</header>
        <div id="container">
            <!-- Menu Button -->
            <button class="menu-btn">&#9776; Menu</button>
            <c:forEach items="${productPolicies}" var="policy">
                <div class="policy-item">
                <a href="/hierarchy/assortment/${policy.id}">${policy.name}</a>
                </div>
            </c:forEach>
        </div>
        <script src="/js/menu.min.js"></script>
    </body>
</html>