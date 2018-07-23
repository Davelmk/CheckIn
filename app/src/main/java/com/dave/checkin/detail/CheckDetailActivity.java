package com.dave.checkin.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.SignMemberAdapter;
import com.dave.checkin.beans.CheckIn;
import com.dave.checkin.beans.User;
import com.dave.checkin.map.PositionActivity;
import com.dave.checkin.map.SignActivity;
import com.dave.checkin.topic.TopicActivity;
import com.dave.checkin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class CheckDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView detail_position;
    private TextView detail_topic;
    private TextView detail_owner;
    private TextView detail_num;
    private TextView position_description;
    private TextView detail_createdDate;
    private TextView detail_description;

    private String checkin_id;
    private String num;
    private String position;

    private RecyclerView recyclerView;
    private List<User> users;
    private List<String> userList;
    private SignMemberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        initComponents();
        getDetailFromBmob();
    }

    private void initComponents(){
        Intent i=getIntent();
        String title=i.getStringExtra("title");
        checkin_id=i.getStringExtra("id");
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setTitle(title);
        }
        detail_position=findViewById(R.id.detail_position);
        detail_position.setOnClickListener(this);
        detail_topic=findViewById(R.id.detail_topic);
        detail_topic.setOnClickListener(this);
        //TextView
        detail_owner=findViewById(R.id.detail_owner);
        detail_num=findViewById(R.id.detail_num);
        detail_description=findViewById(R.id.detail_description);
        detail_createdDate=findViewById(R.id.detail_createdDate);
        position_description=findViewById(R.id.position_description);

        //recyclerView
        users=new ArrayList<User>();
        recyclerView=findViewById(R.id.sign_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SignMemberAdapter(users);
        recyclerView.setAdapter(adapter);
    }

    private void setComponents(String owner,String num,String description,String position,String createdDate){
        detail_owner.setText("创建者: "+owner);
        detail_num.setText("已签到: "+num+"人");
        detail_createdDate.setText("创建时间: "+createdDate);
        position_description.setText(position);
        detail_description.setText(description);
    }
    private void getDetailFromBmob(){
        BmobQuery<CheckIn> query=new BmobQuery<>();
        Log.d("MainActivity", checkin_id);
        query.getObject(checkin_id, new QueryListener<CheckIn>() {
            @Override
            public void done(CheckIn checkIn, BmobException e) {
                if (e == null) {
                    position= checkIn.getPosition();
                    Log.d("position",position);
                    Log.d("position",Utils.getPositionDescription(position));
                    num= checkIn.getNum();
                    userList=checkIn.getSignList();
                    setComponents(checkIn.getOwnerName(),num,
                            checkIn.getDescription(),Utils.getPositionDescription(position),checkIn.getCreatedAt());
                    getSignMember(userList);
                } else {
                    Log.d("MainActivity", e.getMessage());
                }
            }
        });
    }

    private void getSignMember(List<String> idList){
        if (idList==null){
            Log.d("获取签到人员","无人签到");
        }else {
            BmobQuery<User> query=new BmobQuery<>();
            query.addWhereContainedIn("objectId",idList);
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e==null){
                        users=list;
                        adapter.notifyDataSetChanged();
                    }else {
                        Log.d("获取签到人员","查询错误:"+e.getMessage());
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.checkin:
                checckUserLevel();
                break;
            default:
                break;
        }
        return true;
    }

    private void checckUserLevel(){
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        boolean isAdmin=sharedPreferences.getBoolean("isAdmin",false);
        if (isAdmin){
            Toast.makeText(CheckDetailActivity.this,"管理员不能签到",Toast.LENGTH_SHORT).show();
        }else {
            goToCheckSign();
        }
    }

    private void goToCheckPosition(){
        Intent intent=new Intent(CheckDetailActivity.this, PositionActivity.class);
        intent.putExtra("Latitude",Utils.getLatitude(position));
        intent.putExtra("Longitude",Utils.getLongitude(position));
        intent.putExtra("PositionDescription",Utils.getPositionDescription(position));
        startActivity(intent);
    }
    private void goToCheckSign(){
        Intent intent=new Intent(CheckDetailActivity.this, SignActivity.class);
        intent.putExtra("checkId",checkin_id);
        intent.putExtra("num",num);
        startActivityForResult(intent,Utils.REQUEST_SIGN);
    }
    private void goToTopicActivity(){
        Intent intent=new Intent(CheckDetailActivity.this, TopicActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.REQUEST_SIGN && resultCode == Utils.RESULT_SIGN) {
            Log.d("MainActivity", "成功添加签到");
            updateTextView();
            updateRecyclerView();
        }
    }

    private void updateRecyclerView(){
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        String username=sharedPreferences.getString("userName","Dave");
        users.add(0,new User(username));
        adapter.notifyDataSetChanged();
    }

    private void updateTextView(){
        int s=Integer.valueOf(num)+1;
        detail_num.setText("已签到: "+s+"人");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_position:
                Log.d("Detail","查看创建位置");
                goToCheckPosition();
                break;
            case R.id.detail_topic:
                Log.d("Detail","查看话题讨论");
                goToTopicActivity();
                break;
            default:
                break;
        }
    }
}
