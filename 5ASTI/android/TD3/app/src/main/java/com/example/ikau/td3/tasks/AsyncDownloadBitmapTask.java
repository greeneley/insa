package com.example.ikau.td3.tasks;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.ikau.td3.R;
import com.example.ikau.td3.adapters.ImageAdapter;
import com.example.ikau.td3.adapters.RecyclerAdapter;
import com.example.ikau.td3.enums.DownloadAction;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;

public class AsyncDownloadBitmapTask extends AbstractAsyncTask<Object, Void, Bitmap>
{
    private WeakReference<View> weakViewRef;
    private DownloadAction      action;
    private int                 index;

    public AsyncDownloadBitmapTask(View v)
    {
        this.weakViewRef = new WeakReference<View>(v);
    }

    @Override
    protected Bitmap doInBackground(Object... params)
    {
        // Vérification des paramètres
        if(params.length < 3)
        {
            Log.e("INSA", "[ERR] AsyncDownloadBitmapTask.doInBackground: param missing");
            return null;
        }

        // Index où afficher l'image
        this.index = (int) params[1];

        // Action postExec
        this.action = (DownloadAction) params[2];

        // Récupération de l'url et de la position de l'image à afficher
        try {
            return this.getBitmapFromUrlString((String) params[0], this.index);

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
    protected void onPostExecute(Bitmap bitmap)
    {
        if (bitmap == null) {
            Log.e("INSA", "[ERR] AsyncDownloadBitmapTask.onPostExecute: result is null");
            return;
        }
        if (this.weakViewRef.get() == null) {
            Log.e("INSA", "[ERR] AsyncDownloadBitmapTask.onPostExecute: GridView is null");
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

    protected void doGridView(Bitmap bitmap)
    {
        // Création de l'ImageView
        View view            = this.weakViewRef.get();
        GridView gridView    = view.findViewById(R.id.gridViewImages);
        ImageView imageView  = new ImageView(gridView.getContext());
        imageView.setImageBitmap(bitmap);

        // Modification de l'adapter
        ImageAdapter adapter = (ImageAdapter) gridView.getAdapter();
        adapter.replaceView(this.index, imageView);
        adapter.notifyDataSetChanged();
    }

    protected void doRecyclerView(Bitmap bitmap)
    {
        // Récupération de l'adapter
        View view                 = this.weakViewRef.get();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerAdapter adapter   = (RecyclerAdapter) recyclerView.getAdapter();

        // Modification de l'image
        adapter.updateBitmap(this.index, bitmap);
        adapter.notifyItemChanged(this.index);
    }
}
