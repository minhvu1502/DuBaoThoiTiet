package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Seven_day_details extends AppCompatActivity {
    ImageButton btn_back;
    TextView tv_city, tv_country, tv_temp, tv_status, tv_humility, tv_wind, tv_cloud, tv_day;
    ImageView img_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven_day_details);
        AnhXa();
        Getdata();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void AnhXa(){
        btn_back = (ImageButton)findViewById(R.id.btn_seven_details);
        tv_city = (TextView)findViewById(R.id.tv_name);
        tv_country = (TextView)findViewById(R.id.tv_country);
        tv_status = (TextView)findViewById(R.id.tv_line_status);
        tv_temp = (TextView)findViewById(R.id.tv_nhietdo);
        tv_humility = (TextView)findViewById(R.id.tv_doam);
        tv_wind= (TextView)findViewById(R.id.tv_gio);
        tv_cloud= (TextView)findViewById(R.id.tv_may);
        tv_day = (TextView)findViewById(R.id.tv_hours);
        img_status = (ImageView)findViewById(R.id.img_weather);
    }
    private void Getdata(){
        Intent intent = getIntent();
        tv_city.setText("Thành phố: "+intent.getStringExtra("city_name"));
        tv_country.setText("Quốc gia: "+intent.getStringExtra("country"));
        tv_status.setText(intent.getStringExtra("status"));
        Double a = Double.valueOf(intent.getStringExtra("temp"));
        tv_temp.setText(String.valueOf(a.intValue())+"°C");
        tv_humility.setText("Độ ẩm: " + intent.getStringExtra("doam") + "%");
        tv_wind.setText("Gió: " + intent.getStringExtra("gio") + "m/s");
        tv_cloud.setText("Mây: "+intent.getStringExtra("may")+"%");
        tv_day.setText(intent.getStringExtra("Day"));
        String icon = intent.getStringExtra("image");
        Picasso.get().load("http://openweathermap.org/img/wn/" + icon + ".png").into(img_status);
    }
}
