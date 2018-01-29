package com.uptous.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.FileResponseModel;
import com.uptous.model.PhotoAlbumResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.BaseActivity;
import com.uptous.view.activity.MainActivity;
import com.uptous.view.adapter.AlbumsAdapter;
import com.uptous.view.adapter.AttachmentAdapter;

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
public class LibraryFragment extends Fragment implements View.OnClickListener {

    public static RecyclerView mViewAlbumsRecyclerView, mRecyclerViewFiles;
    public static LinearLayout mLinearLayoutAlbumFile;

    private String mAuthenticationId, mAuthenticationPassword;

    private AlbumsAdapter mAlbumsAdapter;
    private AttachmentAdapter mAttachmentAdapter;

    private static TextView mTextViewAlbums,mTextViewEmptyTitle1,mTextViewEmptyTitle2,mTextViewEmptyContent, mTextViewFiles, mTextViewHeading;
    public static TextView mTextViewSearchResult;
    private static View recycler_view_empty;
    public static List<PhotoAlbumResponseModel> photoAlbumResponseModelList = new ArrayList<>();

    public static List<FileResponseModel> attachmentFileResponseModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_albums:
                MainActivity activity = (MainActivity) getActivity();
                activity.mSearchView.clearFocus();
                activity.mSearchView.onActionViewCollapsed();
                mTextViewAlbums.setBackgroundResource(R.drawable.rounded_all_files_fill);
                mTextViewAlbums.setTextColor(Color.WHITE);
                mTextViewFiles.setTextColor(Color.BLACK);
                mTextViewFiles.setBackgroundResource(R.drawable.rounded_all_files_empty_second);
                mTextViewHeading.setVisibility(View.GONE);
                mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerViewFiles.setVisibility(View.GONE);

                Prefs.setAttachment(getActivity(),null);
                Prefs.setAlbum(getActivity(), "album");
                getApiAlbumList();
                break;
            case R.id.text_view_files:
                MainActivity activity1 = (MainActivity) getActivity();
                activity1.mSearchView.clearFocus();
                activity1.mSearchView.onActionViewCollapsed();
                mTextViewFiles.setBackgroundResource(R.drawable.rounded_all_files_fill_second);
                mTextViewFiles.setTextColor(Color.WHITE);
                mTextViewAlbums.setTextColor(Color.BLACK);
                mTextViewAlbums.setBackgroundResource(R.drawable.rounded_all_files_empty);
                mViewAlbumsRecyclerView.setVisibility(View.GONE);
                Prefs.setAttachment(getActivity(),"attachment");
                Prefs.setAlbum(getActivity(), null);
                getApiFileList();
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        String Album = Prefs.getAlbum(getActivity());
        String Attachment = Prefs.getAttachment(getActivity());
        String Detail = Prefs.getAlbumDetail(getActivity());

