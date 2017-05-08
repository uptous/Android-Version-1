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

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.utils.PlayGifView;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.view.activity.RSPVDetailActivity;
import com.uptous.view.activity.VolunteerDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

        int VolunteerCount = listEntities.get(i).getVolunteerCount();
        versionViewHolder.linearLayoutOpenSpot.setTag(i);
        versionViewHolder.linearLayoutVolunteered.setTag(i);
        final String Name = listEntities.get(i).getName();
        if (OpenSpot.equalsIgnoreCase("Open")) {

            versionViewHolder.linearLayoutOpenSpot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    int ItemId = listEntities.get(position).getId();

                    MyApplication.editor.putInt("ItemId", ItemId);
                    MyApplication.editor.putString("Name",  listEntities.get(position).getName());
                    MyApplication.editor.putString("Date", dateTextMain);
                    MyApplication.editor.commit();
                    Intent intent = new Intent(activity, RSPVDetailActivity.class);
                    activity.startActivity(intent);
                }
            });


        } else {
            if (OpenSpot.equalsIgnoreCase("Volunteered")) {

                versionViewHolder.linearLayoutVolunteered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        int ItemId = listEntities.get(position).getId();
                        MyApplication.editor.putString("Type", "RSPV");
                        MyApplication.editor.putString("ToName","");
                        MyApplication.editor.putString("FromName", listEntities.get(position).getName());
                        MyApplication.editor.putInt("ItemId", ItemId);
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, VolunteerDetailActivity.class);
                        activity.startActivity(intent);
                    }
                });

                versionViewHolder.imageViewSignUpType.setImageResource(R.mipmap.volunteer_two);
                versionViewHolder.linearLayoutOpenSpot.setVisibility(View.GONE);
                versionViewHolder.linearLayoutVolunteered.setVisibility(View.VISIBLE);
                versionViewHolder.mTextViewVolunteered.setText(listEntities.get(i).getVolunteerStatus());
            }

            if (OpenSpot.equalsIgnoreCase("Full")) {

                versionViewHolder.linearLayoutVolunteered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        int ItemId = listEntities.get(position).getId();
                        MyApplication.editor.putString("Type", "RSPV");
                        MyApplication.editor.putInt("ItemId", ItemId);
                        MyApplication.editor.putString("ToName","");
                        MyApplication.editor.putString("FromName", listEntities.get(position).getName());
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, VolunteerDetailActivity.class);
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
        TextView mTextViewDate, mTextViewTitle, mteTextViewVolunteerCount, mTextViewVolunteered;
        LinearLayout linearLayoutVolunteered, linearLayoutOpenSpot;
        PlayGifView imageViewSignUpType;
        ImageView imageViewFull;


        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view__name);
            mTextViewVolunteered = (TextView) itemView.findViewById(R.id.text_view_volunteered);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_date);
            mteTextViewVolunteerCount = (TextView) itemView.findViewById(R.id.text_view_volunteer_count);
            linearLayoutVolunteered = (LinearLayout) itemView.findViewById(R.id.layout_volunteered);
            linearLayoutOpenSpot = (LinearLayout) itemView.findViewById(R.id.layout_open);
            imageViewSignUpType = (PlayGifView) itemView.findViewById(R.id.image_view_sign_up_type);
            imageViewFull=(ImageView)itemView.findViewById(R.id.image_view_full);
            mView = itemView;


        }
    }
}