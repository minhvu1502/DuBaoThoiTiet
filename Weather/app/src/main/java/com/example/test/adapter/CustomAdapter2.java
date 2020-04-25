package com.example.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.test.models.Data;
import com.example.test.R;

import java.util.List;

public class CustomAdapter2 extends ArrayAdapter<Data> {
    public CustomAdapter2(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public CustomAdapter2(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public CustomAdapter2(@NonNull Context context, int resource, @NonNull Data[] objects) {
        super(context, resource, objects);
    }

    public CustomAdapter2(@NonNull Context context, int resource, int textViewResourceId, @NonNull Data[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public CustomAdapter2(@NonNull Context context, int resource, @NonNull List<Data> objects) {
        super(context, resource, objects);
    }

    public CustomAdapter2(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Data> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.activity_custom_l_v, null);
        }
        Data p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView txt1 = (TextView) view.findViewById(R.id.txt_option);
            ImageView img = (ImageView) view.findViewById(R.id.imageView2);
            txt1.setText(p.getTen());
            img.setImageResource(p.getAnh());
        }
        return view;
    }
}