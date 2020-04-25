package com.example.test.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.test.R;
import com.example.test.models.Weather_SevenDay;
import com.example.test.adapter.Adapter_SevenDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class seven_day_forecast extends AppCompatActivity {
    private static final int REQ_CODE_SPEECH_INPUT = 1502;
    ListView list_;
    TextView tv_city;
    EditText txt_city;
    ImageButton btn_back, btn_search;

    ArrayList<Weather_SevenDay> weather_item = new ArrayList<Weather_SevenDay>();
    String kinhdo = "", vido = "", city_name, formatted_address;
    ImageButton btn_mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven_day_forecast);
//        list_.setVisibility(View.INVISIBLE);
//        tv_city.setVisibility(View.INVISIBLE);
        AnhXa();
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
                weather_item.clear();
                String city = txt_city.getText().toString();
                if (city.equals("")) {
                    Toast.makeText(seven_day_forecast.this, "Hãy nhập tên thành phố", Toast.LENGTH_SHORT).show();
                } else {
                    city = Validate_places(city);
                    GetLocation(city);
                }
            }
        });
        list_.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(seven_day_forecast.this, Seven_day_details.class);
                intent.putExtra("city_name", city_name);
                intent.putExtra("minmax", weather_item.get(position).getFeels_like());
                intent.putExtra("country", formatted_address);
                intent.putExtra("Day", weather_item.get(position).getDay());
                intent.putExtra("status", weather_item.get(position).getStatus());
                intent.putExtra("image", weather_item.get(position).getImage());
                intent.putExtra("temp", weather_item.get(position).getMax());
                intent.putExtra("doam", weather_item.get(position).getDoam());
                intent.putExtra("gio", weather_item.get(position).getGio());
                intent.putExtra("may", weather_item.get(position).getMay());
                intent.putExtra("sunrise", weather_item.get(position).getSunrise());
                intent.putExtra("sunset", weather_item.get(position).getSunset());
                startActivity(intent);
            }
        });
    }
    public void SpeechToText(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); // Gọi hộp thoại lắng nghe người dùng nói từ thư viện android
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
    private void GetLocation(String city) {
        city = city.trim();
        city = city.replaceAll("\\s+", "");
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + city + "&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyD4oQg9klcCD0fVn-2sb5wbPrNNZs4bhJ4";
        RequestQueue requestQueue = Volley.newRequestQueue(seven_day_forecast.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayCandidates = jsonObject.getJSONArray("candidates");
                    JSONObject jsonObject1 = jsonArrayCandidates.getJSONObject(0);
                    String name = jsonObject1.getString("name");
                    city_name = name;
                    String formatted_addr = jsonObject1.getString("formatted_address");
                    String[] country = formatted_addr.split(", ");
                    formatted_address = country[country.length - 1];
                    JSONObject jsonObjectGeometry = jsonObject1.getJSONObject("geometry");
                    JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");
                    kinhdo = jsonObjectLocation.getString("lng");
                    vido = jsonObjectLocation.getString("lat");
//                bien luu cac request gui len server
                    RequestQueue requestQueue_weather = Volley.newRequestQueue(seven_day_forecast.this);
                    //Doc du lieu duong dan
                    String url_weather = "https://api.openweathermap.org/data/2.5/onecall?lat=" + vido + "&lon=" + kinhdo + "&units=metric&appid=92c6161e0d9ddd64a865f69b71a89c31&lang=vi";
                    final StringRequest stringRequest_weather = new StringRequest(Request.Method.GET, url_weather, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                ArrayList<Weather_SevenDay> weather = new ArrayList<Weather_SevenDay>();
                                //nhận dữ liệu trả về từ api
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArrayDaily = jsonObject.getJSONArray("daily");
                                for (int i = 0; i < jsonArrayDaily.length(); i++) {
                                    JSONObject jsonObjectDaily = jsonArrayDaily.getJSONObject(i);
                                    //get day
                                    String day = jsonObjectDaily.getString("dt");
                                    long l = Long.valueOf(day);
                                    Date date = new Date(l * 1000L);
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy");
                                    String Day = simpleDateFormat.format(date);

                                    //get temp
                                    JSONObject jsonObjectTemp = jsonObjectDaily.getJSONObject("temp");
                                    String Temp_max, Temp_min;
                                    Temp_max = jsonObjectTemp.getString("max");
                                    Temp_min = jsonObjectTemp.getString("min");
                                    String temp = jsonObjectTemp.getString("day");
                                    //Làm tròn nhiệt độ
                                    Double a = Double.valueOf(Temp_min);
                                    Double b = Double.valueOf(Temp_max);

                                    String temp_max, temp_min;
                                    temp_min = String.valueOf(a.intValue());
                                    temp_max = String.valueOf(b.intValue());

                                    //get wind, cloud, humidity

                                    String doam = jsonObjectDaily.getString("humidity");

                                    String gio = jsonObjectDaily.getString("wind_speed");

                                    String may = jsonObjectDaily.getString("clouds");

                                    JSONArray jsonArrayWeather = jsonObjectDaily.getJSONArray("weather");
                                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                    String description = jsonObjectWeather.getString("description");
                                    String icon = jsonObjectWeather.getString("icon");

                                    String sunrise = jsonObjectDaily.getString("sunrise");
                                    String sunset = jsonObjectDaily.getString("sunset");
                                    l = Long.valueOf(sunrise);
                                    date = new Date(l * 1000L);
                                    simpleDateFormat = new SimpleDateFormat("HH:mm");
                                    String Sunrise = simpleDateFormat.format(date);

                                    l = Long.valueOf(sunset);
                                    date = new Date(l * 1000L);
                                    simpleDateFormat = new SimpleDateFormat("HH:mm");
                                    String Sunset = simpleDateFormat.format(date);
                                    //Gán giá trị
                                    String min_max_feelslike = temp_max + "° / " + temp_min + "°";
                                    Adapter_SevenDay customAdapter;
                                    weather.add(new Weather_SevenDay(Day, description, icon, temp_max, temp_min));
                                    weather_item.add(new Weather_SevenDay(formatted_address, Day, description, icon, temp, min_max_feelslike, doam, gio, may, Sunrise, Sunset));
                                    customAdapter = new Adapter_SevenDay(seven_day_forecast.this, weather);
                                    list_.setAdapter(customAdapter);
                                }
                                tv_city.setText(city_name);
                                tv_city.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(seven_day_forecast.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(seven_day_forecast.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    public void AnhXa() {
        btn_mic = (ImageButton)findViewById(R.id.btn_mic);
        list_ = (ListView) findViewById(R.id.list_seven);
        tv_city = (TextView) findViewById(R.id.tv_sevenday_city);
        txt_city = (EditText) findViewById(R.id.txt_seven_city);
        btn_search = (ImageButton) findViewById(R.id.btn_tim);
        btn_back = (ImageButton) findViewById(R.id.btn_seven_back);
    }

    public String Validate_places(String data) {
        data = data.trim();
        data = data.replaceAll("\\s+", "");
        return data;
    }
}
