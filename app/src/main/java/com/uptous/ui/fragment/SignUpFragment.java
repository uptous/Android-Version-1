package com.uptous.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.uptous.ui.activity.MainActivity;
import com.uptous.ui.activity.MessagePostActivity;
import com.uptous.ui.activity.SignUpShiftsActivity;
import com.uptous.ui.activity.SignUpDRIVERActivity;
import com.uptous.ui.activity.SignUpRSPVActivity;
import com.uptous.ui.adapter.SignUpSheetsListAdapter;
import com.uptous.ui.adapter.TypeListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 12/29/2016.
 */

public class SignUpFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initView(view);

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            getApiCommunityList();

            getApiSignUp();
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        return view;
    }

    private void initView(View view) {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewSignUpRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_sign_up);
        mViewSignUpRecyclerView.setLayoutManager(layoutManager);
        mTextViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
        mTextViewCancel.setOnClickListener(this);
        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        SearchView.setOnQueryTextListener(this);
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
        handleSearchView();
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


                    mCommunityName = MyApplication.mSharedPreferences.getString("CommunityName", null);
                    mCommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);

                    if (mCommunityId != 0) {

                        mSignUpResponseModelList = response.body();
                        mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), mSignUpResponseModelList, CommunityList);
                        mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
                        MainActivity.mTextViewTitle.setText(mCommunityName);
                        MyApplication.editor.putString("CommunityFilter", "communityfilter");
                        MyApplication.editor.commit();
//                        Filter = FilterCommunity(mSignUpResponseModelList, CommunityId);
                    } else {
                        if (mCommunityId == 0) {
                            MyApplication.editor.putString("CommunityFilter", null);
                            MyApplication.editor.commit();
                            MainActivity.mTextViewTitle.setText("All Communities");
                            mSignUpResponseModelList = response.body();
                            mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), mSignUpResponseModelList, CommunityList);
                            mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
                        }

                    }


                    ItemClickSupport.addTo(mViewSignUpRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {


//
                            if (mFilteredModelList.size() == 0) {

                                if (mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("RSVP") ||
                                        mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Vote")
                                        ) {
                                    int OpId = mSignUpResponseModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpRSPVActivity.class);
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
                                        mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Snack Schedule") ||
                                        mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Ongoing Volunteering")
                                        || mSignUpResponseModelList.get(position).getType().equalsIgnoreCase("Multi Game/Event RSVP")
                                        ) {
                                    int OpId = mSignUpResponseModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.putString("Type", mSignUpResponseModelList.get(position).getType());
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpShiftsActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "We're sorry but for now, this type of sign-up is not acessible with the app", Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                if (mFilteredModelList.get(position).getType().equalsIgnoreCase("RSVP") ||
                                        mFilteredModelList.get(position).getType().equalsIgnoreCase("Vote")
                                        ) {
                                    int OpId = mFilteredModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpRSPVActivity.class);
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
                                        || mFilteredModelList.get(position).getType().equalsIgnoreCase("Snack Schedule") ||
                                        mFilteredModelList.get(position).getType().equalsIgnoreCase("Ongoing Volunteering")
                                        || mFilteredModelList.get(position).getType().equalsIgnoreCase("Multi Game/Event RSVP")) {
                                    int OpId = mFilteredModelList.get(position).getId();
                                    MyApplication.editor.putInt("Id", OpId);
                                    MyApplication.editor.putString("Type", mSignUpResponseModelList.get(position).getType());
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), SignUpShiftsActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "We're sorry but for now, this type of sign-up is not acessible with the app", Toast.LENGTH_SHORT).show();
                                }


                            }

                        }
                    });


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
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

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

//        String CommunityFilter = MyApplication.mSharedPreferences.getString("CommunityFilter", null);
//        if (CommunityFilter != null) {
//            filteredModelList = filter(Filter, newText);
//        } else {
        mFilteredModelList = filter(mSignUpResponseModelList, newText);
//        }


        return true;
    }

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
        SearchView.clearFocus();
        SearchView.onActionViewCollapsed();
        MyApplication.editor.putString("Files", "file");
        MyApplication.editor.commit();
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

    private List<SignUpResponseModel> FilterCommunity(List<SignUpResponseModel> models, int Id) {
        mFilter = new ArrayList<>();
        try {
            for (SignUpResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    mFilter.add(model);
                }
                mViewSignUpRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mSignUpSheetsListAdapter = new SignUpSheetsListAdapter(getActivity(), mFilter, CommunityList);
                mViewSignUpRecyclerView.setAdapter(mSignUpSheetsListAdapter);
                mSignUpSheetsListAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFilter;
    }

    private void getApiCommunityList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }
}
