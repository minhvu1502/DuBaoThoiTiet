package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeMenu extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);
        listView = (ListView) findViewById(R.id.listView);

        ArrayList<Data> list1 = new ArrayList<Data>();
        list1.add(new Data(R.drawable.rain, "Thời tiết hiện tại của thiết bị"));
        list1.add(new Data(R.drawable.rain, "Xem thời tiết theo địa điểm nhập bất kì"));
        list1.add(new Data(R.drawable.rain, "Xem thời tiết theo địa điểm nhập bất kì và dự báo các ngày kế tiếp"));
        list1.add(new Data(R.drawable.rain, "Xem thời tiết trên Google Map"));
        CustomAdapter2 adapter = new CustomAdapter2(HomeMenu.this, R.layout.activity_custom_l_v, list1);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(HomeMenu.this, "111" + position, Toast.LENGTH_SHORT).show();
                } else if (position == 1) {
                    intent = new Intent(HomeMenu.this, MainActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    intent = new Intent(HomeMenu.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(HomeMenu.this, MapsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}