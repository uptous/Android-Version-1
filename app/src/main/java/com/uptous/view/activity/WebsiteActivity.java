package com.uptous.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.model.SignUpResponseModel;
import com.uptous.sharedpreference.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.uptous.view.activity.InvitationsActivity.INVITATION_ID;
import static com.uptous.view.activity.WebsiteActivity.DeeplinkType.NULL;

public class WebsiteActivity extends BaseActivity {


    public static String WEBSITE_TITLE = "header_title";
    public static String WEBSITE_URL = "url";
    private String Deeplink_Data;
    private WebView webView;


    enum DeeplinkType {
        ALBUM, INVITATION, SIGNUP, NULL
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        initView();

        String urlpath = "", title = getString(R.string.app_name);

        Intent intent = getIntent();
        if (intent.hasExtra(WEBSITE_TITLE)) {
            Bundle extras = getIntent().getExtras();
            if (!extras.getString(WEBSITE_TITLE).equals(null)) {
                title = extras.getString(WEBSITE_TITLE);
            }
        }
        //configurating actionbar...
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setTitle(title);

        Uri data = intent.getData();
        if (data != null) {
            DeeplinkType deeplinkPath = isDeeplinkPathType(data);
            if (deeplinkPath != NULL) {//Incase of deeplinking
                openDeepLink(deeplinkPath);
            } else
                openWebsite(data.toString());
        } else {
            //OpenWebsite URL .....
            Bundle extras = getIntent().getExtras();
            if (intent.hasExtra(WEBSITE_URL) && !extras.getString(WEBSITE_URL).equals(null)) {
                //In case of intent extra values
                urlpath = extras.getString(WEBSITE_URL);
                openWebsite(urlpath);
            }
        }
    }

    private void openDeepLink(DeeplinkType deeplinkPath) {
        if (Prefs.getAuthenticationId(this) != null) { //If not logged in
            if (deeplinkPath == DeeplinkType.ALBUM) {
                String[] split = Deeplink_Data.split("/");
                if (split.length > 1)
                    Prefs.setNewsItemId(this, Integer.parseInt(split[1]));
                startActivity(new Intent(this, AlbumDetailActivity.class));
                finish();
            } else if (deeplinkPath == DeeplinkType.INVITATION) {
                Intent in = new Intent(this, InvitationsActivity.class);
               // in.putExtra(INVITATION_ID, Deeplink_Data);
                startActivity(in);
                finish();
            } else if (deeplinkPath == DeeplinkType.SIGNUP) {
                //351/22454
                String[] split = Deeplink_Data.split("/");
                if (split.length > 1)
                    openSignUp(Integer.parseInt(split[1]));
            }

        } else {
            Intent in = new Intent(this, LogInActivity.class);
            startActivity(in);
            finish();
        }
    }


    private DeeplinkType isDeeplinkPathType(Uri data) {
        //for Album
        if (data.getQueryParameter("action") != null) {
            if (data.getQueryParameter("action").equals("alb"))
                if (data.getQueryParameter("extraId") != null) {
                    Deeplink_Data = data.getQueryParameter("extraId");
                    return DeeplinkType.ALBUM;
                }
        } else if (data.toString().contains("/communityalbum/") || data.toString().contains("/communityAlbum/")) {
            String[] data_split = data.toString().toLowerCase().split("/communityalbum/");
            if (data_split.length > 1) {
                Deeplink_Data = data_split[1];
                return DeeplinkType.ALBUM;
            }
        }
        //for invitation
        else if (data.getQueryParameter("comId") != null) {
            Deeplink_Data = data.getQueryParameter("comId");
            return DeeplinkType.INVITATION;
        } else if (data.toString().contains("/communityinvite/") || data.toString().contains("/communityInvite/")) {
            String[] data_split = data.toString().toLowerCase().split("/communityinvite/");
            if (data_split.length > 1) {
                Deeplink_Data = data_split[1];
                return DeeplinkType.INVITATION;
            }
        }
        else if (data.toString().contains("/communitysignup/") || data.toString().contains("/communitySignup/")) {
            String[] data_split = data.toString().toLowerCase().split("/communitysignup/");
            if (data_split.length > 1) {
                Deeplink_Data = data_split[1];
                return DeeplinkType.SIGNUP;
            }
        }
        return DeeplinkType.NULL;
    }

    private void openWebsite(String urlpath) {
        webView.loadUrl(urlpath);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.web_view);
        webView.setHapticFeedbackEnabled(false);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);


        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(100);
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100)
                    progressBar.setVisibility(View.INVISIBLE);
            }
        });

        webView.setWebViewClient(new WebViewClient());

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return false;

    }


    private void openSignUp(final int mCommunityID) {
        final WebsiteActivity activity = this;
        String mAuthenticationId = Prefs.getAuthenticationId(activity);
        String mAuthenticationPassword = Prefs.getAuthenticationPassword(activity);
        // String CommunityID =String.valueOf(mCommunityID);
        final ProgressDialog mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<SignUpResponseModel>> call = service.GetSignUp();

        call.enqueue(new Callback<List<SignUpResponseModel>>() {
            @Override
            public void onResponse(Call<List<SignUpResponseModel>> call, Response<List<SignUpResponseModel>> response) {
                mProgressDialog.dismiss();
                try {


                    List<SignUpResponseModel> mSignUpResponseModelList = response.body();

                    for (int j = 0; mSignUpResponseModelList.size() > j; j++) {
                        int CommId = mSignUpResponseModelList.get(j).getId();
                        if (CommId == mCommunityID) {
                            if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("RSVP") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Vote")) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                Prefs.setOpportunityId(activity, OpId);
                                Intent intent = new Intent(activity, SignUpRSPVActivity.class);
                                activity.startActivity(intent);
                                finish();
                            } else if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Drivers")) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                Prefs.setOpportunityId(activity, OpId);
                                Intent intent = new Intent(activity, SignUpDRIVERActivity.class);
                                activity.startActivity(intent);
                                finish();
                            } else if (mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Shifts") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Games") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Potluck/Party") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Wish List") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Volunteer") ||
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Snack Schedule")

                                    || mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Multi Game/Event RSVP")
                                    ) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                Prefs.setOpportunityId(activity, OpId);
                                Prefs.setSignUpType(activity, mSignUpResponseModelList.get(j).getType());
                                Intent intent = new Intent(activity, SignUpShiftsActivity.class);
                                activity.startActivity(intent);
                                finish();
                            } else if (
                                    mSignUpResponseModelList.get(j).getType().equalsIgnoreCase("Ongoing Volunteering")

                                    ) {
                                int OpId = mSignUpResponseModelList.get(j).getId();
                                Prefs.setOpportunityId(activity, OpId);
                                Prefs.setSignUpType(activity, mSignUpResponseModelList.get(j).getType());
                                Intent intent = new Intent(activity, SignUpOngoingActivity.class);
                                activity.startActivity(intent);
                                finish();
                            }

                        }
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    Toast.makeText(activity, "Some error Occured ", Toast.LENGTH_LONG).show();
                    finish();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<SignUpResponseModel>> call, Throwable t) {
                mProgressDialog.dismiss();
                Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
