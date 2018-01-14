<!DOCTYPE html>
<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<%@page contentType="text/html;charset=utf-8" %>
<html>
   <head>
      <link rel="stylesheet" type="text/css" href="/css/login.css">
      <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
      <script src="/js/login.js" type="text/javascript"></script>
      <title>Система розробки Товарної Політики</title>
   </head>

   <body>
      <h1>Авторизуйтесь в системі розробки Товарної Політики</h1>
      <form:form method = "POST" commandName="user" action = "/login">
           <div class="group">
               <form:input path = "login"/>
               <form:label path = "login">Логін</form:label>
               <span class="highlight"/>
               <span class="bar"/>
           </div>
           <div class="group">
               <form:password path = "password"/>
               <form:label path = "password">Пароль</form:label>
               <span class="highlight"/>
               <span class="bar"/>
           </div>
           <input type = "submit" class="button buttonBlue" value = "Авторизуватися"/>
      </form:form>
   </body>

</html>