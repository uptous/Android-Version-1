package com.uptous.view.activity;

import android.Manifest;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.UserPicture;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.view.adapter.CommunityListAdapter;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : PicturePostActivity
 * Description : User post picture show on feed
 * Dependencies : CommunityListAdapter
 */

public class PicturePostActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageViewBack, mImageViewSelected;

    private CommunityListAdapter mCommunityListAdapter;

    private Spinner mSpinnerCommunity;

    private TextView mTextViewAlbum, mTextViewCamera;

    private String mAlbumTitle, mImagePath, mImageCaption, mAuthenticationId, mAuthenticationPassword;

    private EditText mEditTextSubject, mEditTextFileName;

    private Button mButtonUpload;

    private int mCommunityID;

    public static final int LOAD_IMAGE_RESULTS = 1;

    private Helper mHelper;
    public static final int REQUEST_CAMERA = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_post);

        initView();


        if (ConnectionDetector.isConnectingToInternet(PicturePostActivity.this)) {
            getApiCommunityList();
        } else {
            Toast.makeText(PicturePostActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_album:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
                break;
            case R.id.text_view_camera:
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, LOAD_IMAGE_RESULTS);
                break;
            case R.id.image_view_back:
                finish();
                break;

            case R.id.button_upload:
                mHelper.keyBoardHidden(PicturePostActivity.this);
                mAlbumTitle = mEditTextFileName.getText().toString().replace("\n", "<br>");
                mImageCaption = mEditTextSubject.getText().toString().replace("\n", "<br>");


                if (mAlbumTitle.length() > 0 && mImageCaption.length() > 0) {
                    if (mImagePath != null && mImagePath.length() > 0) {
                        postApiPicturePost();
                    } else {
                        Toast.makeText(PicturePostActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(PicturePostActivity.this, R.string.fill_all_field, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && null != data) {

            Bitmap yourImage;
            Uri selectedImageUri = data.getData();
            try {
                if (selectedImageUri == null) {
                    Bundle extra2 = data.getExtras();
                    yourImage = extra2.getParcelable("data");
                } else {
                    yourImage = new UserPicture(selectedImageUri, getContentResolver()).getBitmap();
                }

                // convert bitmap to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (yourImage != null) {
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                byte imageInByte[] = stream.toByteArray();

                mImagePath = Base64.encodeToString(imageInByte, Base64.DEFAULT);
                try {
                    mImageViewSelected.setImageBitmap(yourImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //method to initialize view
    private void initView() {
        mHelper = new Helper();

        //Local Variables Initialization
        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        LinearLayout linearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);

        //Global Variables Initialization
        mEditTextFileName = (EditText) findViewById(R.id.edit_text_file_name_picture_post);
        mEditTextSubject = (EditText) findViewById(R.id.edit_text_subject_picture_post);

        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mImageViewSelected = (ImageView) findViewById(R.id.image_view_selected);

        mSpinnerCommunity = (Spinner) findViewById(R.id.spinner_Community);

        mTextViewAlbum = (TextView) findViewById(R.id.text_view_album);
        mTextViewCamera = (TextView) findViewById(R.id.text_view_camera);

        mButtonUpload = (Button) findViewById(R.id.button_upload);

        textViewTitle.setText(R.string.picture_post);
        imageViewFilter.setVisibility(View.GONE);
        linearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.VISIBLE);

        clickListenerOnViews();

        getData();
    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mButtonUpload.setOnClickListener(this);
        mTextViewAlbum.setOnClickListener(this);
        mTextViewCamera.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);
    }

    // Get webservice to get communities
    private void getApiCommunityList() {
        final ProgressDialog progressDialog = new ProgressDialog(PicturePostActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<CommnunitiesResponseModel>> call = service.GetCommunity();

        call.enqueue(new Callback<List<CommnunitiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommnunitiesResponseModel>> call, Response<List<CommnunitiesResponseModel>> response) {
                try {
                    progressDialog.dismiss();

                    if (response.body() != null) {
                        final List<CommnunitiesResponseModel> eventResponseModels = response.body();

                        CommnunitiesResponseModel resultsEntity = new CommnunitiesResponseModel();
                        resultsEntity.setName("SELECT COMMUNITY");
                        resultsEntity.setId("-1");
                        eventResponseModels.add(eventResponseModels.size(), resultsEntity);
                        mCommunityListAdapter = new CommunityListAdapter(PicturePostActivity.this, eventResponseModels);
                        mSpinnerCommunity.setAdapter(mCommunityListAdapter);
                        mSpinnerCommunity.setSelection(mCommunityListAdapter.getCount());
                        mSpinnerCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mCommunityID = eventResponseModels.get(i).getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        final CustomizeDialog customizeDialog = new CustomizeDialog(PicturePostActivity.this);
                        customizeDialog.setCancelable(false);
                        customizeDialog.setContentView(R.layout.dialog_password_change);
                        TextView textViewOk = (TextView) customizeDialog.findViewById(R.id.text_view_log_out);
                        customizeDialog.show();
                        textViewOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customizeDialog.dismiss();
                                logout();
                            }
                        });
                    }

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CommnunitiesResponseModel>> call, Throwable t) {
                Log.d("onFailure", t.toString());
                Toast.makeText(PicturePostActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    // Post webservice to post Picture that show in feed
    private void postApiPicturePost() {

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<PostCommentResponseModel> call = service.PostPicture(mImageCaption, mCommunityID, mAlbumTitle, "File", mImagePath);

        final ProgressDialog mProgressDialog = new ProgressDialog(PicturePostActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        MyApplication.editor.putString("PicturePost", "picture");
                        MyApplication.editor.putString("Feed", null);
                        MyApplication.editor.commit();
                        finish();

                    }
                }
            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                Toast.makeText(PicturePostActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
    }

    //Method to logout from app
    private void logout() {
        MainActivity activity = new MainActivity();
        activity.logOut();
        Application app = getApplication();
        Intent intent = new Intent(app, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);
    }
}

