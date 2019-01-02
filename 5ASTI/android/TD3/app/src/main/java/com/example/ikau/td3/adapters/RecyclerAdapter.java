package com.example.ikau.td3.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ikau.td3.R;
import com.example.ikau.td3.database.MyDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://developer.android.com/guide/topics/ui/layout/recyclerview#java
 * Adapter personnalisé pour afficher des items personnalisés pour l'affichage avancé.
 *
 * Classe bêtement compliquée car la doc propose d'utiliser une classe interne
 * (je n'ai pas essayé de faire autrement car le mécanisme interne semble utiliser la classe interne).
 * Par soucis de facilité, la classe interne a directement accès aux membres de RecyclerAdapter...
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{

    /* ==============================================================
     *                          Propriétés
     * ==============================================================
     */
    /**
     * Ref vers le contexte (ici MainActivity) pour l'utilisation de la BDD.
     */
    private Context context;


    //Chacune des listes contient une information à afficher dans le widget personnalisé.
    private ArrayList<String> titles;
    private ArrayList<String> authors;

    /**
     * Nécessaire dans mon architecture pour enregistrer dans les favoris.
     */
    private ArrayList<String> urls;

    /**
     * Liste des bitmaps qui sera remplie au fur et à mesure de la fin des tâches asynchrones.
     */
    private ArrayList<Bitmap> images;

    /**
     * Nécessaire dans mon architecture pour savoir quand une tâche asynchrone est finie.
     * Simplement vérifier que le bitmap est null ne suffit pas car RecyclerView... recycle les vues !
     */
    private ArrayList<Boolean> taskDone;

    /**
     * La classe interne permettant de stocker chaque item dans le RecyclerView.
     *
     * La vue correspondante à cette classe est fragment_recycler_adapter_item.xml
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        /* --------------------------------------------------------
         *                       Propriétés
         * --------------------------------------------------------
         */

        /**
         * La vue inflatée dans RecyclerView qui doit être contenue dans cette classe.
         */
        public View view;

        /**
         * Ref vers l'adapter car c'est une classe statique
         */
        private RecyclerAdapter adapter;

        /**
         * Indique si l'image est dans la BDD des favoris ou non.
         */
        private boolean isFavorite;


        /**
         * Un OnClickListener personnalisé permettant de demander à mettre/enlever dans la BDD des favoris.
         */
        private View.OnClickListener saveToFavorites = new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(isFavorite)
                {
                    adapter.deleteItemFromFavorite(getAdapterPosition());
                }
                else
                {
                    adapter.saveItemToFavorite(getAdapterPosition());
                }
                isFavorite = !isFavorite;
                setIcon();
            }
        };


        /* --------------------------------------------------------
         *                       Constructeur
         * --------------------------------------------------------
         */
        public ViewHolder(View v, RecyclerAdapter adapter, boolean isFavorite)
        {
            super(v);
            this.view       = v;
            this.adapter    = adapter;
            this.isFavorite = isFavorite;
            this.setIcon();
        }

        /* --------------------------------------------------------
         *                    Méthodes publiques
         * --------------------------------------------------------
         */

        /**
         * Permet de modifier les textes à afficher pour afficher le titre et l'auteur de la photo.
         * @param title Le titre de la photo.
         * @param author L'auteur de la photo.
         */
        public void setTexts(String title, String author)
        {
            // Récupération du TextView du titre + init
            TextView tx = (TextView) this.view.findViewById(R.id.recyclerItemTextViewTitle);
            tx.setText(title);

            // TextView de l'auteur + init
            tx = (TextView) this.view.findViewById(R.id.recyclerItemTextViewAuthor);
            tx.setText(author);

            // Init de l'évènement onClick pour la gestion des favoris
            ImageButton imageButton = (ImageButton) this.view.findViewById(R.id.recyclerItemImageButton);
            imageButton.setOnClickListener(saveToFavorites);
        }

        /**
         * Permet à RecyclerAdapter de modifier le bitmap de l'ImageView à la fin d'une tâche asynchrone.
         * @param image Le bitmap à affecter.
         */
        public void setImage(Bitmap image)
        {
            ImageView v = (ImageView) this.view.findViewById(R.id.recyclerItemImageView);
            v.setImageBitmap(image);
        }

        /* --------------------------------------------------------
         *                    Méthodes privées
         * --------------------------------------------------------
         */

        /**
         * Permet de changer l'icône indiquant si l'item est en favoris ou non.
         */
        private void setIcon()
        {
            ImageButton imageButton = (ImageButton) this.view.findViewById(R.id.recyclerItemImageButton);
            if(this.isFavorite)
            {
                imageButton.setImageResource(android.R.drawable.star_on);
            }
            else
            {
                imageButton.setImageResource(android.R.drawable.star_off);
            }
        }
    }

    /* ==============================================================
     *                         Constructeur
     * ==============================================================
     */
    public RecyclerAdapter(Context context, ArrayList<String> titles, ArrayList<String> authors, ArrayList<String> urls)
    {
        this.context  = context;
        this.titles   = titles;
        this.authors  = authors;
        this.urls     = urls;

        // Au départ, aucune tâche asynchrone n'est réalisée.
        this.images   = new ArrayList<>();
        this.taskDone = new ArrayList<>();
        for(int i=0; i<titles.size(); i++)
        {
            this.images.add(null);
            this.taskDone.add(false);
        }
    }


    /* ==============================================================
     *                          Overrides
     * ==============================================================
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        // Création d'un nouvel élément
        View v = LayoutInflater.from(viewGroup.getContext())
                               .inflate(R.layout.fragment_recycler_adapter_item, viewGroup, false);

        // Enregistrement du nouvel ViewHolder
        return new ViewHolder(v, this, this.isImageFavorite(this.urls.get(i)));
    }

    /**
     * Automatiquement appelée par le RecyclerView lorsqu'une vue est recyclée.
     *
     * C'est ici qu'il faut vérifier si les tâches asynchrones ont terminé ou non.
     * @param viewHolder Le ViewHolder recyclé.
     * @param i Le type du ViewHolder (reçu par getItemViewType).
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        // Si une image est présente on l'affiche
        if(this.images.get(i) != null)
        {
            viewHolder.setImage(this.images.get(i));

            // Enlever la barre de chargement qu'une seule fois pour éviter une exception.
            if(!this.taskDone.get(i))
            {
                ProgressBar progressBar = (ProgressBar) viewHolder.view.findViewById(R.id.recyclerItemProgressBar);
                progressBar.setVisibility(View.GONE);
                this.taskDone.set(i, true);
            }
        }

        // Modifier les textes dans tous les cas à cause du recyclage
        viewHolder.setTexts(this.titles.get(i), this.authors.get(i));
    }

    @Override
    public int getItemCount() {
        return this.titles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    /* ==============================================================
     *                      Méthodes publiques
     * ==============================================================
     */

    /**
     * Permet à une tâche asynchrone venant de terminer de changer l'image d'un ViewHolder.
     * @param index L'index de l'item à modifier.
     * @param bitmap Le bitmap à affecter.
     */
    public void updateBitmap(int index, Bitmap bitmap)
    {
        this.images.set(index, bitmap);
        Log.d("INSA", this.getClass().getSimpleName()+".updateBitmap: [OK] Bitmap updated at {"+index+"}");
    }

    /* ==============================================================
     *                       Méthodes privées
     * ==============================================================
     */

    /**
     * Permet de sauvegarder un item dans la BDD des favoris.
     * @param index L'index de l'item.
     */
    private void saveItemToFavorite(int index)
    {
        // Récupération d'uns instance d'écriture dans la BDD
        MyDatabase instance = new MyDatabase(this.context);
        SQLiteDatabase db = instance.getWritableDatabase();

        // Récupération des données
        String[] idAndSecret = this.getIdAndSecret(this.urls.get(index));
        if(idAndSecret[0] != null && idAndSecret[1] != null)
        {
            instance.insertNewFavorite(db, idAndSecret[0], idAndSecret[1]);
        }
        // Optimisation : fermer sur le onDestroy du fragment contenant l'adapter
        db.close();

        Log.d("INSA", this.getClass().getSimpleName()+".saveItemToFavorite: [OK] Favorite saved at {"+index+"}");
    }

    /**
     * Permet de supprimer l'item de la BDD des favoris.
     * @param index L'index de l'item.
     */
    private void deleteItemFromFavorite(int index)
    {
        // Récupération d'uns instance d'écriture dans la BDD
        MyDatabase instance = new MyDatabase(this.context);
        SQLiteDatabase db = instance.getWritableDatabase();

        // Récupération des données
        String[] idAndSecret = this.getIdAndSecret(this.urls.get(index));
        if(idAndSecret[0] != null && idAndSecret[1] != null)
        {
            instance.deleteFromFavorite(db, idAndSecret[0], idAndSecret[1]);
        }
        // Optimisation : fermer sur le onDestroy du fragment contenant l'adapter
        db.close();

        Log.d("INSA", this.getClass().getSimpleName()+".deleteItemFromFavorite: [OK] Favorite deleted at {"+index+"}");
    }

    /**
     * Indique si l'image est présente dans la BDD des favoris.
     * @param url L'url de l'image.
     * @return true si l'image est dans les favoris, false sinon.
     */
    private boolean isImageFavorite(String url)
    {
        String[] idAndSecret = this.getIdAndSecret(url);

        // Récupération d'uns instance d'écriture dans la BDD
        MyDatabase instance = new MyDatabase(this.context);
        SQLiteDatabase db = instance.getWritableDatabase();

        // Vérification
        boolean isFavorite = instance.contains(db, idAndSecret[0], idAndSecret[1]);
        db.close();

        return isFavorite;
    }

    /**
     * Renvoie un String[2]{photo_id, secret} permettant de récupérer les infos globales de la photo ou stocker en BDD.
     * @param url L'url de la photo pour y extraire les deux informations.
     * @return String[2]{photo_id, secret}
     */
    private String[] getIdAndSecret(String url)
    {
        String[] result   = new String[2];
        String   photo_id = null;
        String   secret   = null;

        // Utilisation d'un regex pour obtenir les infos : perte de perf très possible
        Pattern pattern = Pattern.compile(".com/.*/(.*?)_(.*?)_m.jpg$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
        {
            photo_id = matcher.group(1);
            secret   = matcher.group(2);
        }
        result[0] = photo_id;
        result[1] = secret;

        Log.d("INSA", this.getClass().getSimpleName()+".getIdAndSecret: [OK] ID {"+photo_id+"} ; Secret {"+secret+"}");
        return result;
    }
}
