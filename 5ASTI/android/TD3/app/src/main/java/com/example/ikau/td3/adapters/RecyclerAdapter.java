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
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private Context context;
    private ArrayList<String> titles;
    private ArrayList<String> authors;
    private ArrayList<String> urls;
    private ArrayList<Bitmap> images;
    private ArrayList<Boolean> taskDone;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerAdapter adapter;
        private boolean isFavorite;

        public View view;
        public ViewHolder(View v, RecyclerAdapter adapter, boolean isFavorite)
        {
            super(v);
            this.view       = v;
            this.adapter    = adapter;
            this.isFavorite = isFavorite;
            this.setIcon();
        }

        public void setTexts(String title, String author)
        {
            TextView tx = (TextView) this.view.findViewById(R.id.recyclerItemTextViewTitle);
            tx.setText(title);

            tx = (TextView) this.view.findViewById(R.id.recyclerItemTextViewAuthor);
            tx.setText(author);

            ImageButton imageButton = (ImageButton) this.view.findViewById(R.id.recyclerItemImageButton);
            imageButton.setOnClickListener(saveToFavorites);
        }

        public void setImage(Bitmap image)
        {
            ImageView v = (ImageView) this.view.findViewById(R.id.recyclerItemImageView);
            v.setImageBitmap(image);
        }

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

    public RecyclerAdapter(Context context, ArrayList<String> titles, ArrayList<String> authors, ArrayList<String> urls)
    {
        this.context  = context;
        this.titles   = titles;
        this.authors  = authors;
        this.urls     = urls;
        this.images   = new ArrayList<>();
        this.taskDone = new ArrayList<>();
        for(int i=0; i<titles.size(); i++)
        {
            this.images.add(null);
            this.taskDone.add(false);
        }
    }

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i)
    {
        if(this.images.get(i) != null)
        {
            viewHolder.setImage(this.images.get(i));

            // Enlever la barre de chargement
            if(!this.taskDone.get(i))
            {
                ProgressBar progressBar = (ProgressBar) viewHolder.view.findViewById(R.id.recyclerItemProgressBar);
                progressBar.setVisibility(View.GONE);
                this.taskDone.set(i, true);
            }
        }
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

        Log.i("INSA", "Position fired :"+String.valueOf(index));
    }

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

        Log.i("INSA", "Position fired :"+String.valueOf(index));
    }

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

    private String[] getIdAndSecret(String url)
    {
        String[] result   = new String[2];
        String   photo_id = null;
        String   secret   = null;

        Pattern pattern = Pattern.compile(".com/.*/(.*?)_(.*?)_m.jpg$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find())
        {
            photo_id = matcher.group(1);
            secret   = matcher.group(2);
        }
        result[0] = photo_id;
        result[1] = secret;

        return result;
    }

    public void updateBitmap(int index, Bitmap bitmap)
    {
        this.images.set(index, bitmap);
    }
}
