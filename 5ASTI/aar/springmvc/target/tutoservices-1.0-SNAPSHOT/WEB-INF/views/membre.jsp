<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ikau
  Date: 26/09/2018
  Time: 22:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Menu</title>
    <meta charset="utf-8"/>
</head>
<body>
    <form action="/logout" method="get">
        <button type="submit">Déconnexion</button>
    </form>
    <h1>Bienvenue à toi ${courant.getSurnom()} !</h1>
    <hr/>


    <h1>Mes compétences</h1>
    <button onclick="addCompetenceRow()">Ajouter une compétence</button>
    <form id="form-competences" style="display:none;" action="/member/updateCompetence" method="post">
        <table id="table-competence">
            <tr>
                <th>Intitule competence</th>
                <th>Niveau</th>
                <th>Commentaire</th>
            </tr>
        </table>
        <button type="submit">Mettre à jour les compétences</button>
    </form>
    <hr/>

    <table>
        <tr>
            <th>Intitule</th>
            <th>Niveau</th>
            <th>Commentaires</th>
        </tr>

        <c:forEach items="${courant.getCompetences()}" var="comp">
            <tr>
                <td>${comp.getCompetence().getIntitule()}</td>
                <td>${comp.getNiveau()}</td>
                <td>${comp.getCommentaire()}</td>
            </tr>
        </c:forEach>
    </table>


    <h1>Liste des projets</h1>
    <button onclick="showFormProjet();">Ajouter un projet</button>
    <form id="form-projet" style="display:none;" action="/member/addProjet" method="post">
        <div id="div-projet">
            <br/>Intitule projet : <input type="text" name="intitule"/>
            <br/>Description : <input type="text" name="description"/>
            <br/>Responsable :
            <select name="responsable.login">
                <c:forEach items="${membres}" var="m">
                    <option value="${m.getLogin()}">${m.getLogin()}</option>
                </c:forEach>
            </select>
            <br/><button type="button" onclick="addProjetRow();">+</button>
            <select name="competencesRequises[0].intitule">
                <c:forEach items="${competences}" var="c">
                    <option value="${c.getIntitule()}">${c.getIntitule()}</option>
                </c:forEach>
            </select>
        </div>
        <button type="submit">Ajouter un projet</button>
    </form>
    <hr/>


    <h2>Projet responsable</h2>
    <table>
        <tr>
            <th>Intitule</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${projets}" var="proj">
            <c:if test="${courant.isResponsable(proj)}">
                <tr>
                    <td>${proj.getIntitule()}</td>
                    <td>${proj.getDescription()}</td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
    <hr/>


    <h2>Projet participant</h2>
    <table>
        <tr>
            <th>Intitule</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${projets}" var="proj">
            <c:if test="${proj.hasParticipant(courant)}">
                <tr>
                    <td>${proj.getIntitule()}</td>
                    <td>${proj.getDescription()}</td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
    <hr/>


    <h2>Projets compatibles</h2>
    <hr/>
    <table>
        <tr>
            <th>Intitule</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${projets}" var="proj">
            <c:if test="${proj.isCompatible(courant)
                          && !courant.isResponsable(proj)
                          && !proj.hasParticipant(courant)}">
                <tr>
                    <td>${proj.getIntitule()}</td>
                    <td>${proj.getDescription()}</td>
                </tr>
            </c:if>
        </c:forEach>
    </table>


    <h2>Autres projets existants</h2>
    <table>
        <tr>
            <th>Intitule</th>
            <th>Description</th>
        </tr>

        <c:forEach items="${projets}" var="proj">
            <c:if test="${!(courant.isResponsable(proj)
                            || proj.hasParticipant(courant)
                            || proj.isCompatible(courant))}" >
                <tr>
                    <td>${proj.getIntitule()}</td>
                    <td>${proj.getDescription()}</td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
</body>
</html>

<script src="<c:url value="/resources/js/membre.js"/>"></script>