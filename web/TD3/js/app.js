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
	
}