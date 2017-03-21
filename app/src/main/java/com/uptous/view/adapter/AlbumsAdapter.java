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
import com.uptous.model.PhotoAlbumResponseModel;

import java.util.List;

/**
 * Created by Prakash .
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.VersionViewHolder> {

    List<PhotoAlbumResponseModel> listEntities;
    Activity activity;



    public AlbumsAdapter(Activity a, List<PhotoAlbumResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_albums, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass



        versionViewHolder.mTextViewProductName.setText(listEntities.get(i).getTitle().replace("%20", " "));
        Picasso.with(activity).load(listEntities.get(i).getThumb()).into(versionViewHolder.imageView);


    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView  mTextViewProductName;
        ImageView imageView;

        public VersionViewHolder(View itemView) {
            super(itemView);


            mTextViewProductName = (TextView) itemView.findViewById(R.id.text_view_image_name);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_albums);
            mView = itemView;



        }
    }

}
