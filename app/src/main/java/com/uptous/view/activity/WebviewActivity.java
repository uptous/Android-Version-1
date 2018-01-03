package com.uptous.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.uptous.R;
import com.uptous.sharedpreference.Prefs;

/**
 * Created by Prakash.
 */

public class WebviewActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView mWebView;
    private String Audio, path, ImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);


        initView();

        getData();

    }

    //Method to initialize views
    private void initView() {

        //Local Variables initializations
        LinearLayout linearLayoutFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        ImageView imageViewBack = (ImageView) findViewById(R.id.image_view_back);
        LinearLayout linearLayoutSideMenu = (LinearLayout) findViewById(R.id.imgmenuleft);

        //Global Variables initializations
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);

        linearLayoutFilter.setVisibility(View.GONE);
        linearLayoutSideMenu.setVisibility(View.GONE);
        imageViewBack.setVisibility(View.VISIBLE);

        imageViewBack.setOnClickListener(this);

    }

    //Method to get data from SharedPreferences
    private void getData() {
        path = Prefs.getpath(this);
        ImagePath = Prefs.getImagepath(this);


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



        String resultImage = pdf.substring(pdf.lastIndexOf('.') + 1).trim();
        if(resultImage.equalsIgnoreCase("docx")){
            finish();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(pdf), "text/*");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else {
            String File="http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf;
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.loadUrl(File);
        }

      Prefs.setpath(this,null);
    }

    private void showImage() {

        mWebView.loadUrl(ImagePath);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

       Prefs.setImagepath(this,null);
    }

}
