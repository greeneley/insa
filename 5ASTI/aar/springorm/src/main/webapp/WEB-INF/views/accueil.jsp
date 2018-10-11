<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <h1>Formulaire de connexion</h1>

    <form:form action="/member" method="post" modelAttribute="memberAccueil">
        <form:label path="login"/>
        Login: <form:input type="text" path="login"/>
        <form:errors path="login" cssStyle="color:red;"/>

        <form:label path="motdepasse"/>
        <br />Mot de passe: <form:input type="password" path="motdepasse"/>
        <form:errors path="motdepasse" cssStyle="color:red;"/>

        <br /><form:errors cssStyle="color:red;"/>
        <br /><input type="submit" value="Se connecter"/>
    </form:form>
    <!--
    <hr />
    <h1>Formulaire d'inscription</h1>
    <form action="/signup" method="post">
        Login: <input type="text" name="login">
        <br/>Mot de passe: <input type="password" name="motdepasse"/>
        Confirmer le mot de passe <input type="password" name="confirmation"/>
    </form>
    -->

</body>
</html>
