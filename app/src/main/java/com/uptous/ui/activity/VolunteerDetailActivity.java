package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.uptous.ui.adapter.VolunteeredAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 1/16/2017.
 */

public class VolunteerDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageViewBack;
    private LinearLayout mLinearLayoutImageMenuLeft,mLinearLayoutCommunityFilter;
    private VolunteeredAdapter mVolunteeredAdapter;
    private RecyclerView mRecyclerViewVolunteerComment;
    private String mEventName, mDate, mEndTime, mAuthenticationId, mAuthenticationPassword;
    private TextView mTextViewEventName, mTextViewEventDate, mTextViewCancelAssignment;
    int mItemID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_volunteer);

        initView();
    }

    private void initView() {

        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewVolunteerComment = (RecyclerView) findViewById(R.id.recycler_view_voulnteer_comment);
        mRecyclerViewVolunteerComment.setLayoutManager(layoutManagerFiles);
        mLinearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        mLinearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mTextViewEventName = (TextView) findViewById(R.id.text_view_volunteer_event_name);
        mTextViewEventDate = (TextView) findViewById(R.id.text_view_volunteer_event_date);
        mTextViewCancelAssignment = (TextView) findViewById(R.id.text_view_cancel_assignment);

        mImageViewBack.setVisibility(View.VISIBLE);
        mLinearLayoutCommunityFilter.setVisibility(View.GONE);
        mLinearLayoutImageMenuLeft.setVisibility(View.GONE);

        clickListenerOnViews();

        getApiVolunteer();

        getData();

    }

    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        mEventName = MyApplication.mSharedPreferences.getString("Name", null);
        mDate = MyApplication.mSharedPreferences.getString("Date", null);
        mEndTime = MyApplication.mSharedPreferences.getString("EndDate", null);


        mTextViewEventName.setText(mEventName);
        if (mEndTime == null) {
            mTextViewEventDate.setText(mDate);
        } else {
            mTextViewEventDate.setText(mDate + "," + mEndTime);
        }

    }

    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mTextViewCancelAssignment.setOnClickListener(this);

    }

    private void getApiVolunteer() {

        final ProgressDialog mProgressDialog = new ProgressDialog(VolunteerDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);
        mItemID = MyApplication.mSharedPreferences.getInt("ItemId", 0);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<SignUpDetailResponseModel>> call = service.GetItem(OpId, mItemID);

        call.enqueue(new Callback<List<SignUpDetailResponseModel>>() {
            @Override
            public void onResponse(Call<List<SignUpDetailResponseModel>> call,
                                   Response<List<SignUpDetailResponseModel>> response) {
                try {
                    mProgressDialog.dismiss();

                    if (response.isSuccessful()) {

                        List<SignUpDetailResponseModel> eventResponseModels = response.body();

                        for (int i = 0; eventResponseModels.size() >= i; i++) {

                            List<SignUpDetailResponseModel.ItemsBean> eventResponseModelsItem =
                                    response.body().get(i).getItems();

                            for (int j = 0; eventResponseModelsItem.size() > j; j++) {
                                int itemid = eventResponseModelsItem.get(j).getId();
                                if (itemid == mItemID) {
                                    mVolunteeredAdapter = new VolunteeredAdapter(VolunteerDetailActivity.this,
                                            eventResponseModelsItem.get(j).getVolunteers());
                                    mRecyclerViewVolunteerComment.setAdapter(mVolunteeredAdapter);
                                }


                            }
                        }


                    } else {
                        Toast.makeText(VolunteerDetailActivity.this, "" + response.raw().code(), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(VolunteerDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                Log.d("onFailure", t.toString());
            }

        });
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

    private void postApiCancelAssignment() {
        final ProgressDialog mProgressDialog = new ProgressDialog(VolunteerDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);
        int itemID = MyApplication.mSharedPreferences.getInt("ItemId", 0);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.Cancel_Assignment(OpId, itemID, "");
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.isSuccessful()) {
                            finish();

                        }
                    } else {

                    }
                } else {
                    Toast.makeText(VolunteerDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                Toast.makeText(VolunteerDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
    }
}
