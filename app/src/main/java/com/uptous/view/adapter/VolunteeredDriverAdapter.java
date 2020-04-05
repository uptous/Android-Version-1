package com.uptous.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.sharedpreference.Prefs;

import java.util.List;

/**
 * Created by Prakash .
 */
public class VolunteeredDriverAdapter extends RecyclerView.Adapter<VolunteeredDriverAdapter.VersionViewHolder> {

    List<SignUpDetailResponseModel.ItemsBean.VolunteersBean> listEntities;
    Activity activity;

    private static int counter = 0;
    String mStringType;

    public VolunteeredDriverAdapter(Activity a, List<SignUpDetailResponseModel.ItemsBean.VolunteersBean> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;
        mStringType = Prefs.getSignUpType(a);

    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_driver_comment, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass

        versionViewHolder.mTextViewUserName.setText(listEntities.get(i).getFirstName()
        );

        versionViewHolder.mTextViewComment.setText(listEntities.get(i).getComment());
        versionViewHolder.mTextViewPhone.setText(listEntities.get(i).getPhone());
    }


    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewUserName, mTextViewComment, mTextViewPhone;


        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewComment = (TextView) itemView.findViewById(R.id.text_view_comment);


            mTextViewUserName = (TextView) itemView.findViewById(R.id.text_view_user_name);
            mTextViewPhone = (TextView) itemView.findViewById(R.id.text_view_phone);

            mView = itemView;


        }
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}


