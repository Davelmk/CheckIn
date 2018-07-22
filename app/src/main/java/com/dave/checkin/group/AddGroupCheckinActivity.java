package com.dave.checkin.group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.dave.checkin.R;
import com.dave.checkin.beans.CheckIn;
import com.dave.checkin.beans.Group;
import com.dave.checkin.beans.User;
import com.dave.checkin.main.AddCheckActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddGroupCheckinActivity extends AppCompatActivity {
    private EditText checkin_title;
    private EditText checkin_description;
    private TextView position_description;
    private ImageView get_position;
    //Bean
    private CheckIn checkIn=null;
    //BaiduMap
    private LocationClient locationClient;
    private MyLocationListener locationListener;
    // 是否首次定位
    private boolean isFirstLoc = true;
    //位置描述+纬度+经度
    private String position=null;

    private String ownerId;
    private String groupId;
    private String ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_checkin);
        SDKInitializer.initialize(getApplicationContext());
        checkin_title=findViewById(R.id.checkin_title);
        checkin_description=findViewById(R.id.checkin_description);
        position_description=findViewById(R.id.position_description);
        get_position=findViewById(R.id.get_position);
        get_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddGroupCheckinActivity","获取定位信息");
                isFirstLoc=true;
                getPosition();
            }
        });
    }

    private void initBean(){
        Intent intent=getIntent();
        ownerId=intent.getStringExtra("ownerId");
        groupId=intent.getStringExtra("groupId");
        Log.d("添加群组签到","groupId:"+groupId);

        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        ownerName=sharedPreferences.getString("userName","Dave");

        checkIn=new CheckIn(checkin_title.getText().toString(),ownerId);
        checkIn.setDescription(checkin_description.getText().toString());
        checkIn.setPosition(position);
        checkIn.setOwner(ownerId);
        checkIn.setOwnerName(ownerName);
    }


    private void submitCheckin(){
        checkIn.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    //获取CheckIn的ID
                    checkIn.setId(s);
                    getCheckinListFromGroup();
                }else{
                    Log.i("AddCheckActivity","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void getCheckinListFromGroup(){
        BmobQuery<Group> query=new BmobQuery<>();
        query.getObject(groupId, new QueryListener<Group>() {
            @Override
            public void done(Group group, BmobException e) {
                if (e==null){
                    updateGroupCheckin(group.getCheckin());
                }else {
                    Log.d("添加群组签到",e.getMessage());
                }
            }
        });
    }

    private void updateGroupCheckin(List<String> list){
        Group group=new Group();
        if (list==null){
            Log.d("添加群组签到","list==null");
            List<String> stringList=new ArrayList<>();
            stringList.add(checkIn.getId());
            Log.d("添加群组签到",checkIn.getId());
            group.setCheckin(stringList);
        }else {
            Log.d("群组签到不为空",checkIn.getId());
            list.add(checkIn.getId());
            group.setCheckin(list);
        }
        group.update(groupId,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.d("添加群组签到","创建成功");
                    returnMainView();
                }else {
                    Log.d("添加群组签到",e.getMessage());
                }
            }
        });
    }
    private void returnMainView(){
        Toast.makeText(AddGroupCheckinActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
        finish();
    }
    private void getPosition(){
        //获取定位信息
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_checkin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.finish_check:
                initBean();
                submitCheckin();
                break;
            default:
                break;
        }
        return true;
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (isFirstLoc){
                double latitude = location.getLatitude();    //获取纬度信息
                double longitude = location.getLongitude(); //获取经度信息
                String description=location.getLocationDescribe();
                position_description.setText(description);
                position=description+","+latitude+","+longitude;
                isFirstLoc=false;
            }else {
                locationClient.stop();
            }
        }
    }
}
