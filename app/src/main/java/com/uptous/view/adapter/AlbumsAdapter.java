package com.uptous.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uptous.R;
import com.uptous.model.PhotoAlbumResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.AlbumDetailActivity;

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
        versionViewHolder.imageView.setTag(i);
        versionViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Position = (int) view.getTag();
                int AlbumID = listEntities.get(Position).getId();
                Prefs.setNewsItemId(activity, AlbumID);
                Prefs.setAlbumDetail(activity, "albumdetail");
                Intent intent = new Intent(activity, AlbumDetailActivity.class);
                activity.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewProductName;
        ImageView imageView;

        public VersionViewHolder(View itemView) {
            super(itemView);


            mTextViewProductName = (TextView) itemView.findViewById(R.id.text_view_image_name);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_albums);
            mView = itemView;


        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

}
