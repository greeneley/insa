<?php
$server   = "localhost";
$db       = "Produits";
$user     = "vitrine";
$password = "orange";

// Create connection
$mysqli = new mysqli($server, $user, $password, $db);
if($mysqli->connect_errno)
{
	die("Echec lors de la connexion".$mysqli->connect_error);
}
?>