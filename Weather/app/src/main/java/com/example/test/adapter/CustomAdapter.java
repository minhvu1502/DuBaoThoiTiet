package com.example.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.models.Weather;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    ArrayList<Weather> arrayList;

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.line_second, null);
        }
        Weather p = (Weather) getItem(position);
        if (p != null) {
            //Anh xa
            TextView txt_date = (TextView) view.findViewById(R.id.tv_hours);
            TextView txt_status = (TextView) view.findViewById(R.id.tv_line_status);
            ImageView img = (ImageView) view.findViewById(R.id.img_line_status);
            TextView txt_max = (TextView) view.findViewById(R.id.txt_max);
            TextView txt_min = (TextView) view.findViewById(R.id.txt_min);
            txt_date.setText(p.getHours());
            txt_status.setText(p.getStatus());
            txt_min.setText(p.getMin() + "°C");
            txt_max.setText(p.getMax() + "°C");
            Picasso.get().load("http://openweathermap.org/img/wn/" + p.getImage() + ".png").into(img);
        }
        return view;
    }
}
