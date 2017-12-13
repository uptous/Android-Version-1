package com.uptous.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.controller.utils.GifImageView;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.RSPVDetailActivity;
import com.uptous.view.activity.VolunteerDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.uptous.view.activity.VolunteerDetailActivity.FULL_LIST;

/**
 * Created by Prakash .
 */

public class SignUpRspvAdapter extends RecyclerView.Adapter<SignUpRspvAdapter.VersionViewHolder> {

    List<SignUpDetailResponseModel.ItemsBean> listEntities;
    Activity activity;
String dateTextMain,dateTextTime;
    long val;


    public SignUpRspvAdapter(Activity a, List<SignUpDetailResponseModel.ItemsBean> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public SignUpRspvAdapter.VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rspv_sign_up, viewGroup, false);
        SignUpRspvAdapter.VersionViewHolder viewHolder = new SignUpRspvAdapter.VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SignUpRspvAdapter.VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass

        val = listEntities.get(i).getDateTime();
        if (val == 0) {
            versionViewHolder.mTextViewDate.setVisibility(View.GONE);
        } else {
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
            SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            dateTextMain = df2.format(date) ;
            dateTextTime=dfTime.format(date);
            if(!dateTextTime.equalsIgnoreCase("1:00AM") &&
                    !dateTextTime.equalsIgnoreCase("1:00 AM")){
                versionViewHolder.mTextViewDate.setText(dateTextMain+", "+dateTextTime);
                if (listEntities.get(i).getEndTime() != null && !listEntities.get(i).getEndTime().equalsIgnoreCase("")) {
                    if (!listEntities.get(i).getEndTime().equalsIgnoreCase("1:00AM") &&
                            !listEntities.get(i).getEndTime().equalsIgnoreCase("1:00 AM")) {
                        dateTextMain = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(i).getEndTime();
                        versionViewHolder.mTextViewDate.setText(dateTextMain + " - " + listEntities.get(i).getEndTime());
                    } else {
                        versionViewHolder.mTextViewDate.setText(dateTextMain);
                    }

                } else {
                    versionViewHolder.mTextViewDate.setText(dateTextMain);
                }
            }else {
                versionViewHolder.mTextViewDate.setText(dateTextMain);
            }

        }

//            versionViewHolder.mTextViewDate.setText(dateText);


        versionViewHolder.mTextViewTitle.setText(listEntities.get(i).getName());
        String OpenSpot = listEntities.get(i).getVolunteerStatus();


//        if (OpenSpot.equalsIgnoreCase("Full")) {
//            versionViewHolder.imageSignUp.setVisibility(View.GONE);
//            versionViewHolder.TextViewSignUp.setText("Full");
//            versionViewHolder.linearLayoutrow_parent.setClickable(false);
//        } else {
//            versionViewHolder.imageSignUp.setVisibility(View.VISIBLE);
//            versionViewHolder.TextViewSignUp.setText("Sign up!");
//            versionViewHolder.linearLayoutrow_parent.setClickable(true);
//        }

        int VolunteerCount = listEntities.get(i).getVolunteerCount();
        versionViewHolder.linearLayoutOpenSpot.setTag(i);
        versionViewHolder.linearLayoutVolunteered.setTag(i);
        versionViewHolder.linearLayoutrow_parent.setTag(i);
        final String Name = listEntities.get(i).getName();
        if (OpenSpot.equalsIgnoreCase("Open")) {

            versionViewHolder.linearLayoutrow_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    int ItemId = listEntities.get(position).getId();

                    Prefs.setItemId(activity,ItemId);
                    Prefs.setName(activity,listEntities.get(position).getName());
                    Prefs.setDate(activity,dateTextMain);
                    Intent intent = new Intent(activity, RSPVDetailActivity.class);
                    activity.startActivity(intent);
                }
            });


        } else {
            if (OpenSpot.equalsIgnoreCase("Volunteered")) {

                versionViewHolder.linearLayoutrow_parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        int ItemId = listEntities.get(position).getId();
                        Prefs.setSignUpType(activity,"RSPV");
                        Prefs.setItemId(activity,ItemId);
                        Prefs.setToName(activity,"");
                        Prefs.setFromName(activity,listEntities.get(position).getName());
                        Intent intent = new Intent(activity, VolunteerDetailActivity.class);
                        activity.startActivity(intent);
                    }
                });

                versionViewHolder.imageViewSignUpType.setGifImageResource(R.mipmap.volunteer_two);
                versionViewHolder.linearLayoutOpenSpot.setVisibility(View.GONE);
                versionViewHolder.linearLayoutVolunteered.setVisibility(View.VISIBLE);
                versionViewHolder.mTextViewVolunteered.setText(listEntities.get(i).getVolunteerStatus());
            }

            if (OpenSpot.equalsIgnoreCase("Full")) {

                versionViewHolder.linearLayoutrow_parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        int ItemId = listEntities.get(position).getId();
                        Prefs.setSignUpType(activity,"RSPV");
                        Prefs.setItemId(activity,ItemId);
                        Prefs.setToName(activity,"");
                        Prefs.setFromName(activity,listEntities.get(position).getName());
                        Intent intent = new Intent(activity, VolunteerDetailActivity.class);
                        intent.putExtra(FULL_LIST,true);
                        activity.startActivity(intent);
                    }
                });

                versionViewHolder.imageViewFull.setVisibility(View.VISIBLE);
                versionViewHolder.linearLayoutOpenSpot.setVisibility(View.GONE);
                versionViewHolder.linearLayoutVolunteered.setVisibility(View.VISIBLE);
                versionViewHolder.mTextViewVolunteered.setText(listEntities.get(i).getVolunteerStatus());
            }
        }
//        versionViewHolder.mTextViewDate.setText(dateText);
    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewDate,TextViewSignUp, mTextViewTitle, mteTextViewVolunteerCount, mTextViewVolunteered;
        LinearLayout linearLayoutVolunteered, linearLayoutOpenSpot,linearLayoutrow_parent;
        GifImageView imageViewSignUpType;
        ImageView imageViewFull,imageSignUp;


        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view__name);
            mTextViewVolunteered = (TextView) itemView.findViewById(R.id.text_view_volunteered);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_date);
            TextViewSignUp = (TextView) itemView.findViewById(R.id.text_signup);
            imageSignUp = (ImageView) itemView.findViewById(R.id.img_signup);

            mteTextViewVolunteerCount = (TextView) itemView.findViewById(R.id.text_view_volunteer_count);
            linearLayoutVolunteered = (LinearLayout) itemView.findViewById(R.id.layout_volunteered);
            linearLayoutOpenSpot = (LinearLayout) itemView.findViewById(R.id.layout_open);
            linearLayoutrow_parent = (LinearLayout) itemView.findViewById(R.id.row_parent);
            imageViewSignUpType = (GifImageView) itemView.findViewById(R.id.image_view_sign_up_type);
            imageViewFull=(ImageView)itemView.findViewById(R.id.image_view_full);
            mView = itemView;


        }
    }
}