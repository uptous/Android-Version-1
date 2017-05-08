package com.uptous.view.activity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.controller.utils.RoundedImageView;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.view.adapter.SignUpPartyAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : SignUpPartyActivity
 * Description :show all Party,Potluck sign_up open spots, volunteer, full.
 * Dependencies : SignUpPartyActivity
 */

public class SignUpPartyActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerViewOpenSpot;

    private ImageView mImageViewBack;

    private TextView mTextViewTitle, mViewEventDescriptionTextView, mViewOrganizerOneTextView,
            mViewOrganizerSecondTextView, mTextViewEventDateSignUp, mTextViewOrgnizer, mTextViewFirstNameContactOne,
            mTextViewSecondNameContactOne, mTextViewSecondNameContactTwo, mTextViewFirstNameContactTwo,mTextViewCutOffDateSignUp;

    private RoundedImageView mViewOrganizerOneRoundedImageView, mViewOrganizerSecondRoundedImageView;

    private SignUpPartyAdapter mSignUpPartyAdapter;

    private String mAuthenticationId, mAuthenticationPassword, mResultOne, mResultTwo;

    private LinearLayout mLinearLayoutBackgroundFirstContact, mLinearLayoutBackgroundSecondContact, mLinearLayoutOrganizerOne,
            mLinearLayoutOrganizerTwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_back:
                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConnectionDetector.isConnectingToInternet(this)) {
            getApiSignUpDetail();
        } else {
            Toast.makeText(SignUpPartyActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    //Method to initialize view
    private void initView() {

        //Local Variables Initialization
        LinearLayout linearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        LinearLayout linearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //Global Variables Initialization
        mRecyclerViewOpenSpot = (RecyclerView) findViewById(R.id.recycler_view_open_spots);
        mRecyclerViewOpenSpot.setLayoutManager(layoutManager);

        mLinearLayoutOrganizerOne = (LinearLayout) findViewById(R.id.layout_organizer_one);
        mLinearLayoutOrganizerTwo = (LinearLayout) findViewById(R.id.layout_organizer_two);
        mLinearLayoutBackgroundFirstContact = (LinearLayout) findViewById(R.id.layout_contact);
        mLinearLayoutBackgroundSecondContact = (LinearLayout) findViewById(R.id.layout_contact_image_second);

        mTextViewOrgnizer = (TextView) findViewById(R.id.text_view_title_organizers);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_message_toolbar);
        mViewEventDescriptionTextView = (TextView) findViewById(R.id.text_view_event_description);
        mViewOrganizerOneTextView = (TextView) findViewById(R.id.text_view_organizer_one);
        mViewOrganizerSecondTextView = (TextView) findViewById(R.id.text_view_organizer_second);
        mTextViewEventDateSignUp = (TextView) findViewById(R.id.text_view_event_date_sign_up);
        mTextViewFirstNameContactOne = (TextView) findViewById(R.id.textview_first_name);
        mTextViewSecondNameContactOne = (TextView) findViewById(R.id.textview_last_name);
        mTextViewFirstNameContactTwo = (TextView) findViewById(R.id.textview_first_name_second_contact);
        mTextViewSecondNameContactTwo = (TextView) findViewById(R.id.textview_last_name_second_contact);
        mTextViewCutOffDateSignUp = (TextView) findViewById(R.id.text_view_cutoff_date);
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mViewOrganizerOneRoundedImageView = (RoundedImageView) findViewById(R.id.image_view_contact_one);
        mViewOrganizerSecondRoundedImageView = (RoundedImageView) findViewById(R.id.image_view_contact_two);

        linearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        linearLayoutCommunityFilter.setVisibility(View.GONE);
        imageViewFilter.setVisibility(View.GONE);

        getData();

        clickListenerOnViews();

//        if (ConnectionDetector.isConnectingToInternet(this)) {
//            getApiSignUpDetail();
//        } else {
//            Toast.makeText(SignUpPartyActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
//        }

    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
    }

    // Get webservice to show Party/Potluck sign_up open spots, volunteer, full
    private void getApiSignUpDetail() {

        final ProgressDialog mProgressDialog = new ProgressDialog(SignUpPartyActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<SignUpDetailResponseModel>> call = service.GetSignUpDetail(OpId);

        call.enqueue(new Callback<List<SignUpDetailResponseModel>>() {
                         @Override
                         public void onResponse(Call<List<SignUpDetailResponseModel>> call, Response<List<SignUpDetailResponseModel>> response) {
                             try {
                                 mProgressDialog.dismiss();

                                 if (response.isSuccessful()) {
                                     final List<SignUpDetailResponseModel> eventResponseModels = response.body();

                                     mViewEventDescriptionTextView.setText(eventResponseModels.get(0).getNotes().replace("\n"," "));
                                     mTextViewTitle.setVisibility(View.VISIBLE);

                                     String Name = eventResponseModels.get(0).getName();
                                     if (Name.length() > 20) {
                                         mTextViewTitle.setText(Name);
                                         mTextViewTitle.setTextSize(14);
                                     } else {
                                         mTextViewTitle.setText(Name);
                                     }

                                     String Contactone = eventResponseModels.get(0).getContact();
                                     String Contacttwo = eventResponseModels.get(0).getContact2();


                                     if (Contactone != null && !Contactone.equalsIgnoreCase("")) {
                                         mViewOrganizerOneTextView.setText(Contactone);
                                         mTextViewOrgnizer.setVisibility(View.VISIBLE);
                                         mLinearLayoutOrganizerOne.setVisibility(View.VISIBLE);
                                     } else {
                                         mViewOrganizerOneTextView.setVisibility(View.GONE);
                                     }

                                     if (Contacttwo != null && !Contacttwo.equalsIgnoreCase("")) {
                                         mTextViewOrgnizer.setVisibility(View.VISIBLE);
                                         mLinearLayoutOrganizerTwo.setVisibility(View.VISIBLE);
                                         mViewOrganizerSecondTextView.setText(Contacttwo);
                                     } else {
                                         mViewOrganizerSecondTextView.setVisibility(View.GONE);
                                     }

                                     String Image1 = eventResponseModels.get(0).getOrganizer1PhotoUrl();
                                     String Image2 = eventResponseModels.get(0).getOrganizer2PhotoUrl();

                                     if (Image1 != null) {
                                         mResultOne = Image1.substring(Image1.lastIndexOf(".") + 1);
                                         if (mResultOne.equalsIgnoreCase("gif")) {
                                             String BackgroundColor = eventResponseModels.get(0).getOrganizer1BackgroundColor();

                                             if (BackgroundColor != null) {

                                                 int colorTextView = Color.parseColor(eventResponseModels.get(0).getOrganizer1TextColor());

                                                 String resultLastName = eventResponseModels.get(0).getContact().substring(eventResponseModels.get(0).getContact().lastIndexOf(' ') + 1).trim();

                                                 mTextViewFirstNameContactOne.setText(eventResponseModels.get(0).getContact());
                                                 mTextViewFirstNameContactOne.setTextColor(colorTextView);
                                                 mTextViewSecondNameContactOne.setText(resultLastName);
                                                 mTextViewSecondNameContactOne.setTextColor(colorTextView);
                                                 mLinearLayoutBackgroundFirstContact.setVisibility(View.VISIBLE);
                                                 int color = Color.parseColor(BackgroundColor);
                                                 mLinearLayoutBackgroundFirstContact.setBackgroundResource(R.drawable.circle);
                                                 GradientDrawable gd = (GradientDrawable) mLinearLayoutBackgroundFirstContact.getBackground().getCurrent();
                                                 gd.setColor(color);
                                                 gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
                                             }
                                         } else {
                                             mViewOrganizerOneRoundedImageView.setVisibility(View.VISIBLE);
                                             Picasso.with(SignUpPartyActivity.this).load(eventResponseModels.get(0).getOrganizer1PhotoUrl())
                                                     .into(mViewOrganizerOneRoundedImageView);
                                         }

                                     } else {
                                         String BackgroundColor = eventResponseModels.get(0).getOrganizer1BackgroundColor();

                                         if (BackgroundColor != null) {
                                             mLinearLayoutBackgroundFirstContact.setVisibility(View.VISIBLE);
                                             int colorTextView = Color.parseColor(eventResponseModels.get(0).getOrganizer1TextColor());


                                             String resultLastName = eventResponseModels.get(0).getContact().substring(eventResponseModels.get(0).getContact().lastIndexOf(' ') + 1).trim();

                                             mTextViewFirstNameContactOne.setText(eventResponseModels.get(0).getContact());
                                             mTextViewFirstNameContactOne.setTextColor(colorTextView);
                                             mTextViewSecondNameContactOne.setText(resultLastName);
                                             mTextViewSecondNameContactOne.setTextColor(colorTextView);

                                             int color = Color.parseColor(BackgroundColor);
                                             mLinearLayoutBackgroundFirstContact.setBackgroundResource(R.drawable.circle);
                                             GradientDrawable gd = (GradientDrawable) mLinearLayoutBackgroundFirstContact.getBackground().getCurrent();
                                             gd.setColor(color);
                                             gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});


                                         }

                                     }
                                     if (Image2 != null) {
                                         mResultTwo = Image2.substring(Image2.lastIndexOf(".") + 1);
                                         if (mResultTwo.equalsIgnoreCase("gif")) {
                                             String BackgroundColor = eventResponseModels.get(0).getOrganizer2BackgroundColor();
                                             if (BackgroundColor != null) {
                                                 mLinearLayoutBackgroundSecondContact.setVisibility(View.VISIBLE);
                                                 int color = Color.parseColor(BackgroundColor);
                                                 mLinearLayoutBackgroundSecondContact.setBackgroundResource(R.drawable.circle);
                                                 GradientDrawable gd = (GradientDrawable) mLinearLayoutBackgroundSecondContact.getBackground().getCurrent();
                                                 gd.setColor(color);
                                                 gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
                                                 int colorTextView = Color.parseColor(eventResponseModels.get(0).getOrganizer2TextColor());

                                                 String resultLastName
                                                         = eventResponseModels.get(0).getContact2().substring(eventResponseModels.get(0).getContact2().lastIndexOf(' ') + 1).trim();

                                                 mTextViewFirstNameContactTwo.setText(eventResponseModels.get(0).getContact2());
                                                 mTextViewFirstNameContactTwo.setTextColor(colorTextView);
                                                 mTextViewSecondNameContactTwo.setText(resultLastName);
                                                 mTextViewSecondNameContactTwo.setTextColor(colorTextView);
                                             }

                                         } else {
                                             mViewOrganizerSecondRoundedImageView.setVisibility(View.VISIBLE);
                                             Picasso.with(SignUpPartyActivity.this).load(eventResponseModels.get(0).getOrganizer2PhotoUrl())
                                                     .into(mViewOrganizerSecondRoundedImageView);
                                         }

                                     } else {


                                         String BackgroundColor = eventResponseModels.get(0).getOrganizer2BackgroundColor();
                                         if (BackgroundColor != null) {
                                             mLinearLayoutBackgroundSecondContact.setVisibility(View.VISIBLE);
                                             int color = Color.parseColor(BackgroundColor);
                                             mLinearLayoutBackgroundSecondContact.setBackgroundResource(R.drawable.circle);
                                             GradientDrawable gd = (GradientDrawable) mLinearLayoutBackgroundSecondContact.getBackground().getCurrent();
                                             gd.setColor(color);
                                             gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
                                             int colorTextView = Color.parseColor(eventResponseModels.get(0).getOrganizer2TextColor());
                                             String resultLastName
                                                     = eventResponseModels.get(0).getContact2().substring(eventResponseModels.get(0).getContact2().lastIndexOf(' ') + 1).trim();

                                             mTextViewFirstNameContactTwo.setText(eventResponseModels.get(0).getContact2());
                                             mTextViewFirstNameContactTwo.setTextColor(colorTextView);
                                             mTextViewSecondNameContactTwo.setText(resultLastName);
                                             mTextViewSecondNameContactTwo.setTextColor(colorTextView);
                                         }

                                     }
                                     long val = eventResponseModels.get(0).getDateTime();
                                     if (val == 0) {
                                         mTextViewEventDateSignUp.setVisibility(View.GONE);
                                     } else {
                                         Date date = new Date(val);
                                         SimpleDateFormat df2 = new SimpleDateFormat("MMM d");
                                         SimpleDateFormat dfTime = new SimpleDateFormat("h:mm aa");
                                         df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                                         dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                                         String dateText = df2.format(date);
                                         String dateTime = dfTime.format(date);

                                         String EndTime = response.body().get(0).getEndTime();
                                         if (EndTime != null && !EndTime.equalsIgnoreCase("1:00AM") && !EndTime.equalsIgnoreCase("1:00 AM")) {
                                             mTextViewEventDateSignUp.setText(dateText + "\n" + dateTime + " - " + EndTime);

                                         } else {
                                             if (dateTime != null) {
                                                 if (!dateTime.equalsIgnoreCase("1:00AM") && !dateTime.equalsIgnoreCase("1:00 AM")) {
                                                     mTextViewEventDateSignUp.setText(dateText + "\n" + dateTime);
                                                 } else {
                                                     mTextViewEventDateSignUp.setText(dateText);
                                                 }
                                             }

                                         }
                                     }


                                     long valCutOff = eventResponseModels.get(0).getCutoffDate();
                                     if (valCutOff == 0) {
                                         mTextViewCutOffDateSignUp.setVisibility(View.GONE);
                                     } else {
                                         Date date = new Date(valCutOff);
                                         @SuppressLint("SimpleDateFormat") SimpleDateFormat df2 =
                                                 new SimpleDateFormat("MMM d");
                                         @SuppressLint("SimpleDateFormat") SimpleDateFormat dfTime =
                                                 new SimpleDateFormat("h:mm aa");
                                         df2.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                                         dfTime.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
                                         String dateText = df2.format(date);
//                            String dateTime = dfTime.format(date);

                                         String EndTime = response.body().get(0).getEndTime();
                                         if (EndTime != null && !EndTime.equalsIgnoreCase("1:00AM") && !EndTime.equalsIgnoreCase("1:00 AM")) {
                                             mTextViewCutOffDateSignUp.setText(dateText);

                                         } else {

//                                    if (!dateTime.equalsIgnoreCase("1:00AM")&&!dateTime.equalsIgnoreCase("1:00 AM")) {
                                             mTextViewCutOffDateSignUp.setText(dateText);
//                                    } else {
//                                        mTextViewCutOffDateSignUp.setText(dateText);
//                                    }
//                                }

                                         }
                                     }
                                         mSignUpPartyAdapter = new SignUpPartyAdapter(SignUpPartyActivity.this, eventResponseModels.get(0).getItems());
                                     mRecyclerViewOpenSpot.setAdapter(mSignUpPartyAdapter);

                                 } else {
                                     final CustomizeDialog customizeDialog = new CustomizeDialog(SignUpPartyActivity.this);
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
                                 Log.d("onResponse", "There is an error");
                                 e.printStackTrace();
                             }

                         }

                         @Override
                         public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                             mProgressDialog.dismiss();
                             Toast.makeText(SignUpPartyActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                             Log.d("onFailure", t.toString());
                         }

                     }

        );
    }

    //Method to set on clickListener on views

    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
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
