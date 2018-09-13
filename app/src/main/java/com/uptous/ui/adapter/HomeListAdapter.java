package com.uptous.ui.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.model.FeedResponseModel;
import com.uptous.model.SignUpResponseModel;
import com.uptous.ui.activity.CommentDetailActivity;
import com.uptous.ui.activity.HomeDetailActivity;
import com.uptous.ui.activity.SignUpDRIVERActivity;
import com.uptous.ui.activity.SignUpRSPVActivity;
import com.uptous.ui.activity.SignUpShiftsActivity;
import com.uptous.ui.activity.WebviewActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash .
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.VersionViewHolder> {

    List<FeedResponseModel> listEntities;
    private List<SignUpResponseModel> mSignUpResponseModelList = new ArrayList<>();
    Activity activity;


    public HomeListAdapter(Activity a, List<FeedResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_home, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        versionViewHolder.mTextViewTitle.setText(Html.fromHtml(listEntities.get(i).getNewsItemName().replace("%20", " ")));
        versionViewHolder.mTextViewNewsItemDescription.setText(Html.fromHtml(listEntities.get(i).getNewsItemDescription()));

        versionViewHolder.mTextViewUserName.setText(listEntities.get(i).getOwnerName() + " in: ");
        versionViewHolder.mTextViewCommunityName.setText((Html.fromHtml("<u>" + listEntities.get(i).getCommunityName() + "</u> ")));
        int colorText = Color.parseColor(listEntities.get(i).getCommunityTextColor());
        int colorBackground = Color.parseColor(listEntities.get(i).getCommunityBackgroundColor());
//        versionViewHolder.mRelativeLayoutFileDetail.setBackgroundColor(colorBackground);
//        versionViewHolder.mTextViewNewsItemDescription.setTextColor(colorText);
//        Long date=listEntities.get(i).getCreateDate();

        String s = listEntities.get(i).getNewsItemName();
        String result = s.substring(s.lastIndexOf(".") + 1);
        if (result.equalsIgnoreCase("pdf")) {
            versionViewHolder.mTextViewNewsItemDescription.setText(listEntities.get(i).getNewsItemName());
            versionViewHolder.mImageViewGoDetail.setImageResource(R.mipmap.downloadbutton);
            versionViewHolder.mImageViewFile.setImageResource(R.mipmap.attachment);

        } else if (result.equalsIgnoreCase("zip")) {
            versionViewHolder.mTextViewNewsItemDescription.setText(listEntities.get(i).getNewsItemName());
            versionViewHolder.mImageViewGoDetail.setImageResource(R.mipmap.downloadbutton);
            versionViewHolder.mImageViewFile.setImageResource(R.mipmap.attachment);
        } else if (result.equalsIgnoreCase("docx")) {
            versionViewHolder.mTextViewNewsItemDescription.setText(listEntities.get(i).getNewsItemName());
            versionViewHolder.mImageViewGoDetail.setImageResource(R.mipmap.downloadbutton);
            versionViewHolder.mImageViewFile.setImageResource(R.mipmap.attachment);
        } else if (result.equalsIgnoreCase("xls")) {
            versionViewHolder.mTextViewNewsItemDescription.setText(listEntities.get(i).getNewsItemName());
            versionViewHolder.mImageViewGoDetail.setImageResource(R.mipmap.downloadbutton);
            versionViewHolder.mImageViewFile.setImageResource(R.mipmap.attachment);
        } else if (result.equalsIgnoreCase("png")) {
            versionViewHolder.mTextViewNewsItemDescription.setText(listEntities.get(i).getNewsItemName());
            versionViewHolder.mImageViewGoDetail.setImageResource(R.mipmap.downloadbutton);
            versionViewHolder.mImageViewFile.setImageResource(R.mipmap.attachment);
        }


        String OwnerName = listEntities.get(i).getOwnerName();
        int j = OwnerName.indexOf(" ");
        String OwnerNAME = OwnerName.substring(0, j);
        versionViewHolder.mTextViewReply.setText("Reply to " + OwnerNAME);


        // For Date
        long val = listEntities.get(i).getCreateDate();
        Date date = new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
        String dateText = df2.format(date);
        versionViewHolder.mTextViewDate.setText(dateText);

        String OwnerPhoto = listEntities.get(i).getOwnerPhotoUrl();
        String result1 = OwnerPhoto.substring(OwnerPhoto.lastIndexOf(".") + 1);

        if (OwnerPhoto != null && !result1.equalsIgnoreCase("gif")) {
            versionViewHolder.mImageViewUser.setVisibility(View.VISIBLE);
            Picasso.with(activity).load(OwnerPhoto).into(versionViewHolder.mImageViewUser);
        } else {
            String BackgroundColor = listEntities.get(i).getOwnerBackgroundColor();
            if (BackgroundColor != null) {
                int color = Color.parseColor(BackgroundColor);
                versionViewHolder.mLinearLayoutRoundedBackGround.setVisibility(View.VISIBLE);
                versionViewHolder.mLinearLayoutRoundedBackGround.setBackgroundResource(R.drawable.circle);
                GradientDrawable gd = (GradientDrawable)
                        versionViewHolder.mLinearLayoutRoundedBackGround.getBackground().getCurrent();
                gd.setColor(color);
                gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});


                int colorTextView = Color.parseColor(listEntities.get(i).getOwnerTextColor());


                versionViewHolder.mTextViewOwnerFirstName.setText(listEntities.get(i).getOwnerName());
                versionViewHolder.mTextViewOwnerFirstName.setTextColor(colorTextView);
                versionViewHolder.mTextViewOwnerSecondName.setText(listEntities.get(i).getOwnerName());
                versionViewHolder.mTextViewOwnerSecondName.setTextColor(colorTextView);


            }

        }


        //For show comment count
        int CommentSize = listEntities.get(i).getComments().size();
        if (CommentSize != 0) {
            versionViewHolder.mTextViewCommentCount.setText("" + CommentSize + " Comment");
        } else {
            versionViewHolder.mTextViewCommentCount.setVisibility(View.GONE);
        }


        // Show News type
        if (listEntities.get(i).getNewsType().equalsIgnoreCase("Announcement")) {
            versionViewHolder.mTextViewComment.setText("Reply all");
            versionViewHolder.mRelativeLayoutFileDetail.setVisibility(View.GONE);
            versionViewHolder.mTextViewNewsItemName.setVisibility(View.VISIBLE);
//            versionViewHolder.scrollView.setVisibility(View.VISIBLE);

//
            versionViewHolder.mTextViewNewsItemName.setText(Html.fromHtml(listEntities.get(i).getNewsItemDescription()));
            versionViewHolder.mTextViewNewsItemName.setMovementMethod(new ScrollingMovementMethod());

            if (listEntities.get(i).getNewsItemDescription().length() > 150) {
                versionViewHolder.mTextViewMore.setVisibility(View.VISIBLE);
            }

            final String NewsItem = listEntities.get(i).getNewsItemDescription();
            versionViewHolder.mTextViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    versionViewHolder.mTextViewNewsDetailExpand.setVisibility(View.VISIBLE);
//                    versionViewHolder.mTextViewNewsItemName.setVisibility(View.GONE);
//                    versionViewHolder.mTextViewMore.setVisibility(View.GONE);

                    CustomizeDialog customizeDialog = new CustomizeDialog(activity);
                    customizeDialog.setContentView(R.layout.dialog_read_more);
                    TextView textView = (TextView) customizeDialog.findViewById(R.id.text_view_news_item_name_expand);
                    textView.setText(Html.fromHtml(NewsItem));
                    customizeDialog.setCancelable(true);
                    customizeDialog.show();
//                    versionViewHolder.mTextViewCollapsed.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            versionViewHolder.mTextViewNewsDetailExpand.setVisibility(View.GONE);
//                            versionViewHolder.mTextViewNewsItemName.setVisibility(View.VISIBLE);
//                            versionViewHolder.mTextViewMore.setVisibility(View.VISIBLE);
//                            versionViewHolder.mTextViewCollapsed.setVisibility(View.GONE);
//                        }
//                    });
                }
            });

        } else {
            versionViewHolder.mRelativeLayoutFileDetail.setVisibility(View.VISIBLE);
            versionViewHolder.mTextViewNewsItemName.setVisibility(View.GONE);
        }

        // Go to comment detail page
        versionViewHolder.mTextViewCommentCount.setTag(i);
        versionViewHolder.mTextViewCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int position = (int) view.getTag();
