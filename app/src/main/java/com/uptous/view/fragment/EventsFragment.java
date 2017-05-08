package com.uptous.view.fragment;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.EventResponseModel;
import com.uptous.view.activity.LogInActivity;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.adapter.EventListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * FileName : EventsFragment
 * Description :Show all Events
 * Dependencies :EventListAdapter
 */
public class EventsFragment extends Fragment {


    public static RecyclerView mViewEventsRecyclerView;
    public static List<EventResponseModel> eventList = new ArrayList<>();
    private List<CommnunitiesResponseModel> mCommunityList = new ArrayList<>();
    private EventListAdapter mEventListAdapter;
    private String mAuthenticationId, mAuthenticationPassword;
    private TextView mTextViewSearchResult;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        initView(view);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();


        if (ConnectionDetector.isConnectingToInternet(getActivity())) {

            getApiCommunityList();

                getApiEventList();


        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }


    // Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    //Method to initialize view
    private void initView(View view) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewEventsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
        mViewEventsRecyclerView.setLayoutManager(layoutManager);
        mTextViewSearchResult = (TextView) view.findViewById(R.id.search_result);

        getData();

    }

    // Get webservice to show all EventList
    private void getApiEventList() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<EventResponseModel>> call = service.GetEvent();

        call.enqueue(new Callback<List<EventResponseModel>>() {
            @Override
            public void onResponse(Call<List<EventResponseModel>> call, Response<List<EventResponseModel>> response) {
                mProgressDialog.dismiss();
                try {


                    if (response.body() != null) {
                        mTextViewSearchResult.setVisibility(View.GONE);
                        eventList = response.body();
                        mEventListAdapter = new EventListAdapter(getActivity(), eventList, mCommunityList);
                        mViewEventsRecyclerView.setAdapter(mEventListAdapter);
                        int communityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);
                        if (communityId != 0) {
                            FilterCommunityForSignUp(eventList, communityId);
                        }
                    } else {
                        final CustomizeDialog customizeDialog = new CustomizeDialog(getActivity());
                        customizeDialog.setCancelable(false);
                        customizeDialog.setContentView(R.layout.dialog_password_change);
                        TextView textViewOk = (TextView) customizeDialog.findViewById(R.id.text_view_log_out);
                        customizeDialog.show();
                        textViewOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customizeDialog.dismiss();
                                logout();
                            }
                        });

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<EventResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Method to filter event by search string
    public List<EventResponseModel> SearchFilterForEvent(List<EventResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<EventResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (EventResponseModel model : models) {
                String Title = model.getTitle();
                String Description = model.getDescription();
                String text = "";
                if (Title != null)
                    text = text + Title.toLowerCase();
                if (Description != null)
                    text = text + Description.toLowerCase();


                if (text != null) {
                    if (text.contains(query)) {
                        filteredModelList.add(model);

                    }
                }

                mViewEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mEventListAdapter = new EventListAdapter(getActivity(), filteredModelList, mCommunityList);
                mViewEventsRecyclerView.setAdapter(mEventListAdapter);
                mEventListAdapter.notifyDataSetChanged();
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

    // Method to filter feed by community
    private List<EventResponseModel> FilterCommunityForSignUp(List<EventResponseModel> models, int Id) {
        eventList = new ArrayList<>();
        try {
            for (EventResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    eventList.add(model);
                }
                mViewEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mEventListAdapter = new EventListAdapter(getActivity(), eventList, mCommunityList);
                mViewEventsRecyclerView.setAdapter(mEventListAdapter);
                mEventListAdapter.notifyDataSetChanged();
            }

            int Position = MyApplication.mSharedPreferences.getInt("Position", 0);

            MainActivity activity = (MainActivity) getActivity();
            if (Position == 4) {
                if (eventList.size() == 0) {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventList;
    }

    // Get webservice to show communities
    private void getApiCommunityList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<CommnunitiesResponseModel>> call = service.GetCommunity();

        call.enqueue(new Callback<List<CommnunitiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommnunitiesResponseModel>> call, Response<List<CommnunitiesResponseModel>> response) {
                try {
                    progressDialog.dismiss();

                    if (response.body() != null) {
                        mCommunityList = response.body();
                    } else {
                        final CustomizeDialog customizeDialog = new CustomizeDialog(getActivity());
                        customizeDialog.setCancelable(false);
                        customizeDialog.setContentView(R.layout.dialog_password_change);
                        TextView textViewOk = (TextView) customizeDialog.findViewById(R.id.text_view_log_out);
                        customizeDialog.show();
                        textViewOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customizeDialog.dismiss();
                                logout();
                            }
                        });

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CommnunitiesResponseModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();

            }

        });
    }

    //Method to logout from app
    private void logout() {
        MainActivity activity = new MainActivity();
        activity.logOut();
        Application app = getActivity().getApplication();
        Intent intent = new Intent(app, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);
    }
}


