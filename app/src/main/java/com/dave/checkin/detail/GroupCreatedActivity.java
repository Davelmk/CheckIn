package com.dave.checkin.detail;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.adapter.GroupMemberAdapter;
import com.dave.checkin.beans.Group;
import com.dave.checkin.beans.User;
import com.dave.checkin.group.AddGroupCheckinActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class GroupCreatedActivity extends AppCompatActivity {
    private TextView created_group_description;
    private TextView created_group_time;
    private TextView created_group_owner;
    private TextView created_num;

    //RecyclerView
    private RecyclerView recyclerView;
    private List<User> userList;
    private GroupMemberAdapter adapter;

    private String ownerId;
    private String groupId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_created);
        initComponents();
        setComponents();
        initList();
//        getGroupMemberID();
    }

    private void initComponents(){
        created_group_description=findViewById(R.id.created_group_description);
        created_group_time=findViewById(R.id.created_group_time);
        created_group_owner=findViewById(R.id.created_group_owner);
        created_num=findViewById(R.id.created_num);
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
        created_num.setText("群组成员: : "+i.getStringExtra("num")+"人");

        ownerId=i.getStringExtra("ownerId");
        groupId=i.getStringExtra("id");
    }

    private void initList(){
        userList =new ArrayList<>();
        //临时数据
        userList.add(new User("dave"));
        userList.add(new User("tom"));
        userList.add(new User("mark"));
        userList.add(new User("jeff"));
        userList.add(new User("lili"));
        userList.add(new User("david"));
        userList.add(new User("marry"));
        recyclerView=findViewById(R.id.created_recyclerView);
        GridLayoutManager layoutManager=new GridLayoutManager(this,5);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new GroupMemberAdapter(this, userList);
        adapter.setItemClickListener(new GroupMemberAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(GroupCreatedActivity.this, userList.get(position).getUsername(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

//    private void getGroupMemberID(){
//        BmobQuery<Group> query=new BmobQuery<>();
//        query.getObject(groupId,new QueryListener<Group>() {
//            @Override
//            public void done(Group group, BmobException e) {
//                if (e==null){
//                    getMember(group.getMember());
//                }else {
//                    Log.d("查询成员","失败："+e.getMessage());
//                }
//            }
//        });
//    }

//    private void getMember(final List<String> userIds){
//        if (userIds==null){
//            Log.d("查询群组成员","群组无成员");
//        }else {
//            BmobQuery<User> query=new BmobQuery<>();
//            query.addWhereContainedIn("objectId",userIds);
//            query.findObjects(new FindListener<User>() {
//                @Override
//                public void done(List<User> list, BmobException e) {
//                    if (e==null){
//                        userList=list;
//                        adapter.notifyDataSetChanged();
//                    }else {
//                        Log.d("查询群组成员","查询失败"+e.getMessage());
//                    }
//                }
//            });
//        }
//    }

    private void goToAddGroupCheckin(){
        Intent intent=new Intent(GroupCreatedActivity.this, AddGroupCheckinActivity.class);
        intent.putExtra("ownerId",ownerId);
        intent.putExtra("groupId",groupId);
        startActivity(intent);
    }

    private void dismissGroup(){
        Group group=new Group();
        group.setObjectId(groupId);
        group.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.d("删除分组","删除成功");
                    finish();
                }else {
                    Log.d("删除分组",e.getMessage());
                }
            }
        });
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
            case R.id.dismiss:
                Toast.makeText(GroupCreatedActivity.this, "解散群组",
                        Toast.LENGTH_SHORT).show();
                dismissGroup();
                break;
            case R.id.add_group_checkin:
                Toast.makeText(GroupCreatedActivity.this, "添加群组签到",
                        Toast.LENGTH_SHORT).show();
                goToAddGroupCheckin();
                break;
            default:
                break;
        }
        return true;
    }
}
