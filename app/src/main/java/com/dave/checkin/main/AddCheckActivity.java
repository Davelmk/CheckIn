package com.dave.checkin.main;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.dave.checkin.beans.User;
import com.dave.checkin.utils.CheckinDBHelper;
import com.dave.checkin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddCheckActivity extends AppCompatActivity {
    private EditText checkin_title;
    private EditText checkin_description;
    private TextView position_description;
    private ImageView get_position;
    //SQLite
    private SQLiteDatabase db;
    //Bean
    private CheckIn checkIn=null;
    //BaiduMap
    private LocationClient locationClient;
    private MyLocationListener locationListener;
    // 是否首次定位
    private boolean isFirstLoc = true;
    //位置描述+纬度+经度
    private String position=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check);
        SDKInitializer.initialize(getApplicationContext());
        checkin_title=findViewById(R.id.checkin_title);
        checkin_description=findViewById(R.id.checkin_description);
        position_description=findViewById(R.id.position_description);
        get_position=findViewById(R.id.get_position);
        get_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddCheckActivity","获取定位信息");
                isFirstLoc=true;
                getPosition();
            }
        });
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

    private void initBean(){
        checkIn=new CheckIn(checkin_title.getText().toString(),"Dave");
        checkIn.setDescription(checkin_description.getText().toString());
        checkIn.setPosition(position);
    }

    private void submitCheckin(){
        checkIn.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    //获取CheckIn的ID
                    checkIn.setId(s);
                    //写入数据库
                    writeToDB();
                }else{
                    Log.i("AddCheckActivity","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void submitUserCheckin(){
        /**
         * 目标更改：
         * 合并User-Checkin到User表，则可以根据UserID直接更新用户的签到列表
         */
        List<String> checkinList=getListFromBmob();
        //获取User的ID
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        String userid=sharedPreferences.getString("userID","");

        //更新User的Checkin
        User user=new User();
        user.setCheckInList(checkinList);
        user.setAdmin(true);
        user.update(userid,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Log.d("AddCheckActivity","上传User-Checkin关系List");
                    returnMainView();
                }else {
                    Log.i("AddCheckActivity",e.getErrorCode()+","+e.getMessage());
                }
            }
        });
    }

    /**
     * 待定：
     * 主界面的添加按钮->为owner的所有群组添加同样的签到活动
     */
    private void updateGroupCheckin(){

    }

    private List<String> getListFromBmob(){
        List<String> checkinList=new ArrayList<>();
        CheckinDBHelper dbHelper=new CheckinDBHelper(this,"CheckIn.db", null, 1);
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("checkin", null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                checkinList.add(id);
            } while (cursor.moveToNext());
        }
        Log.d("checkinList","checkinList:"+checkinList.size());
        cursor.close();
        return checkinList;
    }

    private void writeToDB(){
        CheckinDBHelper dbHelper=new CheckinDBHelper(this,"CheckIn.db", null, 1);
        db=dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id",checkIn.getId());
        cv.put("title",checkIn.getTitle());
        cv.put("description",checkIn.getDescription());
        cv.put("num",checkIn.getNum());
        cv.put("owner",checkIn.getOwner());
        cv.put("position",position);
        db.insert("checkin",null,cv);
        cv.clear();
        db.close();
        submitUserCheckin();
    }

    private void returnMainView(){
        Toast.makeText(AddCheckActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
        resultForAdd();
        finish();
    }

    private void resultForAdd(){
        if (checkIn!=null){
            Intent i=getIntent();
            i.putExtra("objectId",checkIn.getId());
            setResult(Utils.RESULT_ADD_CHECKIN,i);
        }
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
