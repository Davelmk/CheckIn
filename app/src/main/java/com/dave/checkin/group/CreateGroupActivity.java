package com.dave.checkin.group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.baidu.mapapi.SDKInitializer;

import com.dave.checkin.R;
import com.dave.checkin.beans.Group;
import com.dave.checkin.utils.Utils;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreateGroupActivity extends AppCompatActivity {
    private EditText group_title;
    private EditText group_description;

    //Bean
    private Group group=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        SDKInitializer.initialize(getApplicationContext());
        group_title=findViewById(R.id.group_title);
        group_description=findViewById(R.id.group_description);
    }

    private void initBean(){
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        String owner=sharedPreferences.getString("userID","未获取到UserID");
        group=new Group(group_title.getText().toString(),owner);
        group.setDescription(group_description.getText().toString());
    }

    private void submitGroup(){
        group.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    //获取Checin的ID
                    group.setId(s);
                    resultForAdd();
                }else{
                    Log.i("AddGroup","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void resultForAdd(){
        if (group!=null){
            Log.i("AddGroup",group.getId());
            Intent i=getIntent();
            i.putExtra("groupId",group.getId());
            setResult(Utils.RESULT_ADD_GROUP,i);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finish_add_group,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.finish_group:
                initBean();
                submitGroup();
                break;
            default:
                break;
        }
        return true;
    }
}
