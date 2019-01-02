package com.example.ikau.td3.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ikau.td3.R;
import com.example.ikau.td3.enums.ActionsEnum;
import com.example.ikau.td3.fragments.PlaceholderFragment;
import com.example.ikau.td3.fragments.ProgressSpinnerFragment;
import com.example.ikau.td3.tasks.AsyncFavoritesTask;
import com.example.ikau.td3.tasks.AsyncFlickrTask;

/**
 * Classe et activité principale de l'application.
 *
 * Elle contient 5 boutons, une toolbar et un layout pour mettre le fragment principal.
 * Ce fragment est remplacé selon le bouton sur lequel on appuie.
 */
public class MainActivity extends AppCompatActivity
{
    /* ==============================================================
     *                          Propriétés
     * ==============================================================
     */

    /**
     * Permet d'indiquer aux tâches asynchrones l'action attendue actuellement.
     * Peut être réutilisée pour faire la tâche répétitive afin de déterminée la tâche à rafraichir.
     */
    private ActionsEnum expectedAction;


    /* ==============================================================
     *                           Overrides
     * ==============================================================
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Son layout

        // Définit le fragment par défaut
        this.expectedAction = ActionsEnum.NONE;
        this.setMainFragment(new PlaceholderFragment());

        // Init des prefs : j'ai pas eu la patience de trouver la méthode la moins dépréciée pour le faire automatiquement
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.contains("keyword"))
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("keyword", "cats");
            editor.apply();
        }
        Log.d("INSA", this.getClass().getSimpleName()+".onCreate: [OK] Ready");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.actionbar, menu); // Layout de la toolbar
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_preferences:
                Intent intent = new Intent(this, MyPreferenceActivity.class);
                this.startActivity(intent);
                Log.d("INSA", this.getClass().getSimpleName()+".onOptionsItemSelected: [OK] Bouton préférence");
                return true;

            default:
                Log.d("INSA", this.getClass().getSimpleName()+".onOptionsItemSelected: [ERR] Bouton inconnu");
                return super.onOptionsItemSelected(item);
        }
    }


    /* ==============================================================
     *                          Event handlers
     * ==============================================================
     */

    /**
     * Callback du bouton "Résultat de la requête" affichant le contenu brut du JSON.
     * @param v Bouton d'id 'buttonResults'.
     */
    protected void onClickButtonResults(View v)
    {
        this.expectedAction = ActionsEnum.PLAIN_JSON;
        String urlString = this.getSearchUrl();
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.PLAIN_JSON);
        Log.d("INSA", this.getClass().getSimpleName()+".onClickButtonResults: [OK] Fired");
    }

    /**
     * Callback du bouton "Nom des images" affichant une Listview des images.
     * @param v Bouton d'id 'buttonTitles'.
     */
    protected void onClickButtonTitles(View v)
    {
        this.expectedAction = ActionsEnum.TITLES;
        String urlString = this.getSearchUrl();
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.TITLES);
        Log.d("INSA", this.getClass().getSimpleName()+".onClickButtonTitles: [OK] Fired");
    }

    /**
     * Callback du bouton "Afficher les images" affichant une GridView 3xK contenant les images.
     * @param v Bouton d'id 'buttonImages'.
     */
    protected void onClickButtonImages(View v)
    {
        this.expectedAction = ActionsEnum.IMAGES;
        String urlString = this.getSearchUrl();
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.IMAGES);
        Log.d("INSA", this.getClass().getSimpleName()+".onClickButtonImages: [OK] Fired");
    }

    /**
     * Callback du bouton "Affichage avancé" affichant un RecyclerView contenant un layout personnalisé.
     * @param v Bouton d'id 'buttonAdvanced'.
     */
    protected void onClickButtonAdvanced(View v)
    {
        this.expectedAction = ActionsEnum.ADVANCED;
        String urlString = this.getSearchUrl();
        new AsyncFlickrTask(this).execute(urlString, ActionsEnum.ADVANCED);
        Log.d("INSA", this.getClass().getSimpleName()+".onClickButtonAdvanced: [OK] Fired");
    }

    /**
     * Callback du bouton "Mes favoris" affichant un RecyclerView contenant les images en favoris dans la BDD.
     *
     * Si le bouton ne fonctionne pas c'est qu'il faut changer la clé (j'ai pas de compte yahoo).
     * La clé que j'ai utilisée doit être quotidiennement mise à jour via
     * https://www.flickr.com/services/api/explore/flickr.photos.getInfo
     * (faire une essai via un lien quelconque et récupérer apikey dans le json résultant)
     *
     * Plus d'infos sur la structure des liens du feed :
     * https://www.flickr.com/services/api/misc.urls.html
     *
     * @param v Bouton d'id 'buttonFavorites'.
     */
    protected void onClickButtonFavorites(View v)
    {
        this.expectedAction = ActionsEnum.FAVORITES;

        // La clé de l'API est arbitrarement celle du site et peut donc expirer
        new AsyncFavoritesTask(this, "2f7b3ca61bf5bbdbbe5928eb5ce05f3c").execute();
        Log.d("INSA", this.getClass().getSimpleName()+".onClickButtonFavorites: [OK] Fired");
    }


    /* ==============================================================
     *                       Méthodes publiques
     * ==============================================================
     */

    /**
     * Permet aux tâches asynchrones de modifier le fragment principal.
     * @param fragment Le nouveau fragment à afficher.
     */
    public void setMainFragment(Fragment fragment)
    {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.layoutFragment, fragment)
            .commit();
    }

    /**
     * Permet aux tâches asynchrones d'afficher un fragment de chargement le temps d'exécution.
     */
    public void showProgressBar()
    {
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.layoutFragment, new ProgressSpinnerFragment())
            .commit();
    }

    /**
     * Getter de l'action actuellement affichée
     * @return L'action actuellement attendue/affichée.
     */
    public ActionsEnum getExpectedAction() {
        return expectedAction;
    }

    /* ==============================================================
     *                        Méthodes privées
     * ==============================================================
     */

    /**
     * Permet de récupérer dynamiquement l'url de recherche sur le feed de Flickr.
     * Attention : l'utilisateur peut sensiblement modifier l'url car on injecte directement son mot-clé favoris.
     * @return L'url de recherche avec le mot-clé de l'utilisateur.
     */
    private String getSearchUrl()
    {
        // Accès aux préférences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String keyword = prefs.getString("keyword", "cats");

        // Création du string
        // Oui c'est moche mais c'est pas censé être sécurisé
        return "https://www.flickr.com/services/feeds/photos_public.gne?format=json&tags="+keyword;
    }
}
