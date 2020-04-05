package com.uptous.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.GifImageView;
import com.uptous.controller.utils.Helper;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.RSVPDetailAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : RSVPDetailActivity
 * Description :show all RSVP sign_up comment and also user can comment it.
 * Dependencies : RSVPDetailActivity
 */

public class RSVPDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImageViewBack;
    private TextView mTextViewTitle, mViewDateTimeTextView, mTextViewSignUpSend, mTextViewEventName;
    private int mItemID;
    private String NumberOfAttendees;
    private EditText mEditTextComment;
    private RSVPDetailAdapter mRSVPDetailAdapter;
    private RecyclerView mRecyclerViewRsvpComment;
    private String mComment, mAuthenticationId, mAuthenticationPassword;

    private Helper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsvp_detail);
        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_send_comment:

                mHelper.keyBoardHidden(RSVPDetailActivity.this);
                mComment = mEditTextComment.getText().toString().replace("\n", "<br>");

                if (ConnectionDetector.isConnectingToInternet(this)) {

                    postApiComment();

                } else {
                    Toast.makeText(RSVPDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //method to initialize view
    private void initView() {
        mHelper = new Helper();

        try {
            //Local Variables Initialization
            LinearLayout linearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
            LinearLayout linearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);
            GifImageView playGifView = (GifImageView) findViewById(R.id.image_gif);

            LinearLayoutManager layoutManagerFiles
                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

            //Global Variables Initialization
            mRecyclerViewRsvpComment = (RecyclerView) findViewById(R.id.recycler_view_rsvp_comment);
            mRecyclerViewRsvpComment.setLayoutManager(layoutManagerFiles);

            mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);
            mViewDateTimeTextView = (TextView) findViewById(R.id.text_view_date_time);
            mTextViewSignUpSend = (TextView) findViewById(R.id.text_view_send_comment);
            //mTextViewTitle = (TextView) findViewById(R.id.text_view_message_toolbar);
            mTextViewEventName = (TextView) findViewById(R.id.text_view_event_name);

            mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

            playGifView.setGifImageResource(R.mipmap.smiley_test);
            //mTextViewTitle.setVisibility(View.VISIBLE);
            mImageViewBack.setVisibility(View.VISIBLE);
            linearLayoutCommunityFilter.setVisibility(View.GONE);
            linearLayoutImageMenuLeft.setVisibility(View.GONE);

            clickListener();

            getData();

            if (ConnectionDetector.isConnectingToInternet(RSVPDetailActivity.this)) {
                getApiRSVPDetail();
            } else {
                Toast.makeText(RSVPDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Method to Get data from SharedPreference
    private void getData() {


        try {
            String title = Prefs.getName(this);
            String endTime = "";
            String date = Prefs.getDate(this);
            mAuthenticationId = Prefs.getAuthenticationId(this);
            mAuthenticationPassword = Prefs.getAuthenticationPassword(this);

            mTextViewEventName.setText(title);
            //mTextViewTitle.setText(title);

            if (endTime == null) {
                mViewDateTimeTextView.setText(date);
            } else {
                if (date != null)
                    mViewDateTimeTextView.setText(date + ", " + endTime);
                else {
                    mViewDateTimeTextView.setText("");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Method to set on clickListener on views
    private void clickListener() {
        mImageViewBack.setOnClickListener(this);
        mTextViewSignUpSend.setOnClickListener(this);
    }

    // Get webservice to get RSVP sign_up comments
    private void getApiRSVPDetail() {

        try {
            showProgressDialog();
            int OpId = Prefs.getOpportunityId(this);
            mItemID = Prefs.getItemId(this);


            APIServices service =
                    ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
            Call<List<SignUpDetailResponseModel>> call = service.GetItem(OpId, mItemID);

            call.enqueue(new Callback<List<SignUpDetailResponseModel>>() {
                             @Override
                             public void onResponse(Call<List<SignUpDetailResponseModel>> call,
                                                    Response<List<SignUpDetailResponseModel>> response) {
                                 try {
                                     hideProgressDialog();
                                     if (response.isSuccessful()) {
                                         List<SignUpDetailResponseModel> eventResponseModels = response.body();
                                         for (int i = 0; eventResponseModels.size() >= i; i++) {
                                             List<SignUpDetailResponseModel.ItemsBean> eventResponseModelsItem =
                                                     response.body().get(i).getItems();
                                             for (int j = 0; eventResponseModelsItem.size() > j; j++) {
                                                 //search mItemID on response
                                                 if (mItemID == eventResponseModelsItem.get(j).getId()) {
                                                     mRSVPDetailAdapter = new RSVPDetailAdapter(RSVPDetailActivity.this,
                                                             eventResponseModelsItem.get(j).getVolunteers());
                                                     mRecyclerViewRsvpComment.setAdapter(mRSVPDetailAdapter);
                                                 }
//                                             for (int j = 0; eventResponseModelsItem.size() > j; j++) {
//                                                 int itemid = eventResponseModelsItem.get(j).getId();
////                                                 logger.setAdEvent("response itemid: "+itemid);
////                                                 LogFile.createLogFile(logger);
//
//
//                                                 if (mItemID != 0) {
//                                                     mRSVPDetailAdapter = new RSVPDetailAdapter(RSVPDetailActivity.this,
//                                                             eventResponseModels.get(0).getItems().get(0).getVolunteers());
//                                                     mRecyclerViewRsvpComment.setAdapter(mRSVPDetailAdapter);
//                                                 } else {
//                                                     mRSVPDetailAdapter = new RSVPDetailAdapter(RSVPDetailActivity.this,
//                                                             eventResponseModelsItem.get(j).getVolunteers());
//                                                     mRecyclerViewRsvpComment.setAdapter(mRSVPDetailAdapter);
//                                                 }
//
//                                             }
                                             }
                                         }


                                     } else {
                                         Toast.makeText(RSVPDetailActivity.this, "" + response.raw().code(), Toast.LENGTH_SHORT).show();
                                     }


                                 } catch (Exception e) {
                                     e.printStackTrace();

                                 }

                             }

                             @Override
                             public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                                 hideProgressDialog();
                                 Toast.makeText(RSVPDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                                 Log.d("onFailure", t.toString());
                             }

                         }

            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Post webservice to post RSVP sign_up comments
    private void postApiComment() {
        showProgressDialog();
        int OpId = Prefs.getOpportunityId(this);
        int itemID = Prefs.getItemId(this);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.SignUp_Send(OpId, itemID, mComment, "");
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                hideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.isSuccessful()) {

                            finish();

                        }
                    } else {
                        Toast.makeText(RSVPDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RSVPDetailActivity.this, "Couldn't comment", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(RSVPDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();


            }
        });
    }
}
