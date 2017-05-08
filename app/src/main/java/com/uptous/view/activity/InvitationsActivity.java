package com.uptous.view.activity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.model.InvitationResponseModel;
import com.uptous.view.adapter.InvitationAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : InvitationsActivity
 * Description :Show invited communities and also user can add communities.
 * Dependencies : InvitationAdapter
 */
public class InvitationsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageViewBack;
    private TextView mTextViewPendingInvitations, mTextViewInvitationcount;
    private RecyclerView mRecyclerViewInvitations;
    private String mAuthenticationId, mAuthenticationPassword;
    private List<InvitationResponseModel> mInvitationResponseModels = new ArrayList<>();
    private InvitationAdapter mInvitationAdapter;
    private int mSize;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);

        initView();

        getData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;
        }
    }


    //Method to initialize views
    private void initView() {

        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        LinearLayout linearLayoutLeftMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        ImageView imageViewInvitations = (ImageView) findViewById(R.id.image_view_community_invitations);
        LinearLayout linearLayoutInvitations = (LinearLayout) findViewById(R.id.layout_invitations);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(InvitationsActivity.this, LinearLayoutManager.VERTICAL, false);

        mRecyclerViewInvitations = (RecyclerView) findViewById(R.id.recycler_view_invitations);
        mRecyclerViewInvitations.setLayoutManager(layoutManager);
        mTextViewInvitationcount = (TextView) findViewById(R.id.text_view_invitation_count);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mTextViewPendingInvitations = (TextView) findViewById(R.id.text_view_pending_invitation);

        mImageViewBack.setVisibility(View.VISIBLE);
        linearLayoutLeftMenu.setVisibility(View.GONE);
        linearLayoutInvitations.setVisibility(View.VISIBLE);
        imageViewInvitations.setVisibility(View.VISIBLE);
        imageViewFilter.setVisibility(View.GONE);
        textViewTitle.setText(R.string.invitations);

        clickListenerOnViews();

        getApiInvitation();

    }

    // Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);

    }

    //Method to set clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
    }

    // Get webservice to show all UpToUs Invitation
    public void getApiInvitation() {
        final ProgressDialog progressDialog = new ProgressDialog(InvitationsActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        progressDialog.setCancelable(false);

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<InvitationResponseModel>> call = service.GetInvitation();

        call.enqueue(new Callback<List<InvitationResponseModel>>() {
            @Override
            public void onResponse(Call<List<InvitationResponseModel>> call, Response<List<InvitationResponseModel>> response) {
                try {
                    progressDialog.dismiss();
                    if (response.body() != null) {

                        mSize = response.body().size();
                        mTextViewPendingInvitations.
                                setText(Html.fromHtml("Pending Invitations: <font color=#ff0000>" + String.valueOf(mSize) + "</font>"));
                        mTextViewInvitationcount.setText("" + mSize);
                        mInvitationResponseModels = response.body();
                        mInvitationAdapter = new InvitationAdapter(InvitationsActivity.this, mInvitationResponseModels);
                        mRecyclerViewInvitations.setAdapter(mInvitationAdapter);
                    } else {
                        final CustomizeDialog customizeDialog = new CustomizeDialog(InvitationsActivity.this);
                        customizeDialog.setCancelable(false);
                        customizeDialog.setContentView(R.layout.dialog_password_change);
                        TextView textViewOk = (TextView) customizeDialog.findViewById(R.id.text_view_log_out);
                        customizeDialog.show();
                        textViewOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                customizeDialog.dismiss();
                                logout();

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<InvitationResponseModel>> call, Throwable t) {
                Toast.makeText(InvitationsActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
    }

    //Method to logout from app
    private void logout() {

        MainActivity activity = new MainActivity();
        activity.logOut();
        Application app = getApplication();
        Intent intent = new Intent(app, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);
    }

}
