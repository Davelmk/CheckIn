package com.dave.checkin.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dave.checkin.beans.User;
import com.dave.checkin.R;
import com.dave.checkin.main.MainActivity;
import com.dave.checkin.utils.Utils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    //注册控件
    private Button btn_login;
    private TextView text_register;
    private EditText login_account;
    private EditText login_password;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.initBombSDK(this);
        checkLoginState();
    }
    private void checkLoginState(){
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        boolean isLogin=sharedPreferences.getBoolean("isLogin",false);
        if (isLogin){
            Log.d("Login","已经处于登录状态");
            goToMainView();
        }else {
            Log.d("Login","尚未登录状态");
            initComponents();
        }
    }

    private void goToRegister(){
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
    private void goToMainView(){
//        progressBar.setVisibility(View.INVISIBLE);
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initComponents() {
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        text_register = findViewById(R.id.text_register);
        text_register.setOnClickListener(this);
        login_account = findViewById(R.id.login_account);
        login_password = findViewById(R.id.login_password);
        progressBar =findViewById(R.id.progressBar);
    }
    private void loginSuccess(){
        SharedPreferences sharedPreferences=getSharedPreferences("LoginState",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("isLogin",true);
        editor.putString("account",login_account.getText().toString());
        editor.putString("password",login_password.getText().toString());
        editor.commit();
        goToMainView();
    }
    private void doLogin(){
        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereEqualTo("account",login_account.getText().toString());
        query.addWhereEqualTo("password",login_password.getText().toString());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    Log.d("Login",list.get(0).getUsername());
                    Log.d("Login",list.get(0).getAccount());
                    loginSuccess();
                }else {
                    Log.d("Login",e.getMessage());
                    Toast.makeText(LoginActivity.this,"用户不存在或者密码错误",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                progressBar.setVisibility(View.VISIBLE);
                doLogin();
                break;
            case R.id.text_register:
                goToRegister();
                break;
            default:
                Log.d("登录","未知点击错误");
                break;
        }
    }
}
