package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    EditText txt_city;
    Button btn_search, btn_next;
    ImageView img_weather;
    TextView tv_city, tv_country, tv_nhietdo, tv_status, tv_doam, tv_may, tv_gio, tv_date;
    ImageButton btn_location;
    String kinhdo, vido, city_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Dự Báo Thời Tiết");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        AnhXa();
        GetCurrentWeather("HaNoi");
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = txt_city.getText().toString();
                city = Validate_places(city);
                GetCurrentWeather(city);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Second.class);
                intent.putExtra("city_name", city_name);
                startActivity(intent);
            }
        });
    }
    private String Validate_places(String data) {
        data = data.trim();
        data = data.replaceAll("\\s+", "");
        return data;
    }
    public void GetCurrentWeather(String data) {
        ArrayList<String> arrayList = GetLocation(data);
        if (arrayList.get(0) == "false")
        {
            Toast.makeText(MainActivity.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
        }
        else {
            kinhdo = arrayList.get(0);
            vido = arrayList.get(1);
            //bien luu cac request gui len server
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            //Doc du lieu duong dan
            String url = "https://api.openweathermap.org/data/2.5/weather?lat="+vido+"&lon="+kinhdo+"&units=metric&appid=92c6161e0d9ddd64a865f69b71a89c31&lang=vi";
            final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //nhận dữ liệu trả về từ api
                        JSONObject jsonObject = new JSONObject(response);
                        String day = jsonObject.getString("dt");
                        String name = jsonObject.getString("name");
                        city_name = name;
                        tv_city.setText("Tên thành phố: " + name);
                        long l = Long.valueOf(day);
                        Date date = new Date(l * 1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm");
                        String Day = simpleDateFormat.format(date);
                        tv_date.setText("Ngày cập nhật: " + Day);
                        JSONArray jsonArray = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        //get status
                        String status = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");
                        //get image
                        Picasso.get().load("http://openweathermap.org/img/wn/" + icon + ".png").into(img_weather);
                        tv_status.setText(status);
                        JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                        String nhietdo = jsonObjectMain.getString("temp");
                        String doam = jsonObjectMain.getString("humidity");

                        Double a = Double.valueOf(nhietdo);
                        String Nhietdo = String.valueOf(a.intValue());
                        tv_nhietdo.setText(Nhietdo + "°C");
                        tv_doam.setText("Độ ẩm: " + doam + "%");

                        JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");
                        tv_gio.setText("Gió: " + wind + "m/s");

                        JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                        String cloud = jsonObjectCloud.getString("all");
                        tv_may.setText("Mây: " + cloud + "%");

                        JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                        String country = jsonObjectSys.getString("country");
                        tv_country.setText("Tên quốc gia: " + country);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(stringRequest);
        }
    }
    private ArrayList<String> GetLocation(String city) {
        ArrayList<String> arrayList = new ArrayList<String>();
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+city+"&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyBE90kLZE6B5l8Ba1cPlFsCpOdJOgbWgA4";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayCandidates = jsonObject.getJSONArray("candidates");
                    Log.d("length", String.valueOf(jsonArrayCandidates.length()));
                    JSONObject jsonObject1 = jsonArrayCandidates.getJSONObject(0);
                    JSONObject jsonObjectGeometry = jsonObject1.getJSONObject("geometry");
                    JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");
                    kinhdo = jsonObjectLocation.getString("lng");
                    vido = jsonObjectLocation.getString("lat");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
        if (kinhdo == null || vido == null){
            arrayList.add("false");
        }else {
            arrayList.add(kinhdo);
            arrayList.add(vido);
        }
        return arrayList;
    }
    private void AnhXa() {
        btn_location = (ImageButton) findViewById(R.id.btn_location);
        txt_city = (EditText) findViewById(R.id.txt_city);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_next = (Button) findViewById(R.id.btn_next);
        img_weather = (ImageView) findViewById(R.id.img_weather);
        tv_city = (TextView) findViewById(R.id.tv_name);
        tv_country = (TextView) findViewById(R.id.tv_country);
        tv_nhietdo = (TextView) findViewById(R.id.tv_nhietdo);
        tv_status = (TextView) findViewById(R.id.tv_line_status);
        tv_doam = (TextView) findViewById(R.id.tv_doam);
        tv_may = (TextView) findViewById(R.id.tv_may);
        tv_gio = (TextView) findViewById(R.id.tv_gio);
        tv_date = (TextView) findViewById(R.id.tv_hours);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent();
        int id = item.getItemId();
        if (id == android.R.id.home) {
            intent.setClass(MainActivity.this, HomeMenu.class);
            startActivity(intent);
            MainActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