//                int FeedId = listEntities.get(position).getFeedId();
//                String Image = listEntities.get(position).getOwnerPhotoUrl();
//                String NewsItem = listEntities.get(position).getNewsItemPhoto();
//                String NewsItemName = listEntities.get(position).getNewsItemName();
//                String NewsItemNameDescription = listEntities.get(position).getNewsItemDescription();
//                String OwnerName = listEntities.get(position).getOwnerName();
//                String CommunityName = listEntities.get(position).getCommunityName();
//                long val = listEntities.get(position).getCreateDate();
//                Date date = new Date(val);
//                SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
//                String dateText = df2.format(date);
//                MyApplication.editor.putInt("FeedId", FeedId);
//                MyApplication.editor.putString("Image", Image);
//                MyApplication.editor.putString("ImageNewsItem", NewsItem);
//                MyApplication.editor.putString("Date", dateText);
//                MyApplication.editor.putString("NewsItemName", NewsItemName);
//                MyApplication.editor.putString("OwnerName", OwnerName);
//                MyApplication.editor.putString("CommunityName", CommunityName);
//                MyApplication.editor.putString("NewsItemDescription", NewsItemNameDescription);
//                MyApplication.editor.commit();
//                Intent intent = new Intent(activity, CommentDetailActivity.class);
//                activity.startActivity(intent);
            }
        });

        // Go to comment detail page
        versionViewHolder.mLinearLayoutComment.setTag(i);
        versionViewHolder.mLinearLayoutComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                int FeedId = listEntities.get(position).getFeedId();
                String Image = listEntities.get(position).getOwnerPhotoUrl();
                String NewsItem = listEntities.get(position).getNewsItemPhoto();
                String NewsItemName = listEntities.get(position).getNewsItemName();
                String NewsItemNameDescription = listEntities.get(position).getNewsItemDescription();
                String OwnerName = listEntities.get(position).getOwnerName();
                String CommunityName = listEntities.get(position).getCommunityName();
                String NewsType = listEntities.get(position).getNewsType();
                long val = listEntities.get(position).getCreateDate();
                Date date = new Date(val);
                SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
                String dateText = df2.format(date);
                MyApplication.editor.putInt("FeedId", FeedId);
                MyApplication.editor.putString("OwnerBackground", listEntities.get(position).getOwnerBackgroundColor());
                MyApplication.editor.putString("OwnerTextColor", listEntities.get(position).getOwnerTextColor());
                MyApplication.editor.putString("Image", Image);
                MyApplication.editor.putString("ImageNewsItem", NewsItem);
                MyApplication.editor.putString("Date", dateText);
                MyApplication.editor.putString("NewsItemName", NewsItemName);
                MyApplication.editor.putString("OwnerName", OwnerName);
                MyApplication.editor.putString("CommunityName", CommunityName);
                MyApplication.editor.putString("NewsItemDescription", NewsItemNameDescription);
                MyApplication.editor.putString("NewsType", NewsType);
                MyApplication.editor.commit();
                Intent intent = new Intent(activity, CommentDetailActivity.class);
                activity.startActivity(intent);
            }
        });


        // if news item is not image type then show another layout


        if (listEntities.get(i).getNewsType().equalsIgnoreCase("Photos")) {
            String Image = listEntities.get(i).getNewsItemPhoto();
            versionViewHolder.mImageViewUploaded.setTag(i);
            if (Image != null && !Image.equalsIgnoreCase("")) {
                versionViewHolder.mRelativeLayoutFileDetail.setVisibility(View.GONE);
                versionViewHolder.mImageViewUploaded.setVisibility(View.VISIBLE);
                Picasso.with(activity).load(listEntities.get(i).getNewsItemPhoto()).into(versionViewHolder.mImageViewUploaded);
                versionViewHolder.mImageViewUploaded.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                    int position = (int) view.getTag();
//                    int NewsItemID = listEntities.get(position).getNewsItemId();
//                    MyApplication.editor.putInt("NewsItemID", NewsItemID);
//                    MyApplication.editor.commit();
//                    Intent intent = new Intent(activity, AlbumDetailActivity.class);
//                    activity.startActivity(intent);
                    }
                });
            }

        } else {


            versionViewHolder.mImageViewUploaded.setVisibility(View.GONE);
            versionViewHolder.mRelativeLayoutFileDetail.setTag(i);
            versionViewHolder.mRelativeLayoutFileDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = (int) view.getTag();
                    String path = listEntities.get(position).getNewsItemUrl();
                    String s = listEntities.get(position).getNewsItemName();
//
//
                    String result = s.substring(s.lastIndexOf(".") + 1);
                    String resultImage = path.substring(path.lastIndexOf('.') + 1).trim();

                    if (result.equalsIgnoreCase("pdf")) {
                        MyApplication.editor.putString("path", path);
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, WebviewActivity.class);
                        activity.startActivity(intent);
                    } else if (result.equalsIgnoreCase("zip")) {

//                        MyApplication.editor.putString("path", path);
//                        MyApplication.editor.commit();
//                        Intent intent = new Intent(activity, WebviewActivity.class);
//                        activity.startActivity(intent);

                    } else if (result.equalsIgnoreCase("docx")) {
                        MyApplication.editor.putString("path", path);
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, WebviewActivity.class);
                        activity.startActivity(intent);
                    } else if (result.equalsIgnoreCase("xls")) {
                        MyApplication.editor.putString("path", path);
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, WebviewActivity.class);
                        activity.startActivity(intent);
                    } else if (resultImage.equalsIgnoreCase("png")) {
                        MyApplication.editor.putString("Imagepath", path);
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, WebviewActivity.class);
                        activity.startActivity(intent);
                    } else if (resultImage.equalsIgnoreCase("jpg")) {
                        MyApplication.editor.putString("Imagepath", path);
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, WebviewActivity.class);
                        activity.startActivity(intent);
                    } else if (resultImage.equalsIgnoreCase("jpeg")) {
                        MyApplication.editor.putString("Imagepath", path);
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, WebviewActivity.class);
                        activity.startActivity(intent);
                    } else {
                        int CommunityID = listEntities.get(position).getNewsItemId();
                        MyApplication.editor.putInt("CommunityID", CommunityID);
                        MyApplication.editor.commit();

                        getApiSignUp();


                    }
                }
            });


        }

        versionViewHolder.mLinearLayoutReplytoSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                activity.startActivity(sendIntent);
            }
        });
    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewUserName, mTextViewAllFeed, mTextViewMore, mTextViewCollapsed,
                mTextViewNewsItemName, mTextViewReply, mTextViewNewsDetailExpand,
                mTextViewCommentCount, mTextViewCommunityName, mTextViewTitle,
                mTextViewComment, mTextViewDate, mTextViewNewsItemDescription, mTextViewOwnerFirstName, mTextViewOwnerSecondName;
        RelativeLayout mRelativeLayoutFile, mRelativeLayoutUploadPicture;
        ImageView mImageViewUser, mImageViewFile, mImageViewUploaded, mImageViewUserComment, mImageViewGoDetail;
        LinearLayout mLinearLayoutReplytoSingle, mLinearLayoutReplyToSinglePicture, mLinearLayoutComment,
                mLinearLayoutCommentPicture, mLinearLayoutRoundedBackGround;
        RelativeLayout mRelativeLayoutDetailComment, mRelativeLayoutFileDetail, mRelativeLayoutMain;
        ScrollView scrollView;

        public VersionViewHolder(View itemView) {
            super(itemView);
            mRelativeLayoutMain = (RelativeLayout) itemView.findViewById(R.id.main);
            mTextViewOwnerFirstName = (TextView) itemView.findViewById(R.id.textview_first_name);
            mTextViewOwnerSecondName = (TextView) itemView.findViewById(R.id.textview_last_name);
            mTextViewReply = (TextView) itemView.findViewById(R.id.text_view_reply);
            mTextViewMore = (TextView) itemView.findViewById(R.id.text_view_more);
//            mTextViewCollapsed = (TextView) itemView.findViewById(R.id.text_view_collapsed);
            mTextViewNewsItemName = (TextView) itemView.findViewById(R.id.text_view_news_item_name);
//            mLinearLayoutTextView = (LinearLayout) itemView.findViewById(R.id.layout_text_view);

//            scrollView = (ScrollView) itemView.findViewById(R.id.scroll_view);
            mTextViewNewsDetailExpand = (TextView) itemView.findViewById(R.id.text_view_news_item_name_expand);
            mTextViewCommentCount = (TextView) itemView.findViewById(R.id.text_view_comment_count);
            mTextViewComment = (TextView) itemView.findViewById(R.id.text_view_comment);
            mTextViewNewsItemDescription = (TextView) itemView.findViewById(R.id.text_view_news_item_description);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_date);
            mImageViewUploaded = (ImageView) itemView.findViewById(R.id.image_view_uploaded_picture);
            mRelativeLayoutFile = (RelativeLayout) itemView.findViewById(R.id.layout_main);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_title);
            mTextViewUserName = (TextView) itemView.findViewById(R.id.text_view_owner_name);
            mTextViewCommunityName = (TextView) itemView.findViewById(R.id.text_view_community_name);
            mImageViewUser = (ImageView) itemView.findViewById(R.id.image_view_owner);
            mImageViewFile = (ImageView) itemView.findViewById(R.id.image_view_file);
            mLinearLayoutReplytoSingle = (LinearLayout) itemView.findViewById(R.id.layout_reply_to_single);
            mLinearLayoutComment = (LinearLayout) itemView.findViewById(R.id.layout_comment);
            mLinearLayoutRoundedBackGround = (LinearLayout) itemView.findViewById(R.id.layout_contact);
            mRelativeLayoutDetailComment = (RelativeLayout) itemView.findViewById(R.id.layout_detail_comment);
            mRelativeLayoutFileDetail = (RelativeLayout) itemView.findViewById(R.id.layout_detail);
            mImageViewGoDetail = (ImageView) itemView.findViewById(R.id.image_view_go_detail);
            mView = itemView;


