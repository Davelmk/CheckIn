package com.dave.checkin.detail;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.GroupMemberAdapter;
import com.dave.checkin.beans.User;

import java.util.ArrayList;
import java.util.List;

public class GroupCreatedActivity extends AppCompatActivity {
    private TextView created_group_description;
    private TextView created_group_time;
    private TextView created_group_owner;
    private TextView created_group_position;
    private TextView created_num;
    private ImageView img_group_position;

    //RecyclerView
    private RecyclerView recyclerView;
    private List<User> list;
    private GroupMemberAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_created);
        initComponents();
        setComponents();
        initList();
    }

    private void initComponents(){
        created_group_description=findViewById(R.id.created_group_description);
        created_group_time=findViewById(R.id.created_group_time);
        created_group_owner=findViewById(R.id.created_group_owner);
        created_group_position=findViewById(R.id.created_group_position);
        created_num=findViewById(R.id.created_num);
        img_group_position=findViewById(R.id.img_group_position);
        img_group_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupCreatedActivity.this,"群组定位",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setComponents(){
        Intent i=getIntent();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(i.getStringExtra("title"));
        }
        created_group_description.setText(i.getStringExtra("description"));
        created_group_time.setText("创建时间:"+i.getStringExtra("time"));
        created_group_owner.setText("创建者: "+i.getStringExtra("owner"));
        created_group_position.setText(i.getStringExtra("position"));
        created_num.setText("签到: "+i.getStringExtra("num")+"人");
    }

    private void initList(){
        list=new ArrayList<>();
        //临时数据
        list.add(new User("dave"));
        list.add(new User("tom"));
        list.add(new User("mark"));
        list.add(new User("jeff"));
        list.add(new User("lili"));
        list.add(new User("david"));
        list.add(new User("marry"));
        recyclerView=findViewById(R.id.created_recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this,5);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GroupMemberAdapter(this,list);
        adapter.setItemClickListener(new GroupMemberAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(GroupCreatedActivity.this, list.get(position).getUsername(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void removeMemberFromGroup(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dismiss_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.search:
                Toast.makeText(GroupCreatedActivity.this, "解散群组",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
