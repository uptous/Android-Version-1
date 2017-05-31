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
import com.uptous.controller.utils.GifImageView;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.view.adapter.RSPVDetailAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : VoteDetailActivity
 * Description :show all Vote sign_up comment and also user can comment it.
 * Dependencies : VoteDetailActivity
 */
public class VoteDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageViewBack;
    private TextView mTextViewTitle, mViewDateTimeTextView, mTextViewSignUpSend;

    private EditText mEditTextComment, mEditTextNumberOfAttendees;
    private RSPVDetailAdapter mRspvDetailAdapter;
    private RecyclerView mRecyclerViewRspvComment;
    private String mComment, mAuthenticationId, mAuthenticationPassword;
    private int mItemID;
    private String mNumberOfAttendees;
    private Helper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail);

        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_send_comment:

                mHelper.keyBoardHidden(VoteDetailActivity.this);
                mComment = mEditTextComment.getText().toString().replace("\n", "<br>");
                mNumberOfAttendees = mEditTextNumberOfAttendees.getText().toString();

                if (ConnectionDetector.isConnectingToInternet(this)) {
                    postApiComment();
                } else {
                    Toast.makeText(VoteDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //Method to initialize view
    private void initView() {

        mHelper = new Helper();

        //Local Variables Initialization
        LinearLayout linearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        LinearLayout linearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);
        GifImageView playGifView = (GifImageView) findViewById(R.id.image_gif);
        playGifView.setGifImageResource(R.mipmap.smiley_test);
        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Global Variables Initialization
        mRecyclerViewRspvComment = (RecyclerView) findViewById(R.id.recycler_view_rspv_comment);
        mRecyclerViewRspvComment.setLayoutManager(layoutManagerFiles);

        mViewDateTimeTextView = (TextView) findViewById(R.id.text_view_date_time);
        mTextViewSignUpSend = (TextView) findViewById(R.id.text_view_send_comment);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_message_toolbar);

        mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);
        mEditTextNumberOfAttendees = (EditText) findViewById(R.id.edit_text_number_of_attendees);

        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mTextViewTitle.setVisibility(View.VISIBLE);
        mImageViewBack.setVisibility(View.VISIBLE);
        linearLayoutCommunityFilter.setVisibility(View.GONE);
        linearLayoutImageMenuLeft.setVisibility(View.GONE);

        clickListener();

        getData();

        if (ConnectionDetector.isConnectingToInternet(VoteDetailActivity.this)) {
            getApiVoteDetail();
        } else {
            Toast.makeText(VoteDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    // Method to Get data from SharedPreference
    private void getData() {
        String mTitle = MyApplication.mSharedPreferences.getString("Name", null);
        String mEndTime = MyApplication.mSharedPreferences.getString("EndTime", null);
        String mDate = MyApplication.mSharedPreferences.getString("Date", null);
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);

        mTextViewTitle.setText("Join the " + mTitle);

        if (mEndTime == null) {
            mViewDateTimeTextView.setText(mDate);
        } else {
            mViewDateTimeTextView.setText(mDate + ", " + mEndTime);
        }
    }

    //Method to set on clickListener on views
    private void clickListener() {
        mTextViewSignUpSend.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);
    }

    // Get webservice to get Vote sign_up comments
    private void getApiVoteDetail() {

        final ProgressDialog mProgressDialog = new ProgressDialog(VoteDetailActivity.this);
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
                                 if (response.body() != null) {

                                     List<SignUpDetailResponseModel> eventResponseModels = response.body();

                                     for (int i = 0; eventResponseModels.size() >= i; i++) {

                                         List<SignUpDetailResponseModel.ItemsBean> eventResponseModelsItem =
                                                 response.body().get(i).getItems();

                                         for (int j = 0; eventResponseModelsItem.size() > j; j++) {
                                             int itemid = eventResponseModelsItem.get(j).getId();
                                             if (itemid == mItemID) {
                                                 mRspvDetailAdapter = new RSPVDetailAdapter(VoteDetailActivity.this,
                                                         eventResponseModelsItem.get(j).getVolunteers());
                                                 mRecyclerViewRspvComment.setAdapter(mRspvDetailAdapter);
                                             }


                                         }
                                     }


                                 }


                             } catch (Exception e) {
                                 Log.d("onResponse", "There is an error");
                                 e.printStackTrace();
                             }

                         }

                         @Override
                         public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                             mProgressDialog.dismiss();
                             Toast.makeText(VoteDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                             Log.d("onFailure", t.toString());
                         }

                     }

        );
    }

    // Post webservice to Post Vote sign_up comments
    private void postApiComment() {
        final ProgressDialog mProgressDialog = new ProgressDialog(VoteDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);
        int itemID = MyApplication.mSharedPreferences.getInt("ItemId", 0);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.SignUp_Send_RSPV(OpId, itemID, mComment, mNumberOfAttendees);
        call.enqueue(new Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.isSuccessful()) {

                            finish();

                        }
                    } else {
                        Toast.makeText(VoteDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VoteDetailActivity.this, "Couldn't comment", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                Toast.makeText(VoteDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
    }

}
