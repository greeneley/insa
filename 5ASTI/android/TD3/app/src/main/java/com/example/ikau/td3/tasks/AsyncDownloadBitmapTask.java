package com.example.ikau.td3.tasks;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.ikau.td3.R;
import com.example.ikau.td3.adapters.GridViewAdapter;
import com.example.ikau.td3.adapters.RecyclerAdapter;
import com.example.ikau.td3.enums.DownloadAction;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;

/**
 * Tâche asynchrone permettant de télécharger une image au format bitmap puis d'envoyer
 * cette image à la classe adéquate le cas échéant.
 *
 * Les arguments sont : String URL de l'image, int index associé à l'image, DownloadAction action post-download.
 */
public class AsyncDownloadBitmapTask extends AbstractAsyncTask<Object, Void, Bitmap>
{

    /* ==============================================================
     *                          Propriétés
     * ==============================================================
     */
    /**
     * Une référence faible vrs la vue ayant demandée l'image.
     */
    private WeakReference<View> weakViewRef;

    /**
     * L'action à faire post-download.
     */
    private DownloadAction action;

    /**
     * L'index de l'élément demandant l'image.
     */
    private int index;


    /* ==============================================================
     *                          Constructeur
     * ==============================================================
     */
    public AsyncDownloadBitmapTask(View v)
    {
        this.weakViewRef = new WeakReference<View>(v);
    }


    /* ==============================================================
     *                          Overrides
     * ==============================================================
     */

    /**
     * Télécharge l'image depuis une ruel.
     * @param params String URL de l'image, int index associé à l'image, DownloadAction action post-download.
     * @return Bitmap de l'image téléchargée
     */
    @Override
    protected Bitmap doInBackground(Object... params)
    {
        // Vérification des paramètres
        if(params.length < 3)
        {
            Log.e("INSA", this.getClass().getSimpleName()+".doInBackground: [ERR] param missing");
            return null;
        }

        // Index où afficher l'image
        this.index = (int) params[1];

        // Action postExec
        this.action = (DownloadAction) params[2];

        // Récupération de l'url et de la position de l'image à afficher
        try {
            return this.getBitmapFromUrlString((String) params[0]);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Récupération de l'adresse de l'image en cas d'erreur
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if (bitmap == null) {
            Log.e("INSA", this.getClass().getSimpleName()+".onPostExecute: [ERR] Result is null");
            return;
        }
        if (this.weakViewRef.get() == null) {
            Log.e("INSA", this.getClass().getSimpleName()+".onPostExecute: [ERR] View is null");
            return;
        }

        switch (this.action) {
            case GridView:
                this.doGridView(bitmap);
                break;

            case RecyclerView:
                this.doRecyclerView(bitmap);
                break;
        }
    }

    /* ==============================================================
     *                       Méthodes privées
     * ==============================================================
     */

    /**
     * Crée une ImageView et l'ajoute dans l'adapter du GridViewFragment
     * @param bitmap L'image récemment téléchargée.
     */
    private void doGridView(Bitmap bitmap)
    {
        // Création de l'ImageView
        View view            = this.weakViewRef.get();
        GridView gridView    = view.findViewById(R.id.gridViewImages);
        ImageView imageView  = new ImageView(gridView.getContext());
        imageView.setImageBitmap(bitmap);

        // Modification de l'adapter
        GridViewAdapter adapter = (GridViewAdapter) gridView.getAdapter();
        adapter.replaceView(this.index, imageView);
        adapter.notifyDataSetChanged();

        Log.d("INSA", this.getClass().getSimpleName()+".doGridView: [OK] GridViewFragment updated");
    }

    /**
     * Met à jour l'adapter du RecyclerViewFragment pour ajouter le bitmap.
     * @param bitmap L'image récemment téléchargée.
     */
    private void doRecyclerView(Bitmap bitmap)
    {
        // Récupération de l'adapter
        View view                 = this.weakViewRef.get();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerAdapter adapter   = (RecyclerAdapter) recyclerView.getAdapter();

        // Modification de l'image
        adapter.updateBitmap(this.index, bitmap);
        adapter.notifyItemChanged(this.index);

        Log.d("INSA", this.getClass().getSimpleName()+".doRecyclerView: [OK] RecyclerViewFragment updated");
    }
}
