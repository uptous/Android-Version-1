package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.UserPicture;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.ui.adapter.TypeListAdapter;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 12/29/2016.
 */

public class PicturePostActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageViewBack, mImageViewFilter, mImageViewSelected;
    private LinearLayout mLinearLayoutNavigation;
    private TextView mTextViewTitle;
    private TypeListAdapter mTypeListAdapter;
    private Spinner mSpinnerCommunity;
    private TextView mTextViewAlbum, mTextViewCamera;
    private String mAlbumTitle, mImagePath, mImageCaption, mAuthenticationId, mAuthenticationPassword;
    private EditText mEditTextSubject, mEditTextFileName;
    private Button mButtonUpload;
    private int mCommunityID;

    public static int LOAD_IMAGE_RESULTS = 1;

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


    private void initView() {

        mEditTextFileName = (EditText) findViewById(R.id.edit_text_file_name_picture_post);
        mEditTextSubject = (EditText) findViewById(R.id.edit_text_subject_picture_post);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mLinearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        mImageViewSelected = (ImageView) findViewById(R.id.image_view_selected);
        mSpinnerCommunity = (Spinner) findViewById(R.id.spinner_Community);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_title);
        mTextViewAlbum = (TextView) findViewById(R.id.text_view_album);
        mTextViewCamera = (TextView) findViewById(R.id.text_view_camera);
        mButtonUpload = (Button) findViewById(R.id.button_upload);
        mImageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        mTextViewTitle.setText("Picture Post");
        mImageViewFilter.setVisibility(View.GONE);
        mLinearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        mTextViewTitle.setVisibility(View.VISIBLE);

        clickListenerOnViews();
    }

    private void clickListenerOnViews() {
        mButtonUpload.setOnClickListener(this);
        mTextViewAlbum.setOnClickListener(this);
        mTextViewCamera.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);
    }

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
                    final List<CommnunitiesResponseModel> eventResponseModels = response.body();

                    CommnunitiesResponseModel resultsEntity = new CommnunitiesResponseModel();
                    resultsEntity.setName("SELECT COMMUNITY");
                    resultsEntity.setId("-1");
                    eventResponseModels.add(eventResponseModels.size(), resultsEntity);
                    mTypeListAdapter = new TypeListAdapter(PicturePostActivity.this, eventResponseModels);
                    mSpinnerCommunity.setAdapter(mTypeListAdapter);
                    mSpinnerCommunity.setSelection(mTypeListAdapter.getCount());
                    mSpinnerCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            mCommunityID = eventResponseModels.get(i).getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
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


    private void postApiPicturePost() {

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<PostCommentResponseModel> call = service.PostPicture(mAlbumTitle, mCommunityID, mImageCaption, "File", mImagePath);

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

                        finish();


                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                Toast.makeText(PicturePostActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
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
                mAlbumTitle = mEditTextFileName.getText().toString();
                mImageCaption = mEditTextSubject.getText().toString();


                if (mAlbumTitle.length() > 0 && mImageCaption.length() > 0) {
                    postApiPicturePost();
                } else {
                    Toast.makeText(PicturePostActivity.this, "Please fill all field", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && null != data) {

            Bitmap yourImage = null;
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
                yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
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
}

