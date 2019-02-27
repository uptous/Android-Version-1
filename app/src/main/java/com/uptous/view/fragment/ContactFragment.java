package com.uptous.view.fragment;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.ContactListResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.adapter.ContactListAdapter;
import com.uptous.R;
import com.uptous.view.adapter.ContactListForSearchAdapter;
import com.uptous.view.adapter.CustomComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.uptous.view.activity.MainActivity.LoadOnce;
import static com.uptous.view.activity.MainActivity.contactListResponseModels;
import static com.uptous.view.activity.MainActivity.mContactListResponseSearch;
import static com.uptous.view.activity.MainActivity.mIsFromSearch;
import static com.uptous.view.activity.MainActivity.mLimit;


/**
 * FileName : ContactFragment
 * Description :Show all UpToUs member
 * Dependencies :ContactListAdapter
 */
public class ContactFragment extends Fragment {
    private ContactListAdapter mContactListAdapter;
    public static RecyclerView mViewContactRecyclerView;
    // public static List<ContactListResponseModel> contactListResponseModels = new ArrayList<>();
    public static TextView mTextViewSearchResult;
    public static View recycler_view_empty1;
    private final int PROGRESS_DISPLAY_LENGTH = 10000;
    private   ProgressDialog progressDialog;

    private boolean onResumeStatus=false;
    // pagination element
//    private ProgressBar progressBar;
//    public static LinearLayoutManager layoutManager;
//    private boolean isScrolling = false;
//    private int currentItem, totalItem, scrolledOutItem;
//    private int pageN0 = 0;

    public static ProgressBar progressBar;
    public static LinearLayoutManager layoutManager;
    public static boolean isScrolling = false;
    public static int currentItem, totalItem, scrolledOutItem;
    public static int pageN0 = 1;
    public static ContactListForSearchAdapter mContactListForSearchAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initView(view);

        MainActivity activity = (MainActivity) getActivity();
        activity.mTextViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewContactRecyclerView.smoothScrollToPosition(0);
            }
        });

        return view;
    }




    //Method to initialize view
    private void initView(View view) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");

        //Global Variables Initialization
        mViewContactRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contact);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        mContactListAdapter = new ContactListAdapter(getActivity(), contactListResponseModels);

