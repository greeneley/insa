<html >

<head>
 <meta charset="UTF-8">
<title>Poulet rapide</title>
<link rel="stylesheet" type="text/css" href="css/intra.css" />
</head>

<body>
<?php
    session_start();
	if ((!isset($_SESSION['login'])) || (empty($_SESSION['login'])))
	{
		// la variable 'login' de session est non déclaré ou vide
		echo '  <p>Petit curieux... <a href="../modele/login.php" title="Connexion">Connexion d\'abord !</a></p>';
		exit();
	}

    print '<div> Bonjour, Monsieur ' .$_SESSION['login'].'</div>';
	print '<form id="logout" method="post" action="../modele/logout.php">
            <p><input type="submit" id="logout" name="logout" value="Déconnexion" /></p>
		</form>';
    print '<hr/> Bienvenue sur notre site p\'tit chef résérvé aux membres!';
    
?>
<h1>Plat du Jour: Poulet rapide</h1>

<table>
	<tr>
		<td>Préparation :</td>
		<td>5 minutes</td>
	</tr>
	<tr>
		<td>Cuisson :</td>
		<td>70 minutes</td>
	</tr>
	<tr>
		<td>Portions :</td>
		<td>4</td>
	</tr>
</table>

<h2>Ingrédients</h2>

<table  style="width: 100%">
	<tr>
		<th>Quantités</th>
		<th>Unités</th>
		<th>Ingrédients</th>
	</tr>
	<tr>
		<td>1</td>
		<td>lb</td>
		<td><span class="viande">Poulet</span> désossé (cubes)</td>
	</tr>
	<tr>
		<td>1</td>
		<td>&nbsp;</td>
		<td><span class="legume">oignon</span> haché</td>
	</tr>
	<tr>
		<td>Au goût</td>
		<td>&nbsp;</td>
		<td>Sel et poivre</td>
	</tr>
	<tr>
		<td>3/4</td>
		<td>tasse</td>
		<td>ketchup</td>
	</tr>
	<tr>
		<td>1</td>
		<td>tasse</td>
		<td>boisson gazeuse (Cola)</td>
	</tr>
</table>

<h2>Méthode</h2>

<ol>
	<li>Mélanger le tout et verser sur les cubes de <span class="viande">poulet</span>.</li>
	<li>Cuire le tout au four à <span class="temperature">350 F</span>. pendant environ 1 heure.</li>
</ol>
<p><a href="../modele/login.php?action=logout" title="Déconnexion">Se déconnecter</a></p>
</body>

</html>