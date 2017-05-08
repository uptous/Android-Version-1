package com.uptous.view.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.GetAllCommentResponseModel;
import com.uptous.model.InvitationResponseModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.view.activity.CommentDetailActivity;
import com.uptous.view.activity.InvitationsActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Prakash .
 */
public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.VersionViewHolder> {

    List<InvitationResponseModel> listEntities;
    InvitationsActivity activity;


    public InvitationAdapter(InvitationsActivity a, List<InvitationResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_invitations, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        versionViewHolder.mTextViewCommunityName.setText(listEntities.get(i).getCommunityName());
        versionViewHolder.mButtonJoin.setTag(i);
        versionViewHolder.mButtonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int) view.getTag();
                int InvitationId = listEntities.get(position).getInvitationId();
                MyApplication.editor.putInt("InvitationId", InvitationId);
                MyApplication.editor.commit();

                if (ConnectionDetector.isConnectingToInternet(activity)) {
                    postApiInvite();
                } else {
                    Toast.makeText(activity, R.string.network_error, Toast.LENGTH_SHORT).show();
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
        private TextView mTextViewCommunityName;
        private Button mButtonJoin;

        public VersionViewHolder(View itemView) {
            super(itemView);

            mTextViewCommunityName = (TextView) itemView.findViewById(R.id.text_view_community_name);
            mButtonJoin = (Button) itemView.findViewById(R.id.button_join);
            mView = itemView;


        }
    }

    // Post webservice to post all  comments
    private void postApiInvite() {

        String mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        String mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        final ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        int InvitationId = MyApplication.mSharedPreferences.getInt("InvitationId", 0);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<PostCommentResponseModel> call = service.PostInvite(InvitationId, "");
        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        if (response.isSuccessful()) {

                            activity.getApiInvitation();

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();


            }
        });
    }

}
