package com.uptous.view.activity;

import android.content.Context;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uptous.model.AlbumDetailResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.AlbumDetailAdapter;
import com.uptous.view.adapter.ViewPagerAdapter;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.controller.utils.downloads.Permission;
import com.uptous.controller.utils.downloads.DownloadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.uptous.controller.utils.Utilities.isLastAppActivity;

/**
 * FileName : AlbumDetailActivity
 * Description :show all photos of album and also provide user to download option
 * Dependencies : AlbumDetailActivity, BaseActivity, AlbumDetailAdapter, Permission, DownloadTask
 */
public class AlbumDetailActivity extends BaseActivity implements View.OnClickListener {


    private Permission mPermission;
    private RecyclerView mRecyclerViewPicture;

    private AlbumDetailAdapter mAlbumDetailAdapter;

    private ImageView mImageViewBack;

    private ViewPagerAdapter mViewPagerAdapter;

    private ViewPager mViewPager;

    private TextView mTextViewPictureName, mTextViewPictureDate;

    private ImageView mImageViewDownArrow;

    private String mAuthenticationId, mAuthenticationPassword;

    private String ImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        initView();

        if (ConnectionDetector.isConnectingToInternet(AlbumDetailActivity.this)) {
            getApiAlbumDetail();
        } else {
            showToast(getString(R.string.network_error));
        }

    }


    //Method to initialize view
    private void initView() {

        initializePermission();

        //Local Variables Initialization
        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        LinearLayout linearLayoutLeftMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);

        LinearLayoutManager layoutManager3
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        //Global Variables Initialization
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mRecyclerViewPicture = (RecyclerView) findViewById(R.id.images_album);
        mRecyclerViewPicture.setLayoutManager(layoutManager3);
        mTextViewPictureName = (TextView) findViewById(R.id.text_view_picture_name);
        mTextViewPictureDate = (TextView) findViewById(R.id.text_view_picture_date);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mImageViewDownArrow = (ImageView) findViewById(R.id.image_view_down_arrow);


        imageViewFilter.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.GONE);
        linearLayoutLeftMenu.setVisibility(View.GONE);

        getData();

        clickListenerOnViews();
    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mImageViewDownArrow.setOnClickListener(this);
    }

    // Get webservice to get all photos of album
    private void getApiAlbumDetail() {

        int NewsItemId = Prefs.getNewsItemId(this);
        showProgressDialog();
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<AlbumDetailResponseModel>> call = service.GetAlbum(NewsItemId);

        call.enqueue(new Callback<List<AlbumDetailResponseModel>>() {
            @Override
            public void onResponse(Call<List<AlbumDetailResponseModel>> call, Response<List<AlbumDetailResponseModel>> response) {
                hideProgressDialog();
                try {

                    final List<AlbumDetailResponseModel> eventResponseModels = response.body();
                    String ImageName = eventResponseModels.get(0).getCaption().replace("\n", " ");
                    if (eventResponseModels != null && eventResponseModels.size() > 0) {

                        long val = eventResponseModels.get(0).getCreateTime();
                        ImageUrl = eventResponseModels.get(0).getPhoto();

                        if (val == 0) {
                            mTextViewPictureDate.setVisibility(View.GONE);
                        } else {
                            Date date = new Date(val);
                            SimpleDateFormat df2 = new SimpleDateFormat("MMM d, yyyy");
                            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            String dateText = df2.format(date);
                            mTextViewPictureDate.setText(dateText);
                        }
                        mAlbumDetailAdapter = new AlbumDetailAdapter(AlbumDetailActivity.this, eventResponseModels);
                        mRecyclerViewPicture.setAdapter(mAlbumDetailAdapter);

                        // Pass results to ViewPagerAdapter Class
                        mViewPagerAdapter = new ViewPagerAdapter(AlbumDetailActivity.this, eventResponseModels);
                        // Binds the Adapter to the ViewPager
                        mViewPager.setAdapter(mViewPagerAdapter);
                        mTextViewPictureName.setText(ImageName);

                        ItemClickSupport.addTo(mRecyclerViewPicture).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                mTextViewPictureName.setText(eventResponseModels.get(position).getCaption().replace("\n", " "));
                                mViewPager.setCurrentItem(position);
                                long val = eventResponseModels.get(position).getCreateTime();
                                ImageUrl = eventResponseModels.get(position).getPhoto();
                                if (val == 0) {
                                    mTextViewPictureDate.setVisibility(View.GONE);
                                } else {
                                    Date date = new Date(val);
                                    SimpleDateFormat df2 = new SimpleDateFormat("MMM d, yyyy");
                                    df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                                    String dateText = df2.format(date);
                                    mTextViewPictureDate.setText(dateText);
                                }
                            }
                        });
                        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                                mRecyclerViewPicture.getLayoutManager().scrollToPosition(position);
                                mRecyclerViewPicture.smoothScrollToPosition(position);
                                mTextViewPictureName.setText(eventResponseModels.get(position).getCaption().replace("\n", " "));

                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });


                    } else {
                        mImageViewDownArrow.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    hideProgressDialog();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<AlbumDetailResponseModel>> call, Throwable t) {
                hideProgressDialog();
                showToast(getString(R.string.error));
            }

        });
    }





    /* check mobile device version method */
    private void checkDeviceVersionOfImage(String url, View view) {

        //check internet connection
        if (ConnectionDetector.isConnectingToInternet(AlbumDetailActivity.this)) {

            /* check Version (Marshmallow or above [23 or above]) */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                // check storage permission
                if (checkPermission(AlbumDetailActivity.this)) {
                    //validate image url
                    validateImageUrl(url, view);
                } else {
                    showToast(getString(R.string.please_give_permission));
                }
            } else {
                //validate image url
                validateImageUrl(url, view);
            }

        } else {
            showToast(getString(R.string.network_error));
        }
    }

    /* check Image url is empty or null  method */
    private void validateImageUrl(String anImageUrl, View view) {
        if (!TextUtils.isEmpty(anImageUrl))
            imageDownloadApiCall(anImageUrl, view);
        else {
            showToast(
                    getString(R.string.image_url_not_found));
        }
    }

    /* call download api */
    private void imageDownloadApiCall(final String imageUrl, final View view) {

        //show progress dialog
        showProgressDialog();

        /* call DownloadTask method for downloading the image. */
        new DownloadTask(AlbumDetailActivity.this,
                imageUrl,
                /*we are using lambda function*/
                status ->
                        new Handler().postDelayed(() -> {
                            //dismiss the progress dialog
                            hideProgressDialog();

                            //check image downloading status
                            if (status) {
                                Snackbar snackbar = Snackbar
                                        .make(view, R.string.photo_save_successfully, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(view, R.string.some_thing_went_wrong, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }, 1000));

    }

    /* checking location permission */
    private boolean checkPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

    }

    /* initialize permission */
    private void initializePermission() {
        // initialize permission
        mPermission = new Permission(AlbumDetailActivity.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermission.requestPermission();
        }
    }


    /* implement onclick method of View.OnClickListener */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                onBackPressed();
                break;

            case R.id.image_view_down_arrow:

                //call for image download
                checkDeviceVersionOfImage(ImageUrl, view);
                break;
        }
    }


    /* permission check default method */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permission.PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allGranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allGranted = true;
                } else {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted)
                mPermission.stillNotHavePermissions();
        }
    }

   /* override onBack press */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isLastAppActivity(this))
            startActivity(new Intent(this, MainActivity.class));
        finish();
    }


}
