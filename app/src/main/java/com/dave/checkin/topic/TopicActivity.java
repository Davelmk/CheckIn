package com.dave.checkin.topic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dave.checkin.R;
import com.dave.checkin.adapter.TopicAdapter;
import com.dave.checkin.beans.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends AppCompatActivity {

    private List<Topic> list;
    private RecyclerView recyclerView;
    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        initList();
    }

    private void initList(){
        list=new ArrayList<>();
        //临时数据
        list.add(new Topic("dave","2018-07-16"));
        list.add(new Topic("tom","2018-07-16"));
        list.add(new Topic("mark","2018-07-16"));
        list.add(new Topic("jeff","2018-07-16"));
        list.add(new Topic("lili","2018-07-16"));
        list.add(new Topic("jeff","2018-07-16"));
        list.add(new Topic("jeff","2018-07-16"));
        list.add(new Topic("lili","2018-07-16"));
        list.add(new Topic("jeff","2018-07-16"));
        recyclerView=findViewById(R.id.topic_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new TopicAdapter(list);
        recyclerView.setAdapter(adapter);
    }
}
