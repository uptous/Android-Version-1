package com.uptous.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.FeedResponseModel;
import com.uptous.ui.activity.CommunityActivity;
import com.uptous.ui.activity.MainActivity;
import com.uptous.ui.activity.MessagePostActivity;
import com.uptous.ui.activity.PicturePostActivity;
import com.uptous.ui.adapter.HomeListAdapter;
import com.uptous.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * FileName : HomeFragment
 * Description :
 * Dependencies : HomeAdapter
 */
public class HomeFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private RecyclerView mViewHomeRecyclerView;
    private HomeListAdapter mHomeListAdapter;
    private TextView mTextViewCancel, mTextViewSearch;
    private List<FeedResponseModel> mFeedResponseModelList = new ArrayList<>();
    private List<FeedResponseModel> mFilteredFeedResponseModels = new ArrayList<>();
    private List<FeedResponseModel> mFilter = new ArrayList<>();
    private boolean FAB_Status = false;
    private FloatingActionButton mFabPost;
    private FloatingActionButton mFabMessagePost;
    private FloatingActionButton mFabPicturePost;
    private Animation mAnimationFabMessagePost;
    private Animation mHideAnimationFabMessagePost;
    private Animation mAnimationFabPicturePost;
    private Animation mHideAnimationFabPicturePost;
    private String mAuthenticationId, mAuthenticationPassword, mCommunityName;
    private int mCommunityId, mAllFeed;

    public static SearchView SearchView;

    protected ContactFragment contactFragment;
    protected SignUpFragment signUpFragment;
    protected EventsFragment eventsFragment;
    protected LibraryFragment libraryFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        initView(view);


        return view;
    }

    private void initView(View view) {

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewHomeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home);
        mViewHomeRecyclerView.setLayoutManager(layoutManager);
        mViewHomeRecyclerView.setNestedScrollingEnabled(false);
        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        SearchView.setOnQueryTextListener(this);
        mTextViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
        mFabPost = (FloatingActionButton) view.findViewById(R.id.fab);
        mFabMessagePost = (FloatingActionButton) view.findViewById(R.id.fab_2);
        mFabPicturePost = (FloatingActionButton) view.findViewById(R.id.fab_3);
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

//        show_fab_1 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab1_show);
//        hide_fab_1 = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab1_hide);
        mAnimationFabMessagePost = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab2_show);
        mHideAnimationFabMessagePost = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab2_hide);
        mAnimationFabPicturePost = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab3_show);
        mHideAnimationFabPicturePost = AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab3_hide);

        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        clickListenerOnViews();

        handleSearchView();
