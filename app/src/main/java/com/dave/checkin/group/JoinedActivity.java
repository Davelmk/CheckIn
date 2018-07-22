package com.dave.checkin.group;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.GroupAdapter;
import com.dave.checkin.beans.Group;
import com.dave.checkin.detail.GroupJoinedActivity;
import com.dave.checkin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class JoinedActivity extends AppCompatActivity {
    //RecyclerView
    private RecyclerView recyclerView;
    private List<Group> groupList;
    private GroupAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined);
        Utils.initBombSDK(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("加入群组");
        }
        initList();
        getGroupFromBmob();
        refreshLayout=findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGroupFromBmob();
            }
        });
    }

    private void getGroupFromBmob(){
        BmobQuery<Group> query=new BmobQuery<>();
        query.findObjects(new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                refreshList(list);
            }
        });
    }

    private void refreshList(List<Group> list){
        Group temp=null;
        for (Group group:list){
            temp=new Group(group.getTitle(),group.getOwner());
            temp.setId(group.getObjectId());
            temp.setTime(group.getCreatedAt());
            temp.setDescription(group.getDescription());
            temp.setNum(group.getNum());
            groupList.add(temp);
        }
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }

    private void initList(){
        groupList=new ArrayList<>();
        recyclerView=findViewById(R.id.joined_recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GroupAdapter(this,groupList);
        adapter.setItemClickListener(new GroupAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(JoinedActivity.this, groupList.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                goToGroupJoinedDetail(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void goToGroupJoinedDetail(int pos){
        Intent intent=new Intent(JoinedActivity.this, GroupJoinedActivity.class);
        Group group=groupList.get(pos);
        intent.putExtra("groupId",group.getId());
        intent.putExtra("title",group.getTitle());
        intent.putExtra("owner",group.getOwnerName());
        intent.putExtra("num",group.getNum());
        intent.putExtra("time",group.getTime());
        intent.putExtra("description",group.getDescription());
        startActivityForResult(intent,Utils.REQUEST_JOIN_GROUP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==Utils.REQUEST_JOIN_GROUP&&resultCode==Utils.RESULT_JOIN_GROUP){
            Log.d("加入群组","加入群组成功");
            groupList.clear();
            getGroupFromBmob();
        }
    }
}
