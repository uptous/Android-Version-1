package com.uptous.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.model.InvitationResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.InvitationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.uptous.controller.utils.Utilities.isLastAppActivity;

/**
 * FileName : InvitationsActivity
 * Description :Show invited communities and also user can add communities.
 * Dependencies : InvitationAdapter
 */
public class InvitationsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mImageViewBack;
    public static String INVITATION_ID ="InvitationValue";
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
//        Log.i("Info",isLastAppActivity());
        if (getIntent().hasExtra(INVITATION_ID)) {
            //In case of intent extra values
            Bundle extras = getIntent().getExtras();
            String invitationid = extras.getString(INVITATION_ID);
            if (!invitationid.equals(null)) {
                postApiInvite(Integer.parseInt(invitationid));
            }
        }
        else {
            getApiInvitation();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isLastAppActivity(this))
            startActivity(new Intent(this,MainActivity.class));
        finish();
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



    }

    // Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword = Prefs.getAuthenticationPassword(this);

    }

    //Method to set clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
    }

    // Get webservice to show all UpToUs Invitation
    public void getApiInvitation() {
        showProgressDialog();

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<InvitationResponseModel>> call = service.GetInvitation();

        call.enqueue(new Callback<List<InvitationResponseModel>>() {
            @Override
            public void onResponse(Call<List<InvitationResponseModel>> call, Response<List<InvitationResponseModel>> response) {
                try {
                    hideProgressDialog();
                    if (response.body() != null) {
                        mSize = response.body().size();
                        mTextViewPendingInvitations.
                                setText(Html.fromHtml("Pending Invitations: <font color=#ff0000>" + String.valueOf(mSize) + "</font>"));
                        mTextViewInvitationcount.setText("" + mSize);
                        mInvitationResponseModels = response.body();
                        mInvitationAdapter = new InvitationAdapter(InvitationsActivity.this, mInvitationResponseModels);
                        mRecyclerViewInvitations.setAdapter(mInvitationAdapter);
                    } else {
                        showLogOutDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<InvitationResponseModel>> call, Throwable t) {
               showToast(getString(R.string.error));
                hideProgressDialog();
            }

        });
    }


    // Post webservice to post all  comments
    public void postApiInvite(final int InvitationId) {
        String mAuthenticationId = Prefs.getAuthenticationId(this);
        String mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
        showProgressDialog();

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);

        Call<JsonElement> call = service.PostInvite(InvitationId, "");
        call.enqueue(new retrofit2.Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hideProgressDialog();
                Log.i("Info",""+response.errorBody());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InvitationsActivity.this, R.string.invitation_accepted, Toast.LENGTH_SHORT).show();
                            getApiInvitation();
                        }
                    }
                }
                else {
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(InvitationsActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(InvitationsActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
              hideProgressDialog();
            }
        });
    }


}
