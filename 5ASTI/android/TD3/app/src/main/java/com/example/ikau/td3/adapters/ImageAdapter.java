package com.example.ikau.td3.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
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
        View v;

        if (convertView == null)
        {
            v = this.items.get(position);
            v.setLayoutParams(new GridView.LayoutParams(240, 240));
            v.setPadding(8, 8, 8, 8);
        }
        else
        {
            v = convertView;
        }
        return v;
    }

    public void replaceView(int index, View newView)
    {
        this.items.remove(index);
        this.items.add(index, newView);
    }
}
