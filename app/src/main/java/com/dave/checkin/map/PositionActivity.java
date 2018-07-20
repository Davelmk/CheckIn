package com.dave.checkin.map;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.dave.checkin.R;

public class PositionActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap baiduMap;

    private TextView pos_description;
    private double latitude;
    private double longitude;
    private String positionDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_position);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle("位置");
        }
        mapView=findViewById(R.id.bdmap);
        pos_description=findViewById(R.id.pos_description);
        getPosition();
        initMap();
        pos_description.setText(positionDescription);
    }
    private void getPosition(){
        Intent i=getIntent();
        latitude=i.getDoubleExtra("Latitude",0);
        longitude=i.getDoubleExtra("Longitude",0);
        positionDescription=i.getStringExtra("PositionDescription");
        Log.d("Position",latitude+","+longitude+","+positionDescription);
    }
    private void initMap(){
        baiduMap=mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);
        setPosition();
    }

    private void setPosition(){
        MyLocationData locationData=new MyLocationData.Builder()
                .direction(100)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        baiduMap.setMyLocationData(locationData);
        LatLng ll = new LatLng(latitude,
                longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
