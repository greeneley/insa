<?php
include "connect.php";
if(empty($_POST['inpNom']) OR empty($_POST['inpDesc']) OR !is_numeric($_POST['inpPrix']))
{
	print "Erreur ! Un ou plusieurs champs ne sont pas corrects";
}
else 
{
	$sql = "INSERT INTO `Produits`.`Produit` (`code`, `nom`, `description`, `prix`) VALUES (NULL, ".$_POST['inpNom'].", ".$_POST['inpDesc'].", ".$_POST['inpPrix'].");";
	$mysqli->query($sql);
	print "Ajout effectue ! La nouvelle table est <br/>";
	include "afficherProduit.php";
}
$mysqli->close();
?>