package com.dave.checkin.map;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.dave.checkin.R;
import com.dave.checkin.beans.CheckIn;
import com.dave.checkin.beans.User;
import com.dave.checkin.utils.Utils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class SignActivity extends AppCompatActivity {
    private MapView mapView;
    private BaiduMap baiduMap;
    private TextView pos_description;
    // 是否首次定位
    private boolean isFirstLoc = true;
    private LocationClient locationClient;
    private MyLocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_sign);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle("签到确认");
        }
        mapView=findViewById(R.id.bdmap);
        pos_description=findViewById(R.id.pos_description);
        initMap();
    }

    private void initMap(){
        baiduMap=mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setMyLocationEnabled(true);
        locationClient=new LocationClient(getApplicationContext());
        setLocationType();
        locationListener=new MyLocationListener();
        locationClient.registerLocationListener(locationListener);
        locationClient.start();
        locationClient.requestLocation();

    }
    private void setLocationType(){
        LocationClientOption option=new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd0911");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIsNeedLocationDescribe(true);
        option.setIgnoreKillProcess(false);
        option.setEnableSimulateGps(false);
        locationClient.setLocOption(option);
    }

    private void confirmCheckIn(){
        Intent intent=getIntent();
        String checkId=intent.getStringExtra("checkId");
        String num=intent.getStringExtra("num");
        CheckIn checkIn=new CheckIn();
        checkIn.setNum(Integer.valueOf(num)+1+"");
        checkIn.update(checkId,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    resultForSign();
                }else {
                    Log.d("更新签到",e.getMessage());
                }
            }
        });
    }

    private void resultForSign(){
        setResult(Utils.RESULT_SIGN);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.confirm_checkin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.confirm_checkin:
                confirmCheckIn();
                break;
            default:
                break;
        }
        return true;
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //设置Text描述
            pos_description.setText(location.getLocationDescribe());

            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude(); //获取经度信息
            MyLocationData locationData=new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            baiduMap.setMyLocationData(locationData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }else {
                //停止定位
                locationClient.stop();
            }
        }
    }
}
