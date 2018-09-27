<%--
  jsp avec scriptlet, old style
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ma Réponse</title>
</head>
<body>
<% boolean reponse= (boolean)request.getAttribute("resp");
    if (reponse==true) { %>
D'accord
<% } else { %>
Hors de question
<% } %>
</body>
</html>
