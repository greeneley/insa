package com.example.ikau.td3.adapters;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ikau.td3.R;

import java.util.ArrayList;

/**
 * https://developer.android.com/guide/topics/ui/layout/recyclerview#java
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{
    private ArrayList<String> titles;
    private ArrayList<String> authors;
    private ArrayList<Bitmap> images;
    private ArrayList<Boolean> taskDone;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private RecyclerAdapter adapter;
        public View view;
        public ViewHolder(View v, RecyclerAdapter adapter)
        {
            super(v);
            this.view    = v;
            this.adapter = adapter;
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
                adapter.saveItemToFavorite(getAdapterPosition());
            }
        };
    }

    public RecyclerAdapter(ArrayList<String> titles, ArrayList<String> authors)
    {
        this.titles   = titles;
        this.authors  = authors;
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
        return new ViewHolder(v, this);
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

    public void saveItemToFavorite(int index)
    {
        Log.i("INSA", "Position fired :"+String.valueOf(index));
    }

    public void updateBitmap(int index, Bitmap bitmap)
    {
        this.images.set(index, bitmap);
    }
}
