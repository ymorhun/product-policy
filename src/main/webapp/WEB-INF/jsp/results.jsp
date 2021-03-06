<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=utf-8" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="/css/menu.css">
    <link rel="stylesheet" type="text/css" href="/css/form.css">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
    <script src="/js/menu.js" type="text/javascript"></script>
<title>Система розробки Товарної Політики</title>
</head>
<body>
    <%@include file="menu.jsp" %>
     <header class="site-header push">Об&#39;єми виробництва продуктів товарної політики ${productPolicy.name}</header>
    <div id="container">
        <!-- Menu Button -->
        <button class="menu-btn">&#9776; Menu</button>
        <c:forEach items="${materialVolumes}" var="materialVolume">
            <div class="volumes">
                ${materialVolume.id.product.title} : ${materialVolume.volume}
            </div>
        </c:forEach>
    </div>
    <script src="/js/menu.min.js"></script>
</body>
</html>