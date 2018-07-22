package com.dave.checkin.group;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.GroupAdapter;
import com.dave.checkin.beans.Group;
import com.dave.checkin.detail.GroupCreatedActivity;
import com.dave.checkin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class CreatedActivity extends AppCompatActivity {
    //RecyclerView
    private RecyclerView recyclerView;
    private List<Group> groupList;
    private GroupAdapter adapter;

    private String ownerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("创建的群组");
        }
        getGroupsFromBmob();
    }

    private void initList(){
        recyclerView=findViewById(R.id.created_recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GroupAdapter(this, groupList);
        adapter.setItemClickListener(new GroupAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(CreatedActivity.this, groupList.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                goToGroupCreatedDetail(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getGroupsFromBmob(){
        groupList =new ArrayList<>();
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        String ownerId=sharedPreferences.getString("userID","未获取到UserID");
        ownerName=sharedPreferences.getString("userName","Dave");
        BmobQuery<Group> query=new BmobQuery<>();
        query.addWhereEqualTo("owner",ownerId);
        query.findObjects(new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                getGroupFromList(list);
            }
        });
    }

    private void getGroupFromList(List<Group> list){
        Group temp=null;
        for (Group group:list){
            temp=new Group(group.getTitle(),ownerName);
            temp.setId(group.getObjectId());
            temp.setTime(group.getCreatedAt());
            temp.setNum(group.getNum());
            groupList.add(temp);
        }
        initList();
    }

    private void goToGroupCreatedDetail(int pos){
        Intent intent=new Intent(CreatedActivity.this, GroupCreatedActivity.class);
        Group group= groupList.get(pos);
        intent.putExtra("id",group.getId());
        intent.putExtra("title",group.getTitle());
        intent.putExtra("ownerId",group.getOwner());
        intent.putExtra("owner",ownerName);
        intent.putExtra("num",group.getNum());
        intent.putExtra("time",group.getTime());
        intent.putExtra("description",group.getDescription());
        startActivity(intent);
    }
    private void goToCreateGroup(){
        Intent intent=new Intent(CreatedActivity.this,CreateGroupActivity.class);
        startActivityForResult(intent, Utils.REQUEST_ADD_GROUP);
    }

    private void addGroupToListFromBmob(String groupId){
        BmobQuery<Group> query=new BmobQuery<>();
        query.getObject(groupId, new QueryListener<Group>() {
            @Override
            public void done(Group group, BmobException e) {
                if(e==null){
                    Group temp=new Group(group.getTitle(),group.getOwner());
                    temp.setId(group.getObjectId());
                    temp.setTime(group.getCreatedAt());
                    temp.setNum(group.getNum());
                    temp.setDescription(group.getDescription());
                    temp.setOwnerName(ownerName);
                    groupList.add(temp);
                    Log.d("List_Len", groupList.size()+"");
                    refreshList();
                }else {
                    Log.e("QueryGroup","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    private void refreshList(){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Group",requestCode+","+resultCode);
        if (requestCode==Utils.REQUEST_ADD_GROUP&&resultCode==Utils.RESULT_ADD_GROUP){
            Log.d("Group","成功添加群组");
            String groupId=data.getStringExtra("groupId");
            Log.d("Group",groupId);
            addGroupToListFromBmob(groupId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.add_group:
                Toast.makeText(CreatedActivity.this, "添加群组",
                        Toast.LENGTH_SHORT).show();
                goToCreateGroup();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_group,menu);
        return true;
    }
}
