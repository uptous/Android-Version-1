package com.uptous.view.activity;

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
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.view.adapter.CommunityListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : MessagePostActivity
 * Description : User post message show on feed
 * Dependencies : CommunityListAdapter
 */
public class MessagePostActivity extends AppCompatActivity implements View.OnClickListener {

    private CommunityListAdapter mCommunityListAdapter;

    private Spinner mSpinnerCommunity;

    private ImageView mImageViewBack;

    private Button mButtonSend;

    private EditText mEditTextMessage,mEditTextSubject;

    private String mMessage, mSubject, mAuthenticationId, mAuthenticationPassword;

    private int mCommunityID;

    Helper helper;

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

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;

            case R.id.button_send_message:

                helper.keyBoardHidden(MessagePostActivity.this);

                mMessage = mEditTextMessage.getText().toString().replace("\n", "<br>");
                mSubject = mEditTextSubject.getText().toString().replace("\n", "<br>");

                if (mMessage.length() > 0 && mSubject.length() > 0) {
                    postApiMessage();
                } else {
                    Toast.makeText(MessagePostActivity.this, "Please fill all field", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    //method to initialize view
    private void initView() {
        helper =new Helper();

        //Local Variables
        LinearLayout linearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);

        //Global Variables
        mSpinnerCommunity = (Spinner) findViewById(R.id.spinner_Community);
        mEditTextMessage = (EditText) findViewById(R.id.edit_text_message);
        mEditTextSubject = (EditText) findViewById(R.id.edit_text_subject);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mButtonSend = (Button) findViewById(R.id.button_send_message);

        imageViewFilter.setVisibility(View.GONE);
        linearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.VISIBLE);
        textViewTitle.setText("Message Post");

        getData();

        clickListenerOnViews();
    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mButtonSend.setOnClickListener(this);
    }

    // Get webservice to get communities
    private void getApiCommunityList() {
        final ProgressDialog progressDialog = new ProgressDialog(MessagePostActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);


        APIServices service =
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
                    mCommunityListAdapter = new CommunityListAdapter(MessagePostActivity.this, eventResponseModels);
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

    // Post webservice to post message that show in feed
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
                        MyApplication.editor.putString("MessagePost", "message");
                        MyApplication.editor.commit();
                        finish();

                    }
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
