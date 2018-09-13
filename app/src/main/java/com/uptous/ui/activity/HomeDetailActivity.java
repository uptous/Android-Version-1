package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.SignUpResponseModel;
import com.uptous.ui.adapter.SignUpSheetsListAdapter;
import com.uptous.ui.fragment.ContactFragment;
import com.uptous.ui.fragment.EventsFragment;
import com.uptous.ui.fragment.HomeFragment;
import com.uptous.ui.fragment.LibraryFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 12/29/2016.
 */

public class HomeDetailActivity extends AppCompatActivity {

    private SignUpSheetsListAdapter mSignUpSheetsListAdapter;
    private RecyclerView mViewSignUpRecyclerView;
    private TextView mTextViewCancel, mTextViewSearch;
    private int mCommunityId;
    private String mCommunityName, mAuthenticationId, mAuthenticationPassword;
    private List<SignUpResponseModel> mSignUpResponseModelList = new ArrayList<>();
    private List<CommnunitiesResponseModel> CommunityList = new ArrayList<>();
    private List<SignUpResponseModel> mFilteredModelList = new ArrayList<>();
    private List<SignUpResponseModel> mFilter = new ArrayList<>();

    public static SearchView SearchView;

    protected ContactFragment contactFragment;
    protected HomeFragment homeFragment;
    protected LibraryFragment libraryFragment;
    protected EventsFragment eventsFragment;
    private int CommunityID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        if (ConnectionDetector.isConnectingToInternet(HomeDetailActivity.this)) {
//            getApiCommunityList();

            getApiSignUp();
        } else {
            Toast.makeText(HomeDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        CommunityID = MyApplication.mSharedPreferences.getInt("CommunityID", 0);
    }

    private void initView() {

//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        mViewSignUpRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_sign_up);
//        mViewSignUpRecyclerView.setLayoutManager(layoutManager);
//        mTextViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
//        mTextViewCancel.setOnClickListener(this);
//        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
//        SearchView.setOnQueryTextListener(this);
//        mTextViewSearch = (TextView) view.findViewById(R.id.text_view_search);
//        SearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mTextViewSearch.setVisibility(View.GONE);
//            }
//        });
//
//        SearchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                mTextViewSearch.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });
//        handleSearchView();
    }

    private void handleSearchView() {
        try {
            if (contactFragment != null) {
                ContactFragment.SearchView.clearFocus();
                ContactFragment.SearchView.onActionViewCollapsed();
            } else {
                ContactFragment.SearchView.clearFocus();
                ContactFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (libraryFragment != null) {
                LibraryFragment.SearchView.clearFocus();
                LibraryFragment.SearchView.onActionViewCollapsed();
            } else {
                LibraryFragment.SearchView.clearFocus();
                LibraryFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (eventsFragment != null) {
                EventsFragment.SearchView.clearFocus();
                EventsFragment.SearchView.onActionViewCollapsed();
            } else {
                EventsFragment.SearchView.clearFocus();
                EventsFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (homeFragment != null) {
                HomeFragment.SearchView.clearFocus();
                HomeFragment.SearchView.onActionViewCollapsed();
            } else {
                HomeFragment.SearchView.clearFocus();
                HomeFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getApiSignUp() {

        final ProgressDialog mProgressDialog = new ProgressDialog(HomeDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<SignUpResponseModel>> call = service.GetSignUp();

        call.enqueue(new Callback<List<SignUpResponseModel>>() {
            @Override
            public void onResponse(Call<List<SignUpResponseModel>> call, Response<List<SignUpResponseModel>> response) {
                mProgressDialog.dismiss();
                try {


//                    mCommunityName = MyApplication.mSharedPreferences.getString("CommunityName", null);
//                    mCommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);

//                    if (mCommunityId != 0) {
                    finish();
                    mSignUpResponseModelList = response.body();
//                        mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), mSignUpResponseModelList ,CommunityList);
//                        mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
//                        MainActivity.mTextViewTitle.setText(mCommunityName);
//                        MyApplication.editor.putString("CommunityFilter", "communityfilter");
//                        MyApplication.editor.commit();
////                        Filter = FilterCommunity(mSignUpResponseModelList, CommunityId);
//                    } else {
//                        if (mCommunityId == 0) {
//                            MyApplication.editor.putString("CommunityFilter", null);
//                            MyApplication.editor.commit();
//                            MainActivity.mTextViewTitle.setText("All Communities");
//                            mSignUpResponseModelList = response.body();
//                            mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), mSignUpResponseModelList,CommunityList);
//                            mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
//                        }

//                    }


//                    ItemClickSupport.addTo(mViewSignUpRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//                        @Override
//                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//
//
////
//                            if (mFilteredModelList.size() == 0) {


                    for (int j = 0; mSignUpResponseModelList.size() > j; j++) {
                        int CommId = mSignUpResponseModelList.get(j).getId();

                        if (CommId == CommunityID) {
                            if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("RSVP") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Vote")) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                MyApplication.editor.putInt("Id", OpId);
                                MyApplication.editor.commit();
                                finish();
                                Intent intent = new Intent(HomeDetailActivity.this, SignUpRSPVActivity.class);
                                startActivity(intent);
                            } else if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Drivers")) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                MyApplication.editor.putInt("Id", OpId);
                                MyApplication.editor.commit();
                                finish();
                                Intent intent = new Intent(HomeDetailActivity.this, SignUpDRIVERActivity.class);
                                startActivity(intent);
                            } else if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Shifts") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Games") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Potluck/Party") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Wish List") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Volunteer")
                                    ) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                MyApplication.editor.putInt("Id", OpId);
                                MyApplication.editor.putString("Type", mSignUpResponseModelList.get(j).getType());
                                MyApplication.editor.commit();
                                finish();
                                Intent intent = new Intent(HomeDetailActivity.this, SignUpShiftsActivity.class);
                                startActivity(intent);
                            }
                        }
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(HomeDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();
//        SearchView.clearFocus();
//        SearchView.onActionViewCollapsed();
//        MyApplication.editor.putString("Files", "file");
//        MyApplication.editor.commit();
//        getApiSignUp();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.editor.putString("CommunityName", null);
        MyApplication.editor.putString("CommunityFilter", null);
        MyApplication.editor.putInt("CommunityId", 0);

        MyApplication.editor.putString("All", null);
        MyApplication.editor.commit();
    }


    private void getApiCommunityList() {
        final ProgressDialog progressDialog = new ProgressDialog(HomeDetailActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<CommnunitiesResponseModel>> call = service.GetCommunity();

        call.enqueue(new Callback<List<CommnunitiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommnunitiesResponseModel>> call, Response<List<CommnunitiesResponseModel>> response) {
                try {
                    progressDialog.dismiss();
                    CommunityList = response.body();


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CommnunitiesResponseModel>> call, Throwable t) {
                Log.d("onFailure", t.toString());
                Toast.makeText(HomeDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }
}
