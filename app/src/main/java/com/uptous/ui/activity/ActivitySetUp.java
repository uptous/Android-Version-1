package com.uptous.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uptous.R;

/**
 * Created by Prakash on 1/2/2017.
 */

public class ActivitySetUp extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageViewBack;
    private LinearLayout mLinearLayoutLeftMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        initView();
    }

    private void initView() {
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mLinearLayoutLeftMenu = (LinearLayout) findViewById(R.id.imgmenuleft);

        mImageViewBack.setVisibility(View.VISIBLE);
        mLinearLayoutLeftMenu.setVisibility(View.GONE);

        mImageViewBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
        }
    }
}
