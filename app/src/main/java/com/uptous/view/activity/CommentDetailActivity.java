package com.uptous.view.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.model.GetAllAnnouncementResponseModel;
import com.uptous.model.GetAllCommentResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.AnnouncementsListAdapter;
import com.uptous.view.adapter.CommentListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : CommentDetailActivity
 * Description :User can comment any feed
 * Dependencies : CommentListAdapter, AnnouncementsListAdapter
 */

public class CommentDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImageViewBack, mViewImageView, mImageViewNewsItem;

    private LinearLayout mLinearLayoutRoundedBackGround;
    private RecyclerView mRecyclerViewComment;

    private CommentListAdapter mCommentListAdapter;
    private AnnouncementsListAdapter mAnnouncementsListAdapter;

    private EditText mEditTextComment;

    private TextView mTextViewFirstName, mTextViewLastName, mTextViewTitle, mViewDateTextView,
            mViewNameTextView, mTextViewCommunityName, mTextViewOwnerName, mTextViewNewsDetail, mTextViewSendComment;

    private String mOwnerImage, mNewsItemImage, mDate, mNewsName, mOwnerName, mCommunityName, mNewsType,
            mNewsItemDescription, mComment, mAuthenticationId, mAuthenticationPassword;

    private Helper mHelper;
    private ScrollView mScrollView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_detail);

        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_send_comment:
                mHelper.keyBoardHidden(CommentDetailActivity.this);

                mComment = mEditTextComment.getText().toString().trim().replace("\n", "<br>");

                if (ConnectionDetector.isConnectingToInternet(this)) {

                    if (mComment.length() > 0) {

                        if (mNewsType.equalsIgnoreCase("Announcement") || mNewsType.equalsIgnoreCase("Private Threads")) {
                            postApiAnnouncementReply();
                        } else {
                            postApiComment();
                        }

                    } else {
                        showToast("Please enter comment");
                    }

                } else {
                    showToast(getString(R.string.network_error));
                }

                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Prefs.setCommunityId(this, 0);
    }

    //Method to initialize view
    private void initView() {
        mHelper = new Helper();

        //Local Variables Initialization
        TextView textViewFilterText = (TextView) findViewById(R.id.text_view_title);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        LinearLayout linearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Global Variables Initialization
        mRecyclerViewComment = (RecyclerView) findViewById(R.id.recycler_view_comment);
        mRecyclerViewComment.setLayoutManager(layoutManager);

        mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view_des);
        mLinearLayoutRoundedBackGround = (LinearLayout) findViewById(R.id.layout_contact);

        mImageViewNewsItem = (ImageView) findViewById(R.id.image_view_news_item);
        mViewImageView = (ImageView) findViewById(R.id.image_view_user);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mTextViewFirstName = (TextView) findViewById(R.id.textview_first_name);
        mTextViewLastName = (TextView) findViewById(R.id.textview_last_name);
        mViewDateTextView = (TextView) findViewById(R.id.text_view_date);
        mViewNameTextView = (TextView) findViewById(R.id.text_view_news_name);
        mTextViewSendComment = (TextView) findViewById(R.id.text_view_send_comment);
        mTextViewCommunityName = (TextView) findViewById(R.id.text_view_community_name);
        mTextViewNewsDetail = (TextView) findViewById(R.id.text_view_news_detail);
        mTextViewOwnerName = (TextView) findViewById(R.id.text_view_owner_name);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_message_toolbar);

        linearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        mTextViewTitle.setVisibility(View.VISIBLE);
        imageViewFilter.setVisibility(View.GONE);
        textViewFilterText.setVisibility(View.GONE);
        textViewFilterText.setVisibility(View.GONE);

        getData();


        if (ConnectionDetector.isConnectingToInternet(this)) {

            if (mNewsType.equalsIgnoreCase("Announcement") || mNewsType.equalsIgnoreCase("Private Threads")) {
                getApiAllAnnouncementComment();
            } else {
                getApiAllComment();
            }


        } else {
            Toast.makeText(CommentDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        clickListenerOnViews();

    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword = Prefs.getAuthenticationPassword(this);

        mNewsType = Prefs.getNewsType(this);
        mOwnerImage = Prefs.getImage(this);
        mNewsItemImage = Prefs.getimageNewsItem(this);
        mDate = Prefs.getDate(this);
        mNewsName = Prefs.getNewsItemName(this);
        mOwnerName = Prefs.getOwnerName(this);
        mCommunityName = Prefs.getFeedCommunityName(this);
        mNewsItemDescription = Prefs.getNewsItemDescription(this);

        setData();
    }

    // Method to setData on views
    private void setData() {
        if (mNewsItemImage != null && !mNewsItemImage.equalsIgnoreCase("")) {
            mImageViewNewsItem.setVisibility(View.VISIBLE);
            mTextViewNewsDetail.setVisibility(View.GONE);
            mScrollView.setVisibility(View.GONE);

            Glide.with(CommentDetailActivity.this).load(mNewsItemImage)
                    .into(mImageViewNewsItem);
        } else {
            mImageViewNewsItem.setVisibility(View.GONE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTextViewNewsDetail.setText(Html.fromHtml(mNewsItemDescription, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTextViewNewsDetail.setText(Html.fromHtml(mNewsItemDescription));
            }

        }


        if (mCommunityName != null) {
            mTextViewOwnerName.setText(mOwnerName + ": ");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTextViewCommunityName.setText(Html.fromHtml("<u>" + mCommunityName + "</u> ", Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTextViewCommunityName.setText(Html.fromHtml("<u>" + mCommunityName + "</u> "));
            }

        } else {
            mTextViewOwnerName.setText(mOwnerName);
            mTextViewCommunityName.setVisibility(View.GONE);
        }


        int i = mOwnerName.indexOf(" ");
        String OwnerNAME = mOwnerName.substring(0, i);
        mTextViewTitle.setText(OwnerNAME + " message");
        mViewNameTextView.setText(mNewsName);
        mViewDateTextView.setText(mDate);
        String result1 = mOwnerImage.substring(mOwnerImage.lastIndexOf(".") + 1);
        if (mOwnerImage != null && !result1.equalsIgnoreCase("gif")) {
            mViewImageView.setVisibility(View.VISIBLE);

            Glide.with(CommentDetailActivity.this).load(mOwnerImage)
                    .into(mViewImageView);
        } else {
            String BackgroundColor = Prefs.getOwnerBackground(this);
            String TextColor = Prefs.getOwnerTextColor(this);
            if (BackgroundColor != null) {
                int color = Color.parseColor(BackgroundColor);
                mLinearLayoutRoundedBackGround.setVisibility(View.VISIBLE);
                mLinearLayoutRoundedBackGround.setBackgroundResource(R.drawable.circle);
                GradientDrawable gd = (GradientDrawable)
                        mLinearLayoutRoundedBackGround.getBackground().getCurrent();
                gd.setColor(color);
                gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});


                int colorTextView = Color.parseColor(TextColor);
                String resultLastName = mOwnerName.substring(mOwnerName.lastIndexOf(' ') + 1).trim();

                mTextViewFirstName.setText(mOwnerName);
                mTextViewFirstName.setTextColor(colorTextView);
                mTextViewLastName.setText(resultLastName);
                mTextViewLastName.setTextColor(colorTextView);


            }

        }

    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mTextViewSendComment.setOnClickListener(this);
    }

    // Get webservice to get all comments
    private void getApiAllComment() {
        mEditTextComment.setHint("Type comments here..");
        showProgressDialog();

        int OpId = Prefs.getFeedId(this);

        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<GetAllCommentResponseModel>> call = service.GetAllComment(OpId);

        call.enqueue(new Callback<List<GetAllCommentResponseModel>>() {
            @Override
            public void onResponse(Call<List<GetAllCommentResponseModel>> call, Response<List<GetAllCommentResponseModel>> response) {
                hideProgressDialog();
                try {

                    final List<GetAllCommentResponseModel> eventResponseModels = response.body();

                    mCommentListAdapter = new CommentListAdapter(CommentDetailActivity.this, eventResponseModels);
                    mRecyclerViewComment.setAdapter(mCommentListAdapter);

                    ItemClickSupport.addTo(mRecyclerViewComment).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {


                        }
                    });


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<GetAllCommentResponseModel>> call, Throwable t) {
                hideProgressDialog();
                showToast(getString(R.string.error));
            }

        });
    }

    // Get webservice to get all Announcement comments
    private void getApiAllAnnouncementComment() {
        mEditTextComment.setHint("Reply all...");
        showProgressDialog();

        int OpId = Prefs.getNewsItemId(this);

        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<GetAllAnnouncementResponseModel>> call = service.GetAllAnnouncementComment(OpId);

        call.enqueue(new Callback<List<GetAllAnnouncementResponseModel>>() {
            @Override
            public void onResponse(Call<List<GetAllAnnouncementResponseModel>> call, Response<List<GetAllAnnouncementResponseModel>> response) {
                hideProgressDialog();
                try {

                    final List<GetAllAnnouncementResponseModel> eventResponseModels = response.body();

                    mAnnouncementsListAdapter = new AnnouncementsListAdapter(CommentDetailActivity.this, eventResponseModels);
                    mRecyclerViewComment.setAdapter(mAnnouncementsListAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<GetAllAnnouncementResponseModel>> call, Throwable t) {
                hideProgressDialog();
                showToast(getString(R.string.error));
            }

        });
    }

    // Post webservice to post all  comments
    private void postApiComment() {
        showProgressDialog();


        int OpId = Prefs.getFeedId(this);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.PostComment(OpId, mComment);
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                hideProgressDialog();
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.isSuccessful()) {

                            getApiAllComment();
                            Prefs.setMessagePost(CommentDetailActivity.this, "message");
                            mEditTextComment.setText("");
                            mEditTextComment.clearFocus();
                        }
                    }
                } else {
                    showToast("Couldn't comment");
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                hideProgressDialog();
                showToast(getString(R.string.error));


            }
        });
    }

    // Post webservice to post all Announcement reply
    private void postApiAnnouncementReply() {

        int NewsItem = Prefs.getNewsItemId(this);
        showProgressDialog();

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.PostReplyAll(NewsItem, mComment);
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                hideProgressDialog();
                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        getApiAllAnnouncementComment();
                       Prefs.setMessagePost(CommentDetailActivity.this,"message");
                        mEditTextComment.setText("");
                        mEditTextComment.clearFocus();


                    }
                }
            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                showToast(getString(R.string.error));
                hideProgressDialog();


            }
        });
    }

}