//            mLinearLayoutReplyToSinglePicture.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                    sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
//                    sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
//                    activity.startActivity(sendIntent);
//                }
//            });
//


        }
    }

    private void getApiSignUp() {
        String mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        String mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        final int CommunityID = MyApplication.mSharedPreferences.getInt("CommunityID", 0);
        final ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<SignUpResponseModel>> call = service.GetSignUp();

        call.enqueue(new Callback<List<SignUpResponseModel>>() {
            @Override
            public void onResponse(Call<List<SignUpResponseModel>> call, Response<List<SignUpResponseModel>> response) {
                mProgressDialog.dismiss();
                try {


//                    mCommunityName = MyApplication.mSharedPreferences.getString("CommunityName", null);
//                    mCommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);

//                    if (mCommunityId != 0) {
                    mSignUpResponseModelList = response.body();
//                   edModelList.size() == 0) {


                    for (int j = 0; mSignUpResponseModelList.size() > j; j++) {
                        int CommId = mSignUpResponseModelList.get(j).getId();
                        if (CommId == CommunityID) {
                            if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("RSVP") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Vote")) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                MyApplication.editor.putInt("Id", OpId);
                                MyApplication.editor.commit();
                                Intent intent = new Intent(activity, SignUpRSPVActivity.class);
                                activity.startActivity(intent);
                            } else if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Drivers")) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                MyApplication.editor.putInt("Id", OpId);
                                MyApplication.editor.commit();
                                Intent intent = new Intent(activity, SignUpDRIVERActivity.class);
                                activity.startActivity(intent);
                            } else if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Shifts") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Games") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Potluck/Party") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Wish List") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Volunteer") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Snack Schedule") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Ongoing Volunteering")
                                    || mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Multi Game/Event RSVP")
                                    ) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                MyApplication.editor.putInt("Id", OpId);
                                MyApplication.editor.putString("Type", mSignUpResponseModelList.get(j).getType());
                                MyApplication.editor.commit();
                                Intent intent = new Intent(activity, SignUpShiftsActivity.class);
                                activity.startActivity(intent);
                            }

                        } else {


                        }
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}

