package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.ItemClickSupport;
import com.uptous.controller.utils.RoundedImageView;
import com.uptous.model.GetAllCommentResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.ui.adapter.CommentListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 1/4/2017.
 */

public class CommentDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageViewBack;
    private LinearLayout mLinearLayoutNavigation;
    private TextView mTextViewTitle;
    private TextView mTextViewFilterText;
    private RecyclerView mRecyclerViewComment;
    private CommentListAdapter mCommentListAdapter;

    private TextView mViewDescriptionTextView;
    private ImageView mViewImageView;
    private TextView mViewDateTextView;
    private RelativeLayout mDateRelativeLayout;
    private TextView mViewNameTextView;
    private TextView mTextViewCommunityName;
    private TextView mTextViewOwnerName;
    private TextView mTextViewNewsDetail;
    private RoundedImageView mViewUserRoundedImageView;
    private RelativeLayout mMainRelativeLayout;
    private RecyclerView mViewCommentRecyclerView;
    private LinearLayout mCommentLinearLayout;
    private TextView mTextViewReply;
    private LinearLayout mLinearLayoutReply;
    private EditText mEditTextComment;
    private TextView mTextViewSendComment;
    private ImageView mImageViewFilter;
    private String OwnerImage, NewsItemImage, Date, NewsName, OwnerName, CommunityName, NewsType,
            NewsItemDescription;
    private ImageView mImageViewNewsItem;
    private String Comment;
    private String AuthenticationId, AuthenticationPassword;
    private LinearLayout mLinearLayoutRoundedBackGround;
    private TextView mTextViewFirstName, mTextViewLastName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_detail);

        initView();

    }

    private void initView() {
        mTextViewFirstName = (TextView) findViewById(R.id.textview_first_name);
        mTextViewLastName = (TextView) findViewById(R.id.textview_last_name);
        mLinearLayoutRoundedBackGround = (LinearLayout) findViewById(R.id.layout_contact);
        mLinearLayoutReply = (LinearLayout) findViewById(R.id.layout_reply);
        mTextViewReply = (TextView) findViewById(R.id.text_view_reply);
        mImageViewNewsItem = (ImageView) findViewById(R.id.image_view_news_item);
        mEditTextComment = (EditText) findViewById(R.id.edit_text_comment);
        mViewImageView = (ImageView) findViewById(R.id.image_view_user);
        mViewDescriptionTextView = (TextView) findViewById(R.id.text_view_description);
        mViewDateTextView = (TextView) findViewById(R.id.text_view_date);
        mViewNameTextView = (TextView) findViewById(R.id.text_view_news_name);
        mTextViewSendComment = (TextView) findViewById(R.id.text_view_send_comment);
        mTextViewFilterText = (TextView) findViewById(R.id.text_view_title);
        mTextViewCommunityName = (TextView) findViewById(R.id.text_view_community_name);
        mTextViewNewsDetail = (TextView) findViewById(R.id.text_view_news_detail);
        mTextViewOwnerName = (TextView) findViewById(R.id.text_view_owner_name);
        mImageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mTextViewFilterText = (TextView) findViewById(R.id.text_view_title);
        mLinearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_message_toolbar);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewComment = (RecyclerView) findViewById(R.id.recycler_view_comment);
        mRecyclerViewComment.setLayoutManager(layoutManager);


        mLinearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        mTextViewTitle.setVisibility(View.VISIBLE);
        mImageViewFilter.setVisibility(View.GONE);
        mTextViewFilterText.setVisibility(View.GONE);
        mTextViewFilterText.setVisibility(View.GONE);

        getData();

        setData();

        if (ConnectionDetector.isConnectingToInternet(this)) {
            getApiAllComment();
        } else {
            Toast.makeText(CommentDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
        clickListenerOnViews();

    }

    private void getData() {
        AuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        AuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        NewsType = MyApplication.mSharedPreferences.getString("NewsType", null);
        OwnerImage = MyApplication.mSharedPreferences.getString("Image", null);
        NewsItemImage = MyApplication.mSharedPreferences.getString("ImageNewsItem", null);
        Date = MyApplication.mSharedPreferences.getString("Date", null);
        NewsName = MyApplication.mSharedPreferences.getString("NewsItemName", null);
        OwnerName = MyApplication.mSharedPreferences.getString("OwnerName", null);
        CommunityName = MyApplication.mSharedPreferences.getString("CommunityName", null);
        NewsItemDescription = MyApplication.mSharedPreferences.getString("NewsItemDescription", null);
    }

    private void setData() {
        if (NewsItemImage != null && !NewsItemImage.equalsIgnoreCase("")) {
            mImageViewNewsItem.setVisibility(View.VISIBLE);
            mTextViewNewsDetail.setVisibility(View.GONE);
            Picasso.with(CommentDetailActivity.this).load(NewsItemImage).into(mImageViewNewsItem);
        } else {
            mImageViewNewsItem.setVisibility(View.INVISIBLE);
            mTextViewNewsDetail.setText(NewsItemDescription);
        }

//        if (NewsType.equalsIgnoreCase("Announcement")) {
//            mLinearLayoutReply.setVisibility(View.VISIBLE);
//            int i = OwnerName.indexOf(" ");
//            String OwnerNAME = OwnerName.substring(0, i);
//            mTextViewReply.setText("Reply to" + " " + OwnerNAME);
//        }
        mTextViewOwnerName.setText(OwnerName + ": ");
        mTextViewCommunityName.setText(Html.fromHtml("<u>" + CommunityName + "</u> "));

        int i = OwnerName.indexOf(" ");
        String OwnerNAME = OwnerName.substring(0, i);
        mTextViewTitle.setText(OwnerNAME + " Message");
//                    mViewDescriptionTextView.setText(eventResponseModels.get(0).getOwnerName());
        mViewNameTextView.setText(NewsName);

        String result1 = OwnerImage.substring(OwnerImage.lastIndexOf(".") + 1);
        if (OwnerImage != null && !result1.equalsIgnoreCase("gif")) {
            mViewImageView.setVisibility(View.VISIBLE);
            Picasso.with(CommentDetailActivity.this).load(OwnerImage).into(mViewImageView);
        } else {
            String BackgroundColor = MyApplication.mSharedPreferences.getString("OwnerBackground", null);
            String TextColor = MyApplication.mSharedPreferences.getString("OwnerTextColor", null);
            if (BackgroundColor != null) {
                int color = Color.parseColor(BackgroundColor);
                mLinearLayoutRoundedBackGround.setVisibility(View.VISIBLE);
                mLinearLayoutRoundedBackGround.setBackgroundResource(R.drawable.circle);
                GradientDrawable gd = (GradientDrawable)
                        mLinearLayoutRoundedBackGround.getBackground().getCurrent();
                gd.setColor(color);
                gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});


                int colorTextView = Color.parseColor(TextColor);
                String resultLastName = OwnerNAME.substring(OwnerNAME.lastIndexOf(' ') + 1).trim();

                mTextViewFirstName.setText(OwnerNAME);
                mTextViewFirstName.setTextColor(colorTextView);
                mTextViewLastName.setText(resultLastName);
                mTextViewLastName.setTextColor(colorTextView);


            }

        }


        mViewDateTextView.setText(Date);
    }

    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
        mTextViewSendComment.setOnClickListener(this);
    }


    private void getApiAllComment() {

        final ProgressDialog progressDialog = new ProgressDialog(CommentDetailActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);

        int OpId = MyApplication.mSharedPreferences.getInt("FeedId", 0);

        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);
        Call<List<GetAllCommentResponseModel>> call = service.GetAllComment(OpId);

        call.enqueue(new Callback<List<GetAllCommentResponseModel>>() {
            @Override
            public void onResponse(Call<List<GetAllCommentResponseModel>> call, Response<List<GetAllCommentResponseModel>> response) {
                progressDialog.dismiss();
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
                progressDialog.dismiss();
                Toast.makeText(CommentDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }


    private void postApiComment() {
        final ProgressDialog mProgressDialog = new ProgressDialog(CommentDetailActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        int OpId = MyApplication.mSharedPreferences.getInt("FeedId", 0);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);

        Call<PostCommentResponseModel> call = service.PostComment(OpId, Comment);
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.isSuccessful()) {

                            getApiAllComment();

                            mEditTextComment.setText("");
                            mEditTextComment.clearFocus();
                        }
                    } else {

                    }
                } else {
                    Toast.makeText(CommentDetailActivity.this, "Couldn't comment", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                Toast.makeText(CommentDetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
            case R.id.text_view_send_comment:

                Comment = mEditTextComment.getText().toString();

                if (ConnectionDetector.isConnectingToInternet(this)) {

                    if (Comment.length() > 0) {
                        postApiComment();
                    } else {
                        Toast.makeText(CommentDetailActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(CommentDetailActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
