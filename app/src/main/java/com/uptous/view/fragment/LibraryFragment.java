package com.uptous.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
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
import com.uptous.model.PhotoAlbumResponseModel;
import com.uptous.view.activity.AlbumDetailActivity;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.adapter.AlbumsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : LibraryFragment
 * Description :Show all albums and attachment files  and also show all photos of album by clicking particular album
 * Dependencies :LibraryFragment,AlbumsAdapter,AttachmentAdapter
 */
public class LibraryFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private RecyclerView mViewAlbumsRecyclerView, mRecyclerViewFiles;

    private String mAuthenticationId, mAuthenticationPassword;

    private RelativeLayout mRelativeLayoutSearchBar;

    private AlbumsAdapter mAlbumsAdapter;

    private TextView mTextViewAlbums, mTextViewFiles, mTextViewHeading, mTextViewCancel, mTextViewSearch;

    private List<PhotoAlbumResponseModel> mPhotoAlbumResponseModelList = new ArrayList<>();


    public SearchView SearchView;

    protected ContactFragment contactFragment;
    protected SignUpFragment signUpFragment;
    protected HomeFragment homeFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        initView(view);

        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
            MyApplication.editor.putString("Files", null);
            MyApplication.editor.commit();
            getApiAlbumList();
        } else {
            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_albums:
                mTextViewAlbums.setBackgroundResource(R.drawable.rounded_all_files_fill);
                mTextViewAlbums.setTextColor(Color.WHITE);
                mTextViewFiles.setTextColor(Color.BLACK);
                mTextViewFiles.setBackgroundResource(R.drawable.rounded_all_files_empty_second);
                mTextViewHeading.setVisibility(View.GONE);
                mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerViewFiles.setVisibility(View.GONE);
                MyApplication.editor.putString("Files", null);
                MyApplication.editor.putString("Album", "album");
                MyApplication.editor.commit();
                getApiAlbumList();
                break;
            case R.id.text_view_files:
                mTextViewFiles.setBackgroundResource(R.drawable.rounded_all_files_fill_second);
                mTextViewFiles.setTextColor(Color.WHITE);
                mTextViewAlbums.setTextColor(Color.BLACK);
                mTextViewAlbums.setBackgroundResource(R.drawable.rounded_all_files_empty);
                mViewAlbumsRecyclerView.setVisibility(View.GONE);
                MyApplication.editor.putString("Files", "file");
                MyApplication.editor.putString("Album", null);
                MyApplication.editor.commit();
                break;
            case R.id.text_view_cancel:
                SearchView.clearFocus();
                SearchView.onActionViewCollapsed();
                mTextViewSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_searchbar:
                SearchView.onActionViewExpanded();
                mTextViewSearch.setVisibility(View.GONE);
                break;
        }

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

        filterAlbum(mPhotoAlbumResponseModelList, newText);

        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        SearchView.clearFocus();
        SearchView.onActionViewCollapsed();

    }

    //Method to initialize view
    private void initView(View view) {
        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        SearchView.setOnQueryTextListener(this);

        mTextViewCancel = (TextView) view.findViewById(R.id.text_view_cancel);
        mTextViewAlbums = (TextView) view.findViewById(R.id.text_view_albums);
        mTextViewFiles = (TextView) view.findViewById(R.id.text_view_files);
        mTextViewHeading = (TextView) view.findViewById(R.id.text_view_heading);
        mTextViewHeading.setVisibility(View.GONE);

        RecyclerView.LayoutManager layoutManager
                = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        mViewAlbumsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_albums);
        mViewAlbumsRecyclerView.setLayoutManager(layoutManager);


        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewFiles = (RecyclerView) view.findViewById(R.id.recycler_view_files);
        mRecyclerViewFiles.setLayoutManager(layoutManagerFiles);

        SearchView = (SearchView) view.findViewById(R.id.serach_view_filter);
        mTextViewSearch = (TextView) view.findViewById(R.id.text_view_search);
        mRelativeLayoutSearchBar = (RelativeLayout) view.findViewById(R.id.layout_searchbar);

        SearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewSearch.setVisibility(View.GONE);
                SearchView.onActionViewExpanded();

            }
        });


        SearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                SearchView.clearFocus();
                SearchView.onActionViewCollapsed();
                mTextViewSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });
        clickListenerOnViews();

        handleSearchView();

        getData();
    }

    // Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    //Method to setClickListener On views
    private void clickListenerOnViews() {
        mRelativeLayoutSearchBar.setOnClickListener(this);
        mTextViewCancel.setOnClickListener(this);
        SearchView.setOnQueryTextListener(this);
        mTextViewAlbums.setOnClickListener(this);
        mTextViewFiles.setOnClickListener(this);
    }

    // Method to handle search view
    private void handleSearchView() {
        MainActivity activity = ((MainActivity) getActivity());
        try {
            contactFragment = (ContactFragment) activity.mViewPagerAdapter.getItem(1);
            if (contactFragment != null) {
                contactFragment.SearchView.clearFocus();
                contactFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            signUpFragment = (SignUpFragment) activity.mViewPagerAdapter.getItem(2);
            if (signUpFragment != null) {
                signUpFragment.SearchView.clearFocus();
                signUpFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            homeFragment = (HomeFragment) activity.mViewPagerAdapter.getItem(0);
            if (homeFragment != null) {
                homeFragment.SearchView.clearFocus();
                homeFragment.SearchView.onActionViewCollapsed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get webservice to show albums
    private void getApiAlbumList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);


        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<PhotoAlbumResponseModel>> call = service.GetAllAlbumPhoto();

        call.enqueue(new Callback<List<PhotoAlbumResponseModel>>() {
            @Override
            public void onResponse(Call<List<PhotoAlbumResponseModel>> call, Response<List<PhotoAlbumResponseModel>> response) {
                try {
                    progressDialog.dismiss();
                    MyApplication.editor.putString("Files", null);
                    MyApplication.editor.commit();

                    mPhotoAlbumResponseModelList = response.body();
                    mAlbumsAdapter = new AlbumsAdapter(getActivity(), mPhotoAlbumResponseModelList);
                    mViewAlbumsRecyclerView.setAdapter(mAlbumsAdapter);
                    mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerViewFiles.setVisibility(View.GONE);


                    ItemClickSupport.addTo(mViewAlbumsRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {


                            if (mPhotoAlbumResponseModelList.size() == 0) {
                                int AlbumID = mPhotoAlbumResponseModelList.get(position).getId();
                                MyApplication.editor.putInt("NewsItemID", AlbumID);
                                MyApplication.editor.commit();
                                Intent intent = new Intent(getActivity(), AlbumDetailActivity.class);
                                startActivity(intent);
                            } else {
                                int OpId = mPhotoAlbumResponseModelList.get(position).getId();
                                MyApplication.editor.putInt("NewsItemID", OpId);
                                MyApplication.editor.commit();
                                Intent intent = new Intent(getActivity(), AlbumDetailActivity.class);
                                startActivity(intent);
                            }

                        }
                    });
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<PhotoAlbumResponseModel>> call, Throwable t) {
                Log.d("onFailure", t.toString());

                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }

        });
    }

    // Method to filter album by search string
    private List<PhotoAlbumResponseModel> filterAlbum(List<PhotoAlbumResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<PhotoAlbumResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (PhotoAlbumResponseModel model : models) {
                final String text = model.getTitle().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
                mViewAlbumsRecyclerView.setLayoutManager(layoutManager);
                mAlbumsAdapter = new AlbumsAdapter(getActivity(), filteredModelList);
                mViewAlbumsRecyclerView.setAdapter(mAlbumsAdapter);
                mAlbumsAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredModelList;
    }

}