        if (Detail == null) {
            if (Album != null) {
                if (ConnectionDetector.isConnectingToInternet(getActivity())) {
                    mTextViewAlbums.setBackgroundResource(R.drawable.rounded_all_files_fill);
                    mTextViewAlbums.setTextColor(Color.WHITE);
                    mTextViewFiles.setTextColor(Color.BLACK);
                    mTextViewFiles.setBackgroundResource(R.drawable.rounded_all_files_empty_second);
                    mTextViewHeading.setVisibility(View.GONE);
                    mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerViewFiles.setVisibility(View.GONE);
                    getApiAlbumList();
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            } else if (Attachment != null) {
                if (ConnectionDetector.isConnectingToInternet(getActivity())) {
                    mTextViewFiles.setBackgroundResource(R.drawable.rounded_all_files_fill_second);
                    mTextViewFiles.setTextColor(Color.WHITE);
                    mTextViewAlbums.setTextColor(Color.BLACK);
                    mTextViewAlbums.setBackgroundResource(R.drawable.rounded_all_files_empty);
                    mViewAlbumsRecyclerView.setVisibility(View.GONE);
                    getApiFileList();
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            } else {
                if (ConnectionDetector.isConnectingToInternet(getActivity())) {
                    mTextViewAlbums.setBackgroundResource(R.drawable.rounded_all_files_fill);
                    mTextViewAlbums.setTextColor(Color.WHITE);
                    mTextViewFiles.setTextColor(Color.BLACK);
                    mTextViewFiles.setBackgroundResource(R.drawable.rounded_all_files_empty_second);
                    mTextViewHeading.setVisibility(View.GONE);
                    mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerViewFiles.setVisibility(View.GONE);
                    getApiAlbumList();
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }


        }


    }

    //Method to initialize view
    private void initView(View view) {
        mTextViewAlbums = (TextView) view.findViewById(R.id.text_view_albums);
        mTextViewFiles = (TextView) view.findViewById(R.id.text_view_files);
        mTextViewHeading = (TextView) view.findViewById(R.id.text_view_heading);
        mTextViewHeading.setVisibility(View.GONE);
        mTextViewSearchResult = (TextView) view.findViewById(R.id.search_result);

        mTextViewEmptyTitle1 = (TextView) view.findViewById(R.id.text_title1);
        mTextViewEmptyTitle2 = (TextView) view.findViewById(R.id.text_title2);
        mTextViewEmptyContent = (TextView) view.findViewById(R.id.text_contain);
        recycler_view_empty= view.findViewById(R.id.recycler_view_empty);
        mLinearLayoutAlbumFile = (LinearLayout) view.findViewById(R.id.layout_album_file);
        RecyclerView.LayoutManager layoutManager
                = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        mViewAlbumsRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_albums);
        mViewAlbumsRecyclerView.setLayoutManager(layoutManager);


        LinearLayoutManager layoutManagerFiles
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewFiles = (RecyclerView) view.findViewById(R.id.recycler_view_files);
        mRecyclerViewFiles.setLayoutManager(layoutManagerFiles);


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
        mTextViewAlbums.setOnClickListener(this);
        mTextViewFiles.setOnClickListener(this);
    }

    public static void checkEmptyAlbum() {
        if (recycler_view_empty != null && mViewAlbumsRecyclerView != null) {
            if (photoAlbumResponseModelList.size() == 0) {
                recycler_view_empty.setVisibility(View.VISIBLE);
                mViewAlbumsRecyclerView.setVisibility(View.GONE);
            } else {
                recycler_view_empty.setVisibility(View.GONE);
                mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
            }
        }

        mTextViewEmptyTitle1.setText("Nothing to see here...");
        mTextViewEmptyTitle2.setText("No photos or albums have been added to the selected community.");
        mTextViewEmptyContent.setText("When photos are added, you’ll be able to see them here. Keep in mind that some communities don’t allow posting photos.\n" +
                "Looking to add new photos yourself? Switch to the Feed tab by tapping the leftmost icon below, then tap on “Post” in the lower right.");
    }

    public static void checkEmptyLib() {
        if (recycler_view_empty != null && mRecyclerViewFiles != null) {
            if (attachmentFileResponseModels.size() == 0) {
                recycler_view_empty.setVisibility(View.VISIBLE);
                mRecyclerViewFiles.setVisibility(View.GONE);
            } else {
                recycler_view_empty.setVisibility(View.GONE);
                mRecyclerViewFiles.setVisibility(View.VISIBLE);
            }
        }

        mTextViewEmptyTitle1.setText("Zero, zilch, nada...");
        mTextViewEmptyTitle2.setText("No files have been shared in the selected community.");
        mTextViewEmptyContent.setText("When files are shared, you’ll be able to see them here. Looking to upload a file yourself? For now, you’ll have to use the UpToUs website to upload and share files.");
    }

    // Get webservice to show albums
    private void getApiAlbumList() {
        ((MainActivity)getActivity()).showProgressDialog();

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<PhotoAlbumResponseModel>> call = service.GetAllAlbumPhoto();

        call.enqueue(new Callback<List<PhotoAlbumResponseModel>>() {
            @Override
            public void onResponse(Call<List<PhotoAlbumResponseModel>> call, Response<List<PhotoAlbumResponseModel>> response) {

                ((MainActivity)getActivity()).hideProgressDialog();
                try {

                    Prefs.setAlbum(getActivity(),"album");


                    if (response.body() != null) {
                        mTextViewSearchResult.setVisibility(View.GONE);
                        photoAlbumResponseModelList = response.body();
                        mAlbumsAdapter = new AlbumsAdapter(getActivity(), photoAlbumResponseModelList);
                        mViewAlbumsRecyclerView.setAdapter(mAlbumsAdapter);
                        mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                        mRecyclerViewFiles.setVisibility(View.GONE);
                        int communityId = Prefs.getCommunityId(getActivity());
                        checkEmptyAlbum();
                        if (communityId != 0) {
                            FilterCommunityForAlbum(photoAlbumResponseModelList, communityId);
                        }

                    } else {

                        BaseActivity baseActivity = (BaseActivity)getActivity();
                        baseActivity.showLogOutDialog();
                    }

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<PhotoAlbumResponseModel>> call, Throwable t) {
                Log.d("onFailure", t.toString());

                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();

                ((MainActivity)getActivity()).hideProgressDialog();
            }

        });
    }

    // Get webservice to show attachment files
    private void getApiFileList() {
        ((MainActivity)getActivity()).showProgressDialog();


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<FileResponseModel>> call = service.GetAttachment();

        call.enqueue(new Callback<List<FileResponseModel>>() {
                         @Override
                         public void onResponse(Call<List<FileResponseModel>> call, Response<List<FileResponseModel>> response) {
                             ((MainActivity)getActivity()).hideProgressDialog();

                             try {

                                 mTextViewSearchResult.setVisibility(View.GONE);
//                                 mTextViewHeading.setVisibility(View.VISIBLE);
                                 if (response.body() != null) {

                                     attachmentFileResponseModels = response.body();
                                     mAttachmentAdapter = new AttachmentAdapter(getActivity(), attachmentFileResponseModels);
                                     mRecyclerViewFiles.setAdapter(mAttachmentAdapter);
                                     mRecyclerViewFiles.setVisibility(View.VISIBLE);
                                     int communityId = Prefs.getCommunityId(getActivity());
                                     if (communityId != 0) {
                                         FilterCommunityForAttachment(attachmentFileResponseModels, communityId);
                                     }
                                 } else {

                                     BaseActivity baseActivity = (BaseActivity)getActivity();
                                     baseActivity.showLogOutDialog();

                                 } checkEmptyLib();

                             } catch (Exception e)

                             {
                                 Log.d("onResponse", "There is an error");
                                 e.printStackTrace();
                             }

                         }

                         @Override
                         public void onFailure(Call<List<FileResponseModel>> call, Throwable t) {
                             Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                             ((MainActivity)getActivity()).hideProgressDialog();
                         }

                     }

        );
    }

    // Method to filter album by search string
    public List<PhotoAlbumResponseModel> SearchFilterForAlbum(List<PhotoAlbumResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<PhotoAlbumResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (PhotoAlbumResponseModel model : models) {
                String Title = model.getTitle();
                String text = "";
                if (Title != null)
                    text = text + Title.toLowerCase();


                if (text != null) {
                    if (text.contains(query)) {
                        filteredModelList.add(model);

                    }
                }

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
                mViewAlbumsRecyclerView.setLayoutManager(layoutManager);
                mAlbumsAdapter = new AlbumsAdapter(getActivity(), filteredModelList);
                mViewAlbumsRecyclerView.setAdapter(mAlbumsAdapter);
                mAlbumsAdapter.notifyDataSetChanged();
                checkEmptyAlbum();
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

    // Method to filter attachment file by search string
    public List<FileResponseModel> SearchFilterForAttachment(List<FileResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<FileResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (FileResponseModel model : models) {
                String Title = model.getTitle();
                String text = "";
                if (Title != null)
                    text = text + Title.toLowerCase();


                if (text != null) {
                    if (text.contains(query)) {
                        filteredModelList.add(model);

                    }
                }

                mAttachmentAdapter = new AttachmentAdapter(getActivity(), filteredModelList);
                mRecyclerViewFiles.setAdapter(mAttachmentAdapter);
                mAttachmentAdapter.notifyDataSetChanged();
                checkEmptyLib();
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

    private List<PhotoAlbumResponseModel> FilterCommunityForAlbum(List<PhotoAlbumResponseModel> models, int Id) {
        photoAlbumResponseModelList = new ArrayList<>();
        try {
            for (PhotoAlbumResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    photoAlbumResponseModelList.add(model);
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
                mViewAlbumsRecyclerView.setLayoutManager(layoutManager);
                mAlbumsAdapter = new AlbumsAdapter(getActivity(), photoAlbumResponseModelList);
                mViewAlbumsRecyclerView.setAdapter(mAlbumsAdapter);
                mAlbumsAdapter.notifyDataSetChanged();
            }
            int Position = Prefs.getPosition(getActivity());

            MainActivity activity = (MainActivity) getActivity();
            if (Position == 3) {
                checkEmptyAlbum();
                if (photoAlbumResponseModelList.size() == 0) {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                    Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoAlbumResponseModelList;
    }

    private List<FileResponseModel> FilterCommunityForAttachment(List<FileResponseModel> models, int Id) {
        attachmentFileResponseModels = new ArrayList<>();
        try {
            for (FileResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    attachmentFileResponseModels.add(model);
                }
                mAttachmentAdapter = new AttachmentAdapter(getActivity(), attachmentFileResponseModels);
                mRecyclerViewFiles.setAdapter(mAttachmentAdapter);
                mAttachmentAdapter.notifyDataSetChanged();
            }
            int Position = Prefs.getPosition(getActivity());
            checkEmptyLib();
            MainActivity activity = (MainActivity) getActivity();
            if (Position == 3) {
                checkEmptyLib();
                if (attachmentFileResponseModels.size() == 0) {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                    Toast.makeText(getActivity(), R.string.no_record_found, Toast.LENGTH_SHORT).show();
                } else {
                    activity.mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return attachmentFileResponseModels;
    }

}