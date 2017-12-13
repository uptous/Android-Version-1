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

import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.ContactListResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.adapter.ContactListAdapter;
import com.uptous.R;

import java.util.ArrayList;
import java.util.List;

import static com.uptous.view.activity.MainActivity.contactListResponseModels;


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

    private static View recycler_view_empty1;
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
        recycler_view_empty1 = view.findViewById(R.id.recycler_view_empty);
        ((TextView)view.findViewById(R.id.text_title1)).setText("Hey, where is everybody?");
        ((TextView)view.findViewById(R.id.text_title2)).setText("There are no contacts to show, most likely because you haven’t joined any communities (yet).");
        ((TextView)view.findViewById(R.id.text_contain)).setText("Once you create a community (on the website) or join a community to which you’ve been invited, you’ll start to see contact info for yourself and other community members.");

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
                mTextViewSearchResult.setVisibility(View.GONE);

                String Message =Prefs.getMessage(getActivity());
                if (Message == null) {
                    mContactListAdapter = new ContactListAdapter(getActivity(), contactListResponseModels);
                    mViewContactRecyclerView.setAdapter(mContactListAdapter);
                    Log.i("ContactFragment","Setting Adapter .... contact ");
                }

                mViewContactRecyclerView.setVisibility(View.VISIBLE);
                int communityId = Prefs.getCommunityId(getActivity());
                if (Message == null) {
                    if (communityId != 0) {
                        Log.i("ContactFragment","Filtering  Adapter .... contact ");
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

}