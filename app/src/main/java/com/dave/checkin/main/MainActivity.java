package com.dave.checkin.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.CheckInAdapter;
import com.dave.checkin.beans.CheckIn;
import com.dave.checkin.detail.CheckDetailActivity;
import com.dave.checkin.group.CreatedActivity;
import com.dave.checkin.group.JoinedActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //抽屉布局
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //RecyclerView
    private RecyclerView recyclerView;
    private List<CheckIn> list;
    private CheckInAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("我的签到");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
        drawerLayout=findViewById(R.id.drawer_layout);
        initNavigationView();
        initList();
    }
    private void initList(){
        list=new ArrayList<>();
        //临时数据
        list.add(new CheckIn("签到1","dave","2018-07-16",R.color.colorSign1));
        list.add(new CheckIn("签到2","tom","2018-07-16",R.color.colorSign2));
        list.add(new CheckIn("签到3","mark","2018-07-16",R.color.colorSign3));
        list.add(new CheckIn("签到4","jeff","2018-07-16",R.color.colorSign4));
        list.add(new CheckIn("签到5","lili","2018-07-16",R.color.colorSign5));
        list.add(new CheckIn("签到6","david","2018-07-16",R.color.colorSign6));
        list.add(new CheckIn("签到7","marry","2018-07-16",R.color.colorSign1));
        recyclerView=findViewById(R.id.main_recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new CheckInAdapter(this,list);
        adapter.setItemClickListener(new CheckInAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, list.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,CheckDetailActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void goToJoinedGroup(){
        Intent intent=new Intent(MainActivity.this, JoinedActivity.class);
        startActivity(intent);
    }
    private void goToCreatedGroup(){
        Intent intent=new Intent(MainActivity.this, CreatedActivity.class);
        startActivity(intent);
    }

    private void initNavigationView(){
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.
                OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mySign:
                        Toast.makeText(MainActivity.this, item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.myGroup:
                        Toast.makeText(MainActivity.this, item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        goToCreatedGroup();
                        break;
                    case R.id.joinedGroup:
                        Toast.makeText(MainActivity.this, item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        goToJoinedGroup();
                        break;
                    case R.id.quit:
                        Toast.makeText(MainActivity.this, item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "未知menu",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        });
    }

    private void drawerOption(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerOption();
                break;
            case R.id.search:
                Toast.makeText(MainActivity.this, "Search",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
