package com.example.ikau.td3.adapters;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Adapter personnalisé pour afficher des images dans une GridView 3xK.
 *
 * La classe peut être grandement optimisée car elle porte des références vers des views...
 * Pour l'améliorer, il faudrait mettre des références vers des objets plus légers (des Bitmap par exemple)
 * et faire un inflater.
 * Le RecyclerAdapter, bien que plus compliqué, est une version plus expérimentée de ce que je voulais
 * faire ici.
 */
public class GridViewAdapter extends BaseAdapter
{

    /* ==============================================================
     *                          Propriétés
     * ==============================================================
     */
    /**
     * Une liste des views à afficher.
     * Peut être remplacée par une liste de Bitmap à condition d'inflate un layout lors de l'init.
     */
    private ArrayList<View> items;


    /* ==============================================================
     *                         Constructeur
     * ==============================================================
     */
    public GridViewAdapter(ArrayList<View> items)
    {
        this.items = items;
    }


    /* ==============================================================
     *                          Overrides
     * ==============================================================
     */
    @Override
    public int getCount()
    {
        return items.size();
    }

    // Non utilisé
    @Override
    public Object getItem(int position)
    {
        return null;
    }

    /**
     * Non utilisé.
     *
     * Permettrait normalement d'obtenir un id unique au lieu de récupérer l'item directement.
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    /**
     * Utilisé automatiquement par le GridView pour récupérer les views de chaque item.
     *
     * C'est ici que l'on peut optimiser en utilisant un inflater au lieu d'utiliser une liste de view.
     * On notera aussi que je n'ai pas réutilisé le convertView : j'ai eu des incohérences d'index dans mes souvenirs.
     * @param position L'index de l'item.
     * @param convertView Si l'item a déjà une vue existante, alors elle est redonnée par la méthode.
     * @param parent Explicite.
     * @return La view de l'item.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            // Si aucune vue n'est préexistante, on initialise son layout.
            this.setParams(position);
        }
        // Sinon on renvoie la vue associée dans notre liste.
        // convertView ne donnait bizarrement pas le même résultat dans mes tests précédents... ?
        View v = this.items.get(position);

        return v;
    }

    /* ==============================================================
     *                       Méthodes publiques
     * ==============================================================
     */
    /**
     * Utilisée par la tâche asynchrone associée pour remplacer la barre de chargement par l'image.
     *
     * L'optimisation mentionnée précédemment consiste à utiliser une liste de bitmap au lieu de view.
     * @param index L'index de la view à remplacer.
     * @param newView La nouvelle view à afficher.
     */
    public void replaceView(int index, View newView)
    {
        this.items.remove(index);
        this.items.add(index, newView);
        this.setParams(index);
        Log.d("INSA", this.getClass().getSimpleName()+".replaceView: [OK] Item {"+index+"} updated");
    }

    /* ==============================================================
     *                       Méthodes privées
     * ==============================================================
     */
    /**
     * Permet de mettre les paramètres de layout (GridView) pour l'item.
     * @param index L'index de la vue à paramétrée.
     */
    private void setParams(int index)
    {
        this.items.get(index).setLayoutParams(new GridView.LayoutParams(300, 300));
        this.items.get(index).setPadding(8, 8, 8, 8);
    }
}
