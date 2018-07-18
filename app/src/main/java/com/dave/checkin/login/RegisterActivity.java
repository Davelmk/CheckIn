package com.dave.checkin.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dave.checkin.beans.User;
import com.dave.checkin.R;
import com.dave.checkin.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    //注册控件
    private EditText register_name;
    private EditText register_account;
    private EditText register_password;
    private Button btn_register;
    private RadioGroup radio_check;

    private boolean isAdmin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponents();
        Utils.initBombSDK(this);
    }

    private void initComponents() {
        register_name = findViewById(R.id.register_name);
        register_account = findViewById(R.id.register_account);
        register_password = findViewById(R.id.register_password);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Register", "提交注册");
                check_submit();
            }
        });
        //radio
        radio_check = findViewById(R.id.radio_check);
        radio_check.setOnCheckedChangeListener(this);
    }

    private void check_submit(){
        String regEx = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(register_account.getText().toString());
        if (matcher.matches()){
            //email匹配
            submitMsg();
        }else {
            Toast.makeText(RegisterActivity.this, R.string.email_error,Toast.LENGTH_SHORT).show();
        }
    }

    private void submitMsg(){
        String user=register_name.getText().toString();
        String email=register_account.getText().toString();
        String pwd=register_password.getText().toString();
        //上传信息
        updateData(user,email,pwd);
    }

    private void updateData(String username, String email, String pwd) {
        User user=new User(username,email,pwd,isAdmin);
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    Log.d("Register","数据提交成功");
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("Register",e.getMessage());
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radio_user:
                isAdmin=false;
                break;
            case R.id.radio_admin:
                isAdmin=true;
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("Register","返回父级Activity");
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
