<?php 
include 'connect.php';

$sql = "SELECT * FROM Produit";
$resultat = $mysqli->query($sql);
$titres = $resultat->fetch_fields();
foreach($titres as $colonne)
{
	echo "<th> ".$colonne->name." </th>";
}
while($ligne = $resultat->fetch_object())
{
	echo "<tr>";
	foreach ($ligne as $colonne => $val) 
	{
		echo "<td> ".$val." </td>";
	}
	echo "</tr>";
}

$mysqli->close();
?>
