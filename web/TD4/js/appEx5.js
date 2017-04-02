function selection(e)
{
	var vide = document.getElementsByClassName("case vide")[0];
	if(e.target.style.left==vide.style.left)
	{
		var diff = parseInt(e.target.style.top, 10) - parseInt(vide.style.top, 10);
		if(Math.abs(diff) == 102)
		{
			var left = e.target.style.left;
			var top  = e.target.style.top;		
			e.target.style.left = vide.style.left;
			e.target.style.top  = vide.style.top;
			vide.style.left = left;
			vide.style.top  = top;
		}
	}
	else if(e.target.style.top==vide.style.top)
	{
		var diff = parseInt(e.target.style.left, 10) - parseInt(vide.style.left, 10);
		if(Math.abs(diff) == 102)
		{
			var left = e.target.style.left;
			var top  = e.target.style.top;		
			e.target.style.left = vide.style.left;
			e.target.style.top  = vide.style.top;
			vide.style.left = left;
			vide.style.top  = top;
		}
	}
}

function init()
{
	var listeDiv = document.getElementsByClassName("case");
	for(var i=0; i<listeDiv.length; i++)
	{
		listeDiv[i].addEventListener("click", selection);
	}
}