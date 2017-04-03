<?php
	session_start();
	if((!isset($_SESSION['login']))||(empty($_SESSION['login'])))		
	{
	// la variable 'login' de session est non déclaré ou vide
		echo '  <p>Petit curieux... <a href="index.php" title="Connexion">Connexion d\'abord !</a></p>';
		exit();
	}
	else
	{
		echo '<h1>bonjour :</h1><br/><hr><br/>'
		echo $_SESSION['login'];
		echo ', pour quitter la session:';
		echo '<br/><input type="button" value="Logout" action="index.php"'
	}
?>