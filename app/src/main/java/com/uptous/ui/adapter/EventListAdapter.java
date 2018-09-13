package com.uptous.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uptous.model.Event;
import com.uptous.model.FeaturedProductResponseModel;
import com.uptous.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Prakash .
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.VersionViewHolder> {

    List<Event> listEntities;
    Activity activity;



    public EventListAdapter(Activity a, List<Event> listEntities) {

        this.listEntities = listEntities;
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

            long valStartTimeCollapsed = listEntities.get(i).getStartTime();
            Date dateCollasped = new Date(valStartTimeCollapsed);
            SimpleDateFormat dfCollpased = new SimpleDateFormat("EEE MMM dd");
            String dateTextCollpased = dfCollpased.format(dateCollasped);
            versionViewHolder.mTextViewCollapsedStartTime.setText(dateTextCollpased);

            long valStartTime = listEntities.get(i).getStartTime();
            Date date = new Date(valStartTime);
            SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd");
            String dateText = df1.format(date);
            versionViewHolder.mTextViewStartTime.setText(dateText);


            long valEndTime = listEntities.get(i).getEndTime();
            Date dateEnd = new Date(valEndTime);
            SimpleDateFormat df2 = new SimpleDateFormat("EEEE MMM dd");
            String dateText1 = df2.format(dateEnd);
            versionViewHolder.mTextViewEndTime.setText(dateText1);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        versionViewHolder.mTextViewSubTitle.setText(listEntities.get(i).getProductname());


    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewCollapsedStartTime,mTextViewCollapsedTitle, mTextViewTitle, mTextViewDescription, mTextViewTitleLocation,
                mTextViewLocationValue, mTextViewStartTime, mTextViewEndTime;
        private boolean mIsSelected = false;
        LinearLayout mLayoutExpand;
     RelativeLayout mRelativeLayoutCollapsed;
        private ImageView mImageViewExpand, mImageViewCollapsed;

        public VersionViewHolder(View itemView) {
            super(itemView);
            mTextViewCollapsedStartTime=(TextView)itemView.findViewById(R.id.text_view_start_time_collapsed) ;
            mTextViewCollapsedTitle=(TextView)itemView.findViewById(R.id.text_view_title_collpsed) ;
            mImageViewExpand = (ImageView) itemView.findViewById(R.id.image_view_event_expand);
            mImageViewCollapsed = (ImageView) itemView.findViewById(R.id.image_view_event_collpsd);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_title);
            mTextViewStartTime = (TextView) itemView.findViewById(R.id.text_view_start_time);
            mTextViewEndTime = (TextView) itemView.findViewById(R.id.text_view_end_time);
            mTextViewDescription = (TextView) itemView.findViewById(R.id.text_view_details_value);
            mTextViewTitleLocation = (TextView) itemView.findViewById(R.id.text_view_loction);
            mRelativeLayoutCollapsed = (RelativeLayout) itemView.findViewById(R.id.layout_collapsed);
            mLayoutExpand = (LinearLayout) itemView.findViewById(R.id.layout_expand);
            mTextViewLocationValue = (TextView) itemView.findViewById(R.id.text_view_loction_value);
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
}


