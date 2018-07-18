package com.dave.checkin.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.GroupMemberAdapter;
import com.dave.checkin.beans.User;

import java.util.ArrayList;
import java.util.List;

public class GroupCreatedActivity extends AppCompatActivity {
    //RecyclerView
    private RecyclerView recyclerView;
    private List<User> list;
    private GroupMemberAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_created);
        initList();
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
        recyclerView=findViewById(R.id.main_recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this,5);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GroupMemberAdapter(this,list);
        adapter.setItemClickListener(new GroupMemberAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(GroupCreatedActivity.this, list.get(position).getUsername(),
                        Toast.LENGTH_SHORT).show();
                removeMemberFromGroup();
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
