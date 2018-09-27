<%--
  Created by IntelliJ IDEA.
  User: ikau
  Date: 26/09/2018
  Time: 22:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Accueil</title>
</head>
<body>
    <p>Formulaire de connexion</p>
    <c:if test="${not empty(error) }">
        <p style="color:red">${error}</p>
    </c:if>

    <form action="/" method="post">
        <input hidden name="TODO" value="selogguer">
        Login: <input type="text" name="login" type="text">
        <br />
        Mot de passe: <input type="text" name="mdp" type="text">
        <br />
        <input type="submit" value="Se connecter">
    </form>
</body>
</html>
