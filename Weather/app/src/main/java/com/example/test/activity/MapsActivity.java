package com.example.test.activity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
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
import com.example.test.adapter.DBhelper;
import com.example.test.models.City;
import com.example.test.models.Weather_SevenDay;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {
    private static final int REQ_CODE_SPEECH_INPUT = 1502;
    EditText txt_tim;
    ImageButton btn_tim, btn_earth, btn_type, btn_back;
    ImageView img_icon;
    ArrayList<Weather_SevenDay> weather_item = new ArrayList<Weather_SevenDay>();
    TextView tv_city, tv_country, tv_date, tv_temp, tv_max_min;
    Button btn_mapdetail;
    private GoogleMap mMap;
    String CityName = "", formatted_address, Text, kinhdo = "", vido = "";
    ImageButton btn_mic;
    Button btn_map_share;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;
    String id_city;
    Button btn_map_save;
    DBhelper dBhelper = new DBhelper(MapsActivity.this);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        AnhXa();

        shareDialog = new ShareDialog(MapsActivity.this);
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
        btn_earth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                btn_earth.setEnabled(false);
                btn_type.setEnabled(true);
            }
        });
        btn_map_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long resultAdd = dBhelper.Insert(2,"Thành phố hồ chí minh", "50", "30");
                if (resultAdd == -1) {
                    Toast.makeText(MapsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapsActivity.this, "Insert Success", Toast.LENGTH_SHORT).show();
                }
                ArrayList<City> cityArrayList = new ArrayList<>();
                cityArrayList = dBhelper.getAllWords();
            }
        });
        btn_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                btn_earth.setEnabled(true);
                btn_type.setEnabled(false);
            }
        });
        btn_tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weather_item.clear();
                String data = txt_tim.getText().toString();
                if (data.equals("")) {
                    Toast.makeText(MapsActivity.this, "Hãy Nhập Tên Thành Phố", Toast.LENGTH_SHORT).show();
                } else {
                    GetLocation(data);
                }
            }
        });
        btn_map_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ShareDialog.canShow(ShareLinkContent.class)){
                    shareLinkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://openweathermap.org/city/"+id_city))
                            .build();
                };
                shareDialog.show(shareLinkContent);
            }
        });
        btn_mapdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, Seven_day_details.class);
                intent.putExtra("city_name", CityName);
                intent.putExtra("minmax", weather_item.get(0).getFeels_like());
                intent.putExtra("country", weather_item.get(0).getCountry());
                intent.putExtra("Day", weather_item.get(0).getDay());
                intent.putExtra("status", weather_item.get(0).getStatus());
                intent.putExtra("image", weather_item.get(0).getImage());
                intent.putExtra("temp", weather_item.get(0).getMax());
                intent.putExtra("doam", weather_item.get(0).getDoam());
                intent.putExtra("gio", weather_item.get(0).getGio());
                intent.putExtra("may", weather_item.get(0).getMay());
                intent.putExtra("sunrise", weather_item.get(0).getSunrise());
                intent.putExtra("sunset", weather_item.get(0).getSunset());
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
                    txt_tim.setText(result.get(0));
                    GetLocation(result.get(0));
                }
                break;
            }

        }
    }
    private void AnhXa() {
        btn_mic = (ImageButton)findViewById(R.id.btn_mic);
        btn_earth = (ImageButton) findViewById(R.id.btn_earth);
        btn_type = (ImageButton) findViewById(R.id.btn_type);
        txt_tim = (EditText) findViewById(R.id.txt_seven_city);
        btn_tim = (ImageButton) findViewById(R.id.btn_tim);
        btn_back = (ImageButton) findViewById(R.id.btn_seven_back);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        tv_city = (TextView) findViewById(R.id.tv_map_city);
        tv_country = (TextView) findViewById(R.id.tv_city);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_temp = (TextView) findViewById(R.id.tvmap_temp);
        tv_max_min = (TextView) findViewById(R.id.tvmap_minmax);
        btn_mapdetail = (Button) findViewById(R.id.btn_map_detail);
        btn_map_share = (Button) findViewById(R.id.btn_map_share);
        btn_map_save = (Button) findViewById(R.id.btn_map_save);
    }

    public String Validate_Place(String city) {
        city = city.trim();
        city = city.replaceAll("\\s+", "");
        return city;
    }
    protected void onStart() {
        super.onStart();
        dBhelper.openDB();
    }

    protected void onStop() {
        super.onStop();
        dBhelper.closeDB();
    }
    private void GetLocation(String city) {
        city = Validate_Place(city);
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + city + "&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyD4oQg9klcCD0fVn-2sb5wbPrNNZs4bhJ4";
        RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
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
                    LatLng ct = new LatLng(Double.valueOf(vido), Double.valueOf(kinhdo));
                    mMap.addMarker(new MarkerOptions().position(ct).title(CityName));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ct, 15));
                    //                bien luu cac request gui len server
                    RequestQueue requestQueue_weather = Volley.newRequestQueue(MapsActivity.this);
                    //Doc du lieu duong dan
                    String url_weather = "https://api.openweathermap.org/data/2.5/weather?lat=" + vido + "&lon=" + kinhdo + "&units=metric&appid=92c6161e0d9ddd64a865f69b71a89c31&lang=vi";
                    final StringRequest stringRequest_weather = new StringRequest(Request.Method.GET, url_weather, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //nhận dữ liệu trả về từ api
                                JSONObject jsonObject = new JSONObject(response);
                                tv_country.setText(formatted_address);
                                tv_city.setText(CityName);
                                id_city = jsonObject.getString("id");
                                String day = jsonObject.getString("dt");
                                long l = Long.valueOf(day);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM HH:mm");
                                String Day = simpleDateFormat.format(date);
                                tv_date.setText(Day);
                                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                                //get status
                                String status = jsonObjectWeather.getString("description");

                                //get image
                                String icon = jsonObjectWeather.getString("icon");

                                Picasso.get().load("http://openweathermap.org/img/wn/" + icon + ".png").into(img_icon);
                                JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                                String nhietdo = jsonObjectMain.getString("temp");

                                //get temp max, temp min
                                String temp_max = jsonObjectMain.getString("temp_max");
                                String temp_min = jsonObjectMain.getString("temp_min");
                                //Lam tron
                                Double a = Double.valueOf(temp_max);
                                Double b = Double.valueOf(temp_min);

                                String Temp_max = String.valueOf(a.intValue());
                                String Temp_min = String.valueOf(b.intValue());

                                tv_max_min.setText(Temp_min + "°/" + Temp_max + "°");

                                String doam = jsonObjectMain.getString("humidity");

                                Double x = Double.valueOf(nhietdo);
                                String Nhietdo = String.valueOf(x.intValue());

                                tv_temp.setText(Nhietdo + "°");

                                JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                                String wind = jsonObjectWind.getString("speed");

                                JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                                String cloud = jsonObjectCloud.getString("all");

                                JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                                String sunrise = jsonObjectSys.getString("sunrise");
                                String sunset = jsonObjectSys.getString("sunset");
                                l = Long.valueOf(sunrise);
                                date = new Date(l * 1000L);
                                simpleDateFormat = new SimpleDateFormat("HH:mm");
                                String Sunrise = simpleDateFormat.format(date);
                                l = Long.valueOf(sunset);
                                date = new Date(l * 1000L);
                                simpleDateFormat = new SimpleDateFormat("HH:mm");
                                String Sunset = simpleDateFormat.format(date);
                                String min_max_feelslike = temp_max + "° / " + temp_min + "°";
                                weather_item.add(new Weather_SevenDay(formatted_address, Day, status, icon, nhietdo, min_max_feelslike, doam, wind, cloud, Sunrise, Sunset));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MapsActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue_weather.add(stringRequest_weather);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MapsActivity.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    // Callback được gợi sau khi Map đã sẵn sàng
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
//        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        btn_earth.setEnabled(true);
        btn_type.setEnabled(false);
        // Add a marker in Ho Chi Minh and move the camera
//        LatLng city = new LatLng(10.8230989, 106.6296638);
//        mMap.addMarker(new MarkerOptions().0(city).title("Hồ Chí Minh, Việt Nam"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 15));
        GetLocation("HàNội");
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        String myLat = String.valueOf(location.getLatitude());
        String myLng = String.valueOf(location.getLongitude());
        Toast.makeText(this, "Kinh độ: " + myLng + " Vĩ độ: " + myLat, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        weather_item.clear();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String myLat = String.valueOf(location.getLatitude());
                        String myLng = String.valueOf(location.getLongitude());
                        //bien luu cac request gui len server
                        RequestQueue requestQueue_weather = Volley.newRequestQueue(MapsActivity.this);
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
                                Toast.makeText(MapsActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        });
                        requestQueue_weather.add(stringRequest_weather);
                    }
                });
        return false;
    }
}
