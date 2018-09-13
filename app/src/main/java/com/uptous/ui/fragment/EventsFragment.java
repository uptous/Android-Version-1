package com.uptous.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.uptous.model.Event;
import com.uptous.ui.adapter.EventListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * FileName : EventsFragment
 * Description :
 * Dependencies :
 */
public class EventsFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {


    private RecyclerView mViewEventsRecyclerView;
    private List<Event> mEventList = new ArrayList<>();
    private EventListAdapter mEventListAdapter;
    private TextView mTextViewCancel, mTextViewSearch;
    private String mAuthenticationId, mAuthenticationPassword;

    public static SearchView SearchView;
    protected SignUpFragment signUpFragment;
    protected LibraryFragment libraryFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);


        initView(view);
//
//        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
//            getApiEventList();
//        } else {
//            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
//        }
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);

        return view;
    }

    private void initView(View view) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewEventsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
        mViewEventsRecyclerView.setLayoutManager(layoutManager);
        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        mTextViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
        mTextViewSearch = (TextView) view.findViewById(R.id.text_view_search);
        SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewSearch.setVisibility(View.GONE);
            }
        });

        SearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mTextViewSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });
        clickListenerOnViews();

        handleSearchView();
    }

    private void clickListenerOnViews() {
        SearchView.setOnQueryTextListener(this);
        mTextViewCancel.setOnClickListener(this);
    }

    private void handleSearchView() {
        try {
            if (signUpFragment != null) {
                SignUpFragment.SearchView.clearFocus();
                SignUpFragment.SearchView.onActionViewCollapsed();
            } else {
                SignUpFragment.SearchView.clearFocus();
                SignUpFragment.SearchView.onActionViewCollapsed();
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
    }

    private void getApiEventList() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<Event>> call = service.GetEvent();

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                mProgressDialog.dismiss();
                try {

                    mEventList = response.body();


                    mEventListAdapter = new EventListAdapter(getActivity(), mEventList);
                    mViewEventsRecyclerView.setAdapter(mEventListAdapter);


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Event> filteredModelList = filter(mEventList, newText);
        return true;
    }

    private List<Event> filter(List<Event> models, String query) {
        query = query.toLowerCase();

        final List<Event> filteredModelList = new ArrayList<>();
        try {
            for (Event model : models) {
                final String text = model.getTitle().toLowerCase() + "" + model.getDescription().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
                mViewEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mEventListAdapter = new EventListAdapter(getActivity(), filteredModelList);
                mViewEventsRecyclerView.setAdapter(mEventListAdapter);
                mEventListAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredModelList;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_cancel:
                SearchView.clearFocus();
                SearchView.onActionViewCollapsed();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.editor.putString("Files", "file");
        MyApplication.editor.commit();
    }
}


