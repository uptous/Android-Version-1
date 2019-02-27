package com.uptous.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.uptous.model.EventResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.BaseActivity;
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
    private static View recycler_view_empty;

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
            getApiEventList();
            getApiCommunityList();


        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }


    // Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(getActivity());
        mAuthenticationPassword = Prefs.getAuthenticationPassword(getActivity());
    }

    //Method to initialize view
    private void initView(View view) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewEventsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
        mViewEventsRecyclerView.setLayoutManager(layoutManager);
        mTextViewSearchResult = (TextView) view.findViewById(R.id.search_result);
        recycler_view_empty = view.findViewById(R.id.recycler_view_empty);
        ((TextView)view.findViewById(R.id.text_title1)).setText("What to do, what to do...");
        ((TextView)view.findViewById(R.id.text_title2)).setText("No events have been added to the calendar for the selected community.");
        ((TextView)view.findViewById(R.id.text_contain)).setText("When events are added to the calendar you’ll be able to find them here. Looking to add an event yourself? For now, you’ll have to use the UpToUs website to add a new event.");
        getData();

    }
    public static void checkEmptyEvent2() {
        if (recycler_view_empty != null && mViewEventsRecyclerView != null) {
            if (eventList.size() == 0) {
                recycler_view_empty.setVisibility(View.VISIBLE);
                mViewEventsRecyclerView.setVisibility(View.GONE);
            } else {
                recycler_view_empty.setVisibility(View.GONE);
                mViewEventsRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
    // Get webservice to show all EventList
    private void getApiEventList() {
        ((MainActivity)getActivity()).showProgressDialog();
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<EventResponseModel>> call = service.GetEvent();

        call.enqueue(new Callback<List<EventResponseModel>>() {
            @Override
            public void onResponse(Call<List<EventResponseModel>> call, Response<List<EventResponseModel>> response) {
                ((MainActivity)getActivity()).hideProgressDialog();
                try {


                    if (response.body() != null) {
                        mTextViewSearchResult.setVisibility(View.GONE);
                        eventList = response.body();
                        Log.i("EventsFragment","val "+eventList.toString());
                        mEventListAdapter = new EventListAdapter(getActivity(), eventList, mCommunityList);
                        mViewEventsRecyclerView.setAdapter(mEventListAdapter);
                        int communityId = Prefs.getCommunityId(getActivity());
                        if (communityId != 0) {
                            FilterCommunityForSignUp(eventList, communityId);
                        }
                        checkEmptyEvent2();
                    } else {
//                        final CustomizeDialog customizeDialog = new CustomizeDialog(getActivity());
//                        customizeDialog.setCancelable(false);
//                        customizeDialog.setContentView(R.layout.dialog_password_change);
//                        TextView textViewOk = (TextView) customizeDialog.findViewById(R.id.text_view_log_out);
//                        customizeDialog.show();
//                        textViewOk.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                customizeDialog.dismiss();
//                                logout();
//                            }
//                        });
                        BaseActivity baseActivity = new BaseActivity();
                        baseActivity.logOut();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<EventResponseModel>> call, Throwable t) {
                ((MainActivity)getActivity()).hideProgressDialog();
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
                checkEmptyEvent2();
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

            int Position = Prefs.getPosition(getActivity());

            MainActivity activity = (MainActivity) getActivity();
            if (Position == 4) {
                if (eventList.size() == 0) {
                    //checkEmptyEvent();
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                    Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }
            //checkEmptyEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eventList;
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
                try {
                    ((MainActivity)getActivity()).hideProgressDialog();

                    if (response.body() != null) {
                        mCommunityList = response.body();
                        //checkEmptyEvent();
                        Log.i("EventsFragment","val "+mCommunityList.toString());
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

}


