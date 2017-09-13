/* Exercice 1 */

function somme()
{
    var s       = 0;
    var nombres = document.getElementsByName('nombre' ) ;
    for (var i=0; i<nombres.length; i++)
    {
        if (nombres[i].checked )
        {
            s =s + parseInt (nombres[i].value );
        }
    }
    alert("la somme vaut "+s) ;
}

function moyenne()
{
    var listVal = document.getElementsByName("nombre");
    var count = 0;
    var avg   = 0;
    for(var i=0; i<listVal.length; i++)
    {
        if(listVal[i].checked)
        {
            avg += parseInt(listVal[i].value);
            count++;
        }
    }
    if(count>0)
    {
        avg /= count;
        document.getElementById("resultMoy").innerHTML = "La moyenne est "+avg;
    }
    else
    {
        document.getElementById("resultMoy").innerHTML = "La moyenne est 0";
    }
}

function creer()
{
    var nombre = parseInt(document.getElementById("combien").value);
    if(nombre>0)
    {
        var div = document.getElementById("zoneCreer");
        while (div.firstChild) 
        {
            div.removeChild(div.firstChild);
        }

        for(var i=1; i<(nombre+1); i++)
        {
            var labelCheck       = document.createElement("label");
            labelCheck.innerHTML = String(i)+' ';

            var checkbox         = document.createElement("input");
            checkbox.type        = "checkbox";
            checkbox.className   = "checkCreer";
            checkbox.value       = String(i);

            div.appendChild(checkbox);
            div.appendChild(labelCheck);
        }

        var paraSum        = document.createElement("p");

        var boutonSum      = document.createElement("input");
        boutonSum.type     = "button";
        boutonSum.value    = "Faire la somme des checkbox crées";
        boutonSum.setAttribute("onclick", "sommeCheckboxes();");

        var labelSum       = document.createElement("label");
        labelSum.id        = "sommeCheck"
        labelSum.innerHTML = "";

        paraSum.appendChild(boutonSum);
        paraSum.appendChild(labelSum);
        div.appendChild(paraSum);
    }
}

function sommeCheckboxes()
{
    var listVal = document.getElementsByClassName("checkCreer");
    var sum = 0;
    for(var i=0; i<listVal.length; i++)
    {
        if(listVal[i].checked)
        {
            sum += parseInt(listVal[i].value);
        }
    }
    if(sum>0)
    {
        document.getElementById("sommeCheck").innerHTML = " La somme est "+sum;
    }
    else
    {
        document.getElementById("sommeCheck").innerHTML = " La somme est 0";
    }    
}




/* Exercice 2 */

function inf() {
    var res  = "",
    at, el, nd, all_attr;
    nd       = document.getElementById('ma_div'); //node
    all_attr = nd.attributes;

    for (i = 0; i != all_attr.length; i++) 
    {
        at   = all_attr[i];
        res += 'ATTR: ' + at.nodeName + ' = "' + at.nodeValue + '"\n';
    }

    var enfants = nd.childNodes;
    for (i = 0; i != enfants.length; i++) 
    {
        el = enfants[i];
        res += el.nodeName;
        if (el.nodeType == 3)
        {
            res += ' = "' + el.nodeValue + '"\n';
        }
        else
        {
            res += "--\n";
        }
    }

    resnode = document.createElement('div');
    body = document.getElementsByTagName('body')[0];
    body.appendChild(resnode);
    titlenode = document.createElement('h1');
    titlenode.appendChild(document.createTextNode('Ceci et le contenu de la div'));
    contentnode = document.createElement('pre');
    contentnode.appendChild(document.createTextNode(res));
    resnode.appendChild(titlenode);
    resnode.appendChild(contentnode);
}

function ajouter()
{
    var entry = document.getElementById("ajout").value;
    if(entry!='')
    {
        var li = document.createElement("li");
        li.innerHTML = entry;
        document.getElementById("courses").appendChild(li);
    }
    else
    {
        alert("Entrer quelque chose à ajouter !");
    }
}

function retirer()
{
    var ul = document.getElementById("courses");
    if(ul.childNodes.length>0)
    {
        ul.removeChild(ul.lastChild);
    }
    else
    {
        alert("La liste est vide !");
    }
}




