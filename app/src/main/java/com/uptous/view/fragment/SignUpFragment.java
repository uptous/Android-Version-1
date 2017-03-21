package com.uptous.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.SignUpResponseModel;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.activity.SignUpOngoingActivity;
import com.uptous.view.activity.SignUpShiftsActivity;
import com.uptous.view.activity.SignUpDRIVERActivity;
import com.uptous.view.activity.SignUpRSPVActivity;
import com.uptous.view.activity.SignUpSnackActivity;
import com.uptous.view.activity.SignUpVoteActivity;
import com.uptous.view.adapter.SignUpSheetsListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : SignUpFragment
 * Description : Show all sing_up types and also show open ,full, volunteer spots etc.
 * Dependencies : SignUpSheetsListAdapter, InternetConnection
 */

public class SignUpFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private SignUpSheetsListAdapter mSignUpSheetsListAdapter;

    private RecyclerView mViewSignUpRecyclerView;

    private TextView mTextViewSearch;


    private String mAuthenticationId, mAuthenticationPassword;

    private List<SignUpResponseModel> mSignUpResponseModelList = new ArrayList<>();

    private List<CommnunitiesResponseModel> CommunityList = new ArrayList<>();

    private List<SignUpResponseModel> mFilteredModelList = new ArrayList<>();


    public  SearchView SearchView;

    protected ContactFragment contactFragment;

    protected HomeFragment homeFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initView(view);

        getData();

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            getApiCommunityList();
            getApiSignUp();
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

        return view;
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
        mFilteredModelList = filter(mSignUpResponseModelList, newText);


        return true;
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
        SearchView.clearFocus();
        SearchView.onActionViewCollapsed();
        MyApplication.editor.putString("Files", "file");
        MyApplication.editor.commit();


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

    //Method to initialize view
    private void initView(View view) {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewSignUpRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_sign_up);
        mViewSignUpRecyclerView.setLayoutManager(layoutManager);
        TextView mTextViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
        mTextViewCancel.setOnClickListener(this);
        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        SearchView.setOnQueryTextListener(this);
        mTextViewSearch = (TextView) view.findViewById(R.id.text_view_search);
        RelativeLayout mRelativeLayoutSearchBar = (RelativeLayout) view.findViewById(R.id.layout_searchbar);

        mRelativeLayoutSearchBar.setOnClickListener(new View.OnClickListener() {
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
        mAuthenticationId = MyApplication.mSharedPreferences.getString(String.valueOf(R.string.AuthenticationId), null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString(String.valueOf(R.string.AuthenticationPassword), null);
    }

    // Method to handle search view
    private void handleSearchView() {
        try {
            MainActivity activity = ((MainActivity) getActivity());
            contactFragment = (ContactFragment) activity.mViewPagerAdapter.getItem(1);
            if (contactFragment != null) {
                contactFragment.SearchView.clearFocus();
                contactFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            MainActivity activity = ((MainActivity) getActivity());
            homeFragment = (HomeFragment) activity.mViewPagerAdapter.getItem(0);

            if (homeFragment != null) {
                homeFragment.SearchView.clearFocus();
                homeFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Get webservice to show all sign_up_types
    private void getApiSignUp() {

        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
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
                    mSignUpResponseModelList = response.body();
                    mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), mSignUpResponseModelList, CommunityList);
                    mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);

                    ItemClickSupport.addTo(mViewSignUpRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            if (mFilteredModelList.size() == 0) {
                                if (mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("RSVP")) {
                                    int OpId = mSignUpResponseModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpRSPVActivity.class);
                                    startActivity(intent);
                                } else if (mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Vote")) {
                                    int OpId = mSignUpResponseModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpVoteActivity.class);
                                    startActivity(intent);
                                } else if (mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Drivers")) {
                                    int OpId = mSignUpResponseModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpDRIVERActivity.class);
                                    startActivity(intent);
                                } else if (mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Shifts") ||
                                        mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Games") ||
                                        mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Potluck/Party") ||
                                        mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Wish List") ||
                                        mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Volunteer") ||
                                        mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Multi Game/Event RSVP")) {
                                    int OpId = mSignUpResponseModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.putString("Type", mSignUpResponseModelList.get(position).getType());
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpShiftsActivity.class);
                                    startActivity(intent);
                                } else if (mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Snack Schedule")) {
                                    int OpId = mSignUpResponseModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.putString("Type", mSignUpResponseModelList.get(position).getType());
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpSnackActivity.class);
                                    startActivity(intent);
                                } else if (mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Ongoing Volunteering")) {
                                    int OpId = mSignUpResponseModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.putString("Type", mSignUpResponseModelList.get(position).getType());
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpOngoingActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), R.string.sing_up_not_allow, Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                if (mFilteredModelList.get(position).getType().equalsIgnoreCase("RSVP")) {
                                    int OpId = mFilteredModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpRSPVActivity.class);
                                    startActivity(intent);
                                } else if (mFilteredModelList.get(position).getType().equalsIgnoreCase("Vote")) {
                                    int OpId = mFilteredModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpVoteActivity.class);
                                    startActivity(intent);
                                } else if (mFilteredModelList.get(position).getType().equalsIgnoreCase("Drivers")) {
                                    int OpId = mFilteredModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpDRIVERActivity.class);
                                    startActivity(intent);
                                } else if (mFilteredModelList.get(position).getType().equalsIgnoreCase("Shifts") ||
                                        mFilteredModelList.get(position).getType().equalsIgnoreCase("Games")
                                        || mFilteredModelList.get(position).getType().equalsIgnoreCase("Potluck/Party") ||
                                        mFilteredModelList.get(position).getType().equalsIgnoreCase("Wish List")
                                        || mFilteredModelList.get(position).getType().equalsIgnoreCase("Volunteer")
                                        || mFilteredModelList.get(position).getType().equalsIgnoreCase("Multi Game/Event RSVP")) {
                                    int OpId = mFilteredModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.putString("Type", mSignUpResponseModelList.get(position).getType());
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpShiftsActivity.class);
                                    startActivity(intent);
                                } else if (mFilteredModelList.get(position).getType().equalsIgnoreCase("Snack Schedule")) {
                                    int OpId = mFilteredModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.putString("Type", mSignUpResponseModelList.get(position).getType());
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpSnackActivity.class);
                                    startActivity(intent);
                                } else if (mFilteredModelList.get(position).getType().equalsIgnoreCase("Ongoing Volunteering")) {
                                    int OpId = mFilteredModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.putString("Type", mSignUpResponseModelList.get(position).getType());
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpOngoingActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), R.string.sing_up_not_allow, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Method to filter sign_up by search string
    private List<SignUpResponseModel> filter(List<SignUpResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<SignUpResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (SignUpResponseModel model : models) {
                final String text = model.getName().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
                mViewSignUpRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), filteredModelList, CommunityList);
                mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
                mSignUpSheetsListAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredModelList;
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
                    CommunityList = response.body();


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


}
