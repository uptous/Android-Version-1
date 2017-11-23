package com.uptous.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.Validation;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.sharedpreference.Prefs;

import retrofit2.Call;
import retrofit2.Response;

import static com.uptous.view.activity.WebsiteActivity.WEBSITE_TITLE;
import static com.uptous.view.activity.WebsiteActivity.WEBSITE_URL;

/**
 * FileName : LogInActivity
 * Description :User LogIn to UpToUs
 */
public class LogInActivity extends BaseActivity implements View.OnClickListener {

    private EditText mTextEmailEditText, mTextPasswordEditText;
    private TextView text_Signu,text_forgot;
    private String mEmail, mPassword;

    private Button button_login;

    private Helper mHelper;

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
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_login:
                mHelper.keyBoardHidden(LogInActivity.this);
                mEmail = mTextEmailEditText.getText().toString();
                mPassword = mTextPasswordEditText.getText().toString();

                Prefs.setAuthenticationId(this, mEmail);
                Prefs.setAuthenticationPassword(this, mPassword);
                if (Validation.isFieldEmpty(mTextEmailEditText) || Validation.isFieldEmpty(mTextPasswordEditText)) {

                    showToast(getString(R.string.login_error));

                } else {

                    if (ConnectionDetector.isConnectingToInternet(LogInActivity.this)) {
                        getApiLogIn();
                    } else {
                        showToast(getString(R.string.network_error));
                    }

                }

                break;


            case R.id.text_forgot:
                intent= new Intent(this, WebsiteActivity.class);
                intent.putExtra(WEBSITE_URL,"https://www.uptous.com/mobileForgotPassword");
                intent.putExtra(WEBSITE_TITLE,"Forgot Password");
                startActivity(intent);
                break;

            case R.id.text_signup:
                intent = new Intent(this, WebsiteActivity.class);
                intent.putExtra(WEBSITE_URL,"https://www.uptous.com/mobileSignup");
                intent.putExtra(WEBSITE_TITLE,"Sign Up");
                startActivity(intent);
                break;

        }
    }
    //method to initialize view
    private void initView() {

        mHelper = new Helper();
        //Global Variables Initialization
        mTextEmailEditText = (EditText) findViewById(R.id.edit_text_email);
        mTextPasswordEditText = (EditText) findViewById(R.id.edit_text_password);
        button_login = (Button) findViewById(R.id.layout_login);

        text_Signu = (TextView) findViewById(R.id.text_signup);
        text_forgot = (TextView) findViewById(R.id.text_forgot);
        setOnClickListener();

    }

    //Method to set on clickListener on views
    private void setOnClickListener() {
        button_login.setOnClickListener(this);
        text_Signu.setOnClickListener(this);
        text_forgot.setOnClickListener(this);
    }

    //Method to check user already logged in or not
    private void checkUserAlreadyLoggedInOrNot() {
        if (Prefs.getIsAlreadyLogin(this)) {
                /* Create an Intent that will start the MainActivity. */
            Intent mainIntent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    // Get webservice to login
    private void getApiLogIn() {

        showProgressDialog();


        APIServices service =
                ServiceGenerator.createService(APIServices.class, mEmail, mPassword);
        Call<PostCommentResponseModel> call = service.login();
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                hideProgressDialog();
                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        Prefs.setIsAlreadyLogin(LogInActivity.this, true);
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {

                    showToast(getString(R.string.invalid_cred));

                }
            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {

                hideProgressDialog();
                showToast(getString(R.string.error));

            }
        });
    }
}
