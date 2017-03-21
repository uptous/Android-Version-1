package com.uptous.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.ContactListResponseModel;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.adapter.ContactListAdapter;
import com.uptous.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * FileName : ContactFragment
 * Description :Show all UpToUs member
 * Dependencies :ContactListAdapter
 */
public class ContactFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private ContactListAdapter mContactListAdapter;

    private RecyclerView mViewContactRecyclerView;

    private List<ContactListResponseModel> mContactListResponseModels = new ArrayList<>();

    private TextView mTextViewSearch;

    private String mAuthenticationId, mAuthenticationPassword;

    public SearchView SearchView;

    protected SignUpFragment signUpFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initView(view);

        getData();

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            getApiContactList();
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_cancel:
                SearchView.clearFocus();
                SearchView.onActionViewCollapsed();
                mTextViewSearch.setVisibility(View.VISIBLE);
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.editor.putString("Files", "file");
        MyApplication.editor.commit();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.equalsIgnoreCase("")) {
            mTextViewSearch.setVisibility(View.GONE);
        } else {
            mTextViewSearch.setVisibility(View.VISIBLE);
        }
        filter(mContactListResponseModels, newText);
        return true;
    }

    //Method to initialize view
    private void initView(View view) {

        signUpFragment = new SignUpFragment();
        //Local Variables
        RelativeLayout relativeLayoutSearchBar = (RelativeLayout) view.findViewById(R.id.layout_searchbar);
        TextView textViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
        textViewCancel.setOnClickListener(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        //Global Variables
        mViewContactRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contact);
        mViewContactRecyclerView.setLayoutManager(layoutManager);

        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        SearchView.setOnQueryTextListener(this);

        mTextViewSearch = (TextView) view.findViewById(R.id.text_view_search);


        relativeLayoutSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchView.onActionViewExpanded();
                mTextViewSearch.setVisibility(View.GONE);

            }
        });
        SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchView.onActionViewExpanded();
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

    // Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);

    }

    // Method to handle search view
    private void handleSearchView() {
        try {
            MainActivity activity = ((MainActivity) getActivity());
            signUpFragment = (SignUpFragment) activity.mViewPagerAdapter.getItem(2);
            if (signUpFragment != null) {
                signUpFragment.SearchView.clearFocus();
                signUpFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Get webservice to show all UpToUs member
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

    // Method to filter member by search string
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

}