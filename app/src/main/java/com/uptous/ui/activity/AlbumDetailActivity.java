package com.uptous.ui.activity;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.AlbumDetailResponseModel;
import com.uptous.model.Event;
import com.uptous.model.FeaturedProductResponseModel;
import com.uptous.ui.adapter.AlbumDetailAdapter;
import com.uptous.ui.adapter.AlbumsAdapter;
import com.uptous.ui.adapter.EventListAdapter;
import com.uptous.ui.adapter.FilesAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 1/3/2017.
 */

public class AlbumDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerViewPicture;
    private AlbumDetailAdapter mAlbumDetailAdapter;
    private LinearLayout mLinearLayoutLeftMenu;
    private ImageView mImageViewBack;
    private ImageView mImageViewFilter;
    private TextView mTextViewTitle;
    private TextView mTextViewPictureName;
    private TextView mTextViewPictureDate;
    private ImageView mImageViewPicture;
    private String AuthenticationId, AuthenticationPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        initView();

        getApiAlbumDetail();
    }

    private void initView() {

        mTextViewPictureName = (TextView) findViewById(R.id.text_view_picture_name);
        mTextViewPictureDate = (TextView) findViewById(R.id.text_view_picture_date);
        mImageViewPicture = (ImageView) findViewById(R.id.image_view_album_detail);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_title);
        mLinearLayoutLeftMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mImageViewFilter = (ImageView) findViewById(R.id.image_view_down);

        LinearLayoutManager layoutManager3
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewPicture = (RecyclerView) findViewById(R.id.images_album);
        mRecyclerViewPicture.setLayoutManager(layoutManager3);


        mImageViewFilter.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        mTextViewTitle.setVisibility(View.GONE);
        mLinearLayoutLeftMenu.setVisibility(View.GONE);

        getData();

        clickListenerOnViews();
    }

    private void getData() {
        AuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        AuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
    }


    private void getApiAlbumDetail() {

        int NewsItemId = MyApplication.mSharedPreferences.getInt("NewsItemID", 0);
        final ProgressDialog mProgressDialog = new ProgressDialog(AlbumDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);
        Call<List<AlbumDetailResponseModel>> call = service.GetAlbum(NewsItemId);

        call.enqueue(new Callback<List<AlbumDetailResponseModel>>() {
            @Override
            public void onResponse(Call<List<AlbumDetailResponseModel>> call, Response<List<AlbumDetailResponseModel>> response) {

                try {

                    final List<AlbumDetailResponseModel> eventResponseModels = response.body();
                    mTextViewPictureName.setText(eventResponseModels.get(0).getCaption());
                    long val = eventResponseModels.get(0).getCreateTime();
                    Date date = new Date(val);
                    SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
                    String dateText = df2.format(date);
                    mTextViewPictureDate.setText(dateText);
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
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<AlbumDetailResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(AlbumDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                Log.d("onFailure", t.toString());
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
        }
    }
}
