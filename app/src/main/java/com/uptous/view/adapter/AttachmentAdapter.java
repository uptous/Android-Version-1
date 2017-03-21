package com.uptous.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.model.FileResponseModel;

import java.util.List;

/**
 * Created by Prakash .
 */
public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.VersionViewHolder> {

    List<FileResponseModel> listEntities;
    Activity activity;


    public AttachmentAdapter(Activity a, List<FileResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_files, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass

        String s = listEntities.get(i).getTitle();
        String result = s.substring(s.lastIndexOf(".") + 1);

        if (result.equalsIgnoreCase("jpeg")) {
            versionViewHolder.imageView.setImageResource(R.mipmap.imageicon);
        } else if (result.equalsIgnoreCase("pdf")) {
            versionViewHolder.imageView.setImageResource(R.mipmap.pdficon);
        } else if (result.equalsIgnoreCase("docx")) {
            versionViewHolder.imageView.setImageResource(R.mipmap.wordicon);
        } else if (result.equalsIgnoreCase("xls")) {
            versionViewHolder.imageView.setImageResource(R.mipmap.excellicon);
        } else if (result.equalsIgnoreCase("xlsx")) {
            versionViewHolder.imageView.setImageResource(R.mipmap.excellicon);
        } else if (result.equalsIgnoreCase("jpg")) {
            versionViewHolder.imageView.setImageResource(R.mipmap.imageicon);
        } else {
            versionViewHolder.imageView.setImageResource(R.mipmap.clipicon);
        }
        versionViewHolder.mTextViewProductName.setText(listEntities.get(i).getTitle().replace("%20", " "));


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


            mTextViewProductName = (TextView) itemView.findViewById(R.id.text_view_file_name);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_files);
            mView = itemView;


        }
    }

}
