package com.uptous.view.activity;

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
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.view.adapter.ShiftsDetailAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : ShiftDetailActivity
 * Description :show all Shifts sign_up comment and also user can comment it.
 * Dependencies : ShiftDetailActivity
 */
public class ShiftDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mImageViewBack;

    private TextView mTextViewEventName, mTextViewEventDate, mTextViewSignUpSend, mTextViewMoreSpot;

    private EditText mEditTextComment;

    private ShiftsDetailAdapter mShiftsDetailAdapter;

    private RecyclerView mRecyclerViewShiftsComment;

    private String mComment, mAuthenticationId, mAuthenticationPassword;

    private int mItemID;

    Helper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_voulnteer);

        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_send_comment:
                helper.keyBoardHidden(ShiftDetailActivity.this);
                mComment = mEditTextComment.getText().toString().replace("\n", "<br>");


                if (ConnectionDetector.isConnectingToInternet(this)) {


                    postApiComment();


                } else {
                    Toast.makeText(ShiftDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //Method to initialize view
    private void initView() {

        helper = new Helper();
        //Local Variables
        LinearLayout linearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        LinearLayout linearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);
        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Global Variables
        mRecyclerViewShiftsComment = (RecyclerView) findViewById(R.id.recycler_view_shifts_comment);
        mRecyclerViewShiftsComment.setLayoutManager(layoutManagerFiles);

        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mTextViewEventName = (TextView) findViewById(R.id.text_view_event_name);
        mTextViewEventDate = (TextView) findViewById(R.id.text_view_date_time);
        mTextViewSignUpSend = (TextView) findViewById(R.id.text_view_send_comment);
        mTextViewMoreSpot = (TextView) findViewById(R.id.text_view_more_spots);

        mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);

        mImageViewBack.setVisibility(View.VISIBLE);
        linearLayoutCommunityFilter.setVisibility(View.GONE);
        linearLayoutImageMenuLeft.setVisibility(View.GONE);


        getData();

        clickListenerOnViews();


        if (ConnectionDetector.isConnectingToInternet(ShiftDetailActivity.this)) {
            getApiShiftDetail();
        } else {
            Toast.makeText(ShiftDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);

        String openSpot = MyApplication.mSharedPreferences.getString("Total Spot", null);
        String totalSpot = MyApplication.mSharedPreferences.getString("Number of volunteer", null);
        String name = MyApplication.mSharedPreferences.getString("Name", null);
        String date = MyApplication.mSharedPreferences.getString("Date", null);

        mTextViewEventName.setText(name);

        if (date != null) {
            mTextViewEventDate.setText(date);
        }

        if (totalSpot != null && !totalSpot.equalsIgnoreCase("null")) {
            mTextViewMoreSpot.setText(openSpot + " out of " + totalSpot + " spots open");
        } else {
            mTextViewMoreSpot.setText("More spots are open");
        }

    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mTextViewSignUpSend.setOnClickListener(this);
    }

    // Get webservice to get Shifts sign_up comments
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

    // Post webservice to post Shifts sign_up comments
    private void postApiComment() {
        final ProgressDialog mProgressDialog = new ProgressDialog(ShiftDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);
        int itemID = MyApplication.mSharedPreferences.getInt("ItemId", 0);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.SignUp_Send(OpId, itemID, mComment, "");
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
