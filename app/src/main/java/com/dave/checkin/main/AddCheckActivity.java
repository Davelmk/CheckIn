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

import com.dave.checkin.R;
import com.dave.checkin.beans.CheckIn;
import com.dave.checkin.beans.UserCheckin;
import com.dave.checkin.utils.CheckinDBHelper;
import com.dave.checkin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddCheckActivity extends AppCompatActivity {
    private EditText checkin_title;
    private EditText checkin_description;
    private TextView position_description;
    private ImageView get_position;
    //SQLite
    private SQLiteDatabase db;
    //Bean
    private CheckIn checkIn=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check);
        checkin_title=findViewById(R.id.checkin_title);
        checkin_description=findViewById(R.id.checkin_description);
        position_description=findViewById(R.id.position_description);
        get_position=findViewById(R.id.get_position);
        get_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddCheckActivity","获取定位信息");
                getPosition();
            }
        });
    }

    private void getPosition(){
        //获取定位信息
        position_description.setText("创建地点:DLUT");
    }

    private void initBean(){
        checkIn=new CheckIn(checkin_title.getText().toString(),"Dave");
        checkIn.setDescription(checkin_description.getText().toString());
    }

    private void submitCheckin(){
        checkIn.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    checkIn.setId(s);
                    writeToDB();
                }else{
                    Log.i("AddCheckActivity","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void submitUserCheckin(){
        /**
         * User-Checkin需要根据objectId更新
         * 目标更改：
         * 合并User-Checkin到User表，则可以根据UserID直接更新用户的签到列表
         */
        List<String> checkinList=getListFromDB();
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        String userid=sharedPreferences.getString("userID","");
        UserCheckin userCheckin=new UserCheckin(userid,checkinList);
        userCheckin.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    Log.d("AddCheckActivity","上传User-Checkin关系");
                    returnMainView();
                }else {
                    Log.i("AddCheckActivity",e.getErrorCode()+","+e.getMessage());
                }
            }
        });
    }

    private List<String> getListFromDB(){
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
        cv.put("position",checkIn.getPosition());
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
}
