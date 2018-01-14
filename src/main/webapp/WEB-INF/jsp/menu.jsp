<%@page contentType="text/html;charset=utf-8" %>
<nav class="pushy pushy-left">
    <div class="pushy-content">
        <ul>
            <c:forEach var="role" items="${user.roles}">
                <c:if test="${role eq 'OPR'}">
                    <li class="pushy-link">
                        <a href="/research/create">Створити завдання</a>
                    </li>
                    <li class="pushy-link">
                        <a href="/research/list">Переглянути дослідження</a>
                    </li>
                </c:if>
                <c:if test="${role eq 'OFR'}">
                    <li class="pushy-link">
                        <a href="/research/list/new">Нові дослідження</a>
                    </li>
                    <li class="pushy-link">
                        <a href="/hierarchy/comparison/done">Визначення об&#39;ємів виробництва</a>
                    </li>
                </c:if>
                <c:if test="${role eq 'EXPERT'}">
                    <li class="pushy-link">
                        <a href="/hierarchy/comparison">Парні порівняння</a>
                    </li>
                </c:if>
                <li class="pushy-link">
                    <a href="/logout">Вийти</a>
                </li>
            </c:forEach>
        </ul>
    </div>
</nav>