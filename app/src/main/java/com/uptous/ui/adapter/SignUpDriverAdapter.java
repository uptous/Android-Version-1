package com.uptous.ui.adapter;

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
import com.uptous.ui.activity.DriverDetailActivity;
import com.uptous.ui.activity.RSPVDetailActivity;
import com.uptous.ui.activity.VolunteerDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Prakash on 1/24/2017.
 */

public class SignUpDriverAdapter extends RecyclerView.Adapter<SignUpDriverAdapter.VersionViewHolder> {

    List<SignUpDetailResponseModel.ItemsBean> listEntities;
    Activity activity;


    public SignUpDriverAdapter(Activity a, List<SignUpDetailResponseModel.ItemsBean> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public SignUpDriverAdapter.VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_driver_sign_up, viewGroup, false);
        SignUpDriverAdapter.VersionViewHolder viewHolder = new SignUpDriverAdapter.VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SignUpDriverAdapter.VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass

        long val = listEntities.get(i).getDateTime();
        Date date = new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("MMM dd");
        final String dateText = df2.format(date);
//            versionViewHolder.mTextViewDate.setText(dateText);


        if (i == 0) {
            versionViewHolder.mTextViewTitle.setText("Form: " + listEntities.get(i).getName());
            versionViewHolder.mTextViewDate.setText("To: " + listEntities.get(1).getName());
        } else if (i == 1) {
            versionViewHolder.mTextViewTitle.setText("Form: " + listEntities.get(i).getName());
            versionViewHolder.mTextViewDate.setText("To: " + listEntities.get(0).getName());
        }

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
                    int ItemID = listEntities.get(position).getId();
                    MyApplication.editor.putInt("ItemId", ItemID);

                    if (position == 0) {
                        MyApplication.editor.putString("FromName", listEntities.get(0).getName());
                        MyApplication.editor.putString("ToName", listEntities.get(1).getName());
                    }else {
                        MyApplication.editor.putString("FromName", listEntities.get(1).getName());
                        MyApplication.editor.putString("ToName", listEntities.get(0).getName());
                    }

                    MyApplication.editor.putString("Date", dateText);
                    MyApplication.editor.commit();
                    Intent intent = new Intent(activity, DriverDetailActivity.class);
                    activity.startActivity(intent);
                }
            });


        } else {
            if (OpenSpot.equalsIgnoreCase("Volunteered")) {

                versionViewHolder.linearLayoutVolunteered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        int ItemID = listEntities.get(position).getId();

                        MyApplication.editor.putInt("ItemId", ItemID);
                        MyApplication.editor.commit();
                        Intent intent = new Intent(activity, VolunteerDetailActivity.class);
                        activity.startActivity(intent);
                    }
                });

                versionViewHolder.imageViewSignUpType.setImageResource(R.mipmap.fav_icon);
                versionViewHolder.linearLayoutOpenSpot.setVisibility(View.GONE);
                versionViewHolder.linearLayoutVolunteered.setVisibility(View.VISIBLE);
                versionViewHolder.mTextViewVolunteered.setText(listEntities.get(i).getVolunteerStatus());
            }

            if (OpenSpot.equalsIgnoreCase("Full")) {

                versionViewHolder.linearLayoutVolunteered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        int ItemID = listEntities.get(position).getId();
                        MyApplication.editor.putInt("ItemId", ItemID);
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

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_name_form);
            mTextViewVolunteered = (TextView) itemView.findViewById(R.id.text_view_volunteered);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_name_to);
            mteTextViewVolunteerCount = (TextView) itemView.findViewById(R.id.text_view_volunteer_count);
            linearLayoutVolunteered = (LinearLayout) itemView.findViewById(R.id.layout_volunteered);
            linearLayoutOpenSpot = (LinearLayout) itemView.findViewById(R.id.layout_open_spot);
            imageViewSignUpType = (PlayGifView) itemView.findViewById(R.id.image_view_sign_up_type);
            imageViewFull=(ImageView)itemView.findViewById(R.id.image_view_full);
            mView = itemView;


        }
    }
}