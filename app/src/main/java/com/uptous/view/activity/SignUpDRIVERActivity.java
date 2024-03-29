package com.uptous.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.SignUpDriverAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.uptous.controller.utils.Utilities.isLastAppActivity;

/**
 * FileName : SignUpDRIVERActivity
 * Description :show all DRIVER sign_up open spots, volunteer, full.
 * Dependencies : SignUpDRIVERActivity
 */
public class SignUpDRIVERActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerViewOpenSpot;

    private ImageView mImageViewBack;

    private ImageView mViewOrganizerOneRoundedImageView, mViewOrganizerSecondRoundedImageView;

    private TextView mViewOrganizerOneTextView, mViewOrganizerSecondTextView, mViewEventDescriptionTextView,
            mTextViewTitle, mTextViewEventDateSignUp, mTextViewOrganizer,
            mTextViewFirstNameContactOne, mTextViewSecondNameContactOne, mTextViewSecondNameContactTwo,
            mTextViewFirstNameContactTwo, mTextViewCutOffDateSignUp;

    private SignUpDriverAdapter mSignUpDriverAdapter;

    private String mAuthenticationId, mAuthenticationPassword, mResultOne, mResultTwo;

    private LinearLayout mLinearLayoutBackgroundFirstContact, mLinearLayoutBackgroundSecondContact,
            mLinearLayoutOrganizerOne, mLinearLayoutOrganizerTwo;

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
            showToast(getString(R.string.network_error));
        }

    }

    //Method to initialize view
    private void initView() {

        //Local Variables Initialization
        LinearLayout linearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        LinearLayout linearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);

        //Global Variables Initialization
        mLinearLayoutOrganizerOne = (LinearLayout) findViewById(R.id.layout_organizer_one);
        mLinearLayoutOrganizerTwo = (LinearLayout) findViewById(R.id.layout_organizer_two);
        mLinearLayoutBackgroundFirstContact = (LinearLayout) findViewById(R.id.layout_contact);
        mLinearLayoutBackgroundSecondContact = (LinearLayout) findViewById(R.id.layout_contact_image_second);

        mTextViewOrganizer = (TextView) findViewById(R.id.text_view_title_organizers);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_message_title);
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
        mViewOrganizerOneRoundedImageView = (ImageView) findViewById(R.id.image_view_contact_one);
        mViewOrganizerSecondRoundedImageView = (ImageView) findViewById(R.id.image_view_contact_two);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewOpenSpot = (RecyclerView) findViewById(R.id.recycler_view_open_spots);
        mRecyclerViewOpenSpot.setLayoutManager(layoutManager);


        linearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        linearLayoutCommunityFilter.setVisibility(View.GONE);
        imageViewFilter.setVisibility(View.GONE);

        getData();

        clickListenerOnViews();


    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
    }

    // Get webservice to show Driver sign_up open spots, volunteer, full
    private void getApiSignUpDetail() {

        showProgressDialog();
        int OpId =Prefs.getOpportunityId(this);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<SignUpDetailResponseModel>> call = service.GetSignUpDetail(OpId);

        call.enqueue(new Callback<List<SignUpDetailResponseModel>>() {
            @Override
            public void onResponse(Call<List<SignUpDetailResponseModel>> call, Response<List<SignUpDetailResponseModel>> response) {
                try {
                    hideProgressDialog();

                    if (response.body() != null) {
                        final List<SignUpDetailResponseModel> eventResponseModels = response.body();

                        mViewEventDescriptionTextView.setText(eventResponseModels.get(0).getNotes().replace("\n", " "));
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
                            mTextViewOrganizer.setVisibility(View.VISIBLE);
                            mLinearLayoutOrganizerOne.setVisibility(View.VISIBLE);
                        } else {
                            mViewOrganizerOneTextView.setVisibility(View.GONE);
                        }

                        if (Contacttwo != null && !Contacttwo.equalsIgnoreCase("")) {
                            mTextViewOrganizer.setVisibility(View.VISIBLE);
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
                                    String OwnerN = eventResponseModels.get(0).getContact();
                                    String resultLastName = OwnerN.substring(OwnerN.lastIndexOf(' ') + 1).trim();
                                    mTextViewFirstNameContactOne.setText(OwnerN);
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

                                Glide.with(SignUpDRIVERActivity.this).load(eventResponseModels.get(0).getOrganizer1PhotoUrl())
                                        .into(mViewOrganizerOneRoundedImageView);
                            }

                        } else {
                            String BackgroundColor = eventResponseModels.get(0).getOrganizer1BackgroundColor();

                            if (BackgroundColor != null) {
                                mLinearLayoutBackgroundFirstContact.setVisibility(View.VISIBLE);

                                int colorTextView = Color.parseColor(eventResponseModels.get(0).getOrganizer1TextColor());


                                String OwnerN = eventResponseModels.get(0).getContact();
                                String resultLastName = OwnerN.substring(OwnerN.lastIndexOf(' ') + 1).trim();
                                mTextViewFirstNameContactOne.setText(OwnerN);
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
                                    String OwnerN = eventResponseModels.get(0).getContact2();
                                    String resultLastName = OwnerN.substring(OwnerN.lastIndexOf(' ') + 1).trim();

                                    mTextViewFirstNameContactTwo.setText(OwnerN);
                                    mTextViewFirstNameContactTwo.setTextColor(colorTextView);
                                    mTextViewSecondNameContactTwo.setText(resultLastName);
                                    mTextViewSecondNameContactTwo.setTextColor(colorTextView);
                                }

                            } else {
                                mViewOrganizerSecondRoundedImageView.setVisibility(View.VISIBLE);

                                Glide.with(SignUpDRIVERActivity.this).load(eventResponseModels.get(0).getOrganizer2PhotoUrl())
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


                                String OwnerN = eventResponseModels.get(0).getContact2();
                                String resultLastName = OwnerN.substring(OwnerN.lastIndexOf(' ') + 1).trim();

                                mTextViewFirstNameContactTwo.setText(OwnerN);
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

                            String EndTime = response.body().get(0).getEndTime();
                            if (EndTime != null && !EndTime.equalsIgnoreCase("1:00AM") && !EndTime.equalsIgnoreCase("1:00 AM")) {
                                mTextViewCutOffDateSignUp.setText(dateText);

                            } else {

                                mTextViewCutOffDateSignUp.setText(dateText);

                            }
                        }
                        mSignUpDriverAdapter = new SignUpDriverAdapter(SignUpDRIVERActivity.this, eventResponseModels.get(0).getItems());
                        mRecyclerViewOpenSpot.setAdapter(mSignUpDriverAdapter);


                    } else {
                        showLogOutDialog();
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
               hideProgressDialog();
                showToast(getString(R.string.error));
                Log.d("onFailure", t.toString());
            }

        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isLastAppActivity(this))
            startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}
