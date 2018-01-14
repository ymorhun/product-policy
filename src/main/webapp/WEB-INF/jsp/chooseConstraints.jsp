<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
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
        <header class="site-header push">Введіть об&#39;єми матеріалів доступні для виробництва</header>
        <div id="container">
            <!-- Menu Button -->
            <button class="menu-btn">&#9776; Menu</button>
            <form action="/production/constraints" method="POST">
                <input type="hidden" name="policyId" value="${policyId}"></input>
                <div class="constraint_names">
                    <c:forEach items="${materials}" var="material">
                        <p>
                            <input type="checkbox" name="material" value="${material.id}" >${material.name}, ${material.units}</input>
                            <input type="text" id="${material.id}" disabled name="${material.id}" value="0"></input>
                        </p>
                    </c:forEach>
                </div>
                <input type="submit">
            </form>
        </div>
        <script src="/js/menu.min.js"></script>
        <script>
            var checkboxes = document.getElementsByName("material");
            checkboxes.forEach(function(item, i, arr) {
                item.onchange = function() {
                    if(item.checked) {
                        var textInputId = item.value;
                        var textInput = document.getElementById(textInputId);
                        textInput.disabled=0;
                    }
                    if(!item.checked) {
                        var textInputId = item.value;
                        var textInput = document.getElementById(textInputId);
                        textInput.disabled=1;
                    }
                }
            });
        </script>
    </body>
</html>