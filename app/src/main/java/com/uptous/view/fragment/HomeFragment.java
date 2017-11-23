package com.uptous.view.fragment;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.model.FeedResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.BaseActivity;
import com.uptous.view.activity.LogInActivity;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.activity.MessagePostActivity;
import com.uptous.view.activity.PicturePostActivity;
import com.uptous.view.adapter.HomeListAdapter;
import com.uptous.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * FileName : HomeFragment
 * Description : Show all new feeds like message, album, files etc.
 * Dependencies : HomeAdapter
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    public static RecyclerView mViewHomeRecyclerView;
    private HomeListAdapter mHomeListAdapter;

    public static List<FeedResponseModel> feedResponseModelList = new ArrayList<>();

    private boolean FAB_Status = false;
    public static FloatingActionButton mFabPost, mFabMessagePost, mFabPicturePost;
    private Animation mAnimationFabMessagePost, mHideAnimationFabMessagePost, mAnimationFabPicturePost,
            mHideAnimationFabPicturePost;

    private String mAuthenticationId, mAuthenticationPassword;

    private TextView mTextViewSearchResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        FAB_Status = false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (!FAB_Status) {
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

                if (!FAB_Status) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;

                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;

                }

                Prefs.setFeedDetail(getActivity(), "feed");

                Intent intent2 = new Intent(getActivity(), MessagePostActivity.class);
                startActivity(intent2);


                break;
            case R.id.fab_3:
                if (!FAB_Status) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }

                Prefs.setFeedDetail(getActivity(), "feed");
                Intent intent1 = new Intent(getActivity(), PicturePostActivity.class);
                startActivity(intent1);

                break;


        }
    }


    @Override
    public void onResume() {
        super.onResume();

        String messagePost = Prefs.getMessagePost(getActivity());
        String picturePost = Prefs.getPicturePost(getActivity());
        String Feed = Prefs.getFeedDetail(getActivity());

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            if (messagePost != null||picturePost != null||Feed == null) {
                getApiFeed();
            }
            else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
      else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    //Method to clear SharedPreference
    private void clearData() {
        Prefs.setMessagePost(getActivity(), null);
        Prefs.setPicturePost(getActivity(), null);
    }

    //Method to initialize view
    private void initView(View view) {

        mAnimationFabMessagePost =
                AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab2_show);
        mHideAnimationFabMessagePost =
                AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab2_hide);
        mAnimationFabPicturePost =
                AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab3_show);
        mHideAnimationFabPicturePost =
                AnimationUtils.loadAnimation(getActivity().getApplication(), R.anim.fab3_hide);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mViewHomeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home);
        mViewHomeRecyclerView.setLayoutManager(layoutManager);
        mViewHomeRecyclerView.setNestedScrollingEnabled(false);
        mFabPost = (FloatingActionButton) view.findViewById(R.id.fab);
        mFabMessagePost = (FloatingActionButton) view.findViewById(R.id.fab_2);
        mFabPicturePost = (FloatingActionButton) view.findViewById(R.id.fab_3);
        mTextViewSearchResult = (TextView) view.findViewById(R.id.search_result);

        clickListenerOnViews();

        getData();

    }

    // Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(getActivity());
        mAuthenticationPassword = Prefs.getAuthenticationPassword(getActivity());
    }

    //Method to setClickListener On views
    private void clickListenerOnViews() {

        mFabPost.setOnClickListener(this);
        mFabMessagePost.setOnClickListener(this);
        mFabPicturePost.setOnClickListener(this);


    }

    // Method to expand floating action button
    private void expandFAB() {

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

    // Method to hide floating action button
    private void hideFAB() {


        //Floating Action Button 2
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

    // Get webservice to show all news feed like : message, picture ,file etc.
    private void getApiFeed() {
        ((MainActivity)getActivity()).showProgressDialog();

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<FeedResponseModel>> call = service.GetNewsFeed();

        call.enqueue(new Callback<List<FeedResponseModel>>() {
            @Override
            public void onResponse(Call<List<FeedResponseModel>> call, Response<List<FeedResponseModel>> response) {
                ((MainActivity)getActivity()).hideProgressDialog();
                try {

                    if (response.body() != null) {
                        mTextViewSearchResult.setVisibility(View.GONE);
                        feedResponseModelList = response.body();
                        mHomeListAdapter = new HomeListAdapter(getActivity(), feedResponseModelList);
                        mViewHomeRecyclerView.setAdapter(mHomeListAdapter);

                        int communityId = Prefs.getCommunityId(getActivity());
                        if (communityId != 0) {
                            FilterCommunityForFeed(feedResponseModelList, communityId);
                        }

                    } else {

                        BaseActivity baseActivity = (BaseActivity)getActivity();
                        baseActivity.showLogOutDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<FeedResponseModel>> call, Throwable t) {
                 ((MainActivity)getActivity()).hideProgressDialog();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Method to filter feed by search string
    public List<FeedResponseModel> SearchFilterForFeed(List<FeedResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<FeedResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (FeedResponseModel model : models) {
                String NewsItemName = model.getNewsItemName();
                String OwnerName = model.getOwnerName();

                String text = "";
                if (NewsItemName != null)
                    text = text + NewsItemName.toLowerCase();
                if (OwnerName != null)
                    text = text + OwnerName.toLowerCase();


                if (text != null) {
                    if (text.contains(query)) {
                        filteredModelList.add(model);
                    }


                }

                mViewHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mHomeListAdapter = new HomeListAdapter(getActivity(), filteredModelList);
                mViewHomeRecyclerView.setAdapter(mHomeListAdapter);
                mHomeListAdapter.notifyDataSetChanged();
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
    private List<FeedResponseModel> FilterCommunityForFeed(List<FeedResponseModel> models, int Id) {
        feedResponseModelList = new ArrayList<>();
        try {
            for (FeedResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    feedResponseModelList.add(model);
                }
                mViewHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mHomeListAdapter = new HomeListAdapter(getActivity(), feedResponseModelList);
                mViewHomeRecyclerView.setAdapter(mHomeListAdapter);
                mHomeListAdapter.notifyDataSetChanged();

            }

            int Position = Prefs.getPosition(getActivity());

            MainActivity activity = (MainActivity) getActivity();
            if (Position == 0) {
                if (feedResponseModelList.size() == 0) {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return feedResponseModelList;

    }


}
