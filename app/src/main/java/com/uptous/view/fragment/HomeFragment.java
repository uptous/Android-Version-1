package com.uptous.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.FeedResponseModel;
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
 * Dependencies : HomeAdapter, InternetConnection
 */
public class HomeFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private RecyclerView mViewHomeRecyclerView;
    private HomeListAdapter mHomeListAdapter;
    private TextView mTextViewCancel;

    private List<FeedResponseModel> mFeedResponseModelList = new ArrayList<>();

    private boolean FAB_Status = false;
    private FloatingActionButton mFabPost, mFabMessagePost, mFabPicturePost;
    private Animation mAnimationFabMessagePost, mHideAnimationFabMessagePost, mAnimationFabPicturePost,
            mHideAnimationFabPicturePost;

    private String mAuthenticationId, mAuthenticationPassword;

    private RelativeLayout mRelativeLayoutSearchBar;

    public  SearchView SearchView;
    public  TextView  TextViewSearch;

    protected ContactFragment contactFragment;

    protected SignUpFragment signUpFragment;

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

                Intent intent1 = new Intent(getActivity(), PicturePostActivity.class);
                startActivity(intent1);

                break;

            case R.id.image_view_community_invitations:
                break;
            case R.id.text_view_cancel:
                SearchView.clearFocus();
                SearchView.onActionViewCollapsed();
                TextViewSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_searchbar:
                SearchView.onActionViewExpanded();
                TextViewSearch.setVisibility(View.GONE);
                break;


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SearchView.onActionViewCollapsed();
        SearchView.clearFocus();
        MyApplication.editor.putString("Files", "file");
        MyApplication.editor.commit();

        String mMessagePost = MyApplication.mSharedPreferences.getString("MessagePost", null);
        String mPicturePost = MyApplication.mSharedPreferences.getString("PicturePost", null);

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            if (mMessagePost != null) {
                getApiFeed();
            }
            if (mPicturePost != null) {
                getApiFeed();
            }

        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        filter(mFeedResponseModelList, newText);


        return true;
    }

    //Method to clear SharedPreference
    private void clearData() {
        MyApplication.editor.putString("MessagePost", null);
        MyApplication.editor.putString("PicturePost", null);
        MyApplication.editor.commit();
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
        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        SearchView.setOnQueryTextListener(this);

        mTextViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
        mFabPost = (FloatingActionButton) view.findViewById(R.id.fab);
        mFabMessagePost = (FloatingActionButton) view.findViewById(R.id.fab_2);
        mFabPicturePost = (FloatingActionButton) view.findViewById(R.id.fab_3);
        TextViewSearch = (TextView) view.findViewById(R.id.text_view_search);
        mRelativeLayoutSearchBar = (RelativeLayout) view.findViewById(R.id.layout_searchbar);

        SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextViewSearch.setVisibility(View.GONE);
                SearchView.onActionViewExpanded();

            }
        });


        SearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                SearchView.clearFocus();
                SearchView.onActionViewCollapsed();
                TextViewSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });

        clickListenerOnViews();

        handleSearchView();

        getData();

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            getApiFeed();
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    //Method to setClickListener On views
    private void clickListenerOnViews() {

        mFabPost.setOnClickListener(this);
        mFabMessagePost.setOnClickListener(this);
        mFabPicturePost.setOnClickListener(this);
        mRelativeLayoutSearchBar.setOnClickListener(this);
        mTextViewCancel.setOnClickListener(this);


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

            signUpFragment = (SignUpFragment) activity.mViewPagerAdapter.getItem(2);
            if (signUpFragment != null) {
                signUpFragment.SearchView.clearFocus();
                signUpFragment.SearchView.onActionViewCollapsed();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


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


//        Floating Action Button 2
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
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<FeedResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    // Method to filter feed by search string
    private List<FeedResponseModel> filter(List<FeedResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<FeedResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (FeedResponseModel model : models) {


                String text = model.getNewsItemName().toLowerCase() +
                        "" + model.getOwnerName().toLowerCase() + "" + model.getNewsItemDescription().toLowerCase();


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

}
