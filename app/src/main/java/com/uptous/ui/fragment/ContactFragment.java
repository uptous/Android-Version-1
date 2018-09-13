package com.uptous.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.model.ContactListResponseModel;
import com.uptous.ui.adapter.ContactListAdapter;
import com.uptous.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * FileName : ContactFragment
 * Description :
 * Dependencies :
 */
public class ContactFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private ContactListAdapter mContactListAdapter;
    private RecyclerView mViewContactRecyclerView;
    private List<ContactListResponseModel> mContactListResponseModels = new ArrayList<>();
    private List<ContactListResponseModel> mFilteredModelListResponseModels = new ArrayList<>();
    private TextView mTextViewCancel, mTextViewSearch;
    private String mAuthenticationId, mAuthenticationPassword, mCommunityName;

    public static SearchView SearchView;

    protected SignUpFragment signUpFragment;
    protected LibraryFragment libraryFragment;
    protected EventsFragment eventsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initView(view);


//        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
//            getApiContactList();
//        } else {
//            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
//        }

        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        return view;
    }

    private void initView(View view) {
        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        SearchView.setOnQueryTextListener(this);
        mTextViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
        mTextViewCancel.setOnClickListener(this);
        mTextViewSearch = (TextView) view.findViewById(R.id.text_view_search);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewContactRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contact);
        mViewContactRecyclerView.setLayoutManager(layoutManager);
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
        handleSearchView();

    }

    private void handleSearchView() {
        try {
            if (signUpFragment != null) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (eventsFragment != null) {
                EventsFragment.SearchView.onActionViewCollapsed();
                EventsFragment.SearchView.clearFocus();
            } else {
                EventsFragment.SearchView.onActionViewCollapsed();
                EventsFragment.SearchView.clearFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void getApiContactList() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<ContactListResponseModel>> call = service.GetContactList();

        call.enqueue(new Callback<List<ContactListResponseModel>>() {
            @Override
            public void onResponse(Call<List<ContactListResponseModel>> call, Response<List<ContactListResponseModel>> response) {
                try {
                    progressDialog.dismiss();
                    mContactListResponseModels = response.body();

                    mContactListAdapter = new ContactListAdapter(getActivity(), mContactListResponseModels);
                    mViewContactRecyclerView.setAdapter(mContactListAdapter);
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<ContactListResponseModel>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mFilteredModelListResponseModels = filter(mContactListResponseModels, newText);
        return true;
    }

    private List<ContactListResponseModel> filter(List<ContactListResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<ContactListResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (ContactListResponseModel model : models) {
                final String text = model.getFirstName().toLowerCase() + "" + model.getLastName().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
                mViewContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mContactListAdapter = new ContactListAdapter(getActivity(), filteredModelList);
                mViewContactRecyclerView.setAdapter(mContactListAdapter);
                mContactListAdapter.notifyDataSetChanged();
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
//        mCommunityName = MyApplication.mSharedPreferences.getString("mCommunityName", null);
//        if (mCommunityName != null) {
//            MainActivity.mTextViewTitle.setText(mCommunityName);
//            mFilteredModelListResponseModels = filter(mContactListResponseModels, mCommunityName);
//        } else {
//
//        }
    }
}