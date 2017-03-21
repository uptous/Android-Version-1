package com.uptous;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by T011Prakash on 11/18/2015.
 */
public class MyApplication extends Application {
    public static SharedPreferences mSharedPreferences;
    public static SharedPreferences.Editor editor;
    public static final String MYPREFERENCES = "MyPrefs";
    public static final String ISLOGIN = "isLogin";

    @Override
    public void onCreate() {
        super.onCreate();

        mSharedPreferences = getSharedPreferences(MYPREFERENCES, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();


    }

}
