<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html;charset=utf-8" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/menu.css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="/js/menu.js" type="text/javascript"></script>
        <title>Система розробки Товарної Політики</title>
    </head>
    <body>
        <%@include file="menu.jsp" %>
        <header class="site-header push">Постановка оптимізаційної задачі</header>
        <div id="container">
            <!-- Menu Button -->
            <button class="menu-btn">&#9776; Menu</button>
            <form action="/production/submit/${policyId}" method="POST">
                <div class="equation">
                    L(X) =
                    <c:forEach begin="0" end="${fn:length(productPrices) - 1}" var="i">
                        <c:if test="${productPrices.get(i).getPrice() != 0}">
                            &nbsp;<fmt:formatNumber value="${productPrices.get(i).getPrice()}" pattern="+#0.000;-#0.000" /> * x<sub>${i+1}</sub>
                        </c:if>
                    </c:forEach>
                    &nbsp;&rarr; max
                </div>
                <div class="constraints">
                    <c:forEach items="${expenses}" var="expense">
                        <div>
                            <c:forEach begin="0" end="${fn:length(productPrices) - 1}" var="i">
                                <c:set var="multiplier" value="${expense.getProductIdToExpenses().getOrDefault(productPrices.get(i).getProductId(), 0)}" />
                                <c:if test="${multiplier != 0}">
                                    &nbsp;<fmt:formatNumber value="${multiplier}" pattern="+#0.000;-#0.000" /> * x<sub>${i+1}</sub>
                                </c:if>
                            </c:forEach>
                            &nbsp;&le;&nbsp;${expense.reserve}
                        </div>
                    </c:forEach>
                </div>
                <div>
                    x<sub>i</sub>&nbsp;&ge;&nbsp;0&nbsp;i=1...${fn:length(productPrices)}
                </div>
                <input type="submit" value="Рахувати"></input>
            </form>
        </div>
        <script src="/js/menu.min.js"></script>
    </body>
</html>