//        mContactListForSearchAdapter= new ContactListForSearchAdapter(getActivity(), mContactListResponseSearch);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewContactRecyclerView.setLayoutManager(layoutManager);
        mViewContactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mViewContactRecyclerView.setAdapter(mContactListForSearchAdapter);


        mTextViewSearchResult = (TextView) view.findViewById(R.id.search_result);
        recycler_view_empty1 = view.findViewById(R.id.recycler_view_empty);
        ((TextView) view.findViewById(R.id.text_title1)).setText("Hey, where is everybody?");
        ((TextView) view.findViewById(R.id.text_title2)).setText("There are no contacts to show, most likely because you haven’t joined any communities (yet).");
        ((TextView) view.findViewById(R.id.text_contain)).setText("Once you create a community (on the website) or join a community to which you’ve been invited, you’ll start to see contact info for yourself and other community members.");


        setScrollView();
    }

    private void setScrollView() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewContactRecyclerView.setLayoutManager(layoutManager);
        mViewContactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mViewContactRecyclerView.setAdapter(mContactListAdapter);
        mViewContactRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = layoutManager.getChildCount();
                totalItem = layoutManager.getItemCount();
                scrolledOutItem = layoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItem + scrolledOutItem == totalItem)) {
                    isScrolling = false;
                    if (!mIsFromSearch) {
                        getData();
                    }
                }
            }
        });
    }

    private void getData() {

        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                int offset = pageN0*mLimit;
                pageN0++;
                // all again api with new limit and offset request
                if (ConnectionDetector.isConnectingToInternet(getActivity())) {
                    getApiContactListApi(mLimit, offset);
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000); // 1 sec delay

    }

    public static void checkEmptyContact() {
        if (recycler_view_empty1 != null && mViewContactRecyclerView != null) {
            if (contactListResponseModels.size() == 0) {
                recycler_view_empty1.setVisibility(View.VISIBLE);
                mViewContactRecyclerView.setVisibility(View.GONE);
            } else {
                mViewContactRecyclerView.setVisibility(View.VISIBLE);
                recycler_view_empty1.setVisibility(View.GONE);
            }
        }
    }


    // Get webservice to show all UpToUs member
    public void getContactList() {
        try {
            if (contactListResponseModels.size() != 0) {

                String Message = Prefs.getMessage(getActivity());
                if (Message == null) {
                    mContactListAdapter = new ContactListAdapter(getActivity(), contactListResponseModels);
                    mViewContactRecyclerView.setAdapter(mContactListAdapter);
                    Log.i("ContactFragment", "Setting Adapter .... contact ");
                }

                mViewContactRecyclerView.setVisibility(View.VISIBLE);
                int communityId = Prefs.getCommunityId(getActivity());
                if (Message == null) {
                    if (communityId != 0) {
                        Log.i("ContactFragment", "Filtering  Adapter .... contact ");
                        FilterCommunityForContact(contactListResponseModels, communityId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Method to filter member by search string
    public List<ContactListResponseModel> SearchFilterForContact(List<ContactListResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<ContactListResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (ContactListResponseModel model : models) {
                String fName = model.getFirstName();
                String lastName = model.getLastName();
                String phone = model.getMobile();
                String text = "";
                if (fName != null)
                    text = text + fName.toLowerCase();
                if (lastName != null)
                    text = text + lastName.toLowerCase();
                if (phone != null)
                    text = text + phone.toLowerCase();


                if (text != null) {
                    if (text.contains(query)) {
                        filteredModelList.add(model);
                    }


                } else {
                    List<ContactListResponseModel.ChildrenBean> com = model.getChildren();
                    for (ContactListResponseModel.ChildrenBean c : com) {
                        final String text1 = c.getFirstName().toLowerCase();
                        if (text1 != null) {
                            if (text1.contains(query)) {
                                filteredModelList.add(model);
                            }
                        }
                    }
                }
            }



            mContactListResponseSearch = filteredModelList;
            mContactListForSearchAdapter = new ContactListForSearchAdapter(getActivity(), mContactListResponseSearch);
            mViewContactRecyclerView.setAdapter(mContactListForSearchAdapter);


            if (query.equalsIgnoreCase("")) {
                mTextViewSearchResult.setVisibility(View.GONE);
            } else {
                mTextViewSearchResult.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredModelList;
    }

    // Method to filter feed by community
    private List<ContactListResponseModel> FilterCommunityForContact(List<ContactListResponseModel> models, int Id) {
        contactListResponseModels = new ArrayList<>();
        try {
            for (ContactListResponseModel model : models) {
                List<ContactListResponseModel.CommunitiesBean> com = model.getCommunities();
                for (ContactListResponseModel.CommunitiesBean c : com) {
                    final int CommunityID = c.getId();
                    if (CommunityID == Id) {
                        contactListResponseModels.add(model);
                    }
                }
                mViewContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mContactListAdapter = new ContactListAdapter(getActivity(), contactListResponseModels);
                mViewContactRecyclerView.setAdapter(mContactListAdapter);
                //    mContactListAdapter.notifyDataSetChanged();

            }
            int Position = Prefs.getPosition(getActivity());

            MainActivity activity = (MainActivity) getActivity();
            if (Position == 1) {
                ContactFragment.checkEmptyContact();
                if (contactListResponseModels.size() == 0) {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                    Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactListResponseModels;
    }



    // Get webservice to show all UpToUs member
    public void getApiContactListApi(int limit, int offset) {
        if (contactListResponseModels != null) {
           //add
            showDialog();

            String mAuthenticationId = Prefs.getAuthenticationId(getActivity());
            String mAuthenticationPassword = Prefs.getAuthenticationPassword(getActivity());
            APIServices service =
                    ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
            Call<List<ContactListResponseModel>> call = service.GetContactList(limit, offset);
            call.enqueue(new Callback<List<ContactListResponseModel>>() {
                @Override
                public void onResponse(Call<List<ContactListResponseModel>> call, Response<List<ContactListResponseModel>> response) {
                    try {
                       /* Log.i("MainActivity", "Got Request .... contact ");*/

                        dialogDismiss( );

                        if(onResumeStatus) {

                            if (response.body() != null && response.body().size() > 0) {
                                for (ContactListResponseModel item : response.body()) {
                                    if (item.getFirstName() != null && item.getFirstName() != "" ||
                                            item.getLastName() != null && item.getLastName() != "") {
                                        MainActivity.contactListResponseModels.add(item);
                                    }
                                }

                                Collections.sort(MainActivity.contactListResponseModels, new CustomComparator());
                                mViewContactRecyclerView.getAdapter().notifyDataSetChanged();
                                Prefs.setContactList(getActivity(), MainActivity.contactListResponseModels.toString());
                                LoadOnce = true;
                                mTextViewSearchResult.setVisibility(View.GONE);
                                mViewContactRecyclerView.setVisibility(View.VISIBLE);

                                int communityId = Prefs.getCommunityId(getActivity());
                                if (communityId != 0) {
                                    FilterCommunityForContact(MainActivity.contactListResponseModels, communityId);
                                }
                                checkEmptyContact();


                            } else {

                                //  Toast.makeText(MainActivity.this, "End list - No Contact", Toast.LENGTH_SHORT).show();
                            }

                        }

                    } catch (Exception e) {
                        dialogDismiss( );
                    }
                }

                @Override
                public void onFailure(Call<List<ContactListResponseModel>> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                    dialogDismiss();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        onResumeStatus=true;

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            getContactList();
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        onResumeStatus=false;
        dialogDismiss();
    }




    //show dialog
    private void showDialog()
    {
        if(progressDialog==null)
            progressDialog=new ProgressDialog(getActivity());

            progressDialog.setCancelable(false);
            progressDialog.show();

    }

    //dismiss dialog
    private void dialogDismiss()
    {
        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.cancel();
    }

}