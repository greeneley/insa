package com.example.ikau.td3.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ikau.td3.R;

import org.json.JSONObject;

public class PlainJSONFragment extends Fragment
{
    public PlainJSONFragment()
    {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plain_json, container, false);
        this.setPlainJSON(v);
        return v;
    }

    public void setPlainJSON(View v)
    {
        TextView textView = (TextView) v.findViewById(R.id.textViewPlainJSON);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(getArguments().getString("json"));
    }
}
