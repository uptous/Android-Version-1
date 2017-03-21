package com.uptous.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uptous.R;
import com.uptous.model.AlbumDetailResponseModel;

import java.util.List;

/**
 * Created by Prakash on 1/3/2017.
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



        Picasso.with(activity).load(listEntities.get(i).getPhoto()).into(versionViewHolder.imageView);


    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewTitle, mTextViewProductModel, mTextViewProductName;
        ImageView imageView;

        public VersionViewHolder(View itemView) {
            super(itemView);


            imageView = (ImageView) itemView.findViewById(R.id.imageView_all);
            mView = itemView;

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

        }
    }


}
