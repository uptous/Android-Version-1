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

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.Helper;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.PartyDetailAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : PartyDetailActivity
 * Description :show all Party/Potluck sign_up comment and also user can comment it.
 * Dependencies : PartyDetailActivity
 */
public class PartyDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImageViewBack;
    private TextView mTextViewEventName, mTextViewEventDate, mTextViewSignUpSend, mTextViewMoreSpot;
    private EditText mEditTextComment, mEditTextNumberOfAttendees;
    private PartyDetailAdapter mPartyDetailAdapter;
    private RecyclerView mRecyclerViewPotluckComment;
    private String mComment, mAuthenticationId, mAuthenticationPassword;
    private int mItemID;
    private String mNumberOfAttendees;
    private Helper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potluck_detail);

        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_send_comment:
                mHelper.keyBoardHidden(PartyDetailActivity.this);
                mComment = mEditTextComment.getText().toString().replace("\n", "<br>");
                mNumberOfAttendees = mEditTextNumberOfAttendees.getText().toString();


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
        //Local Variables Initialization
        LinearLayout linearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        LinearLayout linearLayoutImageMenuLeft = (LinearLayout) findViewById(R.id.imgmenuleft);
        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Global Variables Initialization
        mRecyclerViewPotluckComment = (RecyclerView) findViewById(R.id.recycler_view_potluck_comment);
        mRecyclerViewPotluckComment.setLayoutManager(layoutManagerFiles);

        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mTextViewEventName = (TextView) findViewById(R.id.text_view_event_name);
        mTextViewEventDate = (TextView) findViewById(R.id.text_view_date_time);
        mTextViewSignUpSend = (TextView) findViewById(R.id.text_view_send_comment);
        mTextViewMoreSpot = (TextView) findViewById(R.id.text_view_more_spots);

        mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);
        mEditTextNumberOfAttendees = (EditText) findViewById(R.id.edit_text_number_of_attendees);

        mImageViewBack.setVisibility(View.VISIBLE);
        linearLayoutCommunityFilter.setVisibility(View.GONE);
        linearLayoutImageMenuLeft.setVisibility(View.GONE);


        getData();

        clickListenerOnViews();


        if (ConnectionDetector.isConnectingToInternet(PartyDetailActivity.this)) {
            getApiPartyDetail();
        } else {
            showToast(getString(R.string.network_error));
        }

    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword = Prefs.getAuthenticationPassword(this);

        String openSpot = Prefs.getTotalSpot(this);
        String totalSpot = Prefs.getNumberofvolunteer(this);
        String name = Prefs.getName(this);
        String date = Prefs.getDate(this);

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

    // Get webservice to get Party sign_up comments
    private void getApiPartyDetail() {

       showProgressDialog();
        int OpId =Prefs.getOpportunityId(this);
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
                                             int itemid = eventResponseModelsItem.get(j).getId();
                                             if (itemid == mItemID) {
                                                 mPartyDetailAdapter = new PartyDetailAdapter(PartyDetailActivity.this,
                                                         eventResponseModelsItem.get(j).getVolunteers());
                                                 mRecyclerViewPotluckComment.setAdapter(mPartyDetailAdapter);
                                             }


                                         }
                                     }

                                 } else {
                                     showToast("" + response.raw().code());
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

    // Post webservice to post Shifts sign_up comments
    private void postApiComment() {

        int OpId = Prefs.getOpportunityId(this);
        int itemID = Prefs.getItemId(this);
        final APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.SignUp_Send_RSVP(OpId, itemID, mComment, mNumberOfAttendees);
        call.enqueue(new Callback<PostCommentResponseModel>() {
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
