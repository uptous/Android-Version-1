package com.uptous.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uptous.R;
import com.uptous.model.AlbumDetailResponseModel;
import com.uptous.view.activity.SignUpSnackActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Prakash .
 */

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.VersionViewHolder> {

    List<AlbumDetailResponseModel> listEntities;
    Activity activity;



    public AlbumDetailAdapter(Activity a, List<AlbumDetailResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public AlbumDetailAdapter.VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_picture, viewGroup, false);
        AlbumDetailAdapter.VersionViewHolder viewHolder = new AlbumDetailAdapter.VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AlbumDetailAdapter.VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        Picasso.with(activity).load(listEntities.get(i).getPhoto()).placeholder(R.drawable.loader)
                .into(versionViewHolder.imageView);



    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewTitle;
        ImageView imageView;

        public VersionViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView_all);
            mView = itemView;



        }
    }



}
