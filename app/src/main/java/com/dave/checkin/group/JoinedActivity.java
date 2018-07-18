package com.dave.checkin.group;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.GroupAdapter;
import com.dave.checkin.beans.Group;
import com.dave.checkin.detail.GroupJoinedActivity;

import java.util.ArrayList;
import java.util.List;

public class JoinedActivity extends AppCompatActivity {
    //RecyclerView
    private RecyclerView recyclerView;
    private List<Group> list;
    private GroupAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined);
        initList();
    }

    private void initList(){
        list=new ArrayList<>();
        //临时数据
        list.add(new Group("群组1","dave",R.color.colorSign1));
        list.add(new Group("群组2","tom",R.color.colorSign2));
        list.add(new Group("群组3","mark",R.color.colorSign3));
        list.add(new Group("群组4","jeff",R.color.colorSign4));
        list.add(new Group("群组5","lili",R.color.colorSign5));
        list.add(new Group("群组6","david",R.color.colorSign6));
        list.add(new Group("群组7","marry",R.color.colorSign1));
        recyclerView=findViewById(R.id.joined_recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GroupAdapter(this,list);
        adapter.setItemClickListener(new GroupAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(JoinedActivity.this, list.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                goToGroupJoinedDetail();
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void goToGroupJoinedDetail(){
        Intent intent=new Intent(JoinedActivity.this, GroupJoinedActivity.class);
        startActivity(intent);
    }
}
