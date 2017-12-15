<html>
<body>

<?php
  session_start();
    if((!isset($_SESSION['login']))||(empty($_SESSION['login']))){
    // la variable 'login' de session est non déclaré ou vide
    echo '  <p>Petit curieux... <a href="login.php" title="Connexion">Connexion d\'abord !</a></p>';
    } else {
    print '<div> Bonjour, '.$_SESSION['login'].'</div>';
    print '<div style="color:black;background-color: magenta; border:1px red solid;">';
    print 'Les entêtes de votre requête HTTP.</div>';
    foreach (getallheaders() as $name => $value) {
        echo "<p>$name: $value </p>";
    }
    print '<hr/> session name: '. session_name().'<br>';
    print '<hr/> session ID: ' . $_COOKIE['PHPSESSID'].  '<br>';
   
    print '<hr/>'. 'cliquer sur le bouton pour quitter la session';

 ?>   
 
<p><a href="../modele/login.php?action=logout" title="Déconnexion">Se déconnecter</a></p>;
	     
<?php
}
?>
</body>
</html>
