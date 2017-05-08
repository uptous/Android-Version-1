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

import com.uptous.MyApplication;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.ContactListResponseModel;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.adapter.ContactListAdapter;
import com.uptous.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * FileName : ContactFragment
 * Description :Show all UpToUs member
 * Dependencies :ContactListAdapter
 */
public class ContactFragment extends Fragment {

    private ContactListAdapter mContactListAdapter;

    public static RecyclerView mViewContactRecyclerView;

    public static List<ContactListResponseModel> contactListResponseModels = new ArrayList<>();

    public static TextView mTextViewSearchResult;


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


    @Override
    public void onResume() {
        super.onResume();


//        if (Message == null) {
        if (ConnectionDetector.isConnectingToInternet(getActivity())) {

            getContactList();

        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

//        }
    }

    //Method to initialize view
    private void initView(View view) {

        //Local Variables Initialization
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        //Global Variables Initialization
        mViewContactRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contact);
        mViewContactRecyclerView.setLayoutManager(layoutManager);

        mTextViewSearchResult = (TextView) view.findViewById(R.id.search_result);


    }


    // Get webservice to show all UpToUs member
    public void getContactList() {

        try {
            if (MainActivity.contactListResponseModels.size() != 0) {
                mTextViewSearchResult.setVisibility(View.GONE);

                String Message = MyApplication.mSharedPreferences.getString("Message", null);
                if (Message == null) {
                    mContactListAdapter = new ContactListAdapter(getActivity(), MainActivity.contactListResponseModels);
                    mViewContactRecyclerView.setAdapter(mContactListAdapter);

                }

                mViewContactRecyclerView.setVisibility(View.VISIBLE);
                int communityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);
                if (Message == null) {
                    if (communityId != 0) {
                        FilterCommunityForContact(MainActivity.contactListResponseModels, communityId);
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
            mViewContactRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mContactListAdapter = new ContactListAdapter(getActivity(), filteredModelList);
            mViewContactRecyclerView.setAdapter(mContactListAdapter);
            mContactListAdapter.notifyDataSetChanged();

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
                mContactListAdapter = new ContactListAdapter(getActivity(),  contactListResponseModels);
                mViewContactRecyclerView.setAdapter(mContactListAdapter);
                mContactListAdapter.notifyDataSetChanged();


            }
            int Position = MyApplication.mSharedPreferences.getInt("Position", 0);

            MainActivity activity = (MainActivity) getActivity();
            if (Position == 1) {
                if (contactListResponseModels.size() == 0) {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  contactListResponseModels;
    }

}