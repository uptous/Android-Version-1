package com.uptous.view.activity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.controller.utils.RoundedImageView;
import com.uptous.controller.utils.Validation;
import com.uptous.model.ProfileResponseModel;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Response;


/**
 * FileName : ProfileActivity
 * Description :Show user profile detail,also user signout from uptous
 * Dependencies :ProfileActivity
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageViewBack, mViewEditImageView;

    private ImageView mViewProfileRoundedImageView;

    private EditText mTextFirstNameEditText, mTextLastNameEditText, mTextEmailEditText, mTextPhoneEditText;

    private TextView mTextViewSignOut;

    private Button mButtonSave;

    private LinearLayout mLinearLayoutProfileDetail;

    private String mFirstName, mLastName, mEmail, mImagePath, mPhone, mAuthenticationId, mAuthenticationPassword;

    private Helper mHelper;

    private CustomizeDialog mCustomizeDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile);


        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.button_save:
                mHelper.keyBoardHidden(ProfileActivity.this);
                checkValidationOnView();


                break;
            case R.id.image_view_edit:

                CropImage.activity(null)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
                break;
            case R.id.text_view_sign_out:
                logOut();


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = result.getUri();
                try {


                    BitmapFactory.Options options = null;
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                  Bitmap  bitmap = BitmapFactory.decodeFile(selectedImageUri.getPath(), options);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byte[] byte_arr = stream.toByteArray();

                    mImagePath = Base64.encodeToString(byte_arr, 0);

                    byte[]  imageBytes = Base64.decode(mImagePath, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    mViewProfileRoundedImageView.setImageBitmap(decodedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }

    public void logOut() {
        MainActivity activity = new MainActivity();
        activity.logOut();
        Application app = getApplication();
        Intent intent = new Intent(app, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);
    }


    //method to initialize view
    private void initView() {
        mHelper = new Helper();

        //Local Variables Initialization
        TextView mTextViewTitle = (TextView) findViewById(R.id.text_view_title);
        LinearLayout mLinearLayoutSlideMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);

        //Global Variables Initialization
        mLinearLayoutProfileDetail = (LinearLayout) findViewById(R.id.layout_profile_detail);
        mTextViewSignOut = (TextView) findViewById(R.id.text_view_sign_out);
        mButtonSave = (Button) findViewById(R.id.button_save);

        mViewEditImageView = (ImageView) findViewById(R.id.image_view_edit);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mViewProfileRoundedImageView = (ImageView) findViewById(R.id.image_view_profile);
        mViewEditImageView = (ImageView) findViewById(R.id.image_view_edit);

        mTextFirstNameEditText = (EditText) findViewById(R.id.edit_text_first_name);
        mTextLastNameEditText = (EditText) findViewById(R.id.edit_text_last_name);
        mTextEmailEditText = (EditText) findViewById(R.id.edit_text_email);
        mTextPhoneEditText = (EditText) findViewById(R.id.edit_text_phone);

        mTextViewTitle.setText(R.string.my_profile);
        mTextViewTitle.setVisibility(View.VISIBLE);
        mImageViewBack.setVisibility(View.VISIBLE);
        mLinearLayoutSlideMenu.setVisibility(View.GONE);
        imageViewFilter.setVisibility(View.GONE);
        mTextViewSignOut.setVisibility(View.VISIBLE);
        mTextViewSignOut.setText(R.string.sign_out);

        getData();

        clickListenerOnViews();

        if (ConnectionDetector.isConnectingToInternet(ProfileActivity.this)) {
            getApiProfile();
        } else {
            Toast.makeText(ProfileActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mButtonSave.setOnClickListener(this);
        mViewEditImageView.setOnClickListener(this);
        mTextViewSignOut.setOnClickListener(this);

    }

    // Get webservice to get profile detail
    private void getApiProfile() {
        final ProgressDialog mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<ProfileResponseModel> call = service.ProfileDetail();
        call.enqueue(new retrofit2.Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        try {
                            mLinearLayoutProfileDetail.setVisibility(View.VISIBLE);
                            mTextFirstNameEditText.setText(response.body().getFirstName());
                            mTextLastNameEditText.setText(response.body().getLastName());
                            mTextEmailEditText.setText(response.body().getEmail());
                            mTextPhoneEditText.setText(response.body().getPhone());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String Url=response.body().getPhoto();


                        Glide.with(ProfileActivity.this).load(Url)
                                .into(mViewProfileRoundedImageView);

//                        Picasso.with(ProfileActivity.this).load("https://d12h6ivzrhy8zy.cloudfront.net/873fc4d7-3f7c-4d30-bc1c-142dd8ffdc81dXNlcklkPTI=").into(mViewProfileRoundedImageView);


                    }
                } else {


                    mCustomizeDialog = mHelper.CustomizeDialogAlert(ProfileActivity.this, R.layout.dialog_password_change,
                            YesButtonClickListener);
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    View.OnClickListener YesButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            mCustomizeDialog.dismiss();
            logOut();


        }
    };

    // Post webservice to update profile
    private void postApiUpdateProfile() {
        final ProgressDialog mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<ProfileResponseModel> call = service.UpdateProfile(mFirstName, mLastName, mEmail, mPhone, mImagePath);
        call.enqueue(new retrofit2.Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        getApiProfile();

                    }
                } else {
                    Toast.makeText(ProfileActivity.this, response.raw().code() + " " + response.raw().message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, R.string.error, Toast.LENGTH_SHORT).show();


            }
        });
    }

    //Method to check validation on views
    private void checkValidationOnView() {
        if (!Validation.isFieldEmpty(mTextFirstNameEditText) || !Validation.isFieldEmpty(mTextLastNameEditText)
                || !Validation.isFieldEmpty(mTextEmailEditText)
                || !Validation.isFieldEmpty(mTextPhoneEditText)) {
            if (mTextFirstNameEditText.getText().toString().trim().length() <= 0) {
                Toast.makeText(this, "Please Enter FirstName", Toast.LENGTH_SHORT).show();
                mTextFirstNameEditText.requestFocus();
            } else if (mTextLastNameEditText.getText().toString().trim().length() <= 0) {
                Toast.makeText(this, "Please Enter LastName", Toast.LENGTH_SHORT).show();
                mTextLastNameEditText.requestFocus();
            } else if (mTextEmailEditText.getText().toString().trim().length() <= 0) {
                Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                mTextEmailEditText.requestFocus();
            } else if (!Validation.isEmailValid(mTextEmailEditText.getText().toString())) {
                mTextEmailEditText.setError("Invalid Email");
            } else if (mTextPhoneEditText.getText().toString().trim().length() <= 0) {
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                mTextPhoneEditText.requestFocus();
            } else {
                mFirstName = mTextFirstNameEditText.getText().toString();
                mLastName = mTextLastNameEditText.getText().toString();
                mEmail = mTextEmailEditText.getText().toString();
                mPhone = mTextPhoneEditText.getText().toString();

                if (ConnectionDetector.isConnectingToInternet(this)) {
                    postApiUpdateProfile();
                } else {
                    Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            mTextFirstNameEditText.requestFocus();
            Toast.makeText(this, "Enter All Field", Toast.LENGTH_SHORT).show();
        }
    }


}


