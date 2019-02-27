package com.uptous.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.CommunityListFilterAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : CommunityActivity
 * Description :show community list user can filter feed, contacts, sign-ups, library, event according to selected community.
 * Dependencies : CommunityActivity
 */
public class CommunityActivity extends BaseActivity implements View.OnClickListener {

    private CommunityListFilterAdapter mCommunityListFilterAdapter;

    private RecyclerView mRecyclerViewCommunityList;

    private LinearLayout mLinearLayoutClose;

    private TextView mTextViewAll, mTextViewTitle;

    private ImageView mImageViewBack;

    private String mAuthenticationId, mAuthenticationPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        initView();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_all:
                Prefs.setCommunityNAme(this,null);
                Prefs.setCommunityId(this,0);
               Prefs.setClose(this,null);
               Prefs.setCommunityFilter(this,null);
                finish();

                break;
            case R.id.layout_community_filter: finish();
                break;
            case R.id.text_view_title: finish();
                break;
            case R.id.layout_close:
                finish();
                Prefs.setClose(this,"close");
                Prefs.setCommunityFilter(this,null);
                break;

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Prefs.setClose(this,"close");
    }

    private void initView() {

        //Local Variables Initialization
        LinearLayout linearLayoutSideMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Global Variables Initialization
        mRecyclerViewCommunityList = (RecyclerView) findViewById(R.id.recycler_view_community);
        mRecyclerViewCommunityList.setLayoutManager(layoutManager);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mLinearLayoutClose = (LinearLayout) findViewById(R.id.layout_close);
        mTextViewAll = (TextView) findViewById(R.id.text_view_all);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_title);

        linearLayoutSideMenu.setVisibility(View.VISIBLE);
        mImageViewBack.setVisibility(View.GONE);

        linearLayoutSideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        clickListenerOnViews();

        getData();

        if (ConnectionDetector.isConnectingToInternet(CommunityActivity.this)) {
            getApiCommunityList();
        }

        else {
           showToast(getString(R.string.network_error));
        }
    }

    //Method setClickListener On views
    private void clickListenerOnViews() {
        mTextViewAll.setOnClickListener(this);
        mLinearLayoutClose.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);
        findViewById(R.id.layout_community_filter).setOnClickListener(this);
    }

    //Method to get data from SharedPreferences
    private void getData() {
        int Position = Prefs.getPosition(this);
        String communityName = Prefs.getCommunityNAme(this);

        if (communityName != null) {
            if (Position == 0) {
                mTextViewTitle.setText("Feed - " + communityName);
            } else if (Position == 1) {
                mTextViewTitle.setText("Contacts - " + communityName);
            } else if (Position == 2) {
                mTextViewTitle.setText("Sign-Ups - " + communityName);
            } else if (Position == 3) {
                mTextViewTitle.setText("Library - " + communityName);
            } else if (Position == 4) {
                mTextViewTitle.setText("Calendar - " + communityName);
            }
        } else {
            if (Position == 0) {
                mTextViewTitle.setText(R.string.feed_all_communities);
            } else if (Position == 1) {
                mTextViewTitle.setText(R.string.contacts_all_communities);
            } else if (Position == 2) {
                mTextViewTitle.setText(R.string.sign_ups_all_communities);
            } else if (Position == 3) {
                mTextViewTitle.setText(R.string.library_all_communities);
            } else if (Position == 4) {
                mTextViewTitle.setText(R.string.event_all_communities);
            }
        }

        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword =Prefs.getAuthenticationPassword(this);
    }

    //Get web service to get communities
    private void getApiCommunityList() {
        showProgressDialog();

        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<List<CommnunitiesResponseModel>> call = service.GetCommunity();

        call.enqueue(new Callback<List<CommnunitiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommnunitiesResponseModel>> call, Response<List<CommnunitiesResponseModel>> response) {
                try {
                    hideProgressDialog();

                    if (response.body() != null) {


                        mTextViewAll.setVisibility(View.VISIBLE);
                        final List<CommnunitiesResponseModel> eventResponseModels = response.body();

                        mCommunityListFilterAdapter = new CommunityListFilterAdapter(CommunityActivity.this, eventResponseModels);
                        mRecyclerViewCommunityList.setAdapter(mCommunityListFilterAdapter);

                        ItemClickSupport.addTo(mRecyclerViewCommunityList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                                String CommunityName = eventResponseModels.get(position).getName();
                                int CommunityId = eventResponseModels.get(position).getId();
                               Prefs.setCommunityNAme(CommunityActivity.this,CommunityName);
                                Prefs.setCommunityId(CommunityActivity.this,CommunityId);
                                Prefs.setCommunityId(CommunityActivity.this,CommunityId);
                                finish();
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
                hideProgressDialog();
                showToast(getString(R.string.error));
            }

        });
    }


}
