package com.uptous.ui.activity;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.ui.adapter.ShiftsDetailAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 12/28/2016.
 */

public class ShiftDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mImageViewBack;
    private LinearLayout mLinearLayoutImageMenuLeft, mLinearLayoutCommunityFilter;
    private TextView mTextViewEventName, mTextViewEventDate, mTextViewSignUpSend;
    private EditText mEditTextComment;
    private ShiftsDetailAdapter mShiftsDetailAdapter;
    private RecyclerView mRecyclerViewShiftsComment;
    private String mName, mDate, mTime, mComment, mPhone, mAuthenticationId, mAuthenticationPassword;
    private int mItemID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_voulnteer);

        initView();

    }

    private void initView() {

        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewShiftsComment = (RecyclerView) findViewById(R.id.recycler_view_shifts_comment);
        mRecyclerViewShiftsComment.setLayoutManager(layoutManagerFiles);
        mLinearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        mLinearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mTextViewEventName = (TextView) findViewById(R.id.text_view_event_name);
        mTextViewEventDate = (TextView) findViewById(R.id.text_view_date_time);
        mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);
        mTextViewSignUpSend = (TextView) findViewById(R.id.text_view_send_comment);

        mImageViewBack.setVisibility(View.VISIBLE);
        mLinearLayoutCommunityFilter.setVisibility(View.GONE);
        mLinearLayoutImageMenuLeft.setVisibility(View.GONE);


        getData();

        clickListenerOnViews();


        if (ConnectionDetector.isConnectingToInternet(ShiftDetailActivity.this)) {
            getApiShiftDetail();
        } else {
            Toast.makeText(ShiftDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);

        mName = MyApplication.mSharedPreferences.getString("Name", null);
        mDate = MyApplication.mSharedPreferences.getString("Date", null);
//        mTime = MyApplication.mSharedPreferences.getString("EndDate", null);

        mTextViewEventName.setText(mName);

        if (mTime != null) {
//            mTextViewEventDate.setText(mDate + ", " + mTime);
        } else {
            mTextViewEventDate.setText(mDate);
        }
    }

    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mTextViewSignUpSend.setOnClickListener(this);
    }

    private void getApiShiftDetail() {

        final ProgressDialog mProgressDialog = new ProgressDialog(ShiftDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);
        mItemID = MyApplication.mSharedPreferences.getInt("ItemId", 0);


        APIServices service =
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
                                                 mShiftsDetailAdapter = new ShiftsDetailAdapter(ShiftDetailActivity.this,
                                                         eventResponseModelsItem.get(j).getVolunteers());
                                                 mRecyclerViewShiftsComment.setAdapter(mShiftsDetailAdapter);
                                             }


                                         }
                                     }


                                 } else {
                                     Toast.makeText(ShiftDetailActivity.this, "" + response.raw().code(), Toast.LENGTH_SHORT).show();
                                 }


                             } catch (Exception e) {
                                 Log.d("onResponse", "There is an error");
                                 e.printStackTrace();
                             }

                         }

                         @Override
                         public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                             mProgressDialog.dismiss();
                             Toast.makeText(ShiftDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                             Log.d("onFailure", t.toString());
                         }

                     }

        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_send_comment:

                mComment = mEditTextComment.getText().toString();


                if (ConnectionDetector.isConnectingToInternet(this)) {

                    if (mComment.length() > 0) {
                        postApiComment();
                    } else {
                        Toast.makeText(ShiftDetailActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ShiftDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void postApiComment() {
        final ProgressDialog mProgressDialog = new ProgressDialog(ShiftDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);
        int itemID = MyApplication.mSharedPreferences.getInt("ItemId", 0);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.SignUp_Send(OpId, itemID, mComment, mPhone);
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
                        Toast.makeText(ShiftDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ShiftDetailActivity.this, "Couldn't comment", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                Toast.makeText(ShiftDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
    }
}
