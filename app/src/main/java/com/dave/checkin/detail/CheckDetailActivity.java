package com.dave.checkin.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dave.checkin.R;
import com.dave.checkin.map.PositionActivity;
import com.dave.checkin.map.SignActivity;
import com.dave.checkin.topic.TopicActivity;

public class CheckDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView detail_position;
    private TextView detail_topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        detail_position=findViewById(R.id.detail_position);
        detail_position.setOnClickListener(this);
        detail_topic=findViewById(R.id.detail_topic);
        detail_topic.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.checkin:
                Toast.makeText(CheckDetailActivity.this, "签到",
                        Toast.LENGTH_SHORT).show();
                goToCheckSign();
                break;
            default:
                break;
        }
        return true;
    }

    private void goToCheckPosition(){
        Intent intent=new Intent(CheckDetailActivity.this, PositionActivity.class);
        startActivity(intent);
    }
    private void goToCheckSign(){
        Intent intent=new Intent(CheckDetailActivity.this, SignActivity.class);
        startActivity(intent);
    }
    private void goToTopicActivity(){
        Intent intent=new Intent(CheckDetailActivity.this, TopicActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_position:
                Log.d("Detail","查看创建位置");
                goToCheckPosition();
                break;
            case R.id.detail_topic:
                Log.d("Detail","查看话题讨论");
                goToTopicActivity();
                break;
            default:
                break;
        }
    }
}
