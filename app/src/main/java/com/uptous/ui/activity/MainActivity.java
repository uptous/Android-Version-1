package com.uptous.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;
import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.controller.utils.SlidingTabLayout;
import com.uptous.controller.utils.UserPicture;
import com.uptous.model.ProfileResponseModel;
import com.uptous.ui.adapter.HomeListAdapter;
import com.uptous.ui.adapter.TabAdapter;
import com.uptous.ui.fragment.LibraryFragment;
import com.uptous.ui.fragment.ContactFragment;
import com.uptous.ui.fragment.EventsFragment;
import com.uptous.ui.fragment.HomeFragment;
import com.uptous.ui.fragment.SignUpFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * FileName : MainActivity
 * Description : Show Tabs that open  different fragments like : home ,upcoming,winner,howitwork,profile,suppot etc.
 * Dependencies : TabAdapter
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLinearLayoutSideMenu;
    public static TextView mTextViewTitle;
    public static ImageView mImageViewCommunityInvitation;
    public static SearchView mSearchView;
    private LinearLayout mLinearLayoutCommunityFilter;
    private SimpleSideDrawer slide_me;
    private ImageView mImageViewProfile;
    private TextView mTextViewUserName;
    private String mStringProfileImage;
    public String mPhoto, mFirstName, mLastName, mEmail, mPhone;
    private String AuthenticationId, AuthenticationPassword;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ViewPagerAdapter adapter;
    public static int LOAD_IMAGE_RESULTS = 1;
    private static final int iconRes_over[] = {
            R.mipmap.news,
            R.mipmap.contact,
            R.mipmap.sign_up,

            R.mipmap.library,
            R.mipmap.event};


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_profile:

//                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
//                startActivity(intent);
                slide_me.toggleLeftDrawer();

                break;
            case R.id.imgmenuleft:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
//                slide_me.toggleLeftDrawer();
                break;
            case R.id.button_uploaded_picture:
                dialogForCameraGallery();
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void getApiProfile() {
        final ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);

        Call<ProfileResponseModel> call = service.ProfileDetail();
        call.enqueue(new retrofit2.Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.body() != null) {

                    if (response.isSuccessful()) {

                        mStringProfileImage = response.body().getPhoto();
                        Picasso.with(MainActivity.this).load(mStringProfileImage).into(mImageViewProfile);
                        mTextViewUserName.setText(response.body().getFirstName() + " " + response.body().getLastName());

                        mFirstName = response.body().getFirstName();
                        mLastName = response.body().getLastName();
                        mEmail = response.body().getEmail();
                        mPhone = response.body().getPhone();
//                        Picasso.with(MainActivity.this).load(response.body().getPhoto()).into(mViewProfileRoundedImageView);

                    }
                } else {

                    Toast.makeText(MainActivity.this, "Server Error! Please Try After Some Time", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();


            }
        });
    }

    /*
          * Setting the navigation slider
          * */
    private void SetSlider() {
        slide_me = new SimpleSideDrawer(this);
        slide_me.setLeftBehindContentView(R.layout.menu_layout);
        mLinearLayoutSideMenu.setOnClickListener(this);


        setLeftSideMenu(slide_me);
    }

    /**
     * Method to initialization of views
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);
        setupTabIcons();
        RelativeLayout relativeLayout =
                (RelativeLayout) findViewById(R.id.relative);
        relativeLayout.setFocusableInTouchMode(true);
        relativeLayout.requestFocus();
        mSearchView = (SearchView) findViewById(R.id.serach_view_filter_feed);
        mLinearLayoutCommunityFilter = (LinearLayout) findViewById(R.id.layout_community_filter);
//        mLinearLayoutCommunityFilter.setVisibility(View.GONE);
        mLinearLayoutSideMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        mImageViewCommunityInvitation = (ImageView) findViewById(R.id.image_view_community_invitations);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_title);
        AuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        AuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        SetSlider();

        getApiProfile();
    }

    public void setLeftSideMenu(SimpleSideDrawer slide_me) {
        this.slide_me = slide_me;
        findIDSForSliderChilds();
    }

    public void findIDSForSliderChilds() {


        mImageViewProfile = (ImageView) slide_me.findViewById(R.id.image_view_profile_picture);
        mTextViewUserName = (TextView) slide_me.findViewById(R.id.text_view_user_name);
        slide_me.findViewById(R.id.text_view_profile).setOnClickListener(this);
        slide_me.findViewById(R.id.text_view_notifications).setOnClickListener(this);
        slide_me.findViewById(R.id.text_view_help).setOnClickListener(this);
        slide_me.findViewById(R.id.button_uploaded_picture).setOnClickListener(this);

    }


    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(iconRes_over[0]);
        tabLayout.getTabAt(1).setIcon(iconRes_over[1]);
        tabLayout.getTabAt(2).setIcon(iconRes_over[2]);
        tabLayout.getTabAt(3).setIcon(iconRes_over[3]);
        tabLayout.getTabAt(4).setIcon(iconRes_over[4]);
    }

    private void setupViewPager(final ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "");
        adapter.addFrag(new ContactFragment(), "");
        adapter.addFrag(new SignUpFragment(), "");
        adapter.addFrag(new LibraryFragment(), "");
        adapter.addFrag(new EventsFragment(), "");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    private void dialogForCameraGallery() {
        final CustomizeDialog customizeDialog = new CustomizeDialog(MainActivity.this);
        customizeDialog.show();
        customizeDialog.setContentView(R.layout.dialog_camera_gallery);
        TextView buttonCamera = (TextView) customizeDialog.findViewById(R.id.button_camera);
        TextView buttonAlbum = (TextView) customizeDialog.findViewById(R.id.button_album);
        TextView buttonCancel = (TextView) customizeDialog.findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customizeDialog.dismiss();
            }
        });

        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, LOAD_IMAGE_RESULTS);
                customizeDialog.dismiss();
            }
        });

        buttonAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
                customizeDialog.dismiss();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && null != data) {

            Bitmap yourImage = null;
            Uri selectedImageUri = data.getData();
            try {
                if (selectedImageUri == null) {
                    Bundle extra2 = data.getExtras();
                    yourImage = extra2.getParcelable("data");
                } else {
                    yourImage = new UserPicture(selectedImageUri, getContentResolver()).getBitmap();
                }

                // convert bitmap to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte imageInByte[] = stream.toByteArray();

                mPhoto = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                postApiUpdateProfile();
                try {
                    mImageViewProfile.setImageBitmap(yourImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void postApiUpdateProfile() {
        final ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        APIServices service =/* = retrofit.create(APIServices.class,"","");*/
                ServiceGenerator.createService(APIServices.class, AuthenticationId, AuthenticationPassword);

        Call<ProfileResponseModel> call = service.UpdateProfile(mFirstName, mLastName, mEmail, mPhone, mPhoto);
        call.enqueue(new retrofit2.Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(Call<ProfileResponseModel> call, Response<ProfileResponseModel> response) {

                mProgressDialog.dismiss();
                if (response.body() != null) {

                    if (response.isSuccessful()) {

                    }
                } else {
                    Toast.makeText(MainActivity.this, response.raw().code() + " " + response.raw().message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponseModel> call, Throwable t) {

                mProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();


            }
        });
    }
}
