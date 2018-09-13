package com.uptous.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.model.SignUpDetailResponseModel;

import java.util.List;

/**
 * Created by Prakash .
 */
public class RSPVDetailAdapter extends RecyclerView.Adapter<RSPVDetailAdapter.VersionViewHolder> {

    List<SignUpDetailResponseModel.ItemsBean.VolunteersBean> listEntities;
    Activity activity;



    public RSPVDetailAdapter(Activity a, List<SignUpDetailResponseModel.ItemsBean.VolunteersBean> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_volunteer_comment, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass

        versionViewHolder.mTextViewCommentedUserName.setText(listEntities.get(i).getFirstName());
        versionViewHolder.mTextViewComment.setText(listEntities.get(i).getPhone());

    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewCommentedUserName, mTextViewComment;


        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewCommentedUserName = (TextView) itemView.findViewById(R.id.text_view_comment_name);

            mTextViewComment = (TextView) itemView.findViewById(R.id.text_view_comment);
            mView = itemView;


        }
    }
}


