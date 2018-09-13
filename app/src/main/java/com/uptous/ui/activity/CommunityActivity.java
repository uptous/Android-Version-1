package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.ui.adapter.CommunityListSortingListAdapter;
import com.uptous.ui.adapter.TypeListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 1/11/2017.
 */

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {

    private CommunityListSortingListAdapter mCommunityListSortingListAdapter;
    private RecyclerView mRecyclerViewCommunityList;
    private ImageView mImageViewBack;
    private LinearLayout mLinearLayoutSideMenu;
    private LinearLayout mLinearLayoutCommunityFilter;
    private LinearLayout mLinearLayoutClose;
    private TextView mTextViewAll;
    private String AuthenticationId, AuthenticationPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        initView();


    }

    private void initView() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewCommunityList = (RecyclerView) findViewById(R.id.recycler_view_community);
        mRecyclerViewCommunityList.setLayoutManager(layoutManager);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
//        mLinearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
//        mLinearLayoutCommunityFilter.setVisibility(View.GONE);
        mImageViewBack.setOnClickListener(this);
        mImageViewBack.setVisibility(View.VISIBLE);
        mLinearLayoutSideMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        mLinearLayoutClose = (LinearLayout) findViewById(R.id.layout_close);
        mLinearLayoutSideMenu.setVisibility(View.GONE);
        mTextViewAll = (TextView) findViewById(R.id.text_view_all);

        clickListenerOnViews();

        getData();

        if (ConnectionDetector.isConnectingToInternet(CommunityActivity.this)) {
            getApiCommunityList();
        } else {
            Toast.makeText(CommunityActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void clickListenerOnViews() {
        mTextViewAll.setOnClickListener(this);
        mLinearLayoutClose.setOnClickListener(this);
    }

    private void getData() {
        AuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        AuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    private void getApiCommunityList() {
        final ProgressDialog progressDialog = new ProgressDialog(CommunityActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);

        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);
        Call<List<CommnunitiesResponseModel>> call = service.GetCommunity();

        call.enqueue(new Callback<List<CommnunitiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommnunitiesResponseModel>> call, Response<List<CommnunitiesResponseModel>> response) {
                try {
                    progressDialog.dismiss();
                    mTextViewAll.setVisibility(View.VISIBLE);
                    final List<CommnunitiesResponseModel> eventResponseModels = response.body();

                    mCommunityListSortingListAdapter = new CommunityListSortingListAdapter(CommunityActivity.this, eventResponseModels);
                    mRecyclerViewCommunityList.setAdapter(mCommunityListSortingListAdapter);

                    ItemClickSupport.addTo(mRecyclerViewCommunityList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            String CommunityName = eventResponseModels.get(position).getName();
                            int CommunityId = eventResponseModels.get(position).getId();
                            MyApplication.editor.putString("CommunityName", CommunityName);
                            MyApplication.editor.putInt("CommunityId", CommunityId);
                            MyApplication.editor.commit();
                            finish();


                        }
                    });
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CommnunitiesResponseModel>> call, Throwable t) {
                Log.d("onFailure", t.toString());
                Toast.makeText(CommunityActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_all:
                MyApplication.editor.putInt("CommunityId", 0);
                MyApplication.editor.putString("CommunityName", null);
                MyApplication.editor.commit();
                finish();
                break;
            case R.id.layout_close:
                finish();
                break;

        }
    }
}
