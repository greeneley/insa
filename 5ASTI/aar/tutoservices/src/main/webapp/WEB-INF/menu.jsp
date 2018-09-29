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
    <h1>Bienvenue à toi ${sessionScope.courant.getSurnom()} !</h1>
    <hr/>


    <h1>Mes compétences</h1>
    <button onclick="addCompetenceRow()">Ajouter une compétence</button>
    <form id="form-competences" style="display:none;" action="/" method="post">
        <input hidden name="TODO" value="actualiseCompetence"/>
        <input id="input-number-competence" hidden name="numberOptions" value="0"/>
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

        <c:forEach items="${sessionScope.courant.getCompetences()}" var="comp">
            <tr>
                <td>${comp.getCompetence().getIntitule()}</td>
                <td>${comp.getNiveau()}</td>
                <td>${comp.getCommentaire()}</td>
            </tr>
        </c:forEach>
    </table>


    <h1>Liste des projets</h1>
    <button onclick="showFormProjet();">Ajouter un projet</button>
    <form id="form-projet" style="display:none;" action="/" method="post">
        <input hidden name="TODO" value="actualiseProjets"/>
        <input id="input-number-projet" hidden name="numberOptions" value="1"/>
        <div id="div-projet">
            <br/>Intitule projet : <input type="text" name="intitule-projet"/>
            <br/>Description : <input type="text" name="description-projet"/>
            <br/>Responsable :
            <select name="responsable">
                <c:forEach items="${sessionScope.membres}" var="m">
                    <option value="${m.getLogin()}">${m.getLogin()}</option>
                </c:forEach>
            </select>
            <br/><button type="button" onclick="addProjetRow();">+</button>
            <select name="select0">
                <c:forEach items="${sessionScope.competences}" var="c">
                    <option value="${c.getIntitule()}">${c.getIntitule()}</option>
                </c:forEach>
            </select>
        </div>
        <button type="submit">Mettre à jour les compétences</button>
    </form>
    <hr/>


    <h2>Projet responsable</h2>
    <table>
        <tr>
            <th>Intitule</th>
            <th>Description</th>
        </tr>
        <c:forEach items="${sessionScope.projets}" var="proj">
            <c:if test="${sessionScope.courant.isResponsable(proj)}">
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
        <c:forEach items="${sessionScope.projets}" var="proj">
            <c:if test="${proj.hasParticipant(sessionScope.courant)}">
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
        <c:forEach items="${sessionScope.projets}" var="proj">
            <c:if test="${proj.isCompatible(sessionScope.courant)
                          && !sessionScope.courant.isResponsable(proj)
                          && !sessionScope.projet.hasParticipant(sessionScope.courant)}">
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

        <c:forEach items="${sessionScope.projets}" var="proj">
            <c:if test="${!(sessionScope.courant.isResponsable(proj)
                            || proj.hasParticipant(sessionScope.courant)
                            || proj.isCompatible(sessionScope.courant))}" >
                <tr>
                    <td>${proj.getIntitule()}</td>
                    <td>${proj.getDescription()}</td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
</body>
</html>

<script type="text/javascript">
    var n = 0;
    var m = 1;

    var competences = ["Football", "Handball", "Natation",
                       "Violon", "Piano", "Chant",
                       "Programmation", "Design", "Rapidite"
    ];

    function addCompetenceRow(){
        showFormCompetences();

        var row = document.createElement("tr");
        var select = createOptions(n);
        var niveau = createInputNiveau();
        var commentaire = createInputCommentaire();

        row.appendChild(select);
        row.appendChild(niveau);
        row.appendChild(commentaire);
        document.getElementById("table-competence").appendChild(row);

        updateN();
    }

    function addProjetRow(){
        var td = createOptions(m);

        document.getElementById("div-projet").appendChild(document.createElement("br"));
        document.getElementById("div-projet").appendChild(td.firstChild);

        updateM();
    }

    function updateN(){
        document.getElementById("input-number-competence").setAttribute("value", n);
        n++;
    }

    function updateM(){
        document.getElementById("input-number-projet").setAttribute("value", m);
        m++;
    }

    function createOptions(valeur) {
        var select = document.createElement("select");
        competences.forEach(function(competence){
            var option = document.createElement("option");
            option.setAttribute("value", competence);
            option.innerText = competence;
            select.appendChild(option);
        });
        select.setAttribute("name", "select"+valeur);

        var td = document.createElement("td");
        td.appendChild(select);
        return td;
    }

    function createInputNiveau(){
        var niveau = document.createElement("input");
        niveau.setAttribute("name", "niveau"+n);
        niveau.setAttribute("type", "number");
        niveau.setAttribute("value", "0");

        var td = document.createElement("td");
        td.appendChild(niveau);
        return td;
    }

    function createInputCommentaire(){
        var commentaire = document.createElement("input");
        commentaire.setAttribute("name", "commentaire"+n);
        commentaire.setAttribute("type", "text");
        commentaire.setAttribute("value", "");

        var td = document.createElement("td");
        td.appendChild(commentaire);
        return td;
    }

    function showFormCompetences(){
        document.getElementById("form-competences").setAttribute("style","display: inline;");
    }

    function showFormProjet(){
        document.getElementById("form-projet").setAttribute("style","display: inline");
    }
</script>