package com.example.ikau.td3.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.ikau.td3.R;
import com.example.ikau.td3.adapters.ImageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class AsyncDownloadTask extends AbstractAsyncTask<Object, Void, Bitmap>
{
    WeakReference<View> viewGridWkRef;
    private int index;

    public AsyncDownloadTask(View v)
    {
        this.viewGridWkRef = new WeakReference<View>(v);
    }

    @Override
    protected Bitmap doInBackground(Object... params)
    {
        // Vérification des paramètres
        if(params.length < 2)
        {
            Log.e("INSA", "[ERR] AsyncDownloadTask.doInBackground: param missing");
            return null;
        }

        // Index où afficher l'image
        this.index = (int) params[1];

        // Récupération de l'url et de la position de l'image à afficher
        try {
            return this.getBitmapFromFlickr((String) params[0], this.index);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Récupération de l'adresse de l'image
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap == null)
        {
            Log.e("INSA", "[ERR] AsyncDownloadTask.onPostExecute: result is null");
            return;
        }
        if(this.viewGridWkRef.get() == null)
        {
            Log.e("INSA", "[ERR] AsyncDownloadTask.onPostExecute: GridView is null");
            return;
        }

        // Création de l'ImageView
        View view            = this.viewGridWkRef.get();
        GridView gridView    = view.findViewById(R.id.gridViewImages);
        ImageView imageView  = new ImageView(gridView.getContext());
        imageView.setImageBitmap(bitmap);

        // Modification de l'adapter
        ImageAdapter adapter = (ImageAdapter) gridView.getAdapter();
        adapter.replaceView(this.index, imageView);
        adapter.notifyDataSetChanged();
    }

    /**
     * Crédits https://stackoverflow.com/questions/8992964/android-load-from-url-to-bitmap
     * @param urlImage
     * @param index
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private Bitmap getBitmapFromFlickr(String urlImage, int index) throws IOException, JSONException
    {
        // Ouverture de la connexion
        URL url                       = new URL(urlImage);
        URLConnection connectionImage = url.openConnection();
        connectionImage.setDoInput(true);
        connectionImage.connect();

        // Récupération du bitmap
        InputStream input = connectionImage.getInputStream();
        Bitmap myBitmap   = BitmapFactory.decodeStream(input);
        return myBitmap;
    }
}