//
//        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
//            getApiFeed();
//        } else {
//            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
//        }
    }

    private void clickListenerOnViews() {
        mFabPost.setOnClickListener(this);
        mFabMessagePost.setOnClickListener(this);
        mFabPicturePost.setOnClickListener(this);
        mTextViewCancel.setOnClickListener(this);
        MainActivity.mTextViewTitle.setOnClickListener(this);
        MainActivity.mImageViewCommunityInvitation.setOnClickListener(this);


    }

    private void handleSearchView() {
        try {
            if (contactFragment != null) {
                ContactFragment.SearchView.clearFocus();
                ContactFragment.SearchView.onActionViewCollapsed();
            } else if (signUpFragment != null) {
                SignUpFragment.SearchView.clearFocus();
                SignUpFragment.SearchView.onActionViewCollapsed();
            } else if (libraryFragment != null) {
                LibraryFragment.SearchView.onActionViewCollapsed();
                LibraryFragment.SearchView.clearFocus();

            } else if (eventsFragment != null) {
                EventsFragment.SearchView.clearFocus();
                EventsFragment.SearchView.onActionViewCollapsed();
            } else {
                EventsFragment.SearchView.clearFocus();
                EventsFragment.SearchView.onActionViewCollapsed();
                LibraryFragment.SearchView.onActionViewCollapsed();
                LibraryFragment.SearchView.clearFocus();
                SignUpFragment.SearchView.clearFocus();
                SignUpFragment.SearchView.onActionViewCollapsed();
                ContactFragment.SearchView.clearFocus();
                ContactFragment.SearchView.onActionViewCollapsed();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void expandFAB() {

        //Floating Action Button 1
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
//        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
//        fab1.setLayoutParams(layoutParams);
//        fab1.startAnimation(show_fab_1);
//        fab1.setClickable(true);

//        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) mFabMessagePost.getLayoutParams();
        layoutParams2.rightMargin += (int) (mFabMessagePost.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (mFabMessagePost.getHeight() * 1.5);
        mFabMessagePost.setLayoutParams(layoutParams2);
        mFabMessagePost.startAnimation(mAnimationFabMessagePost);
        mFabMessagePost.setClickable(true);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) mFabPicturePost.getLayoutParams();
        layoutParams3.rightMargin += (int) (mFabPicturePost.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (mFabPicturePost.getHeight() * 1.7);
        mFabPicturePost.setLayoutParams(layoutParams3);
        mFabPicturePost.startAnimation(mAnimationFabPicturePost);
        mFabPicturePost.setClickable(true);
    }


    private void hideFAB() {

        //Floating Action Button 1
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
//        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
//        fab1.setLayoutParams(layoutParams);
//        fab1.startAnimation(hide_fab_1);
//        fab1.setClickable(false);

//        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) mFabMessagePost.getLayoutParams();
        layoutParams2.rightMargin -= (int) (mFabMessagePost.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (mFabMessagePost.getHeight() * 1.5);
        mFabMessagePost.setLayoutParams(layoutParams2);
        mFabMessagePost.startAnimation(mHideAnimationFabMessagePost);
        mFabMessagePost.setClickable(false);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) mFabPicturePost.getLayoutParams();
        layoutParams3.rightMargin -= (int) (mFabPicturePost.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (mFabPicturePost.getHeight() * 1.7);
        mFabPicturePost.setLayoutParams(layoutParams3);
        mFabPicturePost.startAnimation(mHideAnimationFabPicturePost);
        mFabPicturePost.setClickable(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
                break;
            case R.id.fab_2:

                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
//                    FAB_Status = false;

                Intent intent2 = new Intent(getActivity(), MessagePostActivity.class);
                startActivity(intent2);


                break;
            case R.id.fab_3:
                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
                //Close FAB menu
//                    FAB_Status = false;

                Intent intent1 = new Intent(getActivity(), PicturePostActivity.class);
                startActivity(intent1);

                break;

            case R.id.text_view_title:

//                Intent intent = new Intent(getActivity(), CommunityActivity.class);
//                startActivity(intent);
                break;
            case R.id.image_view_community_invitations:
                break;
            case R.id.text_view_cancel:
                SearchView.clearFocus();
                SearchView.onActionViewCollapsed();
                break;


        }
    }

    private void getApiFeed() {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<FeedResponseModel>> call = service.GetNewsFeed();

        call.enqueue(new Callback<List<FeedResponseModel>>() {
            @Override
            public void onResponse(Call<List<FeedResponseModel>> call, Response<List<FeedResponseModel>> response) {
                mProgressDialog.dismiss();
                try {

                    mFeedResponseModelList = response.body();
                    mHomeListAdapter = new HomeListAdapter(getActivity(), mFeedResponseModelList);
                    mViewHomeRecyclerView.setAdapter(mHomeListAdapter);


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<FeedResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                Log.d("onFailure", t.toString());
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        SearchView.onActionViewCollapsed();
        SearchView.clearFocus();
        MyApplication.editor.putString("Files", "file");
        MyApplication.editor.commit();

        mCommunityName = MyApplication.mSharedPreferences.getString("CommunityName", null);
        mCommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            getApiFeed();
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        //Code for community filter on top
//        if (CommunityId != 0) {
//            MainActivity.mTextViewTitle.setText(CommunityName);
//            MyApplication.editor.putString("CommunityFilter", "communityfilter");
//            MyApplication.editor.commit();
//            Filter = FilterCommunity(mFeedResponseModelList, CommunityId);
//        } else {
//            if (CommunityId == 0) {
//                MyApplication.editor.putString("CommunityFilter", null);
//                MyApplication.editor.commit();
//                MainActivity.mTextViewTitle.setText("All");
//                if (ConnectionDetector.isConnectingToInternet(getActivity())) {
//                    getApiFeed();
//                } else {
//                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String CommunityFilter = MyApplication.mSharedPreferences.getString("CommunityFilter", null);
        if (CommunityFilter != null) {
            mFilteredFeedResponseModels = filter(mFilter, newText);
        } else {
            mFilteredFeedResponseModels = filter(mFeedResponseModelList, newText);
        }


        return true;
    }

    private List<FeedResponseModel> filter(List<FeedResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<FeedResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (FeedResponseModel model : models) {
                final String text = model.getNewsItemName().toLowerCase() + "" + model.getCommunityName().toLowerCase() +
                        "" + model.getOwnerName().toLowerCase() + "" + model.getNewsItemDescription().toLowerCase()
                        + "" + model.getCommunityName().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
                mViewHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mHomeListAdapter = new HomeListAdapter(getActivity(), filteredModelList);
                mViewHomeRecyclerView.setAdapter(mHomeListAdapter);
                mHomeListAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredModelList;
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

    private List<FeedResponseModel> FilterCommunity(List<FeedResponseModel> models, int Id) {
        mFilter = new ArrayList<>();
        try {
            for (FeedResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    mFilter.add(model);
                }
                mViewHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mHomeListAdapter = new HomeListAdapter(getActivity(), mFilter);
                mViewHomeRecyclerView.setAdapter(mHomeListAdapter);
                mHomeListAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFilter;
    }


}
