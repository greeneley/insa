

>>> Concernant les formulaires <<<
(ne pas oublier composer require laravelcollective/html)

<?php
{{ Form::open(['url' => 'mon_url'])}}
	{{ Form::champ(...) }}
{{ Form::close() }}
php?>

========================

>>> Pour les injections dependencies <<<

Créer un dossier qui contiendra les dependencies
Créer une interface par dependency
Créer une classe concrète qui implémente l'interface
NE PAS OUBLIER LES NAMESPACES

Enregistrer le bind dans :
app\Providers\AppServiceProvider.php
<?php 
public function register()
{
	$this->app->bind(
		'Interface', 
		'Implementation'
	);
}
php?>
Si erreur d'instanciation :
php artisan clear-compiled

========================

>>> Pour la BDD <<<

Création
php artisan make:install

Creer le fichier php de creation de table
php artisan make:migration createur_table_matable -m

Creer la table
php artisan migrate

Annuler la DERNIERE action de migrate
php artisan migrate:rollback

Creer un modele PHP
php artisan make:model monmdel

========================

>>> PHP artisan <<<
php artisan make:controller MONCONTROLLER
php artisan make:request    MAREQUEST