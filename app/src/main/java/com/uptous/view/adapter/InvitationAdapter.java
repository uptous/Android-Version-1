package com.uptous.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.InvitationResponseModel;
import com.uptous.view.activity.InvitationsActivity;

import java.util.List;

/**
 * Created by Prakash .
 */
public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.VersionViewHolder> {

    List<InvitationResponseModel> listEntities;
    InvitationsActivity activity;


    public InvitationAdapter(InvitationsActivity a, List<InvitationResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_invitations, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        versionViewHolder.mTextViewCommunityName.setText(listEntities.get(i).getCommunityName());
        versionViewHolder.mButtonJoin.setTag(i);
        versionViewHolder.mButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                int InvitationId = listEntities.get(position).getInvitationId();


                if (ConnectionDetector.isConnectingToInternet(activity)) {
                    if(activity instanceof InvitationsActivity)
                          activity.postApiInvite(InvitationId);
                } else {
                    Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        private TextView mTextViewCommunityName;
        private Button mButtonJoin;

        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewCommunityName = (TextView) itemView.findViewById(R.id.text_view_community_name);
            mButtonJoin = (Button) itemView.findViewById(R.id.button_join);
            mView = itemView;


        }
    }



}
