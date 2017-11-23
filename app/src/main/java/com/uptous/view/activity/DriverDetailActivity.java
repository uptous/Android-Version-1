package com.uptous.view.activity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.DriverDetailAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : DriverDetailActivity
 * Description :User show all driver comment and also user can comment it.
 * Dependencies : DriverDetailAdapter
 */


public class DriverDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImageViewBack;

    private TextView mViewDrivingFromTextView, mViewDrivingToTextView, mViewDateTimeTextView,
            mTextViewSignUpSend;

    private EditText mEditTextComment, mEditTextPhone;

    private DriverDetailAdapter mDriverDetailAdapter;

    private RecyclerView mRecyclerViewDriverComment;

    private String mComment, mAuthenticationId, mPhone, mAuthenticationPassword;

    private int mItemID;

    private Helper mHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);

        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_send_comment:
                mHelper.keyBoardHidden(DriverDetailActivity.this);

                mComment = mEditTextComment.getText().toString().replace("\n", "<br>");
                mPhone = mEditTextPhone.getText().toString().replace("\n", "<br>");

                if (ConnectionDetector.isConnectingToInternet(this)) {
                    postApiComment();
                } else {
                    showToast(getString(R.string.network_error));
                }
                break;
        }
    }

    //Method to initialize view
    private void initView() {
        mHelper = new Helper();
        //Global Variables Initialization
        LinearLayout linearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        LinearLayout linearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);
        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Global Variables Initialization
        mRecyclerViewDriverComment = (RecyclerView) findViewById(R.id.recycler_view_driver_comment);
        mRecyclerViewDriverComment.setLayoutManager(layoutManagerFiles);

        mViewDrivingFromTextView = (TextView) findViewById(R.id.text_view_driving_from);
        mViewDrivingToTextView = (TextView) findViewById(R.id.text_view_driving_to);
        mViewDateTimeTextView = (TextView) findViewById(R.id.text_view_date_time);
        mTextViewSignUpSend = (TextView) findViewById(R.id.text_view_send_comment);

        mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);
        mEditTextPhone = (EditText) findViewById(R.id.edit_text_phone);
        mEditTextPhone.clearFocus();
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mImageViewBack.setVisibility(View.VISIBLE);

        linearLayoutCommunityFilter.setVisibility(View.GONE);
        linearLayoutImageMenuLeft.setVisibility(View.GONE);

        if (ConnectionDetector.isConnectingToInternet(DriverDetailActivity.this)) {
            getApiDriverDetail();
        } else {
            showToast(getString(R.string.network_error));
        }


        getData();

        clickListenerOnViews();
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mTextViewSignUpSend.setOnClickListener(this);
    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword =Prefs.getAuthenticationPassword(this);

        String FromName = Prefs.getFromName(this);
        String ToName = Prefs.getToName(this);
        String Date =Prefs.getDate(this);

        mViewDrivingFromTextView.setText("Driving from: " + FromName);
        mViewDrivingToTextView.setText("To: " + ToName);

        if (Date != null)
            mViewDateTimeTextView.setText(Date);
    }

    // Get webservice to get all driver comments
    private void getApiDriverDetail() {

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
                                 if (response.body() != null) {
                                     List<SignUpDetailResponseModel> eventResponseModels = response.body();
                                     for (int i = 0; eventResponseModels.size() >= i; i++) {

                                         List<SignUpDetailResponseModel.ItemsBean> eventResponseModelsItem =
                                                 response.body().get(i).getItems();

                                         for (int j = 0; eventResponseModelsItem.size() > j; j++) {
                                             int itemid = eventResponseModelsItem.get(j).getId();
                                             if (itemid == mItemID) {
                                                 mDriverDetailAdapter = new DriverDetailAdapter(DriverDetailActivity.this,
                                                         eventResponseModelsItem.get(j).getVolunteers());
                                                 mRecyclerViewDriverComment.setAdapter(mDriverDetailAdapter);
                                             }

                                         }
                                     }

                                 } else {
                                    showLogOutDialog();


                                 }


                             } catch (Exception e) {
                                 e.printStackTrace();
                             }

                         }

                         @Override
                         public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                            hideProgressDialog();
                            showToast(getString(R.string.error));
                             Log.d("onFailure", t.toString());
                         }

                     }

        );
    }

    // Post webservice to post comments
    private void postApiComment() {
      showProgressDialog();


        int OpId =Prefs.getOpportunityId(this);
        mItemID = Prefs.getItemId(this);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.SignUp_Send(OpId, mItemID, mComment, mPhone);
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
                        showToast(getString(R.string.error));
                    }
                } else {
                    showToast("Couldn't comment");
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
               showToast(getString(R.string.error));
               hideProgressDialog();


            }
        });
    }
}
