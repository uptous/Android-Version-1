package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.ui.adapter.TypeListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 12/29/2016.
 */

public class MessagePostActivity extends AppCompatActivity implements View.OnClickListener {

    private TypeListAdapter mTypeListAdapter;
    private Spinner mSpinnerCommunity;
    private ImageView mImageViewBack;
    private LinearLayout mLinearLayoutNavigation;
    private TextView mTextViewTitle;
    private ImageView mImageViewFilter;
    private Button mButtonSend;
    private EditText mEditTextMessage;
    private EditText mEditTextSubject;
    private String mMessage, mSubject, mAuthenticationId, mAuthenticationPassword;
    private int mCommunityID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_post);

        initView();


        if (ConnectionDetector.isConnectingToInternet(MessagePostActivity.this)) {
            getApiCommunityList();
        } else {
            Toast.makeText(MessagePostActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {
        mSpinnerCommunity = (Spinner) findViewById(R.id.spinner_Community);
        mEditTextMessage = (EditText) findViewById(R.id.edit_text_message);
        mEditTextSubject = (EditText) findViewById(R.id.edit_text_subject);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mLinearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_title);
        mImageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        mButtonSend = (Button) findViewById(R.id.button_send_message);

        mImageViewFilter.setVisibility(View.GONE);
        mLinearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        mTextViewTitle.setVisibility(View.VISIBLE);
        mTextViewTitle.setText("Message Post");

        getData();

        clickListenerOnViews();
    }

    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mButtonSend.setOnClickListener(this);
    }

    private void getApiCommunityList() {
        final ProgressDialog progressDialog = new ProgressDialog(MessagePostActivity.this);
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
                    mTypeListAdapter = new TypeListAdapter(MessagePostActivity.this, eventResponseModels);
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
                Toast.makeText(MessagePostActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;

            case R.id.button_send_message:


                mMessage = mEditTextMessage.getText().toString();
                mSubject = mEditTextSubject.getText().toString();


                if (mMessage.length() > 0 && mSubject.length() > 0) {
                    postApiMessage();
                } else {
                    Toast.makeText(MessagePostActivity.this, "Please fill all field", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private void postApiMessage() {
        final ProgressDialog mProgressDialog = new ProgressDialog(MessagePostActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.PostMessage(mCommunityID, mSubject, mMessage);
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
                Toast.makeText(MessagePostActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
    }
}
