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
import com.uptous.view.activity.DriverDetailActivity;
import com.uptous.view.activity.VolunteerDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Prakash .
 */

public class SignUpDriverAdapter extends RecyclerView.Adapter<SignUpDriverAdapter.VersionViewHolder> {

    List<SignUpDetailResponseModel.ItemsBean> listEntities;
    Activity activity;

    String dateTextMain, dateTextTime;
    long val;

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

        val = listEntities.get(i).getDateTime();
        if (val == 0) {
            versionViewHolder.mTextViewDate.setVisibility(View.GONE);
        } else {
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
            SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            dateTextMain = df2.format(date);
            dateTextTime = dfTime.format(date);
            if (!dateTextTime.equalsIgnoreCase("1:00AM") &&
                    !dateTextTime.equalsIgnoreCase("1:00 AM")) {

                if (listEntities.get(i).getEndTime() != null && !listEntities.get(i).getEndTime().equalsIgnoreCase("")) {
                    if (!listEntities.get(i).getEndTime().equalsIgnoreCase("1:00AM") &&
                            !listEntities.get(i).getEndTime().equalsIgnoreCase("1:00 AM")) {
//                        dateTextMain = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(i).getEndTime();
                        versionViewHolder.mTextViewDate.setText(dateTextMain + ", " + dateTextTime + " - " + listEntities.get(i).getEndTime());
                    } else {
                        versionViewHolder.mTextViewDate.setText(dateTextMain + ", " + dateTextTime);
                    }

                } else {
                    versionViewHolder.mTextViewDate.setText(dateTextMain + ", " + dateTextTime);
                }
            } else {
                versionViewHolder.mTextViewDate.setText(dateTextMain);
            }


        }


