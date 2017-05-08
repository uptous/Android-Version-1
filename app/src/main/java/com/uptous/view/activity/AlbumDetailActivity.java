package com.uptous.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.AlbumDetailResponseModel;
import com.uptous.view.adapter.AlbumDetailAdapter;
import com.uptous.view.adapter.ViewPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : AlbumDetailActivity
 * Description :show all photos of album
 * Dependencies : AlbumDetailActivity, AlbumDetailAdapter
 */
public class AlbumDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerViewPicture;

    private AlbumDetailAdapter mAlbumDetailAdapter;

    private ImageView mImageViewBack;

    private ViewPagerAdapter mViewPagerAdapter;

    private ViewPager mViewPager;

    private TextView mTextViewPictureName, mTextViewPictureDate;

    private String mAuthenticationId, mAuthenticationPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        initView();

        if (ConnectionDetector.isConnectingToInternet(AlbumDetailActivity.this)) {
            getApiAlbumDetail();
        } else {
            Toast.makeText(AlbumDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
        }
    }

    //Method to initialize view
    private void initView() {

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

        imageViewFilter.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.GONE);
        linearLayoutLeftMenu.setVisibility(View.GONE);

        getData();

        clickListenerOnViews();
    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
    }

    // Get webservice to get all photos of album
    private void getApiAlbumDetail() {

        int NewsItemId = MyApplication.mSharedPreferences.getInt("NewsItemID", 0);
        final ProgressDialog mProgressDialog = new ProgressDialog(AlbumDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<AlbumDetailResponseModel>> call = service.GetAlbum(NewsItemId);

        call.enqueue(new Callback<List<AlbumDetailResponseModel>>() {
            @Override
            public void onResponse(Call<List<AlbumDetailResponseModel>> call, Response<List<AlbumDetailResponseModel>> response) {
                mProgressDialog.dismiss();
                try {

                    final List<AlbumDetailResponseModel> eventResponseModels = response.body();
                    String ImageName = eventResponseModels.get(0).getCaption().replace("\n"," ");


                    long val = eventResponseModels.get(0).getCreateTime();
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
                    mProgressDialog.dismiss();
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
                            mTextViewPictureName.setText(eventResponseModels.get(position).getCaption().replace("\n"," "));

                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                } catch (Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<AlbumDetailResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AlbumDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }

}
