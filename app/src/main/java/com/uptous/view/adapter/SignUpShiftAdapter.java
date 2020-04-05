package com.uptous.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.uptous.view.activity.ShiftDetailActivity;
import com.uptous.view.activity.VolunteerDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.uptous.view.activity.VolunteerDetailActivity.FULL_LIST;

/**
 * Created by Prakash .
 */
public class SignUpShiftAdapter extends RecyclerView.Adapter<SignUpShiftAdapter.VersionViewHolder> {

    List<SignUpDetailResponseModel.ItemsBean> listEntities;
    Activity activity;

    private static int counter = 0;
    String dateTextMain,dateTextTime;
    long val;
    int Total, NumberOfVolunteer, VolunteerCount;

    public SignUpShiftAdapter(Activity a, List<SignUpDetailResponseModel.ItemsBean> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_individual_sign_up, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        SignUpDetailResponseModel.ItemsBean itemsBean = listEntities.get(i);
        val = itemsBean.getDateTime();
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

                if (itemsBean.getEndTime() != null && !itemsBean.getEndTime().equalsIgnoreCase("")) {
                    if (!itemsBean.getEndTime().equalsIgnoreCase("1:00AM") &&
                            !itemsBean.getEndTime().equalsIgnoreCase("1:00 AM")) {
//                        dateTextMain = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(i).getEndTime();
                        versionViewHolder.mTextViewDate.setText(dateTextMain +", "+dateTextTime+ " - " + itemsBean.getEndTime());
                    } else {
                        versionViewHolder.mTextViewDate.setText(dateTextMain+", "+dateTextTime);
                    }

                } else {
                    versionViewHolder.mTextViewDate.setText(dateTextMain+", "+dateTextTime);
                }
            }else {
                versionViewHolder.mTextViewDate.setText(dateTextMain);
            }

        }

        versionViewHolder.mTextViewTitle.setText(itemsBean.getName());
        String OpenSpot = itemsBean.getVolunteerStatus();
        VolunteerCount = itemsBean.getVolunteerCount();
        NumberOfVolunteer = itemsBean.getNumVolunteers();
        Total = NumberOfVolunteer - VolunteerCount;
        versionViewHolder.linearLayoutOpenSpot.setTag(i);
        versionViewHolder.linearLayoutVolunteered.setTag(i);
        versionViewHolder.linearLayoutParent.setTag(i);
        final String Name = itemsBean.getName();
        final String Time = itemsBean.getEndTime();
        if (OpenSpot.equalsIgnoreCase("Open")) {

            versionViewHolder.linearLayoutParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String dateText = null;
                    String TimeText = null;
                    long val = 0;
                    int position = (int) view.getTag();
                    val = listEntities.get(position).getDateTime();
                    if(val!=0){
                        Date date = new Date(val);
                        SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
                        SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                        df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                        dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                        dateText = df2.format(date)  ;
                        TimeText=dfTime.format(date);
                        if(!TimeText.equalsIgnoreCase("1:00AM") &&
                                !TimeText.equalsIgnoreCase("1:00 AM")){
                            dateText = dateText +", "+ TimeText;
                            if (listEntities.get(position).getEndTime() != null && !listEntities.get(position).getEndTime().equalsIgnoreCase("")) {
                                if (!listEntities.get(position).getEndTime().equalsIgnoreCase("1:00AM") &&
                                        !listEntities.get(position).getEndTime().equalsIgnoreCase("1:00 AM")) {
                                    dateText = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(position).getEndTime();
                                } else {
                                    dateText = df2.format(date) + ", " + dfTime.format(date);
                                }
                            }
                        }else {
                            dateText = df2.format(date)  ;
                        }

                    }

                    int ItemID = listEntities.get(position).getId();

                    NumberOfVolunteer = listEntities.get(position).getNumVolunteers();
                    VolunteerCount = listEntities.get(position).getVolunteerCount();
                    Total = NumberOfVolunteer - VolunteerCount;
                    if (Total <= 0) {
                        Prefs.setTotalSpot(activity,""+Total);
                        Prefs.setNumberofvolunteer(activity,"null");
                    } else {
                        Prefs.setTotalSpot(activity,""+Total);
                        Prefs.setNumberofvolunteer(activity,""+NumberOfVolunteer);
                    }


                    Prefs.setItemId(activity,ItemID);
                    Prefs.setName(activity, listEntities.get(position).getName());
                    Prefs.setDate(activity,dateText);
                    Prefs.setSignUpType(activity,null);
                    Intent intent = new Intent(activity, ShiftDetailActivity.class);
                    activity.startActivity(intent);
                }
            });

            if (Total <= 0) {
                versionViewHolder.mteTextViewVolunteerCount.setText("Open");
            } else {
                versionViewHolder.linearLayoutOpenSpot.setVisibility(View.VISIBLE);
                versionViewHolder.mteTextViewVolunteerCount.
                        setText((Html.fromHtml("<font color='#EE0000'>" + Total + "</font> " +
                                " spots" + " " + itemsBean
                                .getVolunteerStatus())));
            }


        } else

        {
            if (OpenSpot.equalsIgnoreCase("Volunteered")) {

                versionViewHolder.linearLayoutParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dateText = null;
                        String TimeText = null;
                        long val = 0;
                        int position = (int) view.getTag();
                        val = listEntities.get(position).getDateTime();
                        if(val!=0){
                            Date date = new Date(val);
                            SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
                            SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            dateText = df2.format(date)  ;
                            TimeText=dfTime.format(date);
                            if(!TimeText.equalsIgnoreCase("1:00AM") &&
                                    !TimeText.equalsIgnoreCase("1:00 AM")){
                                dateText = dateText +", "+ TimeText;
                                if (listEntities.get(position).getEndTime() != null && !listEntities.get(position).getEndTime().equalsIgnoreCase("")) {
                                    if (!listEntities.get(position).getEndTime().equalsIgnoreCase("1:00AM") &&
                                            !listEntities.get(position).getEndTime().equalsIgnoreCase("1:00 AM")) {
                                        dateText = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(position).getEndTime();
                                    } else {
                                        dateText = df2.format(date) + ", " + dfTime.format(date);
                                    }
                                }
                            }else {
                                dateText = df2.format(date)  ;
                            }

                        }

                        int ItemID = listEntities.get(position).getId();
                        Prefs.setNumberofvolunteer(activity,null);
                        Prefs.setItemId(activity,ItemID);
                        Prefs.setName(activity, listEntities.get(position).getName());
                        Prefs.setDate(activity,dateText);
                        Prefs.setSignUpType(activity,null);
                        Intent intent = new Intent(activity, VolunteerDetailActivity.class);
                        activity.startActivity(intent);
                    }
                });


                versionViewHolder.imageViewSignUpType.setGifImageResource(R.mipmap.volunteer_one);
                versionViewHolder.linearLayoutOpenSpot.setVisibility(View.GONE);
                versionViewHolder.linearLayoutVolunteered.setVisibility(View.VISIBLE);
                versionViewHolder.mTextViewVolunteered.setText(itemsBean.getVolunteerStatus());
            }

            if (OpenSpot.equalsIgnoreCase("Full")) {

                if (val == 0) {
                    versionViewHolder.mTextViewDate.setVisibility(View.GONE);
                } else {
                    String dateText = null;
                    String TimeText = null;
                    Date date = new Date(val);
                    SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
                    SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                    df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                    dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                    dateText = df2.format(date)  ;
                    TimeText = dfTime.format(date);
                    if(!TimeText.equalsIgnoreCase("1:00AM") && !TimeText.equalsIgnoreCase("1:00 AM")) {
                        dateTextMain = dateText + ", " + TimeText;
                    } else if (itemsBean.getEndTime() != null && !itemsBean.getEndTime().equalsIgnoreCase("1:00AM")) {
                        dateTextMain = df2.format(date) + ", " + dfTime.format(date) + " - " + itemsBean.getEndTime();
                        versionViewHolder.mTextViewDate.setText(dateTextMain + " - " + itemsBean.getEndTime());
                    } else {
                        versionViewHolder.mTextViewDate.setText(dateTextMain);
                    }

                }
                versionViewHolder.linearLayoutParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String dateText = null;
                        String TimeText = null;
                        long val = 0;
                        int position = (int) view.getTag();
                        val = listEntities.get(position).getDateTime();
                        if(val!=0){
                            Date date = new Date(val);
                            SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
                            SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                            dateText = df2.format(date)  ;
                            TimeText=dfTime.format(date);
                            if(!TimeText.equalsIgnoreCase("1:00AM") &&
                                    !TimeText.equalsIgnoreCase("1:00 AM")){
                                dateText = dateText +", "+ TimeText;
                                if (listEntities.get(position).getEndTime() != null && !listEntities.get(position).getEndTime().equalsIgnoreCase("")) {
                                    if (!listEntities.get(position).getEndTime().equalsIgnoreCase("1:00AM") &&
                                            !listEntities.get(position).getEndTime().equalsIgnoreCase("1:00 AM")) {
                                        dateText = df2.format(date) + ", " + dfTime.format(date) + " - " + listEntities.get(position).getEndTime();
                                    } else {
                                        dateText = df2.format(date) + ", " + dfTime.format(date);
                                    }
                                }
                            }else {
                                dateText = df2.format(date)  ;
                            }

                        }
                        int ItemID = listEntities.get(position).getId();

                        NumberOfVolunteer = listEntities.get(position).getNumVolunteers();
                        VolunteerCount = listEntities.get(position).getVolunteerCount();
                        Total = NumberOfVolunteer - VolunteerCount;
                        if (Total <= 0) {
                            Prefs.setNumberofvolunteer(activity,"null");
                        } else {
                            Prefs.setTotalSpot(activity,""+Total);
                            Prefs.setNumberofvolunteer(activity,""+NumberOfVolunteer);
                        }
                        Prefs.setNumberofvolunteer(activity,null);
                        Prefs.setItemId(activity,ItemID);
                        Prefs.setName(activity, listEntities.get(position).getName());
                        Prefs.setDate(activity,dateText);
                        Prefs.setSignUpType(activity,null);
                        Intent intent = new Intent(activity, VolunteerDetailActivity.class);
                        intent.putExtra(FULL_LIST,true);
                        activity.startActivity(intent);
                    }
                });

