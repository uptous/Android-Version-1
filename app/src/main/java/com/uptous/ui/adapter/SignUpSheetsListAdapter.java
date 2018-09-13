package com.uptous.ui.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.SignUpResponseModel;
import com.uptous.ui.activity.MessagePostActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash .
 */
public class SignUpSheetsListAdapter extends RecyclerView.Adapter<SignUpSheetsListAdapter.VersionViewHolder> {

    List<SignUpResponseModel> listEntities;
    List<CommnunitiesResponseModel> listEntitiesCommunity;
    Activity activity;


    public SignUpSheetsListAdapter(Activity a, List<SignUpResponseModel> listEntities, List<CommnunitiesResponseModel> listEntitiesCommunity) {

        this.listEntities = listEntities;
        this.listEntitiesCommunity = listEntitiesCommunity;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sign_up, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass

        long val = listEntities.get(i).getDateTime();
        if (val == 0) {
            versionViewHolder.mTextViewDate.setText("Event date: ");
        } else {
            Date date = new Date(val);
            SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
            final String dateText = df2.format(date);
            versionViewHolder.mTextViewDate.setText("Event date: " + dateText);

        }
        versionViewHolder.mTextViewTitle.setText(listEntities.get(i).getName());

        int ID = listEntities.get(i).getCommunityId();
        try {

            for (int j = 0; listEntitiesCommunity.size() > j; j++) {
                int CommId = listEntitiesCommunity.get(j).getId();
                if (CommId == ID) {
                    versionViewHolder.mTextViewType.setText(listEntitiesCommunity.get(j).getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewDate, mTextViewTitle, mTextViewType;


        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_view_title);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_view_event_date);
            mTextViewType = (TextView) itemView.findViewById(R.id.text_view_community_name);
            mView = itemView;


        }
    }
}


