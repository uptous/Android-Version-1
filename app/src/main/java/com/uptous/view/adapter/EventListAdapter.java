package com.uptous.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.EventResponseModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Prakash .
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.VersionViewHolder> {

    List<EventResponseModel> listEntities;
    private List<CommnunitiesResponseModel> mListEntitiesCommunity;
    Activity activity;


    public EventListAdapter(Activity a, List<EventResponseModel> listEntities, List<CommnunitiesResponseModel> listEntitiesCommunity) {

        this.listEntities = listEntities;
        this.mListEntitiesCommunity = listEntitiesCommunity;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_events, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        try {
            versionViewHolder.mTextViewTitle.setText(listEntities.get(i).getTitle());
            versionViewHolder.mTextViewCollapsedTitle.setText(listEntities.get(i).getTitle());
            versionViewHolder.mTextViewDescription.setText(listEntities.get(i).getDescription());
            versionViewHolder.mTextViewLocationValue.setText(listEntities.get(i).getLocation());
            versionViewHolder.mTextViewLocationValueCollapsed.setText(listEntities.get(i).getLocation());

            long val = listEntities.get(i).getStartTime();

            if (val != 0) {
                Date date = new Date(val);
                SimpleDateFormat df2 = new SimpleDateFormat("EE, MMM d");
                df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                String dateText = df2.format(date);
                String dateTime = dfTime.format(date);
                versionViewHolder.mTextViewCollapsedStartTime.setText(dateText + "\n" + dateTime);
                versionViewHolder.mTextViewStartTime.setText(dateText + "\n" + dateTime);
            } else {
                versionViewHolder.mTextViewCollapsedStartTime.setVisibility(View.GONE);
                versionViewHolder.mTextViewStartTime.setVisibility(View.GONE);
            }

            long valEndTime = listEntities.get(i).getEndTime();

            if (valEndTime != 0) {
                Date date = new Date(valEndTime);
                SimpleDateFormat df2 = new SimpleDateFormat("EE, MMM d");
                df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                String dateText = df2.format(date);
                String dateTime = dfTime.format(date);
                versionViewHolder.mTextViewCollapsedEndTime.setText(dateText + "\n" + dateTime);
                versionViewHolder.mTextViewEndTime.setText(dateText + "\n" + dateTime);
            } else {
                versionViewHolder.mTextViewCollapsedEndTime.setVisibility(View.GONE);
                versionViewHolder.mTextViewEndTime.setVisibility(View.GONE);
            }
            int ID = listEntities.get(i).getCommunityId();


            for (int j = 0; mListEntitiesCommunity.size() > j; j++) {
                int CommId = mListEntitiesCommunity.get(j).getId();
                if (CommId == ID) {
                    versionViewHolder.mTextViewCommunityNameCollapsed.setText(mListEntitiesCommunity.get(j).getName());
                    versionViewHolder.mTextViewCommunityNameExpand.setText(mListEntitiesCommunity.get(j).getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewCollapsedStartTime,mTextViewLocationValueCollapsed, mTextViewCollapsedEndTime, mTextViewCollapsedTitle, mTextViewTitle, mTextViewDescription, mTextViewTitleLocation,
                mTextViewLocationValue, mTextViewStartTime, mTextViewEndTime, mTextViewCommunityNameCollapsed, mTextViewCommunityNameExpand;
        private boolean mIsSelected = false;
        LinearLayout mLayoutExpand;
        RelativeLayout mRelativeLayoutCollapsed;
        private ImageView mImageViewExpand, mImageViewCollapsed;

        public VersionViewHolder(View itemView) {
            super(itemView);
            mTextViewCollapsedStartTime = (TextView) itemView.findViewById(R.id.text_view_start_time_collapsed);
            mTextViewCollapsedEndTime = (TextView) itemView.findViewById(R.id.text_view_end_time_collapsed);
            mTextViewCollapsedTitle = (TextView) itemView.findViewById(R.id.text_view_title_collpsed);
            mImageViewExpand = (ImageView) itemView.findViewById(R.id.image_view_event_expand);
            mImageViewCollapsed = (ImageView) itemView.findViewById(R.id.image_view_event_collpsd);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_title_expand);
            mTextViewStartTime = (TextView) itemView.findViewById(R.id.text_view_start_time_expand);
            mTextViewEndTime = (TextView) itemView.findViewById(R.id.text_view_end_time_expand);
            mTextViewDescription = (TextView) itemView.findViewById(R.id.text_view_details_value_expand);
            mTextViewTitleLocation = (TextView) itemView.findViewById(R.id.text_view_location_expand);
            mRelativeLayoutCollapsed = (RelativeLayout) itemView.findViewById(R.id.layout_collapsed);
            mLayoutExpand = (LinearLayout) itemView.findViewById(R.id.layout_expand);
            mTextViewLocationValue = (TextView) itemView.findViewById(R.id.text_view_location_value_expand);
            mTextViewLocationValueCollapsed = (TextView) itemView.findViewById(R.id.text_view_loction_value_collapsed);
            mTextViewCommunityNameCollapsed = (TextView) itemView.findViewById(R.id.text_view_community_collapsed);
            mTextViewCommunityNameExpand = (TextView) itemView.findViewById(R.id.text_view_community_expand);
            mView = itemView;
            mImageViewExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIsSelected == false) {

                        mLayoutExpand.setVisibility(View.VISIBLE);
                        mRelativeLayoutCollapsed.setVisibility(View.GONE);
                        mIsSelected = true;
                    } else {

                        mLayoutExpand.setVisibility(View.GONE);
                        mRelativeLayoutCollapsed.setVisibility(View.VISIBLE);
                        mIsSelected = false;
                    }

                }
            });
            mImageViewCollapsed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIsSelected == false) {

                        mLayoutExpand.setVisibility(View.VISIBLE);
                        mRelativeLayoutCollapsed.setVisibility(View.GONE);
                        mIsSelected = true;
                    } else {

                        mLayoutExpand.setVisibility(View.GONE);
                        mRelativeLayoutCollapsed.setVisibility(View.VISIBLE);
                        mIsSelected = false;
                    }
                }
            });

        }
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }
}


