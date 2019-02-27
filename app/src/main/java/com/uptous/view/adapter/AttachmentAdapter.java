package com.uptous.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.model.FileResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.activity.WebviewActivity;

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
        String s = listEntities.get(i).getPath();
        String result = s.substring(s.lastIndexOf(".") + 1);
        if (result.equalsIgnoreCase("jpeg")||result.equalsIgnoreCase("png")) {
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

        versionViewHolder.parentRow.setTag(i);
        versionViewHolder.parentRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                String path = listEntities.get(position).getPath();
                String s = listEntities.get(position).getTitle();
                String result = path.substring(path.lastIndexOf(".") + 1);

                if (result.equalsIgnoreCase("jpeg")) {
                    Prefs.setImagepath(activity, path);
                    Prefs.setAlbumDetail(activity, "albumdetail");
                    Intent intent = new Intent(activity, WebviewActivity.class);
                    activity.startActivity(intent);
                } else if (result.equalsIgnoreCase("jpg")
                        ||result.equalsIgnoreCase("png") ) {
                    Prefs.setImagepath(activity, path);
                    Prefs.setAlbumDetail(activity, "albumdetail");
                    Intent intent = new Intent(activity, WebviewActivity.class);
                    activity.startActivity(intent);
                } else if (result.equalsIgnoreCase("MOV")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(path), "video/*");
                    activity.startActivity(intent);
                } else if (result.equalsIgnoreCase("mp3")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(path), "audio/*");
                    activity.startActivity(intent);
                } else if (result.equalsIgnoreCase("xlsx") || result.equalsIgnoreCase("xls") ||
                        result.equalsIgnoreCase("pdf") || result.equalsIgnoreCase("docx") ||
                        result.equalsIgnoreCase("tif")) {
                    Prefs.setpath(activity, path);

                    Prefs.setAlbumDetail(activity, "albumdetail");
                    Intent intent = new Intent(activity, WebviewActivity.class);
                    activity.startActivity(intent);
                } else if (result.equalsIgnoreCase("zip")) {
                    Toast.makeText(activity, "Files in this format cannot be \n download to the Android", Toast.LENGTH_SHORT).
                            show();
                } else if (result.equalsIgnoreCase("csv")) {


                    Toast.makeText(activity, "Files in this format cannot be \n download to the Android", Toast.LENGTH_SHORT).
                            show();
                } else {

                    Toast.makeText(activity, "Files in this format cannot be \n download to the Android", Toast.LENGTH_SHORT).
                            show();
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
        TextView mTextViewProductName;
        ImageView imageView, mImageViewDownload;
        LinearLayout parentRow;

        public VersionViewHolder(View itemView) {
            super(itemView);
            parentRow = (LinearLayout) itemView.findViewById(R.id.row);
            mTextViewProductName = (TextView) itemView.findViewById(R.id.text_view_file_name);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_files);
            mImageViewDownload = (ImageView) itemView.findViewById(R.id.image_view_down_arrow);
            mView = itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
