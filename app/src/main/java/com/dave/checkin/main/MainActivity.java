package com.dave.checkin.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.dave.checkin.adapter.CheckInAdapter;
import com.dave.checkin.beans.CheckIn;
import com.dave.checkin.beans.UserCheckin;
import com.dave.checkin.detail.CheckDetailActivity;
import com.dave.checkin.group.CreatedActivity;
import com.dave.checkin.group.JoinedActivity;
import com.dave.checkin.utils.CheckinDBHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends AppCompatActivity {
    //抽屉布局
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //RecyclerView
    private RecyclerView recyclerView;
    private List<CheckIn> list;
    private CheckInAdapter adapter;

    //FloatingActionButton
    private FloatingActionButton add_checkin;

    //SQLite
    private SQLiteDatabase db;

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
        list=new ArrayList<>();
        getCheckinFromDB();
        add_checkin=findViewById(R.id.add_checkin);
        add_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddCheckIn();
            }
        });
    }

    private void goToAddCheckIn(){
        Intent intent=new Intent(MainActivity.this,AddCheckActivity.class);
        startActivity(intent);
    }

    private void initList(){
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

    private void getCheckinFromBmob(){
        list.clear();
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        String userid=sharedPreferences.getString("userID","");

        BmobQuery<UserCheckin> query=new BmobQuery<>();
        query.addWhereEqualTo("user",userid);
        query.findObjects(new FindListener<UserCheckin>() {
            @Override
            public void done(List<UserCheckin> list, BmobException e) {
                if (e==null){
                    UserCheckin userCheckin=list.get(0);
                    List<String> checkinList=userCheckin.getCheckin();
                    getCheckinDetail(checkinList);
                }
                else {
                    Log.d("MainActivity",e.getMessage());
                }
            }
        });
    }

    private void getCheckinDetail(List<String> checkin){
        BmobQuery<CheckIn> query=new BmobQuery<>();
        query.addWhereContainedIn("objectId",checkin);
        query.findObjects(new FindListener<CheckIn>() {
            @Override
            public void done(List<CheckIn> list, BmobException e) {
                if (e==null){
                   for (CheckIn checkIn:list){
                       String title = checkIn.getTitle();
                       String owner = checkIn.getOwner();
                       String num = checkIn.getNum();
                       list.add(new CheckIn(title,owner,num));
                   }
                   initList();
                }
                else {
                    Log.d("MainActivity",e.getMessage());
                }
            }
        });
    }

    private void getCheckinFromDB(){
        CheckinDBHelper dbHelper=new CheckinDBHelper(this,"CheckIn.db", null, 1);
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("checkin", null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String owner = cursor.getString(cursor.getColumnIndex("owner"));
                String num = cursor.getString(cursor.getColumnIndex("num"));
                list.add(new CheckIn(title,owner,num));
            } while (cursor.moveToNext());
        }
        cursor.close();
        initList();
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
