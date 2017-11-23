package com.uptous.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.sharedpreference.Prefs;
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
public class MessagePostActivity extends BaseActivity implements View.OnClickListener {

    private CommunityListAdapter mCommunityListAdapter;

    private Spinner mSpinnerCommunity;

    private ImageView mImageViewBack;

    private Button mButtonSend;

    private EditText mEditTextMessage, mEditTextSubject;

    private String mMessage, mSubject, mAuthenticationId, mAuthenticationPassword;

    private int mCommunityID;

    private Helper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_post);

        initView();


        if (ConnectionDetector.isConnectingToInternet(MessagePostActivity.this)) {
            getApiCommunityList();
        } else {
            showToast(getString(R.string.network_error));
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;

            case R.id.button_send_message:

                mHelper.keyBoardHidden(MessagePostActivity.this);

                mMessage = mEditTextMessage.getText().toString().replace("\n", "<br>");
                mSubject = mEditTextSubject.getText().toString().replace("\n", "<br>");

                if (mMessage.length() > 0 && mSubject.length() > 0) {
                    postApiMessage();
                } else {
                    showToast(getString(R.string.fill_all_field));
                }


                break;
        }
    }

    //method to initialize view
    private void initView() {
        mHelper = new Helper();

        //Local Variables Initialization
        LinearLayout linearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);

        //Global Variables Initialization
        mSpinnerCommunity = (Spinner) findViewById(R.id.spinner_Community);
        mEditTextMessage = (EditText) findViewById(R.id.edit_text_message);
        mEditTextSubject = (EditText) findViewById(R.id.edit_text_subject);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mButtonSend = (Button) findViewById(R.id.button_send_message);

        imageViewFilter.setVisibility(View.GONE);
        linearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.VISIBLE);
        textViewTitle.setText(R.string.message_post);

        getData();

        clickListenerOnViews();
    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mButtonSend.setOnClickListener(this);
    }

    // Get webservice to get communities
    private void getApiCommunityList() {
        showProgressDialog();


        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<CommnunitiesResponseModel>> call = service.GetCommunity();

        call.enqueue(new Callback<List<CommnunitiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommnunitiesResponseModel>> call, Response<List<CommnunitiesResponseModel>> response) {
                try {
                    hideProgressDialog();

                    if (response.body() != null) {


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
                    } else {
                        showLogOutDialog();
                    }

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CommnunitiesResponseModel>> call, Throwable t) {
                Log.d("onFailure", t.toString());
                showToast(getString(R.string.error));
                hideProgressDialog();
            }

        });
    }

    // Post webservice to post message that show in feed
    private void postApiMessage() {
        showProgressDialog();

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.PostMessage(mCommunityID, mSubject, mMessage);
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                hideProgressDialog();
                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        Prefs.setMessagePost(MessagePostActivity.this,"message");

                        Prefs.setFeed(MessagePostActivity.this,null);
                        finish();

                    }
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
