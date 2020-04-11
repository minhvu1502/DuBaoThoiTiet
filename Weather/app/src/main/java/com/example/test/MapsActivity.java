package com.example.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.underscore.lodash.Json;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    EditText txt_tim;
    Button btn_tim;
    ImageButton btn_back;
    private GoogleMap mMap;
    String CityName = "";
    String kinhdo = "", vido = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        Places.initialize(getApplicationContext(), "AIzaSyBE90kLZE6B5l8Ba1cPlFsCpOdJOgbWgA4");
//        // Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        // Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
//        // Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                // remove old marker when add new marker
//                if (marker != null) marker.remove();
//                LatLng myLatLng = place.getLatLng();
//                marker = mMap.addMarker(
//                        new MarkerOptions().position(myLatLng).title(String.valueOf(place.getName())));
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
//            }
//
//            @Override
//            public void onError(@NonNull Status status) {
//
//            }
//        });
        AnhXa();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = txt_tim.getText().toString();
                GetLocation(data);
            }
        });
    }

    private void AnhXa() {
        txt_tim = (EditText) findViewById(R.id.txt_tim);
        btn_tim = (Button) findViewById(R.id.btn_tim);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
    }

    private void GetLocation(String city) {
        city = city.trim();
        city = city.replaceAll("\\s+", "");
        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + city + "&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=AIzaSyBE90kLZE6B5l8Ba1cPlFsCpOdJOgbWgA4";
        RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayCandidates = jsonObject.getJSONArray("candidates");
                    JSONObject jsonObject1 = jsonArrayCandidates.getJSONObject(0);
                    CityName = jsonObject1.getString("formatted_address");
                    JSONObject jsonObjectGeometry = jsonObject1.getJSONObject("geometry");
                    JSONObject jsonObjectLocation = jsonObjectGeometry.getJSONObject("location");
                    kinhdo = jsonObjectLocation.getString("lng");
                    vido = jsonObjectLocation.getString("lat");
                    LatLng ct = new LatLng(Double.valueOf(vido), Double.valueOf(kinhdo));
                    mMap.addMarker(new MarkerOptions().position(ct).title(CityName));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ct, 15));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, "Không tìm thấy", Toast.LENGTH_SHORT).show();
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
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Add a marker in Sydney and move the camera
        LatLng city = new LatLng(10.8230989, 106.6296638);
        mMap.addMarker(new MarkerOptions().position(city).title("Hồ Chí Minh, Việt Nam"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 15));
    }
}
