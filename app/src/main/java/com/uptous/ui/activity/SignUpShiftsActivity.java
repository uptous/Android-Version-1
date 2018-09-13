package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.uptous.controller.utils.RoundedImageView;
import com.uptous.model.SignUpDetailResponseModel;
import com.uptous.ui.adapter.SignUpShiftAdapter;
import com.uptous.ui.adapter.SignUpShiftAdapterSecond;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Prakash on 12/29/2016.
 */

public class SignUpShiftsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextViewTitle;
    private LinearLayout mLinearLayoutCommunityFilter;
    private RecyclerView mRecyclerViewOpenSpot;
    private ImageView mImageViewBack, mImageViewFilter;
    private LinearLayout mLinearLayoutNavigation;
    private TextView mViewEventDescriptionTextView, mViewTitleOrganizersTextView, mViewOrganizerOneTextView, mViewOrganizerSecondTextView;
    private RoundedImageView mViewOrganizerOneRoundedImageView, mViewOrganizerSecondRoundedImageView;
    private SignUpShiftAdapter mSignUpShiftAdapter;
    private SignUpShiftAdapterSecond mSignUpShiftAdapterSecond;
    private String AuthenticationId, AuthenticationPassword, Type;
    private LinearLayout mLinearLayoutBackgroundFirstContact, mLinearLayoutBackgroundSecondContact;
    String result1, result2;
    private TextView mTextViewFirstNameContactOne, mTextViewSecondNameContactOne,
            mTextViewSecondNameContactTwo, mTextViewFirstNameContactTwo, mTextViewEventDateSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

    }

    private void initView() {
        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_message_toolbar);
        mTextViewEventDateSignUp = (TextView) findViewById(R.id.text_view_event_date_sign_up);
        mLinearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        mImageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        mLinearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
        mViewEventDescriptionTextView = (TextView) findViewById(R.id.text_view_event_description);
        mViewOrganizerOneRoundedImageView = (RoundedImageView) findViewById(R.id.image_view_contact_one);
        mViewOrganizerOneTextView = (TextView) findViewById(R.id.text_view_organizer_one);
        mViewOrganizerSecondRoundedImageView = (RoundedImageView) findViewById(R.id.image_view_contact_two);
        mViewOrganizerSecondTextView = (TextView) findViewById(R.id.text_view_organizer_second);
        mLinearLayoutBackgroundFirstContact = (LinearLayout) findViewById(R.id.layout_contact);
        mLinearLayoutBackgroundSecondContact = (LinearLayout) findViewById(R.id.layout_contact_image_second);
        mTextViewFirstNameContactOne = (TextView) findViewById(R.id.textview_first_name);
        mTextViewSecondNameContactOne = (TextView) findViewById(R.id.textview_last_name);
        mTextViewFirstNameContactTwo = (TextView) findViewById(R.id.textview_first_name_second_contact);
        mTextViewSecondNameContactTwo = (TextView) findViewById(R.id.textview_last_name_second_contact);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewOpenSpot = (RecyclerView) findViewById(R.id.recycler_view_open_spots);
        mRecyclerViewOpenSpot.setLayoutManager(layoutManager);

        mLinearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        mLinearLayoutCommunityFilter.setVisibility(View.GONE);
        mImageViewFilter.setVisibility(View.GONE);


        getData();

        clickListenerOnViews();

        if (ConnectionDetector.isConnectingToInternet(this)) {
            getApiSignUpDetail();
        } else {
            Toast.makeText(SignUpShiftsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }


    }

    private void getData() {
        AuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        AuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);

    }


    private void getApiSignUpDetail() {

        final ProgressDialog mProgressDialog = new ProgressDialog(SignUpShiftsActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        int OpId = MyApplication.mSharedPreferences.getInt("Id", 0);


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);
        Call<List<SignUpDetailResponseModel>> call = service.GetSignUpDetail(OpId);

        call.enqueue(new Callback<List<SignUpDetailResponseModel>>() {
            @Override
            public void onResponse(Call<List<SignUpDetailResponseModel>> call, Response<List<SignUpDetailResponseModel>> response) {
                try {
                    mProgressDialog.dismiss();

                    if (response.isSuccessful()) {
                        final List<SignUpDetailResponseModel> eventResponseModels = response.body();


                        mViewEventDescriptionTextView.setText(eventResponseModels.get(0).getNotes());
                        mTextViewTitle.setVisibility(View.VISIBLE);

                        String Name = eventResponseModels.get(0).getName();
                        mTextViewTitle.setText(Name);

                        mViewOrganizerOneTextView.setText(eventResponseModels.get(0).getContact());
                        mViewOrganizerSecondTextView.setText(eventResponseModels.get(0).getContact2());

                        String Image1 = eventResponseModels.get(0).getOrganizer1PhotoUrl();
                        String Image2 = eventResponseModels.get(0).getOrganizer2PhotoUrl();

                        if (Image1 != null) {
                            result1 = Image1.substring(Image1.lastIndexOf(".") + 1);
                            if (result1.equalsIgnoreCase("gif")) {
                                String BackgroundColor = eventResponseModels.get(0).getOrganizer2BackgroundColor();

                                if (BackgroundColor != null) {

                                    int colorTextView = Color.parseColor(eventResponseModels.get(0).getOrganizer1TextColor());

                                    String resultContact1LastName = eventResponseModels.get(0).getContact().
                                            substring(eventResponseModels.get(0).getContact().lastIndexOf(' ') + 1).trim();
                                    mTextViewFirstNameContactOne.setText(eventResponseModels.get(0).getContact());
                                    mTextViewFirstNameContactOne.setTextColor(colorTextView);
                                    mTextViewSecondNameContactOne.setText(resultContact1LastName);
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
                                Picasso.with(SignUpShiftsActivity.this).load(eventResponseModels.get(0).getOrganizer1PhotoUrl())
                                        .into(mViewOrganizerOneRoundedImageView);
                            }

                        } else {
                            String BackgroundColor = eventResponseModels.get(0).getOrganizer2BackgroundColor();

                            if (BackgroundColor != null) {
                                mLinearLayoutBackgroundFirstContact.setVisibility(View.VISIBLE);


                                int color = Color.parseColor(BackgroundColor);
                                mLinearLayoutBackgroundFirstContact.setBackgroundResource(R.drawable.circle);
                                GradientDrawable gd = (GradientDrawable) mLinearLayoutBackgroundFirstContact.getBackground().getCurrent();
                                gd.setColor(color);
                                gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});

                                int colorTextView = Color.parseColor(eventResponseModels.get(0).getOrganizer2TextColor());

                                String resultContact1LastName = eventResponseModels.get(0).getContact().
                                        substring(eventResponseModels.get(0).getContact().lastIndexOf(' ') + 1).trim();
                                mTextViewFirstNameContactOne.setText(eventResponseModels.get(0).getContact());
                                mTextViewFirstNameContactOne.setTextColor(colorTextView);
                                mTextViewSecondNameContactOne.setText(resultContact1LastName);
                                mTextViewSecondNameContactOne.setTextColor(colorTextView);
                            }

                        }
                        if (Image2 != null) {
                            result2 = Image2.substring(Image2.lastIndexOf(".") + 1);
                            if (result2.equalsIgnoreCase("gif")) {
                                String BackgroundColor = eventResponseModels.get(0).getOrganizer2BackgroundColor();
                                if (BackgroundColor != null) {
                                    mLinearLayoutBackgroundSecondContact.setVisibility(View.VISIBLE);
                                    int color = Color.parseColor(BackgroundColor);
                                    mLinearLayoutBackgroundSecondContact.setBackgroundResource(R.drawable.circle);
                                    GradientDrawable gd = (GradientDrawable) mLinearLayoutBackgroundSecondContact.getBackground().getCurrent();
                                    gd.setColor(color);
                                    gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
                                    int colorTextView = Color.parseColor(eventResponseModels.get(0).getOrganizer2TextColor());


                                    String Contact2 = eventResponseModels.get(0).getContact2();
                                    String resultImage = Contact2.substring(Contact2.lastIndexOf(' ') + 1).trim();

                                    mTextViewFirstNameContactTwo.setText(eventResponseModels.get(0).getContact2());
                                    mTextViewFirstNameContactTwo.setTextColor(colorTextView);
                                    mTextViewSecondNameContactTwo.setText(resultImage);
                                    mTextViewSecondNameContactTwo.setTextColor(colorTextView);
                                }

                            } else {
                                mViewOrganizerSecondRoundedImageView.setVisibility(View.VISIBLE);
                                Picasso.with(SignUpShiftsActivity.this).load(eventResponseModels.get(0).getOrganizer2PhotoUrl())
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

                                String Contact2 = eventResponseModels.get(0).getContact2();
                                String resultImage = Contact2.substring(Contact2.lastIndexOf(' ') + 1).trim();

                                mTextViewFirstNameContactTwo.setText(eventResponseModels.get(0).getContact2());
                                mTextViewFirstNameContactTwo.setTextColor(colorTextView);
                                mTextViewSecondNameContactTwo.setText(resultImage);
                                mTextViewSecondNameContactTwo.setTextColor(colorTextView);
                            }

                        }


                        //
//
//
                        long val = eventResponseModels.get(0).getDateTime();
                        if (val == 0) {
                            mTextViewEventDateSignUp.setVisibility(View.GONE);
                        } else {
                            Date date = new Date(val);
                            SimpleDateFormat df2 = new SimpleDateFormat("MMM dd");
                            String dateText = df2.format(date);
                            mTextViewEventDateSignUp.setText(dateText);

                        }

                        mSignUpShiftAdapter = new SignUpShiftAdapter(SignUpShiftsActivity.this,
                                eventResponseModels.get(0).getItems());
                        mRecyclerViewOpenSpot.setAdapter(mSignUpShiftAdapter);


                    } else {
                        Toast.makeText(SignUpShiftsActivity.this, "" + response.raw().code(), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpDetailResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(SignUpShiftsActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                Log.d("onFailure", t.toString());
            }

        });
    }


    private void clickListenerOnViews() {
        mImageViewBack.setOnClickListener(this);
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
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConnectionDetector.isConnectingToInternet(this)) {
            getApiSignUpDetail();
        } else {
            Toast.makeText(SignUpShiftsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }
}
