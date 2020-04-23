package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class weather_location extends AppCompatActivity {
    EditText txt_city;
    Button btn_next;
    ImageView img_weather;
    TextView tv_city, tv_country, tv_nhietdo, tv_status, tv_doam, tv_may, tv_gio, tv_date, tv_sunrise, tv_sunset, tv_maxmin;
    ImageButton btn_search, btn_back;
    String kinhdo = "", vido = "", formatted_address, CityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_location);
        AnhXa();
        GetWeather();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void GetWeather() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(weather_location.this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String myLat = String.valueOf(location.getLatitude());
                        String myLng = String.valueOf(location.getLongitude());
                        //bien luu cac request gui len server
                        RequestQueue requestQueue_weather = Volley.newRequestQueue(weather_location.this);
                        //Doc du lieu duong dan
                        String url_weather = "https://api.openweathermap.org/data/2.5/weather?lat=" + myLat + "&lon=" + myLng + "&units=metric&appid=92c6161e0d9ddd64a865f69b71a89c31&lang=vi";
                        final StringRequest stringRequest_weather = new StringRequest(Request.Method.GET, url_weather, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    //nhận dữ liệu trả về từ api
                                    JSONObject jsonObject = new JSONObject(response);
                                    String name = jsonObject.getString("name");
                                    GetLocation(name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(weather_location.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestQueue_weather.add(stringRequest_weather);
                    }
                });
    }

    public String Validate_Place(String city) {
        city = city.trim();
        city = city.replaceAll("\\s+", "");
        return city;
    }

    private void GetLocation(String city) {
        city = Validate_Place(city);
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + city + "&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyD4oQg9klcCD0fVn-2sb5wbPrNNZs4bhJ4";
        RequestQueue requestQueue = Volley.newRequestQueue(weather_location.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayCandidates = jsonObject.getJSONArray("candidates");
                    JSONObject jsonObject1 = jsonArrayCandidates.getJSONObject(0);
                    CityName = jsonObject1.getString("name");
                    String formatted_addr = jsonObject1.getString("formatted_address");
                    String[] country = formatted_addr.split(", ");

                    formatted_address = country[country.length - 1];
                    JSONObject jsonObjectGeometry = jsonObject1.getJSONObject("geometry");
                    JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");
                    kinhdo = jsonObjectLocation.getString("lng");
                    vido = jsonObjectLocation.getString("lat");
                    //                bien luu cac request gui len server
                    RequestQueue requestQueue_weather = Volley.newRequestQueue(weather_location.this);
                    //Doc du lieu duong dan
                    String url_weather = "https://api.openweathermap.org/data/2.5/weather?lat=" + vido + "&lon=" + kinhdo + "&units=metric&appid=92c6161e0d9ddd64a865f69b71a89c31&lang=vi";
                    final StringRequest stringRequest_weather = new StringRequest(Request.Method.GET, url_weather, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //nhận dữ liệu trả về từ api
                                JSONObject jsonObject = new JSONObject(response);
                                String day = jsonObject.getString("dt");
                                tv_city.setText( CityName);
                                long l = Long.valueOf(day);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm");
                                String Day = simpleDateFormat.format(date);
                                tv_date.setText(Day);
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
                                String feels_like = jsonObjectMain.getString("feels_like");
                                String temp_max = jsonObjectMain.getString("temp_max");
                                String temp_min = jsonObjectMain.getString("temp_min");
                                tv_maxmin.setText(temp_max + "° / " + temp_min + "°");
                                Double a = Double.valueOf(nhietdo);
                                String Nhietdo = String.valueOf(a.intValue());
                                tv_nhietdo.setText(Nhietdo + "°");
                                tv_doam.setText("Độ ẩm: " + doam + "%");

                                JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                                String wind = jsonObjectWind.getString("speed");
                                tv_gio.setText("Gió: " + wind + "m/s");

                                JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                                String cloud = jsonObjectCloud.getString("all");
                                tv_may.setText("Mây: " + cloud + "%");

                                JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                                String sunrise = jsonObjectSys.getString("sunrise");
                                String sunset = jsonObjectSys.getString("sunset");
                                l = Long.valueOf(sunrise);
                                date = new Date(l * 1000L);
                                simpleDateFormat = new SimpleDateFormat("HH:mm");
                                Day = simpleDateFormat.format(date);
                                tv_sunrise.setText("Mặt trời mọc: " + Day);
                                l = Long.valueOf(sunset);
                                date = new Date(l * 1000L);
                                simpleDateFormat = new SimpleDateFormat("HH:mm");
                                Day = simpleDateFormat.format(date);
                                tv_sunset.setText("Mặt trời lặn: " + Day);
                                tv_country.setText(formatted_address);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(weather_location.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue_weather.add(stringRequest_weather);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(weather_location.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void AnhXa() {
        tv_maxmin = (TextView) findViewById(R.id.textView);
        tv_sunrise = (TextView) findViewById(R.id.tv_sunrise);
        tv_sunset = (TextView) findViewById(R.id.tv_sunset);
        txt_city = (EditText) findViewById(R.id.txt_seven_city);
        btn_search = (ImageButton) findViewById(R.id.btn_tim);
        btn_next = (Button) findViewById(R.id.btn_next);
        img_weather = (ImageView) findViewById(R.id.img_weather);
        tv_city = (TextView) findViewById(R.id.tv_name);
        tv_country = (TextView) findViewById(R.id.tv_city);
        tv_nhietdo = (TextView) findViewById(R.id.tv_nhietdo);
        tv_status = (TextView) findViewById(R.id.tv_line_status);
        tv_doam = (TextView) findViewById(R.id.tv_doam);
        tv_may = (TextView) findViewById(R.id.tv_may);
        tv_gio = (TextView) findViewById(R.id.tv_gio);
        tv_date = (TextView) findViewById(R.id.tv_hours);
        btn_back = (ImageButton) findViewById(R.id.btn_location_back);
    }
}
