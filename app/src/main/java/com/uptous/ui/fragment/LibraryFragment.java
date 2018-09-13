package com.uptous.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.FileResponseModel;
import com.uptous.model.PhotoAlbumResponseModel;
import com.uptous.ui.activity.AlbumDetailActivity;
import com.uptous.ui.activity.MainActivity;
import com.uptous.ui.activity.WebviewActivity;
import com.uptous.ui.adapter.AlbumsAdapter;
import com.uptous.R;
import com.uptous.ui.adapter.AttachmentAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * FileName : LibraryFragment
 * Description :
 * Dependencies :
 */
public class LibraryFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener {

    private RecyclerView mViewAlbumsRecyclerView;
    private RecyclerView mRecyclerViewFiles;
    private String mCommunityName, mAuthenticationId, mAuthenticationPassword;
    private int mCommunityId;
    private AlbumsAdapter mAlbumsAdapter;
    private AttachmentAdapter mAttachmentAdapter;
    private TextView mTextViewAlbums, mTextViewFiles, mTextViewHeading, mTextViewCancel, mTextViewSearch;
    private List<PhotoAlbumResponseModel> mPhotoAlbumResponseModelList = new ArrayList<>();

    private List<PhotoAlbumResponseModel> mAlbumFilteredModelList = new ArrayList<>();
    private List<FileResponseModel> mAttachmentFileResponseModels = new ArrayList<>();
    private List<FileResponseModel> mFileFilterResponseModels = new ArrayList<>();
    private List<PhotoAlbumResponseModel> mFilterAlbum = new ArrayList<>();
    private List<FileResponseModel> mFilterFiles = new ArrayList<>();

    public static SearchView SearchView;

    protected ContactFragment contactFragment;
    protected SignUpFragment signUpFragment;
    protected HomeFragment homeFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        initView(view);

//        if (ConnectionDetector.isConnectingToInternet(getActivity())) {
//            MyApplication.editor.putString("Files", null);
//            MyApplication.editor.commit();
//            getApiAlbumList();
//        } else {
//            Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
//        }

