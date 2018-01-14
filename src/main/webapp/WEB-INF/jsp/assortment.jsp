<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
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
            <p><b>Створено: </b>${createdBy}</p>
            <p><b>Інформація про товарну політику: </b>${productPolicy.description}</p>
            <p><b>Завдання створено: </b> <fmt:formatDate type="date" pattern="dd-MM-yyyy" value="${productPolicy.initiated}" /></p>
            <p>Товарна політика створюється на ${productPolicy.duration} років </p>

            <p>Оберіть кількість рівнів критеріїв в ієрархії
            <select id="levelsSelect" onchange="return changeHierarchyBlock(this)">
                <option value="1">1</option>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
                <option value="5">5</option>
            </select>
            </p>
            <form action="/hierarchy/assortment" method="POST">
                <input type="hidden" name="productPolicyId" value="${productPolicy.productPolicyId}"></input>
                <input type="hidden" id="levels" name="levels"></input>
                <div class="hierarchy_block" id="hierarchy_block">
                </div>
                <select id="experts" name="experts" multiple>
                    <c:forEach items="${experts}" var="expert">
                    <option value="${expert.id}">${expert.name}</option>
                    </c:forEach>
                </select>
                <p>
                    <button type="submit">Створити</button>
                </p>
            </form>
        </div>
        <script src="/js/menu.min.js"></script>
        <script>
            $(window, document, undefined).ready(function() {
                var changeHierarchySelect = document.getElementById("levelsSelect");
                changeHierarchyBlock(changeHierarchySelect);
            });
            function changeHierarchyBlock(selectElement) {
                var hierarchy_block = document.getElementById("hierarchy_block");
                hierarchy_block.innerHTML = '';
                var countOfLevels = selectElement.value;
                document.getElementById("levels").value=countOfLevels;
                for(var level = 1; level <=countOfLevels; level++) {
                    var hierarchyLevelDivElement = document.createElement("DIV");
                    hierarchyLevelDivElement.className='hierarchy_level_block';
                    var addCriteriaButton = document.createElement("BUTTON");
                    addCriteriaButton.addEventListener("click", function(){
                        var level = this.value;
                        var input = document.createElement("INPUT");
                        input.type = 'text';
                        input.name = 'level_' + level;
                        input.classList.add("hierarchy_element");
                        var container = this.parentElement
                        container.appendChild(input);
                    });
                    addCriteriaButton.type = "button";
                    addCriteriaButton.value = level;
                    addCriteriaButton.innerHTML = "Додати критерій";
                    hierarchyLevelDivElement.appendChild(addCriteriaButton);
                    hierarchy_block.appendChild(hierarchyLevelDivElement);
                }
            }
        </script>
    </body>
</html>