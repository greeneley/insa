function defTitre1()
{
	var objetH1 = document.getElementById("titre");
	document.title = objetH1.textContent;
}

function defTitre2()
{
	listeH2 = document.getElementsByTagName("h2");
	document.title = listeH2[0].textContent;
}

function defTitre3()
{
	listeH3 = document.getElementsByTagName("h2");
	var dernier = listeH3.length - 1;
	if(dernier >= 0)
	{
		document.title = listeH3[dernier].textContent;
	}
	else
	{
		document.title = "votre nom et prénom";
	}
	
}

function defTitre4()
{
	listeH4 = document.getElementsByClassName("firstOrLast");
	taille = listeH4.length;
	if( taille == 0 )
	{
		document.title = "votre nom et prénom";
	}
	else if( taille % 2 ) 
	{
		document.title = listeH4[taille - 1].textContent;
	}
	else // sinon pair
	{
		document.title = listeH4[0].textContent;
	}
}

function inverseTexte()
{
	//Echange le contenu des deux balises <p>
	var listeP          = document.getElementsByTagName("p");
	var content1        = listeP[0].innerHTML;
	listeP[0].innerHTML = listeP[1].innerHTML;
	listeP[1].innerHTML = content1;
}

function datemodif()
{
	var listeMeta = document.getElementsByTagName("meta");
	var auteur    = "";

	for(var i=0; i<listeMeta.length; i++)
	{
		if(listeMeta[i].getAttribute("name") == "author")
		{
			auteur = listeMeta[i].getAttribute("content");
			break;
		}
	}

	var maDate = new Date(document.lastModified);
	document.getElementById("date_modif").innerHTML = 
	"Dernière modification : le " + maDate.toLocaleString() + " par " + auteur;
}

function majNbJours()
{
	var datePresent = new Date();
	var dateDoc     = new Date(2015, 7, 19);

	var joursPresent = datePresent.getTime();
	var joursDoc     = dateDoc.getTime();
	var joursRestants = (joursDoc - joursPresent) / 1000 / 60 / 60 / 24;
	document.getElementById("tpsRestant").innerHTML = joursRestants;
}

function majHorloge1() //setinterval
{
	setInterval(function(){
		var date  = new Date();
		var heure = "setInterval : "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
		document.getElementById("horloge1").innerHTML = heure;
	}, 1000);
}

function majHorloge2()
{
	var date  = new Date();
	var heure = "setTimeout : "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	document.getElementById("horloge2").innerHTML = heure;

	setTimeout(majHorloge2, 1000);
}

function changeCouleur()
{
	var champs = document.getElementById("champs");
	if(champs.value == "")
	{
		champs.className = "blanc";
	}
	else if(isNaN(champs.value))
	{
		champs.className = "rouge";
	}
	else
	{
		champs.className = "vert";
	}
}

//hh:mm:ss
defTitre1();
defTitre2();
defTitre3();
defTitre4();
inverseTexte();
datemodif();
majNbJours();
majHorloge1();
majHorloge2();
document.getElementById("champs").addEventListener("input", changeCouleur, false);