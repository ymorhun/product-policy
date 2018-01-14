<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=utf-8" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/materialsPerProduct.css">
        <link rel="stylesheet" type="text/css" href="/css/menu.css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="/js/menu.js" type="text/javascript"></script>
        <title>Система розробки Товарної Політики</title>
    </head>
    <body>
        <%@include file="menu.jsp" %>
        <header class="site-header push">Введіть об&#39;єми матеріалів необхідні для виробництва товарів</header>
        <div id="container">
            <!-- Menu Button -->
            <button class="menu-btn">&#9776; Menu</button>
            <form action="/production/expenses" method="POST">
                <input type="hidden" name="policyId" value="${policyId}"></input>
                <div class="table_header">
                    <div class="product_column_header"></div>
                    <c:forEach items="${products}" var="product">
                        <div class="material_column_header">${product.title}</div>
                    </c:forEach>
                </div>
                <c:forEach items="${materialsToProductsMap.entrySet()}" var="entry">
                    <div class="table_content">
                        <div class="product_cell">${entry.key.title}</div>
                        <c:forEach items="${entry.value}" var="product">
                            <div class="material_input">
                                <input type="text" name="${product.id}To${entry.key.id}" value="0"></input>
                            </div>
                        </c:forEach>
                        &nbsp;(${entry.key.unit.title})
                    </div>
                </c:forEach>
                <input type="submit"></input>
            </form>
        </div>
        <script src="/js/menu.min.js"></script>
    </body>
</html>