package com.uptous.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.model.CommnunitiesResponseModel;

import java.util.List;

/**
 * Created by Prakash .
 */
public class CommunityListFilterAdapter extends RecyclerView.Adapter<CommunityListFilterAdapter.VersionViewHolder> {

    List<CommnunitiesResponseModel> listEntities;
    Activity activity;



    public CommunityListFilterAdapter(Activity a, List<CommnunitiesResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_community_for_filter, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        try {
            versionViewHolder.mTextViewCommunity.setText(listEntities.get(i).getName());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewCommunity;


        public VersionViewHolder(View itemView) {


            super(itemView);

            mTextViewCommunity=(TextView)itemView.findViewById(R.id.text_view_community_name_sorting);

        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}


