package com.uptous.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uptous.R;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.FeaturedProductResponseModel;
import com.uptous.model.GetAllCommentResponseModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Prakash .
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.VersionViewHolder> {

    List<GetAllCommentResponseModel> listEntities;
    Activity activity;



    public CommentListAdapter(Activity a, List<GetAllCommentResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_comment, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        versionViewHolder.mTextViewProductName.setText(listEntities.get(i).getOwnerName());
        versionViewHolder.mTextViewCommentDes.setText(listEntities.get(i).getBody());
        Picasso.with(activity).load(listEntities.get(i).getOwnerPhotoUrl()).into(versionViewHolder.imageView);


        long val = listEntities.get(i).getCreateDate();
        Date date = new Date(val);
        SimpleDateFormat df2 = new SimpleDateFormat("MMM dd, yyyy");
        String dateText = df2.format(date);
        versionViewHolder.mTextViewDate.setText(dateText);
    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewProductName, mTextViewDate, mTextViewCommentDes;
        ImageView imageView;

        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewCommentDes = (TextView) itemView.findViewById(R.id.text_view_comment_description);
            mTextViewProductName = (TextView) itemView.findViewById(R.id.text_view_comment_user);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_comment_user);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_comment_date);
            mView = itemView;


        }
    }

}
