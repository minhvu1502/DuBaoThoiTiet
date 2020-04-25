package com.example.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.R;
import com.example.test.models.Weather;
import com.example.test.adapter.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//http://api.openweathermap.org/data/2.5/forecast?q=hanoi&units=metric&cnt=7&appid=92c6161e0d9ddd64a865f69b71a89c31
public class Second extends AppCompatActivity {
    String city_name;
    ImageButton btn_back;
    TextView tv_city, tv_err;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Weather> weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        setTitle("Các giờ tiếp theo");
        AnhXa();
//        tv_err.setVisibility(View.INVISIBLE);
        final Intent intent = getIntent();
        city_name = intent.getStringExtra("city_name");
        if (city_name == null) {
            tv_city.setText("Hà Nội");
            Get3HoursData("Hà Nội");
        } else {
            Get3HoursData(city_name);
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void AnhXa() {
        tv_city = (TextView) findViewById(R.id.tv_name);
        btn_back = (ImageButton) findViewById(R.id.btn_seven_back);
        listView = (ListView) findViewById(R.id.list_seven);
        weather = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(Second.this, weather);
        listView.setAdapter(customAdapter);
//        tv_err = (TextView)findViewById(R.id.tv_err);
    }

    private void Get3HoursData(String data) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + data + "&units=metric&cnt=7&appid=92c6161e0d9ddd64a865f69b71a89c31&lang=vi";
        RequestQueue requestQueue = Volley.newRequestQueue(Second.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject1 = new JSONObject(response);
                    JSONObject jsonObjectCity = jsonObject1.getJSONObject("city");
                    String name = jsonObjectCity.getString("name");
                    tv_city.setText(name);
                    JSONArray jsonArray = jsonObject1.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectList = jsonArray.getJSONObject(i);
                        String day = jsonObjectList.getString("dt");
                        long l = Long.valueOf(day);
                        Date date = new Date(l * 1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm");
                        String Day = simpleDateFormat.format(date);

                        JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                        String temp_min = jsonObjectMain.getString("temp_min");
                        String temp_max = jsonObjectMain.getString("temp_max");

                        //Làm tròn nhiệt độ
                        Double a = Double.valueOf(temp_min);
                        Double b = Double.valueOf(temp_max);

                        String Temp_min = String.valueOf(a.intValue());
                        String Temp_max = String.valueOf(b.intValue());

                        JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");

                        //Gán giá trị
                        weather.add(new Weather(Day, description, icon, Temp_max, Temp_min));
                    }
                    customAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                ArrayList<String> arrayList = new ArrayList<String>();
//                arrayList.add("Không có dữ liệu về thành phố này");
//                ArrayAdapter arrayAdapter = new ArrayAdapter(Second.this, R.layout.support_simple_spinner_dropdown_item, arrayList);
//                listView.setAdapter(arrayAdapter);
                tv_city.setText("Không có dữ liệu cho thành phố này");
            }
        });
        requestQueue.add(stringRequest);
    }
}
