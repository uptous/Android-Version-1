package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.Validation;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.ProfileResponseModel;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Prakash on 12/30/2016.
 */

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mTextEmailEditText;
    private EditText mTextPasswordEditText;
    private LinearLayout mLinearLayoutLogin;
    private String Email, Password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        mTextEmailEditText = (EditText) findViewById(R.id.edit_text_email);
        mTextPasswordEditText = (EditText) findViewById(R.id.edit_text_password);
        mLinearLayoutLogin = (LinearLayout) findViewById(R.id.layout_login);
        Email = mTextEmailEditText.getText().toString();
        Password = mTextPasswordEditText.getText().toString();
        mLinearLayoutLogin.setOnClickListener(this);
        mTextEmailEditText.setText("testp3@uptous.com");
        mTextPasswordEditText.setText("alpha1");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_login:

                Email = mTextEmailEditText.getText().toString();
                Password = mTextPasswordEditText.getText().toString();
                MyApplication.editor.putString("AuthenticationId", Email);
                MyApplication.editor.putString("AuthenticationPassword", Password);
                MyApplication.editor.commit();
                if (Validation.isFieldEmpty(mTextEmailEditText) && Validation.isFieldEmpty(mTextPasswordEditText)) {

                    Toast.makeText(LogInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();

                } else {
//                    if (Email.equalsIgnoreCase("testp3@uptous.com") && Password.equalsIgnoreCase("alpha1")) {
//
                        getApiLogIn();
//                    } else {
//                        Toast.makeText(LogInActivity.this, "Please enter valid email and password", Toast.LENGTH_SHORT).show();
//                    }


                }


                break;

        }
    }

    private void getApiLogIn() {
        final ProgressDialog mProgressDialog = new ProgressDialog(LogInActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, Email, Password);

        Call<PostCommentResponseModel> call = service.login();
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    Toast.makeText(LogInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(LogInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }
}
