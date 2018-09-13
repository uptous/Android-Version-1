package com.uptous.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.uptous.R;


public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private LinearLayout mLinearLayoutLogin;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        mLinearLayoutLogin = (LinearLayout) findViewById(R.id.layout_login);
        mLinearLayoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(SplashActivity.this, LogInActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        });

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
                     /* Create an Intent that will start the Login-Activity. */

//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, SPLASH_DISPLAY_LENGTH);
    }


}

