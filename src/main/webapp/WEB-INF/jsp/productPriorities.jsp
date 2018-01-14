<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html;charset=utf-8" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/menu.css">
        <link rel="stylesheet" type="text/css" href="/css/priorities.css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="/js/menu.js" type="text/javascript"></script>
        <title>Система розробки Товарної Політики</title>
    </head>
    <body>
        <%@include file="menu.jsp" %>
        <header class="site-header push">Глобальні пріоритети продуктів</header>
        <div id="container">
            <!-- Menu Button -->
            <button class="menu-btn">&#9776; Menu</button>
            <form action="/production/assortment/${policyId}/save" method="POST">
                <c:forEach items="${productPriorities}" var="product">
                    <div class="product-priority">
                        <div class="product">
                            <input type="checkbox" name="productId" value="${product.productId}">${product.name}</input>
                        </div>
                        <b><fmt:formatNumber pattern="#0.0000" value="${product.priority}" /></b><br/>
                    </div>
                </c:forEach>
                <input type="submit" value="Обрати"></input>
            </form>
        </div>
        <script src="/js/menu.min.js"></script>
    </body>
</html>