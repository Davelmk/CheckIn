package com.dave.checkin.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.beans.Group;
import com.dave.checkin.beans.User;
import com.dave.checkin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class GroupJoinedActivity extends AppCompatActivity {

    private TextView description_group_detail;
    private TextView time_group_detail;
    private TextView owner_group_detail;
    private TextView num_group_detail;

    private String groupId;
    private String userId;

    private int numOfMember=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        initComponents();
    }

    private void initComponents() {
        description_group_detail = findViewById(R.id.description_group_detail);
        time_group_detail = findViewById(R.id.time_group_detail);
        owner_group_detail = findViewById(R.id.owner_group_detail);
        num_group_detail = findViewById(R.id.num_group_detail);
        setComponents();
    }

    private void setComponents() {
        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(intent.getStringExtra("title"));
        }
        description_group_detail.setText(intent.getStringExtra("description"));
        time_group_detail.setText("创建时间:" + intent.getStringExtra("time"));
        owner_group_detail.setText("创建者: " + intent.getStringExtra("owner"));
        num_group_detail.setText("群组人数: " + intent.getStringExtra("num") + "人");

        groupId = intent.getStringExtra("groupId");
    }

    private void joinGroup() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginState", MODE_PRIVATE);
        userId = sharedPreferences.getString("userID", "");
        BmobQuery<Group> query = new BmobQuery<>();
        query.getObject(groupId, new QueryListener<Group>() {
            @Override
            public void done(Group group, BmobException e) {
                numOfMember=Integer.valueOf(group.getNum());
                if (group.getMember() == null) {
                    List<String> list = new ArrayList<>();
                    list.add(userId);
                    updateGroup(list);
                }else {
                    updateGroup(group.getMember());
                }
            }
        });
    }

    private void updateGroup(List<String> member) {
        Group group=new Group();
        member.add(userId);
        group.setMember(member);
        numOfMember=numOfMember+1;
        group.setNum(numOfMember+"");
        group.update(groupId,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Toast.makeText(GroupJoinedActivity.this, "加入群组成功",
                            Toast.LENGTH_SHORT).show();
                    getGroupFromUser();
                }else {
                    Log.d("加入群组",e.getErrorCode()+","+e.getMessage());
                }
            }
        });
    }

    private void getGroupFromUser(){
        BmobQuery<User> query=new BmobQuery<>();
        query.getObject(userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (user.getGroupList()==null){
                    List<String> list = new ArrayList<>();
                    list.add(groupId);
                    updateUser(list);
                }else {
                    updateUser(user.getGroupList());
                }
            }
        });
    }
    private void updateUser(List<String> list){
        User user=new User();
        user.setAdmin(false);
        list.add(groupId);
        user.setGroupList(list);
        user.update(userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    Log.d("加入群组","更新User");
                    setTextView();
                    resultForJoin();
                }else {
                    Log.d("加入群组",e.getErrorCode()+","+e.getMessage());
                }
            }
        });
    }

    private void setTextView(){
        num_group_detail.setText("群组人数: " + numOfMember + "人");
    }

    private void resultForJoin(){
        setResult(Utils.RESULT_JOIN_GROUP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_join, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.join_group:
                Toast.makeText(GroupJoinedActivity.this, "加入群组",
                        Toast.LENGTH_SHORT).show();
                joinGroup();
                break;
            default:
                break;
        }
        return true;
    }
}
