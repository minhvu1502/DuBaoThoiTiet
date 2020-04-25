package com.example.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import com.example.test.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 1502;
    EditText txt_city;
    Button btn_next;
    ImageView img_weather;
    TextView tv_city, tv_country, tv_nhietdo, tv_status, tv_doam, tv_may, tv_gio, tv_date, tv_sunrise, tv_sunset, tv_maxmin;
    ImageButton btn_search, btn_location, btn_back;
    String city_name, kinhdo = "", vido = "", formatted_address;
    ImageButton btn_mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("Dự Báo Thời Tiết");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        AnhXa();
        GetLocation("HàNội");
        btn_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechToText();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = txt_city.getText().toString();
                if (city.equals("")) {
                    Toast.makeText(MainActivity.this, "Hãy nhập tên thành phố", Toast.LENGTH_SHORT).show();
                } else {
                    city = Validate_places(city);
                    GetLocation(city);
                }
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

    public void SpeechToText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Máy của bạn không hỗ trợ mic",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txt_city.setText(result.get(0));
                    GetLocation(result.get(0));
                }
                break;
            }

        }
    }

    public String Validate_places(String data) {
        data = data.trim();
        data = data.replaceAll("\\s+", "");
        return data;
    }

    private void GetLocation(String city) {
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + city + "&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyD4oQg9klcCD0fVn-2sb5wbPrNNZs4bhJ4";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayCandidates = jsonObject.getJSONArray("candidates");
                    JSONObject jsonObject1 = jsonArrayCandidates.getJSONObject(0);
                    JSONObject jsonObjectGeometry = jsonObject1.getJSONObject("geometry");
                    String name = jsonObject1.getString("name");
                    city_name = name;
                    String formatted_addr = jsonObject1.getString("formatted_address");
                    String[] country = formatted_addr.split(", ");
                    formatted_address = country[country.length - 1];
                    JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");
                    String _kinhdo = jsonObjectLocation.getString("lng");
                    String _vido = jsonObjectLocation.getString("lat");
                    kinhdo = _kinhdo;
                    vido = _vido;
//                bien luu cac request gui len server
                    RequestQueue requestQueue_weather = Volley.newRequestQueue(MainActivity.this);
                    //Doc du lieu duong dan
                    String url_weather = "https://api.openweathermap.org/data/2.5/weather?lat=" + vido + "&lon=" + kinhdo + "&units=metric&appid=92c6161e0d9ddd64a865f69b71a89c31&lang=vi";
                    final StringRequest stringRequest_weather = new StringRequest(Request.Method.GET, url_weather, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //nhận dữ liệu trả về từ api
                                JSONObject jsonObject = new JSONObject(response);
                                String day = jsonObject.getString("dt");
                                tv_city.setText(city_name);
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
                            Toast.makeText(MainActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void AnhXa() {
        btn_mic = (ImageButton)findViewById(R.id.btn_mic);
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
        btn_back = (ImageButton) findViewById(R.id.btn_seven_back);
    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent intent = new Intent();
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            intent.setClass(MainActivity.this, HomeMenu.class);
//            startActivity(intent);
//            MainActivity.this.finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
