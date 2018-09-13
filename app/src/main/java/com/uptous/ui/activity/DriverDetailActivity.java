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
import com.uptous.ui.adapter.DriverDetailAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 1/24/2017.
 */

public class DriverDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageViewBack;
    private LinearLayout mLinearLayoutImageMenuLeft;
    private LinearLayout mLinearLayoutCommunityFilter;
    private TextView mViewDrivingFromTextView;
    private TextView mViewDrivingToTextView;
    private TextView mViewDateTimeTextView;
    private TextView mTextViewSignUpSend;
    private DriverDetailAdapter mDriverDetailAdapter;
    private RecyclerView mRecyclerViewDriverComment;
    private String Comment, Phone, FromName, ToName, Date, AuthenticationId, AuthenticationPassword;
    private EditText mEditTextComment, mEditTextPhone;
    int itemID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);

        initView();

    }

    private void initView() {
        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewDriverComment = (RecyclerView) findViewById(R.id.recycler_view_driver_comment);
        mRecyclerViewDriverComment.setLayoutManager(layoutManagerFiles);
        mViewDrivingFromTextView = (TextView) findViewById(R.id.text_view_driving_from);
        mViewDrivingToTextView = (TextView) findViewById(R.id.text_view_driving_to);
        mViewDateTimeTextView = (TextView) findViewById(R.id.text_view_date_time);
        mTextViewSignUpSend = (TextView) findViewById(R.id.text_view_send_comment);
        mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);
        mEditTextPhone = (EditText) findViewById(R.id.edit_text_phone);
        mLinearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        mLinearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mImageViewBack.setVisibility(View.VISIBLE);
        mLinearLayoutCommunityFilter.setVisibility(View.GONE);
        mLinearLayoutImageMenuLeft.setVisibility(View.GONE);

        if (ConnectionDetector.isConnectingToInternet(DriverDetailActivity.this)) {
            getApiDriverDetail();
        } else {
            Toast.makeText(DriverDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


        getData();

        clickListenerOnViews();
    }

    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mTextViewSignUpSend.setOnClickListener(this);
    }

    private void getData() {
        AuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        AuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        FromName = MyApplication.mSharedPreferences.getString("FromName", null);
        ToName = MyApplication.mSharedPreferences.getString("ToName", null);
        Date = MyApplication.mSharedPreferences.getString("Date", null);

        mViewDrivingFromTextView.setText("Driving from: " + FromName);
        mViewDrivingToTextView.setText("To: " + ToName);
        mViewDateTimeTextView.setText(Date);
    }

    private void getApiDriverDetail() {

        final ProgressDialog mProgressDialog = new ProgressDialog(DriverDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);
        itemID = MyApplication.mSharedPreferences.getInt("ItemId", 0);


        APIServices service =
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);
        Call<List<SignUpDetailResponseModel>> call = service.GetItem(OpId, itemID);

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
                                             if (itemid == itemID) {
                                                 mDriverDetailAdapter = new DriverDetailAdapter(DriverDetailActivity.this,
                                                         eventResponseModelsItem.get(j).getVolunteers());
                                                 mRecyclerViewDriverComment.setAdapter(mDriverDetailAdapter);
                                             }

                                         }
                                     }


                                 } else {
                                     Toast.makeText(DriverDetailActivity.this, "" + response.raw().code(), Toast.LENGTH_SHORT).show();
                                 }


                             } catch (Exception e) {
                                 Log.d("onResponse", "There is an error");
                                 e.printStackTrace();
                             }

                         }

                         @Override
                         public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                             mProgressDialog.dismiss();
                             Toast.makeText(DriverDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
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

                Comment = mEditTextComment.getText().toString();
                Phone = mEditTextPhone.getText().toString();

                if (ConnectionDetector.isConnectingToInternet(this)) {

                    if (Comment.length() > 0) {
                        postApiComment();
                    } else {
                        Toast.makeText(DriverDetailActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(DriverDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void postApiComment() {
        final ProgressDialog mProgressDialog = new ProgressDialog(DriverDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);
        int itemID = MyApplication.mSharedPreferences.getInt("ItemId", 0);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);

        Call<PostCommentResponseModel> call = service.SignUp_Send(OpId, itemID, Comment, Phone);
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
                        Toast.makeText(DriverDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DriverDetailActivity.this, "Couldn't comment", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                Toast.makeText(DriverDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
    }
}
