package com.dave.checkin.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dave.checkin.R;

public class AddCheckActivity extends AppCompatActivity {
    private EditText checkin_title;
    private EditText checkin_description;
    private TextView position_description;
    private ImageView get_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check);
        checkin_title=findViewById(R.id.checkin_title);
        checkin_description=findViewById(R.id.checkin_description);
        position_description=findViewById(R.id.position_description);
        get_position=findViewById(R.id.detail_position);
        get_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddCheckActivity","获取定位信息");
                position_description.setText("DLUT");
            }
        });
    }

    private void submitCheckin(){

    }

    private void writeToDB(){
        checkin_title.getText().toString();
        checkin_description.getText().toString();
        submitCheckin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_checkin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.finish_check:
                writeToDB();
                break;
            default:
                break;
        }
        return true;
    }
}
