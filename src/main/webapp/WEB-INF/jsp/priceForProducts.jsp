<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=utf-8" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/menu.css">
        <link rel="stylesheet" type="text/css" href="/css/priceForProducts.css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="/js/menu.js" type="text/javascript"></script>
        <title>Система розробки Товарної Політики</title>
    </head>
    <body>
        <%@include file="menu.jsp" %>
        <header class="site-header push">Введіть ціни на обрані товари</header>
        <div id="container">
            <!-- Menu Button -->
            <button class="menu-btn">&#9776; Menu</button>
            <form action="/production/assortment/price" method="POST">
                <input type="hidden" name="policyId" value="${policyId}"></input>
                <c:forEach items="${products}" var="product">
                    <div class="product-prices">
                        <div class="product-name">${product.title}</div>
                        <input type="text" name="product_${product.id}"></input>
                        грн/шт.
                    </div>
                </c:forEach>
                <input type="Submit" value="Зберегти"></input>
            </form>
            <script src="/js/menu.min.js"></script>
    </body>
</html>