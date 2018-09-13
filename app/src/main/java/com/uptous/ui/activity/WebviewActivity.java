package com.uptous.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.uptous.MyApplication;
import com.uptous.R;

/**
 * Created by Prakash on 1/13/2017.
 */

public class WebviewActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mLinearLayoutSideMenu;
    private ImageView mImageViewBack;
    private LinearLayout mLinearLayoutFilter;
    private WebView mWebView;
    private String Audio, path, ImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview_activity);


        initView();

        getData();

    }

    private void initView() {
        mLinearLayoutFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mLinearLayoutSideMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mLinearLayoutSideMenu.setVisibility(View.GONE);
        mLinearLayoutFilter.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);

        mImageViewBack.setOnClickListener(this);
    }

    private void getData() {
        path = MyApplication.mSharedPreferences.getString("path", null);
        ImagePath = MyApplication.mSharedPreferences.getString("Imagepath", null);


        if (ImagePath != null) {
            showImage();
        }
        if (path != null) {
            showPdf();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
        }
    }

    private void showPdf() {
        String pdf = path;
        mWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        MyApplication.editor.putString("path", null);
        MyApplication.editor.commit();
    }

    private void showImage() {

        String html = "<html><body><img src=\"" + ImagePath + "\" width=\"100%\" height=\"100%\"\"/></body></html>";
        mWebView.loadData(html, "text/html", null);
//        mWebView.loadUrl(ImagePath);
        mWebView.getSettings().setBuiltInZoomControls(true);
        MyApplication.editor.putString("Imagepath", null);
        MyApplication.editor.commit();
    }
}
