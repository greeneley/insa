package com.example.ikau.td3.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private ArrayList<View> items;

    public GridViewAdapter(ArrayList<View> items)
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
            this.setParams(position);
        }
        v = this.items.get(position);
        return v;
    }

    public void replaceView(int index, View newView)
    {
        this.items.remove(index);
        this.items.add(index, newView);
        this.setParams(index);
    }

    private void setParams(int index)
    {
        this.items.get(index).setLayoutParams(new GridView.LayoutParams(300, 300));
        this.items.get(index).setPadding(8, 8, 8, 8);
    }
}
