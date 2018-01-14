<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
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
    <div id="container">
        <!-- Menu Button -->
        <button class="menu-btn">&#9776; Menu</button>

        <div class="enter_form">
            <form:form method="POST" commandName="createProductPolicyDto" action="/research/create">
                <ul class="form-style-1">
                    <li><form:label path="name">Ім&#39;я</form:label><form:input path="name" class="field-long"/></li>
                    <li>
                        <form:label path="description">Загальна інформація</form:label>
                        <form:input path="description" class="field-long"/>
                    </li>
                    <li>
                        <form:label path="duration">Тривалість нової товарної політики, роки</form:label>
                        <form:input path="duration" class="field-long" type="number"/>
                    </li>
                    <li>
                        <form:label path="personCreatesDecisionId">Особа, що формує рішення</form:label>
                        <form:select path="personCreatesDecisionId" items="${personsCreateDecision}" itemLabel="name" itemValue="id" class="field-select"/>
                    </li>
                    <li>
                        <input type="submit" value="Створити"/>
                    </li>
                </ul>
            </form:form>
        </div>
    </div>
    <script src="/js/menu.min.js"></script>
</body>
</html>