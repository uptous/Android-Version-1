package com.uptous.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.SignUpResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.SignUpDRIVERActivity;
import com.uptous.view.activity.SignUpOngoingActivity;
import com.uptous.view.activity.SignUpPartyActivity;
import com.uptous.view.activity.SignUpRSVPActivity;
import com.uptous.view.activity.SignUpShiftsActivity;
import com.uptous.view.activity.SignUpSnackActivity;
import com.uptous.view.activity.SignUpVoteActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by Prakash .
 */
public class SignUpSheetsListAdapter extends RecyclerView.Adapter<SignUpSheetsListAdapter.VersionViewHolder> {

    List<SignUpResponseModel> listEntities;
    private List<CommnunitiesResponseModel> mListEntitiesCommunity;
    Activity activity;


    public SignUpSheetsListAdapter(Activity a, List<SignUpResponseModel> listEntities, List<CommnunitiesResponseModel> listEntitiesCommunity) {

        this.listEntities = listEntities;
        this.mListEntitiesCommunity = listEntitiesCommunity;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sign_up, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass

        long val = listEntities.get(i).getDateTime();
        if (val == 0) {
            versionViewHolder.mTextViewDate.setText("Event date: ");
        } else {
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            final String dateText = df2.format(date);
            versionViewHolder.mTextViewDate.setText("Event date: " + dateText);
        }
        long cutoffVal = listEntities.get(i).getCutoffDate();
        if (cutoffVal == 0) {
            versionViewHolder.mTextViewCutoffDate.setText("Cutoff date: ");
        } else {
            Date date = new Date(cutoffVal);
            SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
            df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
            final String dateText = df2.format(date);
            versionViewHolder.mTextViewCutoffDate.setText("Cutoff date: " + dateText);
        }

        versionViewHolder.mTextViewTitle.setText(listEntities.get(i).getName());

        int ID = listEntities.get(i).getCommunityId();
        try {

            for (int j = 0; mListEntitiesCommunity.size() > j; j++) {
                int CommId = mListEntitiesCommunity.get(j).getId();
                if (CommId == ID) {
                    versionViewHolder.mTextViewType.setText(mListEntitiesCommunity.get(j).getName());
                }
            }
            versionViewHolder.mView.setTag(i);
            versionViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    if (listEntities.get(position).getType().equalsIgnoreCase("RSVP")) {
                        int OpId = listEntities.get(position).getId();
                        Prefs.setOpportunityId(activity, OpId);
//                        signupdetail
                        Prefs.setSignUpDetail(activity, "signupdetail");
                        Intent intent = new Intent(activity, SignUpRSVPActivity.class);
                        activity.startActivity(intent);
                    } else if (listEntities.get(position).getType().equalsIgnoreCase("Vote")) {
                        int OpId = listEntities.get(position).getId();
                        Prefs.setOpportunityId(activity, OpId);
                        Prefs.setSignUpDetail(activity, "signupdetail");
                        Intent intent = new Intent(activity, SignUpVoteActivity.class);
                        activity.startActivity(intent);
                    } else if (listEntities.get(position).getType().equalsIgnoreCase("Potluck/Party")) {
                        int OpId = listEntities.get(position).getId();
                        Prefs.setOpportunityId(activity, OpId);
                        Prefs.setSignUpDetail(activity, "signupdetail");
                        Intent intent = new Intent(activity, SignUpPartyActivity.class);
                        activity.startActivity(intent);
                    } else if (listEntities.get(position).getType().equalsIgnoreCase("Drivers")) {
                        int OpId = listEntities.get(position).getId();
                        Prefs.setOpportunityId(activity, OpId);
                        Prefs.setSignUpDetail(activity, "signupdetail");
                        Intent intent = new Intent(activity, SignUpDRIVERActivity.class);
                        activity.startActivity(intent);
                    } else if (listEntities.get(position).getType().equalsIgnoreCase("Shifts") ||
                            listEntities.get(position).getType().equalsIgnoreCase("Games") ||
                            listEntities.get(position).getType().equalsIgnoreCase("Wish List") ||
                            listEntities.get(position).getType().equalsIgnoreCase("Volunteer") ||
                            listEntities.get(position).getType().equalsIgnoreCase("Multi Game/Event RSVP")) {
                        int OpId = listEntities.get(position).getId();
                        Prefs.setOpportunityId(activity, OpId);
                        Prefs.setSignUpType(activity, listEntities.get(position).getType());
                        Prefs.setSignUpDetail(activity, "signupdetail");
                        Intent intent = new Intent(activity, SignUpShiftsActivity.class);
                        activity.startActivity(intent);
                    } else if (listEntities.get(position).getType().equalsIgnoreCase("Snack Schedule")) {
                        int OpId = listEntities.get(position).getId();
                        Prefs.setOpportunityId(activity, OpId);
                        Prefs.setSignUpDetail(activity, "signupdetail");
                        Prefs.setSignUpType(activity, listEntities.get(position).getType());
                        Intent intent = new Intent(activity, SignUpSnackActivity.class);
                        activity.startActivity(intent);
                    } else if (listEntities.get(position).getType().equalsIgnoreCase("Ongoing Volunteering")) {
                        int OpId = listEntities.get(position).getId();
                        Prefs.setOpportunityId(activity, OpId);
                        Prefs.setSignUpDetail(activity, "signupdetail");
                        Prefs.setSignUpType(activity, listEntities.get(position).getType());
                        Intent intent = new Intent(activity, SignUpOngoingActivity.class);
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, R.string.sing_up_not_allow, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } catch (Exception e) {
            Log.e(getClass().getName(), "" + e);
        }


    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewDate, mTextViewCutoffDate, mTextViewTitle, mTextViewType;
        ImageView imageViewDetail;


        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_event_date);
            mTextViewCutoffDate = (TextView) itemView.findViewById(R.id.text_view_cutoff_date);
            mTextViewType = (TextView) itemView.findViewById(R.id.text_view_community_name);
            imageViewDetail = (ImageView) itemView.findViewById(R.id.image_view_detail);
            mView = itemView;


        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}


