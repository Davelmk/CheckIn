package com.dave.checkin.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.dave.checkin.beans.Group;
import com.dave.checkin.beans.User;
import com.dave.checkin.detail.CheckDetailActivity;
import com.dave.checkin.group.CreatedActivity;
import com.dave.checkin.group.JoinedActivity;
import com.dave.checkin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends AppCompatActivity {
    //抽屉布局
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //RecyclerView
    private RecyclerView recyclerView;
    private List<CheckIn> checkInList;
    private CheckInAdapter adapter;

    //Swipe_Refresh
    private SwipeRefreshLayout refreshLayout;

    //FloatingActionButton
    private FloatingActionButton add_checkin;

    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initBombSDK(this);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("我的签到");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.menu);
        }
        drawerLayout=findViewById(R.id.drawer_layout);
        initNavigationView();
        checkInList = new ArrayList<>();
        getUserId();
        initList();
        //get数据
        checkUserLevel();

        //Swipe_Refresh
        refreshLayout=findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
            }
        });
        //功能未完成
        add_checkin = findViewById(R.id.add_checkin);
        add_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "功能暂不开放", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshLayout(){
        checkInList.clear();
        checkUserLevel();
    }

    private void checkUserLevel() {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginState", MODE_PRIVATE);
        boolean isAdmin = sharedPreferences.getBoolean("isAdmin", false);
        if (isAdmin) {
            Log.d("MainActivity","直接获取Bmob中的签到列表");
            getCheckinFromBmob();
        } else {
            Toast.makeText(MainActivity.this, "您不是管理员用户", Toast.LENGTH_SHORT).show();
            getGroupListFromBmob();
        }
    }

    private void goToAddCheckIn() {
        Intent intent = new Intent(MainActivity.this, AddCheckActivity.class);
        startActivityForResult(intent, Utils.REQUEST_ADD_CHECKIN);
    }

    private void initList() {
        recyclerView = findViewById(R.id.main_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CheckInAdapter(this, checkInList);
        adapter.setItemClickListener(new CheckInAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, checkInList.get(position).getTitle(),
                        Toast.LENGTH_SHORT).show();
                goToCheckDetailActivity(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void goToCheckDetailActivity(int position) {
        Intent intent = new Intent(MainActivity.this, CheckDetailActivity.class);
        intent.putExtra("id", checkInList.get(position).getId());
        intent.putExtra("title", checkInList.get(position).getTitle());
        startActivity(intent);
    }


    private void getGroupListFromBmob(){
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(userid, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    if (user.getGroupList()==null){
                        Toast.makeText(MainActivity.this, "未参加任何群组", Toast.LENGTH_SHORT).show();
                    }else {
                        getGroupCheckin(user.getGroupList());
                    }
                } else {
                    Log.d("MainActivity", e.getMessage());
                }
            }
        });
    }

    private void getGroupCheckin(List<String> groupList){
        BmobQuery<Group> query=new BmobQuery<>();
        query.addWhereContainedIn("objectId",groupList);
        query.findObjects(new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e==null){
                    List<String> checkList=new ArrayList<>();
                    for (Group group:list){
                        checkList.addAll(group.getCheckin());
                    }
                    getCheckinDetail(checkList);
                }else {
                    Log.d("getGroupCheckin", e.getMessage());
                }
            }
        });
    }

    private void getCheckinFromBmob() {
        checkInList.clear();

        BmobQuery<Group> query = new BmobQuery<>();
        query.addWhereEqualTo("owner",userid);
        query.findObjects(new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e==null){
                    List<String> checkList=new ArrayList<>();
                    for (Group group:list){
                        checkList.addAll(group.getCheckin());
                    }
                    getCheckinDetail(checkList);
                }else {
                    Log.d("getGroupCheckin", e.getMessage());
                }
            }
        });
    }

    private void getUserId(){
        SharedPreferences sharedPreferences = getSharedPreferences("LoginState", MODE_PRIVATE);
        userid = sharedPreferences.getString("userID", "");
    }

    private void getCheckinDetail(final List<String> checkin) {
        Log.d("MainActivity", checkin.toString());
        BmobQuery<CheckIn> query = new BmobQuery<>();
        query.addWhereContainedIn("objectId", checkin);
        query.findObjects(new FindListener<CheckIn>() {
            @Override
            public void done(List<CheckIn> list, BmobException e) {
                if (e == null) {
                    refreshList(list);
                } else {
                    Log.d("Main", e.getMessage());
                }
            }
        });

    }

    private void refreshList(List<CheckIn> list) {
        CheckIn temp = null;
        for (CheckIn checkIn : list) {
            temp = new CheckIn(checkIn.getTitle(), checkIn.getOwner(), checkIn.getNum(),checkIn.getOwnerName());
            temp.setId(checkIn.getObjectId());
            temp.setTime(checkIn.getCreatedAt());
            checkInList.add(temp);
        }
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
    }


    private void goToJoinGroup() {
        Intent intent = new Intent(MainActivity.this, JoinedActivity.class);
        startActivity(intent);
    }

    private void goToCreatedGroup() {
        Intent intent = new Intent(MainActivity.this, CreatedActivity.class);
        startActivity(intent);
    }

    private void initNavigationView() {
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
                    case R.id.joinGroup:
                        Toast.makeText(MainActivity.this, item.getTitle(),
                                Toast.LENGTH_SHORT).show();
                        goToJoinGroup();
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

    private void drawerOption() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.REQUEST_ADD_CHECKIN && resultCode == Utils.RESULT_ADD_CHECKIN) {
            Log.d("MainActivity", "成功添加签到活动");
            String objectId = data.getStringExtra("objectId");
            Log.d("MainActivity", objectId);
            addCheckinToListFromBmob(objectId);
        }
    }

    private void addCheckinToListFromBmob(String objectId) {
        BmobQuery<CheckIn> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<CheckIn>() {
            @Override
            public void done(CheckIn checkIn, BmobException e) {
                if (e == null) {
                    Log.e("MainActivity", checkIn.getTitle() + "," + checkIn.getOwner());
                    CheckIn temp = new CheckIn(checkIn.getTitle(), checkIn.getOwner(), checkIn.getNum(),checkIn.getOwnerName());
                    temp.setId(checkIn.getObjectId());
                    temp.setTime(checkIn.getCreatedAt());
                    checkInList.add(temp);
                    Log.e("MainActivity", checkInList.size() + "");
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("MainActivity", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
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
