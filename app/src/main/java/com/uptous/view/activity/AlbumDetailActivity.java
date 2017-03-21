package com.uptous.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.AlbumDetailResponseModel;
import com.uptous.view.adapter.AlbumDetailAdapter;

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

    private ImageView mImageViewBack, mImageViewPicture;

    private TextView mTextViewPictureName, mTextViewPictureDate;

    private String mAuthenticationId, mAuthenticationPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        initView();

        getApiAlbumDetail();
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

        //Local Variables
        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        LinearLayout linearLayoutLeftMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);

        LinearLayoutManager layoutManager3
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        //Global Variables
        mRecyclerViewPicture = (RecyclerView) findViewById(R.id.images_album);
        mRecyclerViewPicture.setLayoutManager(layoutManager3);
        mTextViewPictureName = (TextView) findViewById(R.id.text_view_picture_name);
        mTextViewPictureDate = (TextView) findViewById(R.id.text_view_picture_date);
        mImageViewPicture = (ImageView) findViewById(R.id.image_view_album_detail);
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

                try {

                    final List<AlbumDetailResponseModel> eventResponseModels = response.body();
                    mTextViewPictureName.setText(eventResponseModels.get(0).getCaption());
                    long val = eventResponseModels.get(0).getCreateTime();
                    if (val == 0) {
                        mTextViewPictureDate.setVisibility(View.GONE);
                    } else {
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("MMM d, yyyy");
                        df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
//                        SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
//                        dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                        String dateText = df2.format(date);
//                        String dateTime = dfTime.format(date);
                        mTextViewPictureDate.setText(dateText);
                    }

                    Picasso.with(AlbumDetailActivity.this).load(eventResponseModels.get(0).getPhoto()).into(mImageViewPicture);
                    mAlbumDetailAdapter = new AlbumDetailAdapter(AlbumDetailActivity.this, eventResponseModels);
                    mRecyclerViewPicture.setAdapter(mAlbumDetailAdapter);
                    mProgressDialog.dismiss();
                    ItemClickSupport.addTo(mRecyclerViewPicture).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            mTextViewPictureName.setText(eventResponseModels.get(position).getCaption());
                            long val = eventResponseModels.get(position).getCreateTime();
                            Date date = new Date(val);
                            SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
                            String dateText = df2.format(date);
                            mTextViewPictureDate.setText(dateText);
                            Picasso.with(AlbumDetailActivity.this).load(eventResponseModels.get(position).getPhoto()).into(mImageViewPicture);
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
