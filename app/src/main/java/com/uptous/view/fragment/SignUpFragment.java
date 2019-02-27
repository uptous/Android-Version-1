package com.uptous.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.SignUpResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.BaseActivity;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.adapter.SignUpSheetsListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : SignUpFragment
 * Description : Show all sing_up types and also show open ,full, volunteer spots etc.
 * Dependencies : SignUpSheetsListAdapter
 */

public class SignUpFragment extends Fragment {

    private SignUpSheetsListAdapter mSignUpSheetsListAdapter;

    public static RecyclerView mViewSignUpRecyclerView;

    private String mAuthenticationId, mAuthenticationPassword;

    private TextView mTextViewSearchResult;
    private static View recycler_view_empty;
    public static List<SignUpResponseModel> signUpResponseModelList = new ArrayList<>();

    private List<CommnunitiesResponseModel> mCommunityList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initView(view);

        getData();
        getApiSignUp();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        String Detail = Prefs.getSignUpDetail(getActivity());


        if (Detail == null) {
            if (ConnectionDetector.isConnectingToInternet(getActivity())) {

                getApiCommunityList();

                getApiSignUp();
            } else {
                Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
            }

        }


    }

    public static void checkEmptySignUp() {
        if (recycler_view_empty != null && mViewSignUpRecyclerView != null) {
            if (signUpResponseModelList.size() == 0) {
                recycler_view_empty.setVisibility(View.VISIBLE);
                mViewSignUpRecyclerView.setVisibility(View.GONE);
            } else {
                recycler_view_empty.setVisibility(View.GONE);
                mViewSignUpRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }


    //Method to initialize view
    private void initView(View view) {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewSignUpRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_sign_up);
        mViewSignUpRecyclerView.setLayoutManager(layoutManager);
        mTextViewSearchResult = (TextView) view.findViewById(R.id.search_result);
        recycler_view_empty = view.findViewById(R.id.recycler_view_empty);
        ((TextView)view.findViewById(R.id.text_title1)).setText("Just trying to help...");
        ((TextView)view.findViewById(R.id.text_title2)).setText("No sign-ups have been posted to the selected community.");
        ((TextView)view.findViewById(R.id.text_contain)).setText("When people post sign-ups you’ll be able to find them here. Looking to organize volunteers yourself? For now, you’ll have to use the UpToUs website to create a new sign-up.");

    }


    // Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(getActivity());
        mAuthenticationPassword = Prefs.getAuthenticationPassword(getActivity());
    }

    // Get webservice to show all sign_up_types
    private void getApiSignUp() {
        ((MainActivity)getActivity()).showProgressDialog();
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<SignUpResponseModel>> call = service.GetSignUp();

        call.enqueue(new Callback<List<SignUpResponseModel>>() {
            @Override
            public void onResponse(Call<List<SignUpResponseModel>> call, Response<List<SignUpResponseModel>> response) {
                ((MainActivity)getActivity()).hideProgressDialog();
                try {

                    if (response.body() != null) {
                        mViewSignUpRecyclerView.setVisibility(View.VISIBLE);
                        mTextViewSearchResult.setVisibility(View.GONE);
                        signUpResponseModelList = response.body();
                        mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), signUpResponseModelList, mCommunityList);
                        mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
                        int communityId = Prefs.getCommunityId(getActivity());
                        if (communityId != 0) {
                            FilterCommunityForSignUp(signUpResponseModelList, communityId);
                        }
                    } else {
                        BaseActivity baseActivity = (BaseActivity)getActivity();
                        baseActivity.showLogOutDialog();

                    }
                    SignUpFragment.checkEmptySignUp();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpResponseModel>> call, Throwable t) {
                ((MainActivity)getActivity()).hideProgressDialog();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }


    // Method to filter sign_up by search string
    public List<SignUpResponseModel> SearchFilterForSignUp(List<SignUpResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<SignUpResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (SignUpResponseModel model : models) {

                String Name = model.getName();
                String text = "";
                if (Name != null)
                    text = text + Name.toLowerCase();


                if (text != null) {
                    if (text.contains(query)) {
                        filteredModelList.add(model);

                    }
                }

                mViewSignUpRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), filteredModelList, mCommunityList);
                mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
                mSignUpSheetsListAdapter.notifyDataSetChanged();
                if (query.equalsIgnoreCase("")) {
                    mTextViewSearchResult.setVisibility(View.GONE);
                } else {
                    mTextViewSearchResult.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredModelList;
    }

    // Get webservice to show communities
    private void getApiCommunityList() {
        ((MainActivity)getActivity()).showProgressDialog();


        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<CommnunitiesResponseModel>> call = service.GetCommunity();

        call.enqueue(new Callback<List<CommnunitiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommnunitiesResponseModel>> call, Response<List<CommnunitiesResponseModel>> response) {
                ((MainActivity)getActivity()).hideProgressDialog();
                try {

                    if (response.body() != null) {
                        mCommunityList = response.body();
                    } else {


                        BaseActivity baseActivity = (BaseActivity)getActivity();
                        baseActivity.showLogOutDialog();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CommnunitiesResponseModel>> call, Throwable t) {
                ((MainActivity)getActivity()).hideProgressDialog();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();

            }

        });
    }

    // Method to filter feed by community
    private List<SignUpResponseModel> FilterCommunityForSignUp(List<SignUpResponseModel> models, int Id) {
        signUpResponseModelList = new ArrayList<>();
        try {
            for (SignUpResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    signUpResponseModelList.add(model);
                }
                mViewSignUpRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), signUpResponseModelList, mCommunityList);
                mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
                mSignUpSheetsListAdapter.notifyDataSetChanged();

            }
            int Position = Prefs.getPosition(getActivity());

            MainActivity activity = (MainActivity) getActivity();
            if (Position == 2) {
                if (signUpResponseModelList.size() == 0) {
                    checkEmptySignUp();
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                    Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return signUpResponseModelList;
    }

}
