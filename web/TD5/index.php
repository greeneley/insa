<?php
  session_start();
  $_SESSION['login'] = " ";
  $_SESSION['password'] = " ";
  if (isset($_POST['submit']))
  {
    $login = (isset($_POST['login'])) ? $_POST['login'] : " ";
    $pass  = (isset($_POST['pass']))  ? $_POST['pass']  : " ";
    if (($login == "Matthieu") && ($pass == "NewsletTux"))
   {
     $_SESSION['login'] = "Matthieu";
     $_SESSION['password'] = "NewsletTux";
     echo '<a href="accueil.php" title="Accueil de la section membre">Accueil</a>';
    }
   else
   {
     echo '<p style="color:#FF0000; font-weight:bold;">Erreur de connexion.</p>';
   }
};
if (!isset($_POST['submit']))
{
// Bouton submit non pressé j'affiche le formulaire
echo '<form id="conn" method="post" action=""> <p><label for="login">Login :</label><input type="text" id="login" name="login" /></p><p><label  for="pass">Mot  de  Passe  :</label> <input  type="password"  id="pass"  name="pass" /></p><p><input type="submit" id="submit" name="submit" value="Connexion" /></p></form>';
};
?>