        if (i == 0) {
            versionViewHolder.mTextViewTitle.setText("From: " + listEntities.get(i).getName());

            if (listEntities.size()>1) {
                versionViewHolder.mTextViewTo.setText("To: " + listEntities.get(1).getName());
            }

        } else if (i == 1) {
            versionViewHolder.mTextViewTitle.setText("From: " + listEntities.get(i).getName());
            versionViewHolder.mTextViewTo.setText("To: " + listEntities.get(0).getName());
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
                    String dateText = null;
                    String TimeText = null;
                    long val = 0;
                    int position = (int) view.getTag();
                    val = listEntities.get(position).getDateTime();
                    if (val != 0) {
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
                        SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                        df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                        dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                        dateText = df2.format(date);
                        TimeText = dfTime.format(date);
                        if (!TimeText.equalsIgnoreCase("1:00AM") &&
                                !TimeText.equalsIgnoreCase("1:00 AM")) {
                            dateText = dateText + ", " + TimeText;
                            if (listEntities.get(position).getEndTime() != null && !listEntities.get(position).getEndTime().equalsIgnoreCase("")) {
                                if (!listEntities.get(position).getEndTime().equalsIgnoreCase("1:00AM") &&
                                        !listEntities.get(position).getEndTime().equalsIgnoreCase("1:00 AM")) {
                                    dateText = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(position).getEndTime();
                                } else {
                                    dateText = df2.format(date) + ", " + dfTime.format(date);
                                }
                            }
                        } else {
                            dateText = df2.format(date);
                        }

                    }


                    int ItemID = listEntities.get(position).getId();
                    Prefs.setItemId(activity,ItemID);
                    if (position == 0) {
                        Prefs.setFromName(activity,listEntities.get(0).getName());
                        Prefs.setToName(activity,listEntities.get(1).getName());
                    } else {
                        Prefs.setFromName(activity,listEntities.get(1).getName());
                        Prefs.setToName(activity,listEntities.get(0).getName());
                    }

                    Prefs.setDate(activity,dateText);
                    Intent intent = new Intent(activity, DriverDetailActivity.class);
                    activity.startActivity(intent);
                }
            });


        } else {
            if (OpenSpot.equalsIgnoreCase("Volunteered")) {

                versionViewHolder.linearLayoutVolunteered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dateText = null;
                        String TimeText = null;
                        long val = 0;
                        int position = (int) view.getTag();
                        val = listEntities.get(position).getDateTime();
                        if (val != 0) {
                            Date date = new Date(val);
                            SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
                            SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            dateText = df2.format(date);
                            TimeText = dfTime.format(date);
                            if (!TimeText.equalsIgnoreCase("1:00AM") &&
                                    !TimeText.equalsIgnoreCase("1:00 AM")) {
                                dateText = dateText + ", " + TimeText;
                                if (listEntities.get(position).getEndTime() != null && !listEntities.get(position).getEndTime().equalsIgnoreCase("")) {
                                    if (!listEntities.get(position).getEndTime().equalsIgnoreCase("1:00AM") &&
                                            !listEntities.get(position).getEndTime().equalsIgnoreCase("1:00 AM")) {
                                        dateText = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(position).getEndTime();
                                    } else {
                                        dateText = df2.format(date) + ", " + dfTime.format(date);
                                    }
                                }
                            } else {
                                dateText = df2.format(date);
                            }

                        }
                        int ItemID = listEntities.get(position).getId();


                        Prefs.setSignUpType(activity,"Driver");
                        Prefs.setItemId(activity,ItemID);

                        Prefs.setDate(activity,dateText);
                        if (position == 0) {
                            Prefs.setFromName(activity,listEntities.get(0).getName());
                            Prefs.setToName(activity,listEntities.get(1).getName());
                        } else {
                            Prefs.setFromName(activity,listEntities.get(1).getName());
                            Prefs.setToName(activity,listEntities.get(0).getName());
                        }
                        Intent intent = new Intent(activity, VolunteerDetailActivity.class);
                        activity.startActivity(intent);
                    }
                });

                versionViewHolder.imageViewSignUpType.setGifImageResource(R.mipmap.fav_icon);
                versionViewHolder.linearLayoutOpenSpot.setVisibility(View.GONE);
                versionViewHolder.linearLayoutVolunteered.setVisibility(View.VISIBLE);
                versionViewHolder.mTextViewVolunteered.setText(listEntities.get(i).getVolunteerStatus());
            }

            if (OpenSpot.equalsIgnoreCase("Full")) {

                versionViewHolder.linearLayoutVolunteered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dateText = null;
                        String TimeText = null;
                        long val = 0;
                        int position = (int) view.getTag();
                        val = listEntities.get(position).getDateTime();
                        if (val != 0) {
                            Date date = new Date(val);
                            SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
                            SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            dateText = df2.format(date);
                            TimeText = dfTime.format(date);
                            if (!TimeText.equalsIgnoreCase("1:00AM") &&
                                    !TimeText.equalsIgnoreCase("1:00 AM")) {
                                dateText = dateText + ", " + TimeText;
                                if (listEntities.get(position).getEndTime() != null && !listEntities.get(position).getEndTime().equalsIgnoreCase("")) {
                                    if (!listEntities.get(position).getEndTime().equalsIgnoreCase("1:00AM") &&
                                            !listEntities.get(position).getEndTime().equalsIgnoreCase("1:00 AM")) {
                                        dateText = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(position).getEndTime();
                                    } else {
                                        dateText = df2.format(date) + ", " + dfTime.format(date);
                                    }
                                }
                            } else {
                                dateText = df2.format(date);
                            }

                        }
                        int ItemID = listEntities.get(position).getId();
                        Prefs.setItemId(activity,ItemID);
                        Prefs.setSignUpType(activity,"Driver");
                       Prefs.setDate(activity,dateText);

                        if (position == 0) {
                            Prefs.setFromName(activity,listEntities.get(0).getName());
                            Prefs.setToName(activity,listEntities.get(1).getName());
                        } else {
                            Prefs.setFromName(activity,listEntities.get(1).getName());
                            Prefs.setToName(activity,listEntities.get(0).getName());
                        }
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
        TextView mTextViewTo, mTextViewDate, mTextViewTitle, mteTextViewVolunteerCount, mTextViewVolunteered;
        LinearLayout linearLayoutVolunteered, linearLayoutOpenSpot;
        GifImageView imageViewSignUpType;
        ImageView imageViewFull;


        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_name_form);
            mTextViewVolunteered = (TextView) itemView.findViewById(R.id.text_view_volunteered);
            mTextViewTo = (TextView) itemView.findViewById(R.id.text_view_name_to);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_date);
            mteTextViewVolunteerCount = (TextView) itemView.findViewById(R.id.text_view_volunteer_count);
            linearLayoutVolunteered = (LinearLayout) itemView.findViewById(R.id.layout_volunteered);
            linearLayoutOpenSpot = (LinearLayout) itemView.findViewById(R.id.layout_open_spot);
            imageViewSignUpType = (GifImageView) itemView.findViewById(R.id.image_view_sign_up_type);
            imageViewFull = (ImageView) itemView.findViewById(R.id.image_view_full);
            mView = itemView;


        }
    }
}