<html >

<head>
 <meta charset="UTF-8">
<title>Poulet rapide</title>
<link rel="stylesheet" type="text/css" href="../vue/css/intra.css" />
</head>

<body>
<?php
ini_set('session.cookie_lifetime', 1*60);
session_start();
$_SESSION['login'] = '';
$_SESSION['password'] = '';
// Destruction de la session ?
	if ((isset($_GET['action'])) && ($_GET['action'] == 'logout'))
	{
	 include_once("logout.php");
	 
	}
if (isset($_POST['submit']))
{
    // bouton submit pressé, je traite le formulaire
    $login = (isset($_POST['login'])) ? $_POST['login'] : '';
    $pass  = (isset($_POST['pass']))  ? $_POST['pass']  : '';

    if (($login == "Matthieu") && ($pass == "tux"))
    {
        $_SESSION['login'] = "Matthieu";
        $_SESSION['password'] = "tux";
        echo '<a href="../vue/accueil.php" title="Accueil de la section membre" >Vers page Accueil|</a>';
		echo '<a href="../vue/informations.php" title="Informations" >Vers page Informations</a>';
   
	}
    else
    {
        // une erreur de saisie ...?
        echo '<p style="color:#FF0000; font-weight:bold;">Erreur de connexion.</p>';
    }
}; // fin if (isset($_POST['submit']))


if (!isset($_POST['submit']))
{
    
    // Bouton submit non pressé j'affiche le formulaire
    echo '
		<form id="conn" method="post" action="">
			<p><label for="login">Login :</label><input type="text" id="login" name="login" /></p>
			<p><label for="pass">Mot de Passe :</label><input type="password" id="pass" name="pass" /></p>
			<p><input type="submit" id="submit" name="submit" value="Connexion" /></p>
		</form>';
}; // fin if (!isset($_POST['submit'])))
?>
</body>

</html>