//                versionViewHolder.imageViewFull.setImageResource(R.mipmap.full_volunteer);
                versionViewHolder.imageViewFull.setVisibility(View.VISIBLE);
                versionViewHolder.linearLayoutOpenSpot.setVisibility(View.GONE);
                versionViewHolder.linearLayoutVolunteered.setVisibility(View.VISIBLE);
                versionViewHolder.mTextViewVolunteered.setText(itemsBean.getVolunteerStatus());
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
        TextView mTextViewDate, mTextViewTitle,TextViewSignUp, mteTextViewVolunteerCount, mTextViewVolunteered;
        LinearLayout linearLayoutVolunteered,linearLayoutParent, linearLayoutOpenSpot;
        GifImageView imageViewSignUpType;
        ImageView imageViewFull,imageSignUp;


        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_date_name);
            mTextViewVolunteered = (TextView) itemView.findViewById(R.id.text_view_volunteered);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_date);
            mteTextViewVolunteerCount = (TextView) itemView.findViewById(R.id.text_view_volunteer_count);
            linearLayoutVolunteered = (LinearLayout) itemView.findViewById(R.id.layout_volunteered);
            linearLayoutOpenSpot = (LinearLayout) itemView.findViewById(R.id.layout_open_spot);
            linearLayoutParent = (LinearLayout) itemView.findViewById(R.id.layout_parent);
            TextViewSignUp = (TextView) itemView.findViewById(R.id.text_signup);
            imageSignUp = (ImageView) itemView.findViewById(R.id.img_signup);
            imageViewSignUpType = (GifImageView) itemView.findViewById(R.id.image_view_sign_up_type);
            imageViewFull = (ImageView) itemView.findViewById(R.id.image_view_full);
            mView = itemView;


        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}


