package com.uptous.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.VolunteeredAdapter;
import com.uptous.view.adapter.VolunteeredRspvAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : VolunteerDetailActivity
 * Description :
 * Dependencies : VolunteerDetailActivity
 */

public class VolunteerDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImageViewBack;
    private VolunteeredAdapter mVolunteeredAdapter;
    private VolunteeredRspvAdapter mVolunteeredRspvAdapter;
    private RecyclerView mRecyclerViewVolunteerComment;
    private String mEventName, mDate, mEndTime, mAuthenticationId, mAuthenticationPassword, mStringType, mFromName,
            mToName;
    private TextView mTextViewEventName, mTextViewEventDate, mTextViewCancelAssignment, mViewDrivingFromTextView,
            mViewDrivingToTextView;
    private int mItemID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_volunteer);

        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_cancel_assignment:
                dialogCancelAssignment();
                break;
        }
    }

    //Method to initialize view
    private void initView() {

        //Local Variables Initialization
        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayout linearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        LinearLayout linearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);

        //Global Variables Initialization
        mRecyclerViewVolunteerComment = (RecyclerView) findViewById(R.id.recycler_view_voulnteer_comment);
        mRecyclerViewVolunteerComment.setLayoutManager(layoutManagerFiles);

        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mViewDrivingFromTextView = (TextView) findViewById(R.id.text_view_driving_from);
        mViewDrivingToTextView = (TextView) findViewById(R.id.text_view_driving_to);
        mTextViewEventName = (TextView) findViewById(R.id.text_view_volunteer_event_name);
        mTextViewEventDate = (TextView) findViewById(R.id.text_view_volunteer_event_date);
        mTextViewCancelAssignment = (TextView) findViewById(R.id.text_view_cancel_assignment);

        mImageViewBack.setVisibility(View.VISIBLE);
        linearLayoutCommunityFilter.setVisibility(View.GONE);
        linearLayoutImageMenuLeft.setVisibility(View.GONE);

        clickListenerOnViews();

        getApiVolunteer();

        getData();

    }

    // Method to Get data from SharedPreference
    private void getData() {

        mFromName = Prefs.getFromName(this);
        mToName = Prefs.getToName(this);
        mStringType = Prefs.getSignUpType(this);
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
        mEventName =Prefs.getName(this);
        mDate = Prefs.getDate(this);
        mEndTime = "";

        setData();

    }

    // Method to Set data
    private void setData() {
        if (mStringType != null) {
            if (!mStringType.equalsIgnoreCase("Party")) {

                mViewDrivingFromTextView.setVisibility(View.VISIBLE);
                mViewDrivingToTextView.setVisibility(View.VISIBLE);
                mTextViewEventName.setVisibility(View.GONE);
                mViewDrivingFromTextView.setText("Driving from: " + mFromName);
                mViewDrivingToTextView.setText("To: " + mToName);
            }
        }

        mTextViewEventName.setText(mEventName);


        if (mDate != null) {
            if (mEndTime != null) {
                mTextViewEventDate.setText(mDate + "," + mEndTime);
            } else {
                mTextViewEventDate.setText(mDate);
            }
        }

        if (mStringType != null) {
            if (mStringType.equalsIgnoreCase("RSPV")) {
                mTextViewEventDate.setVisibility(View.GONE);
            } else if (mStringType.equalsIgnoreCase("Driver")) {
                mTextViewEventDate.setVisibility(View.VISIBLE);
            } else if (mStringType.equalsIgnoreCase("Vote")) {
                mTextViewEventDate.setVisibility(View.GONE);
            }
        }

    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mTextViewCancelAssignment.setOnClickListener(this);

    }

    // Get webservice to get Volunteer  comments
    private void getApiVolunteer() {

        showProgressDialog();
        int OpId = Prefs.getOpportunityId(this);
        mItemID = Prefs.getItemId(this);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
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
                                    if (mStringType == null || mStringType.equalsIgnoreCase("Driver") ||
                                            mStringType.equalsIgnoreCase("Party")) {
                                        mVolunteeredAdapter = new VolunteeredAdapter(VolunteerDetailActivity.this,
                                                eventResponseModelsItem.get(j).getVolunteers());
                                        mRecyclerViewVolunteerComment.setAdapter(mVolunteeredAdapter);
                                    } else {
                                        mVolunteeredRspvAdapter = new VolunteeredRspvAdapter(VolunteerDetailActivity.this,
                                                eventResponseModelsItem.get(j).getVolunteers());
                                        mRecyclerViewVolunteerComment.setAdapter(mVolunteeredRspvAdapter);
                                    }

                                }


                            }
                        }


                    } else {
                        showLogOutDialog();
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                hideProgressDialog();
                showToast(getString(R.string.error));
                Log.d("onFailure", t.toString());
            }

        });
    }

    //Method to show cancel assignment dialog
    private void dialogCancelAssignment() {
        final CustomizeDialog customizeDialog = new CustomizeDialog(VolunteerDetailActivity.this);
        customizeDialog.show();
        customizeDialog.setContentView(R.layout.dialog_cancel_assignment);
        Button buttonCancel = (Button) customizeDialog.findViewById(R.id.button_cancel);
        Button buttonOk = (Button) customizeDialog.findViewById(R.id.button_ok);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customizeDialog.dismiss();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postApiCancelAssignment();
                customizeDialog.dismiss();
            }
        });


    }

    // Post webservice to cancel volunteer
    private void postApiCancelAssignment() {
       showProgressDialog();


        int OpId =Prefs.getOpportunityId(this);
        int itemID = Prefs.getItemId(this);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.Cancel_Assignment(OpId, itemID, "");
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

               hideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.isSuccessful()) {
                            finish();
                        }
                    }

                } else {
                    Toast.makeText(VolunteerDetailActivity.this, "Could not add volunteer", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
               hideProgressDialog();
                Toast.makeText(VolunteerDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
