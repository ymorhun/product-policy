<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=utf-8" %>
<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/css/menu.css">
        <link rel="stylesheet" type="text/css" href="/css/alternative.css">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script src="/js/menu.js" type="text/javascript"></script>
        <title>Система розробки Товарної Політики</title>
    </head>
    <body>
        <%@include file="menu.jsp" %>

        <header class="site-header push">Нові дослідження товарної політики</header>
        <div id="container">
            <!-- Menu Button -->
            <button class="menu-btn">&#9776; Menu</button>
            <form action="/hierarchy/compare" method="POST">
                <input type="hidden" name="productPolicyId" value="${productPolicyId}"></input>
                <input type="hidden" name="criteriaId" value="${criteria.id}"></input>
                <c:forEach begin="0" end="${fn:length(alternatives) - 1}" var="mainAlternativeIndex">
                    <c:forEach begin="${mainAlternativeIndex + 1}" end="${fn:length(alternatives) - 1}" var="secondAlternativeIndex">
                        <div class="comparison">
                            <h3>Оцініть наскільки альтернатива <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span> краща у порівнянні з альтернативою <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span> для виконання криетрію <b>${criteria.title}</b></h3>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="9">Дуже сильна перевага альтернативи <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="8">Висока перевага альтернативи <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="7">Значна перевага альтернативи <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="6">Істотна перевага альтернативи <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="5">Середня перевага альтернативи <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="4">Достатня перевага альтернативи <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="3">Помірна перевага альтернативи <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="2">Слабка перевага альтернативи <span class="alternative-name">${alternatives.get(mainAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="1" checked>Альтернативи рівні</input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="0.11111">Дуже сильна перевага альтернативи <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="0.125">Висока перевага альтернативи <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="0.14286">Значна перевага альтернативи <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="0.16667">Істотна перевага альтернативи <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="0.2">Середня перевага альтернативи <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="0.25">Достатня перевага альтернативи <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="0.33333">Помірна перевага альтернативи <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span></input><br/>
                            <input type="radio" name="${alternatives.get(mainAlternativeIndex).id}To${alternatives.get(secondAlternativeIndex).id}" value="0.5">Слабка перевага альтернативи <span class="alternative-name">${alternatives.get(secondAlternativeIndex).title}</span></input><br/>
                        </div>
                    </c:forEach>
                </c:forEach>
                <input type="submit"></input>
            </form>
        </div>
        <script src="/js/menu.min.js"></script>
    </body>
</html>