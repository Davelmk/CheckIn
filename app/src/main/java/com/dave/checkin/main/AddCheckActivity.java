package com.dave.checkin.main;

import android.content.ContentValues;
import android.content.Intent;
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

import com.dave.checkin.R;
import com.dave.checkin.beans.CheckIn;
import com.dave.checkin.utils.CheckinDBHelper;
import com.dave.checkin.utils.Utils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class AddCheckActivity extends AppCompatActivity {
    private EditText checkin_title;
    private EditText checkin_description;
    private TextView position_description;
    private ImageView get_position;
    //SQLite
    private SQLiteDatabase db;
    //objectId
    private String objectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check);
        checkin_title=findViewById(R.id.checkin_title);
        checkin_description=findViewById(R.id.checkin_description);
        position_description=findViewById(R.id.position_description);
        get_position=findViewById(R.id.detail_position);
        get_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddCheckActivity","获取定位信息");
                position_description.setText("DLUT");
            }
        });
    }

    private void submitCheckin(){
        CheckIn checkIn=new CheckIn(checkin_title.getText().toString(),"Dave");
        checkIn.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    objectId=s;
                    writeToDB(s);
                }else{
                    Log.i("AddCheckActivity","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void writeToDB(String objectId){
        CheckinDBHelper dbHelper=new CheckinDBHelper(this,"CheckIn.db", null, 1);
        db=dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id",objectId);
        cv.put("title",checkin_title.getText().toString());
        cv.put("description",checkin_description.getText().toString());
        cv.put("num",0+"");
        cv.put("owner","Dave");
        cv.put("position","DLUT");
        db.insert("checkin",null,cv);
        cv.clear();
        db.close();
    }

    private void resultForAdd(){
        Intent i=getIntent();
        i.putExtra("objectId",objectId);
        setResult(Utils.RESULT_ADD_CHECKIN,i);
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
                resultForAdd();
                finish();
                break;
            case R.id.finish_check:
                submitCheckin();
                break;
            default:
                break;
        }
        return true;
    }
}