        return view;
    }

    private void initView(View view) {
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
        clickListenerOnViews();

        handleSearchView();

        getData();
    }

    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    private void clickListenerOnViews() {
        mTextViewCancel.setOnClickListener(this);
        SearchView.setOnQueryTextListener(this);
        mTextViewAlbums.setOnClickListener(this);
        mTextViewFiles.setOnClickListener(this);
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
            if (signUpFragment != null) {
                SignUpFragment.SearchView.clearFocus();
                SignUpFragment.SearchView.onActionViewCollapsed();
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

    private void getApiAlbumList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<PhotoAlbumResponseModel>> call = service.GetAllAlbumPhoto();

        call.enqueue(new Callback<List<PhotoAlbumResponseModel>>() {
            @Override
            public void onResponse(Call<List<PhotoAlbumResponseModel>> call, Response<List<PhotoAlbumResponseModel>> response) {
                try {
                    progressDialog.dismiss();
                    MyApplication.editor.putString("Files", null);
                    MyApplication.editor.commit();

                    mCommunityName = MyApplication.mSharedPreferences.getString("CommunityName", null);
                    mCommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);

                    if (mCommunityId != 0) {
                        mPhotoAlbumResponseModelList = response.body();

                        mAlbumsAdapter = new AlbumsAdapter(getActivity(), mPhotoAlbumResponseModelList);
                        mViewAlbumsRecyclerView.setAdapter(mAlbumsAdapter);
                        mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                        mRecyclerViewFiles.setVisibility(View.GONE);
                        MainActivity.mTextViewTitle.setText(mCommunityName);
                        MyApplication.editor.putString("CommunityFilter", "communityfilter");
                        MyApplication.editor.commit();
                        mFilterAlbum = FilterCommunity(mPhotoAlbumResponseModelList, mCommunityId);
                    } else {
                        if (mCommunityId == 0) {
                            MyApplication.editor.putString("CommunityFilter", null);
                            MyApplication.editor.commit();
                            MainActivity.mTextViewTitle.setText("All");
                            mPhotoAlbumResponseModelList = response.body();
                            mAlbumsAdapter = new AlbumsAdapter(getActivity(), mPhotoAlbumResponseModelList);
                            mViewAlbumsRecyclerView.setAdapter(mAlbumsAdapter);
                            mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                            mRecyclerViewFiles.setVisibility(View.GONE);
                        }

                    }

                    ItemClickSupport.addTo(mViewAlbumsRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {


                            if (mAlbumFilteredModelList.size() == 0) {
                                int AlbumID = mPhotoAlbumResponseModelList.get(position).getId();
                                MyApplication.editor.putInt("NewsItemID", AlbumID);
                                MyApplication.editor.commit();
                                Intent intent = new Intent(getActivity(), AlbumDetailActivity.class);
                                startActivity(intent);
                            } else {
                                int OpId = mAlbumFilteredModelList.get(position).getId();
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


    private void getApiFileList() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<FileResponseModel>> call = service.GetAttachment();

        call.enqueue(new Callback<List<FileResponseModel>>() {
            @Override
            public void onResponse(Call<List<FileResponseModel>> call, Response<List<FileResponseModel>> response) {
                try {
                    progressDialog.dismiss();


                    mCommunityName = MyApplication.mSharedPreferences.getString("CommunityName", null);
                    mCommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);

                    if (mCommunityId != 0) {
                        mTextViewHeading.setVisibility(View.VISIBLE);
                        mAttachmentFileResponseModels = response.body();
                        mAttachmentAdapter = new AttachmentAdapter(getActivity(), mAttachmentFileResponseModels);
                        mRecyclerViewFiles.setAdapter(mAttachmentAdapter);
                        mRecyclerViewFiles.setVisibility(View.VISIBLE);

                        MainActivity.mTextViewTitle.setText(mCommunityName);
                        MyApplication.editor.putString("CommunityFilter", "communityfilter");
                        MyApplication.editor.commit();
                        mFilterFiles = FilterCommunityForFiles(mAttachmentFileResponseModels, mCommunityId);
                    } else {
                        if (mCommunityId == 0) {
                            MyApplication.editor.putString("CommunityFilter", null);
                            MyApplication.editor.commit();
                            MainActivity.mTextViewTitle.setText("All");
                            mTextViewHeading.setVisibility(View.VISIBLE);
                            mAttachmentFileResponseModels = response.body();
                            mAttachmentAdapter = new AttachmentAdapter(getActivity(), mAttachmentFileResponseModels);
                            mRecyclerViewFiles.setAdapter(mAttachmentAdapter);
                            mRecyclerViewFiles.setVisibility(View.VISIBLE);
                        }

                    }

                    ItemClickSupport.addTo(mRecyclerViewFiles).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                            if (mFileFilterResponseModels.size() == 0) {
                                String path = mAttachmentFileResponseModels.get(position).getPath();
                                String s = mAttachmentFileResponseModels.get(position).getTitle();
                                String result = s.substring(s.lastIndexOf(".") + 1);

                                if (result.equalsIgnoreCase("jpeg")) {
                                    MyApplication.editor.putString("Imagepath", path);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), WebviewActivity.class);
                                    startActivity(intent);
                                } else if (result.equalsIgnoreCase("jpg")) {
                                    MyApplication.editor.putString("Imagepath", path);

                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), WebviewActivity.class);
                                    startActivity(intent);
                                } else if (result.equalsIgnoreCase("MOV")) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(path), "video/*");
                                    startActivity(intent);
                                } else if (result.equalsIgnoreCase("mp3")) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(path), "audio/*");
                                    startActivity(intent);
                                } else {
                                    MyApplication.editor.putString("path", path);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), WebviewActivity.class);
                                    startActivity(intent);
                                }
                            } else {
                                String path = mFileFilterResponseModels.get(position).getPath();
                                String s = mFileFilterResponseModels.get(position).getTitle();
                                String result = s.substring(s.lastIndexOf(".") + 1);

                                if (result.equalsIgnoreCase("jpeg")) {
//                                MyApplication.editor.putString("Imagepath", path);
//                                MyApplication.editor.commit();
//                                Intent intent = new Intent(getActivity(), WebviewActivity.class);
//                                startActivity(intent);
                                } else if (result.equalsIgnoreCase("jpg")) {
//                                MyApplication.editor.putString("Imagepath", path);
//
//                                MyApplication.editor.commit();
//                                Intent intent = new Intent(getActivity(), WebviewActivity.class);
//                                startActivity(intent);
                                } else if (result.equalsIgnoreCase("MOV")) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(path), "video/*");
                                    startActivity(intent);
                                } else if (result.equalsIgnoreCase("mp3")) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(path), "audio/*");
                                    startActivity(intent);
                                } else {
                                    MyApplication.editor.putString("path", path);
                                    MyApplication.editor.commit();
                                    Intent intent = new Intent(getActivity(), WebviewActivity.class);
                                    startActivity(intent);
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
            public void onFailure(Call<List<FileResponseModel>> call, Throwable t) {
                Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
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
                getApiFileList();
                break;
            case R.id.text_view_cancel:
                SearchView.clearFocus();
                SearchView.onActionViewCollapsed();
                break;
        }

    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String CommunityFilter = MyApplication.mSharedPreferences.getString("CommunityFilter", null);
        if (CommunityFilter != null) {
            mAlbumFilteredModelList = filter(mFilterAlbum, newText);
        } else {
            mAlbumFilteredModelList = filter(mPhotoAlbumResponseModelList, newText);
            mFileFilterResponseModels = filterAttachmentFiles(mAttachmentFileResponseModels, newText);
        }
//        AlbumfilteredModelList = filter(mPhotoAlbumResponseModelList, newText);


        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
        SearchView.clearFocus();
        SearchView.onActionViewCollapsed();
        String Files = MyApplication.mSharedPreferences.getString("Files", null);
        String Album = MyApplication.mSharedPreferences.getString("Album", null);
//        if (Files != null) {
//            getApiFileList();
//        }
//        if (Album != null) {
//            getApiAlbumList();
//        }

//

    }

    private List<PhotoAlbumResponseModel> filter(List<PhotoAlbumResponseModel> models, String query) {
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


    private List<FileResponseModel> filterAttachmentFiles(List<FileResponseModel> models, String query) {
        query = query.toLowerCase();

        final List<FileResponseModel> filteredModelList = new ArrayList<>();
        try {
            for (FileResponseModel model : models) {
                final String text = model.getTitle().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
                mAttachmentAdapter = new AttachmentAdapter(getActivity(), filteredModelList);
                mRecyclerViewFiles.setAdapter(mAttachmentAdapter);
                mAttachmentAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filteredModelList;
    }

    private List<PhotoAlbumResponseModel> FilterCommunity(List<PhotoAlbumResponseModel> models, int Id) {
        mFilterAlbum = new ArrayList<>();
        try {
            for (PhotoAlbumResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    mFilterAlbum.add(model);
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
                mViewAlbumsRecyclerView.setLayoutManager(layoutManager);
                mAlbumsAdapter = new AlbumsAdapter(getActivity(), mFilterAlbum);
                mViewAlbumsRecyclerView.setAdapter(mAlbumsAdapter);
                mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerViewFiles.setVisibility(View.GONE);
                mAlbumsAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFilterAlbum;
    }

    private List<FileResponseModel> FilterCommunityForFiles(List<FileResponseModel> models, int Id) {
        mFilterFiles = new ArrayList<>();
        try {
            for (FileResponseModel model : models) {
                final int CommunityID = model.getCommunityId();
                if (CommunityID == Id) {
                    mFilterFiles.add(model);
                }
                mAttachmentAdapter = new AttachmentAdapter(getActivity(), mFilterFiles);
                mRecyclerViewFiles.setAdapter(mAttachmentAdapter);
                mAttachmentAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFilterFiles;
    }
}