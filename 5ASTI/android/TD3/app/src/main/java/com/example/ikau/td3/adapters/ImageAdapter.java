package com.example.ikau.td3.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.ikau.td3.R;
import com.example.ikau.td3.fragments.ImageFragment;
import com.example.ikau.td3.fragments.ProgressSpinnerFragment;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private ArrayList<View> items;

    public ImageAdapter(ArrayList<View> items)
    {
        this.items    = items;
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
        View v;

        if (convertView == null)
        {
            v = this.items.get(position);
//            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(this.activity);
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            v = (ProgressBar) convertView;
        }
        return v;
    }
}
