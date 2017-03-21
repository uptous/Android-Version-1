package com.uptous.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.Validation;
import com.uptous.model.PostCommentResponseModel;

import retrofit2.Call;
import retrofit2.Response;

/**
 * FileName : LogInActivity
 * Description :User LogIn to UpToUs
 */
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mTextEmailEditText,mTextPasswordEditText;

    private String mEmail, mPassword;

    private LinearLayout mLinearLayoutLogin;

    Helper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        initView();

        checkUserAlreadyLoggedInOrNot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_login:
                helper.keyBoardHidden(LogInActivity.this);
                mEmail = mTextEmailEditText.getText().toString();
                mPassword = mTextPasswordEditText.getText().toString();
                MyApplication.editor.putString("AuthenticationId", mEmail);
                MyApplication.editor.putString("AuthenticationPassword", mPassword);
                MyApplication.editor.commit();
                if (Validation.isFieldEmpty(mTextEmailEditText) && Validation.isFieldEmpty(mTextPasswordEditText)) {

                    Toast.makeText(LogInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();

                } else {

                    if (ConnectionDetector.isConnectingToInternet(LogInActivity.this)) {
                        getApiLogIn();
                    } else {
                        Toast.makeText(LogInActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }
    }

    //method to initialize view
    private void initView() {

        helper =new Helper();
        //Global Variables
        mTextEmailEditText = (EditText) findViewById(R.id.edit_text_email);
        mTextPasswordEditText = (EditText) findViewById(R.id.edit_text_password);
        mLinearLayoutLogin = (LinearLayout) findViewById(R.id.layout_login);
        mEmail = mTextEmailEditText.getText().toString();
        mPassword = mTextPasswordEditText.getText().toString();

        setOnClickListener();

        setData();


    }

    //Method to set on clickListener on views
    private void setOnClickListener() {
        mLinearLayoutLogin.setOnClickListener(this);
    }

    //Method to set data on views
    private void setData() {
        mTextEmailEditText.setText("testp3@uptous.com");
        mTextPasswordEditText.setText("alpha1");
    }

    //Method to check user already logged in or not
    private void checkUserAlreadyLoggedInOrNot() {
        if (MyApplication.mSharedPreferences.getBoolean(MyApplication.ISLOGIN, false)) {
                /* Create an Intent that will start the MainActivity. */
            Intent mainIntent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    // Get webservice to login
    private void getApiLogIn() {

        final ProgressDialog mProgressDialog = new ProgressDialog(LogInActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        APIServices service =
                ServiceGenerator.createService(APIServices.class, mEmail, mPassword);
        Call<PostCommentResponseModel> call = service.login();
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        MyApplication.editor.putBoolean(MyApplication.ISLOGIN, true);
                        MyApplication.editor.commit();
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
