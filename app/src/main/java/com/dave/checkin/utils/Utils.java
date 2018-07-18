package com.dave.checkin.utils;

import android.content.Context;

import cn.bmob.v3.Bmob;

public class Utils {
    public static final String ApplicationID="a1e5460c7fde5d0f0b0e5c3cb5e6ad2e";
    public static void initBombSDK(Context context){
        Bmob.initialize(context, ApplicationID);
    }
}
