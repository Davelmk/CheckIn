package com.dave.checkin.utils;

import android.content.Context;

import com.dave.checkin.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import cn.bmob.v3.Bmob;

public class Utils {
    public static final String ApplicationID="a1e5460c7fde5d0f0b0e5c3cb5e6ad2e";
    public static final int REQUEST_ADD_CHECKIN=1;
    public static final int RESULT_ADD_CHECKIN=1;

    public static int[] colors={R.color.colorSign1,R.color.colorSign2,
            R.color.colorSign3,R.color.colorSign4,R.color.colorSign5 ,R.color.colorSign6};

    public static void initBombSDK(Context context){
        Bmob.initialize(context, ApplicationID);
    }

    public static int getColor(){
        int index=0;
        Random rand = new Random();
        index=rand.nextInt(6);
        return colors[index];
    }

    public static String getTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time =df.format(new Date());
        return time;
    }

    public static String getPositionDescription(String s){
        String[] arry= s.split(",");
        return arry[0];
    }
    public static double getLatitude(String s){
        //获取纬度信息
        String[] arry= s.split(",");
        return Double.valueOf(arry[1]);
    }
    public static double getLongitude(String s){
        //获取经度信息
        String[] arry= s.split(",");
        return Double.valueOf(arry[2]);
    }
}
