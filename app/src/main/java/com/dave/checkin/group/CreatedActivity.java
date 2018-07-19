package com.dave.checkin.group;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.GroupAdapter;
import com.dave.checkin.beans.Group;
import com.dave.checkin.detail.GroupCreatedActivity;

import java.util.ArrayList;
import java.util.List;

public class CreatedActivity extends AppCompatActivity {
    //RecyclerView
    private RecyclerView recyclerView;
    private List<Group> list;
    private GroupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("创建的群组");
        }
        initList();
    }

    private void initList(){
        list=new ArrayList<>();
        //临时数据
        list.add(new Group("群组1","dave",R.color.colorSign1));
        list.add(new Group("群组2","tom",R.color.colorSign2));
        list.add(new Group("群组3","mark",R.color.colorSign3));
        list.add(new Group("群组4","jeff",R.color.colorSign4));
        recyclerView=findViewById(R.id.created_recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GroupAdapter(this,list);
        adapter.setItemClickListener(new GroupAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(CreatedActivity.this, list.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                goToGroupCreatedDetail();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void goToGroupCreatedDetail(){
        Intent intent=new Intent(CreatedActivity.this, GroupCreatedActivity.class);
        startActivity(intent);
    }
}