/* Exercice 3 */
function animer(e)
{
    var monCarre = document.getElementById("carre");
    switch(e.keyCode)
    {
        case 37:
            if(isNaN(parseInt(monCarre.style.right, 10)))
            {
                monCarre.style.right = "10px";
            }
            else
            {
                var valAv = parseInt(monCarre.style.right, 10);
                var valAp = valAv + 10;
                monCarre.style.right = valAp + "px";
            }
            break
        case 38:
            if(isNaN(parseInt(monCarre.style.bottom, 10)))
            {
                monCarre.style.bottom = "10px";
            }
            else
            {
                var valAv = parseInt(monCarre.style.bottom, 10);
                var valAp = valAv + 10;
                monCarre.style.bottom = valAp + "px";
            }
            break
        case 39:
            if(isNaN(parseInt(monCarre.style.right, 10)))
            {
                monCarre.style.right = "-10px";
            }
            else
            {
                var valAv = parseInt(monCarre.style.right, 10);
                var valAp = valAv - 10;
                monCarre.style.right = valAp + "px";
            }
            break
        case 40:
            if(isNaN(parseInt(monCarre.style.bottom, 10)))
            {
                monCarre.style.bottom = "-10px";
            }
            else
            {
                var valAv = parseInt(monCarre.style.bottom, 10);
                var valAp = valAv - 10;
                monCarre.style.bottom = valAp + "px";
            }
            break
    }
}

/* Exercice 4 */

/*

var animal = {
    init:function(type)
    {
        this.type = type;    
    },
    
    sound:"the voice",
    
    talk:function()
    {
        console.log("sound : "+this.sound.toUpperCase());    
    },
    eat:function()
    {
        console.log("manger : "+this.type);
    }  
    
};

var duck = Object.create(animal);
duck.init("carrot");
duck.eat();
duck.talk()

*/

/*
var animal = function(type)
{
    this.type = type;
    this.eat = function()
    {
        console.log("eat : "+this.type);
    }
};

var terrier = new animal("os");
terrier.eat();

animal.prototype.talk = function()
{
    console.log("wouf wouf");
};

terrier.talk()
*/

var Compte = function()
{
    this.transactions = [];
    this.idt          = 0;

    this.addTrans = function(e)
    {
        var montant = document.getElementById("inputMontant").value;
        if(!(isNaN(montant)))
        {
            this.transactions.push({"idt: ":this.idt, "amount: ":montant})
            this.idt++;
            document.getElementById("inputMontant").value = "";
        }
        else
        {
            document.getElementById("inputMontant").value = "";
        }
    };
    
    this.getAmount = function()
    {
        var amount = 0;
        transactions.forEach(function(record)
        {
            amount += transactions.amount;
        });
        return amount;
    };

    this.printCompte = function(e)
    {
        var div = document.getElementById("printCompte");
        div.innerHTML = "";

        for(var i=0; i<this.idt; i++)
        {
            var lab = document.createElement("label");
            lab.innerHTML = "idt: "+this.transactions[i]["idt: "]+" amount: "+this.transactions[i]["amount: "];
            div.appendChild(lab);
            div.appendChild(document.createElement("br"));
        }
    }
};

var monCompte = new Compte();

function initCompte()
{
    var boutonInit = document.getElementById("bInit");
    boutonInit.setAttribute("value", "Reinitialiser le compte");

    var lab = document.createElement("label");
    lab.innerHTML = "Ajouter une transaction ";

    var inpM = document.createElement("input");
    inpM.setAttribute("id", "inputMontant");
    inpM.setAttribute("action", "monCompte.addTrans(event)");

    var bInp = document.createElement("input");
    bInp.setAttribute("type", "button");
    bInp.setAttribute("onclick", "monCompte.addTrans(event)");
    bInp.setAttribute("value", "ajouter");

    var bPrint = document.createElement("input");
    bPrint.setAttribute("type", "button");
    bPrint.setAttribute("value", "Afficher compte");
    bPrint.setAttribute("onclick", "monCompte.printCompte(event)");

    var div = document.getElementById("divCompte");
    div.innerHTML = "";
    div.appendChild(lab);
    div.appendChild(inpM);
    div.appendChild(bInp);
    div.appendChild(document.createElement("br"));
    div.appendChild(bPrint);

    document.getElementById("printCompte").innerHTML = "";
};