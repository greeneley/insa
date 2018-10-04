var n = 0;
var m = 1;

var competences = ["Football", "Handball", "Natation",
                   "Violon", "Piano", "Chant",
                   "Programmation", "Design", "Rapidite"
];

function addCompetenceRow(){
    showFormCompetences();

    var row = document.createElement("tr");
    var select = createOptions("competences");
    var niveau = createInputNiveau();
    var commentaire = createInputCommentaire();

    row.appendChild(select);
    row.appendChild(niveau);
    row.appendChild(commentaire);
    document.getElementById("table-competence").appendChild(row);

    updateN();
}

function addProjetRow(){
    var td = createOptions("projet");

    document.getElementById("div-projet").appendChild(document.createElement("br"));
    document.getElementById("div-projet").appendChild(td.firstChild);

    updateM();
}

function updateN(){
    n++;
}

function updateM(){
    m++;
}

function createOptions(typeForm) {
    var select = document.createElement("select");
    competences.forEach(function(competence){
        var option = document.createElement("option");
        option.setAttribute("value", competence);
        option.innerText = competence;
        select.appendChild(option);
    });

    if(typeForm === "competences")
    {
        select.setAttribute("name", "list["+n+"].competence.intitule");
    }
    else if (typeForm === "projet")
    {
        select.setAttribute("name", "competencesRequises["+m+"].intitule");
    }

    var td = document.createElement("td");
    td.appendChild(select);
    return td;
}

function createInputNiveau(){
    var niveau = document.createElement("input");
    niveau.setAttribute("name", "list["+n+"].niveau");
    niveau.setAttribute("type", "number");
    niveau.setAttribute("value", "0");

    var td = document.createElement("td");
    td.appendChild(niveau);
    return td;
}

function createInputCommentaire(){
    var commentaire = document.createElement("input");
    commentaire.setAttribute("name", "list["+n+"].commentaire");
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