package com.uptous.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.Helper;
import com.uptous.model.ContactListResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.ContactListAdapter;
import com.uptous.view.adapter.CustomComparator;
import com.uptous.view.fragment.LibraryFragment;
import com.uptous.view.fragment.ContactFragment;
import com.uptous.view.fragment.EventsFragment;
import com.uptous.view.fragment.HomeFragment;
import com.uptous.view.fragment.SignUpFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.uptous.view.fragment.ContactFragment.currentItem;
import static com.uptous.view.fragment.ContactFragment.isScrolling;
import static com.uptous.view.fragment.ContactFragment.layoutManager;
import static com.uptous.view.fragment.ContactFragment.mContactListForSearchAdapter;
import static com.uptous.view.fragment.ContactFragment.mTextViewSearchResult;
import static com.uptous.view.fragment.ContactFragment.mViewContactRecyclerView;
import static com.uptous.view.fragment.ContactFragment.pageN0;
import static com.uptous.view.fragment.ContactFragment.progressBar;
import static com.uptous.view.fragment.ContactFragment.recycler_view_empty1;
import static com.uptous.view.fragment.ContactFragment.scrolledOutItem;
import static com.uptous.view.fragment.ContactFragment.totalItem;


/**
 * FileName : MainActivity
 * Description : Show Tabs that open  different fragments like : feed, contacts, sign_ups, albums, event.
 * Dependencies : ViewPagerAdapter
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private static final int TAG_COMMUNITY_CHANGE = 55;
    private TabLayout mTabLayout;

    private static final int ICON_RES_OVER[] = {
            R.mipmap.news,
            R.mipmap.contact,
            R.mipmap.sign_up,
            R.mipmap.library,
            R.mipmap.event};


    private Helper mHelper;
    public TextView mTextViewSignOut;
    private TextView mTextViewSearch, mTextViewCancel, mTextViewTitle;
    private RelativeLayout mRelativeLayoutSearchBar;
    private LinearLayout mLinearLayoutNavigationMenu;
    private ViewPagerAdapter mViewPagerAdapter;
    private ImageView mImageViewInvitation;
    public static final int REQUEST_CAMERA = 1;
    public ImageView mImageViewSorting;
    public static List<ContactListResponseModel> contactListResponseModels = new ArrayList<>();
    private ContactListAdapter mContactListAdapter;
    public static Boolean LoadOnce;

    public SearchView mSearchView;
    private long differenceTime;
    private String text;
    private static final int REQUEST_RUNTIME_PERMISSION = 123;
    // pagination element
    public static int mLimit = 150;
    private int mOffset = 0;
    public static List<ContactListResponseModel> mContactListResponseSearch = new ArrayList<>();
    private boolean mIsQueryText = false;
    public static boolean mIsFromSearch = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkAndRequestPermissions();

    }

    private boolean checkAndRequestPermissions() {
//        int permissionPhoneState = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_PHONE_STATE);


        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
//
        int camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        int storagePermissionWrite = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
//        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//        if (storagePermissionWrite != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
//


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_RUNTIME_PERMISSION);
            return false;
        }

        return true;
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

//

            case REQUEST_RUNTIME_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    //TODO
                }
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgmenuleft:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.text_view_title:
                Prefs.setCommunityFilter(this, "community");
                Prefs.setMessage(this, null);
                Intent intentCommunity = new Intent(MainActivity.this, CommunityActivity.class);
                startActivityForResult(intentCommunity, TAG_COMMUNITY_CHANGE);

            case R.id.text_view_cancel:
                mSearchView.clearFocus();
                mSearchView.onActionViewCollapsed();
                mTextViewSearch.setVisibility(View.VISIBLE);

                if (mIsQueryText) {
                    getContactList();
                    setScrollView();
                    mIsQueryText = false;
                }

                break;

            case R.id.layout_searchbar:
                mSearchView.onActionViewExpanded();
                break;

            case R.id.image_view_community_invitations:
                Intent intentInvitations = new Intent(MainActivity.this, InvitationsActivity.class);
                startActivity(intentInvitations);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        int Position = Prefs.getPosition(this);
        int CommunityId = Prefs.getCommunityId(this);
        String communityName = Prefs.getCommunityNAme(this);

        if (CommunityId != 0) {
            if (Position == 0) {

                HomeFragment.checkEmptyView();

                if (HomeFragment.feedResponseModelList.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                    Toast.makeText(MainActivity.this, "No Record Found2", Toast.LENGTH_SHORT).show();
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
                mTextViewTitle.setText("Feed - " + communityName);
            } else if (Position == 1) {
                if (contactListResponseModels.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    //    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
                mTextViewTitle.setText("Contacts - " + communityName);
            } else if (Position == 2) {
                SignUpFragment.checkEmptySignUp();
                if (SignUpFragment.signUpResponseModelList.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    //   Toast.makeText(MainActivity.this, "No Record Found3", Toast.LENGTH_SHORT).show();
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
                mTextViewTitle.setText("Sign-Ups - " + communityName);
            } else if (Position == 3) {
                String Album = Prefs.getAlbum(this);
                String Attachment = Prefs.getAttachment(this);

                if (Album != null) {
                    if (LibraryFragment.photoAlbumResponseModelList.size() == 0) {
                        mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                        //      Toast.makeText(MainActivity.this, "No Record Found4", Toast.LENGTH_SHORT).show();
                    } else {
                        mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                    }
                }
                if (Attachment != null) {
                    if (LibraryFragment.attachmentFileResponseModels.size() == 0) {
                        mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                        Toast.makeText(MainActivity.this, "No Record Found5", Toast.LENGTH_SHORT).show();
                    } else {
                        mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                    }
                }

                mTextViewTitle.setText("Library - " + communityName);
            }
            else if (Position == 4) {
                //    EventsFragment.checkEmptyEvent();
                if (EventsFragment.eventList.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    //  EventsFragment.checkEmptyEvent();
//                    Toast.makeText(MainActivity.this, "No Record Found6", Toast.LENGTH_SHORT).show();
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
                mTextViewTitle.setText("Calendar - " + communityName);
            }
        } else {
            if (Position == 0) {
                if (homeFragment != null)
                    homeFragment.checkEmptyView();
                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                mTextViewTitle.setText(R.string.feed_all_communities);
            } else if (Position == 1) {
                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                mTextViewTitle.setText(R.string.contacts_all_communities);

                String Message = Prefs.getMessage(this);
                String Close = Prefs.getClose(this);
                if (Message == null) {


                    if (Close == null) {
                        if (contactListResponseModels != null) {
                            // contactListResponseModels.clear();
                        }
                        if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                            getApiContactList();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }

                    }

//
                }

            } else if (Position == 2) {
                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                mTextViewTitle.setText(R.string.sign_ups_all_communities);
            } else if (Position == 3) {
                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                mTextViewTitle.setText(R.string.library_all_communities);
            } else if (Position == 4) {
                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                mTextViewTitle.setText(R.string.event_all_communities);
            }
        }

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Prefs.setCommunityFilter(this, null);
        Prefs.setCommunityNAme(this, null);
        Prefs.setCommunityId(this, 0);
        Prefs.setDetail(this, null);
        Prefs.setFeedDetail(this, null);
        Prefs.setClose(this, null);
        Prefs.setAttachment(this, null);
        Prefs.setMessage(this, null);
        Prefs.setSignUpDetail(this, null);
        Prefs.setAlbum(this, null);


    }

    @Override
    public void onStop() {
        super.onStop();
        mSearchView.onActionViewCollapsed();
        mSearchView.clearFocus();


    }

    public static ViewPager viewPager;

    // method to initialization of views
    private void initView() {
        mHelper = new Helper();
        //Local Variables Initialization
        viewPager = (ViewPager) findViewById(R.id.pager);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        relativeLayout.setFocusableInTouchMode(true);
        relativeLayout.requestFocus();

        //Global Variables Initialization
        mImageViewSorting = (ImageView) findViewById(R.id.image_view_down);
        mLinearLayoutNavigationMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTextViewTitle = (TextView) findViewById(R.id.text_view_title);
        mSearchView = (SearchView) findViewById(R.id.serach_view_filter);
        mTextViewSearch = (TextView) findViewById(R.id.text_view_search);
        mRelativeLayoutSearchBar = (RelativeLayout) findViewById(R.id.layout_searchbar);
        mTextViewCancel = (TextView) findViewById(R.id.text_view_cancel);
        mTextViewSignOut = (TextView) findViewById(R.id.text_view_sign_out);
        mImageViewInvitation = (ImageView) findViewById(R.id.image_view_community_invitations);


        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewSearch.setVisibility(View.GONE);
                mSearchView.onActionViewExpanded();

            }
        });


        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mSearchView.clearFocus();
                mSearchView.onActionViewCollapsed();
                mTextViewSearch.setVisibility(View.VISIBLE);
                return false;
            }
        });
        clickListenerOnViews();

        setupViewPager(viewPager);

        setupTabIcons();


    }

    //Method to setClickListener On views
    private void clickListenerOnViews() {
        mImageViewInvitation.setOnClickListener(this);
        mSearchView.setOnQueryTextListener(this);
        mTextViewTitle.setOnClickListener(this);
        mLinearLayoutNavigationMenu.setOnClickListener(this);
        mRelativeLayoutSearchBar.setOnClickListener(this);
        mTextViewCancel.setOnClickListener(this);
    }

    // method to set tab icons
    private void setupTabIcons() {
        if (mTabLayout != null) {
            mTabLayout.getTabAt(0).setIcon(ICON_RES_OVER[0]);
            mTabLayout.getTabAt(1).setIcon(ICON_RES_OVER[1]);
            mTabLayout.getTabAt(2).setIcon(ICON_RES_OVER[2]);
            mTabLayout.getTabAt(3).setIcon(ICON_RES_OVER[3]);
            mTabLayout.getTabAt(4).setIcon(ICON_RES_OVER[4]);
        }
    }

    HomeFragment homeFragment;

    // method to add fragment on viewpager
    private void setupViewPager(final ViewPager viewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();

        mViewPagerAdapter.addFrag(homeFragment, "");
        mViewPagerAdapter.addFrag(new ContactFragment(), "");
        mViewPagerAdapter.addFrag(new SignUpFragment(), "");
        mViewPagerAdapter.addFrag(new LibraryFragment(), "");
        mViewPagerAdapter.addFrag(new EventsFragment(), "");
        viewPager.setAdapter(mViewPagerAdapter);
        int mCommunityId = Prefs.getCommunityId(this);
        if (mCommunityId == 0) {
            mTextViewTitle.setText(R.string.feed_all_communities);
            Prefs.setPosition(this, 0);
            mTextViewSignOut.setVisibility(View.GONE);
            mImageViewInvitation.setVisibility(View.VISIBLE);
        }

        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                mSearchView.onActionViewCollapsed();
                mSearchView.clearFocus();
                mIsFromSearch = false;

                if (HomeFragment.mViewHomeRecyclerView != null) {
                    HomeFragment.mFabPost.setVisibility(View.GONE);
                }
                mViewContactRecyclerView.setVisibility(View.GONE);
                if (LibraryFragment.mViewAlbumsRecyclerView != null) {
                    LibraryFragment.mViewAlbumsRecyclerView.setVisibility(View.GONE);
                    LibraryFragment.mLinearLayoutAlbumFile.setVisibility(View.GONE);
                }


                if (SignUpFragment.mViewSignUpRecyclerView != null)
                    SignUpFragment.mViewSignUpRecyclerView.setVisibility(View.GONE);

                if (HomeFragment.mViewHomeRecyclerView != null)
                    HomeFragment.mViewHomeRecyclerView.setVisibility(View.GONE);

                if (EventsFragment.mViewEventsRecyclerView != null)
                    EventsFragment.mViewEventsRecyclerView.setVisibility(View.GONE);


                Prefs.setDetail(MainActivity.this, null);
                Prefs.setFeedDetail(MainActivity.this, null);
                Prefs.setAlbumDetail(MainActivity.this, null);
                Prefs.setMessage(MainActivity.this, null);
                Prefs.setSignUpDetail(MainActivity.this, null);
                mHelper.keyBoardHidden(MainActivity.this);

                int mCommunityId = Prefs.getCommunityId(MainActivity.this);
                String communityName = Prefs.getCommunityNAme(MainActivity.this);
                int position = tab.getPosition();
                if (mCommunityId == 0) {
                    if (position == 0) {
                        // HomeFragment.checkEmptyView();
                        HomeFragment.mFabPost.setVisibility(View.VISIBLE);
                        mImageViewInvitation.setVisibility(View.VISIBLE);
                        mTextViewSignOut.setVisibility(View.GONE);
                        mTextViewTitle.setText(R.string.feed_all_communities);
                        Prefs.setPosition(MainActivity.this, 0);
                        HomeFragment.mViewHomeRecyclerView.setVisibility(View.VISIBLE);
                        mIsFromSearch = false;
                    } else if (position == 1) {
                        mTextViewTitle.setText(R.string.contacts_all_communities);
                        mTextViewSignOut.setVisibility(View.VISIBLE);
                        mTextViewSignOut.setText("Top");
                        Prefs.setPosition(MainActivity.this, 1);
                        mImageViewInvitation.setVisibility(View.GONE);
                        mIsFromSearch = false;

                        if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                            if (contactListResponseModels.size() == 0) {
                                getApiContactList();
                            } else {
                                mViewContactRecyclerView.setVisibility(View.VISIBLE);
                                ContactFragment.checkEmptyContact();

                                String CommunityFilter = Prefs.getCommunityFilter(MainActivity.this);

                                if (CommunityFilter != null) {
                                    Prefs.setCommunityFilter(MainActivity.this, null);
                                    if (contactListResponseModels != null) {
                                        //e  contactListResponseModels.clear();
                                    }
                                    if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                                        getApiContactList();
                                        //  setScrollView();
                                    } else {
                                        Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    getContactList();
                                    setScrollView();
                                }
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }


                    } else if (position == 2) {
                        // SignUpFragment.checkEmptySignUp();
                        mTextViewTitle.setText(R.string.sign_ups_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        Prefs.setPosition(MainActivity.this, 2);
                        mImageViewInvitation.setVisibility(View.GONE);
                        SignUpFragment.mViewSignUpRecyclerView.setVisibility(View.VISIBLE);
                        mIsFromSearch = false;
                    } else if (position == 3) {
                        //   LibraryFragment.checkEmptyAlbum();
                        mTextViewTitle.setText(R.string.library_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        Prefs.setPosition(MainActivity.this, 3);
                        mImageViewInvitation.setVisibility(View.GONE);
                        LibraryFragment.mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                        LibraryFragment.mLinearLayoutAlbumFile.setVisibility(View.VISIBLE);
                        mIsFromSearch = false;
                    } else if (position == 4) {
                        mTextViewTitle.setText(R.string.event_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        //  EventsFragment.checkEmptyEvent();
                        Prefs.setPosition(MainActivity.this, 4);
                        mImageViewInvitation.setVisibility(View.GONE);
                        EventsFragment.mViewEventsRecyclerView.setVisibility(View.VISIBLE);
                        mIsFromSearch = false;
                    }

                } else {
                    if (position == 0) {
                        HomeFragment.mFabPost.setVisibility(View.VISIBLE);
                        HomeFragment.mViewHomeRecyclerView.setVisibility(View.VISIBLE);
                        if (HomeFragment.feedResponseModelList.size() == 0) {
                            mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                            Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        } else {
                            mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                        }
                        mImageViewInvitation.setVisibility(View.VISIBLE);
                        mTextViewTitle.setText("Feed - " + communityName);
                        mTextViewSignOut.setVisibility(View.GONE);
                        Prefs.setPosition(MainActivity.this, 0);
                        mIsFromSearch = false;
                    } else if (position == 1) {

                        if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                            if (contactListResponseModels.size() == 0) {
                                getApiContactList();
                            } else {
                                mViewContactRecyclerView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }

                        if (contactListResponseModels.size() == 0) {
                            mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                            Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        } else {
                            mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                        }
                        ContactFragment.checkEmptyContact();
                        mTextViewTitle.setText("Contacts - " + communityName);
                        mTextViewSignOut.setVisibility(View.VISIBLE);
                        mTextViewSignOut.setText("Top");
                        Prefs.setPosition(MainActivity.this, 1);
                        mImageViewInvitation.setVisibility(View.GONE);
                        mIsFromSearch = false;
                    } else if (position == 2) {
                        SignUpFragment.checkEmptySignUp();
                        SignUpFragment.mViewSignUpRecyclerView.setVisibility(View.VISIBLE);
                        if (SignUpFragment.signUpResponseModelList.size() == 0) {
                            mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                            Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        } else {
                            mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                        }
                        mTextViewTitle.setText("Sign-Ups - " + communityName);
                        mTextViewSignOut.setVisibility(View.GONE);
                        Prefs.setPosition(MainActivity.this, 2);
                        mImageViewInvitation.setVisibility(View.GONE);
                        mIsFromSearch = false;
                    } else if (position == 3) {

                        LibraryFragment.mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                        LibraryFragment.mLinearLayoutAlbumFile.setVisibility(View.VISIBLE);
                        String Album = Prefs.getAlbum(MainActivity.this);
                        String Attachment = Prefs.getAttachment(MainActivity.this);
                        if (Album != null) {
                            //  LibraryFragment.checkEmptyAlbum();
                            if (LibraryFragment.photoAlbumResponseModelList.size() == 0) {
                                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                                Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                            } else {
                                mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                            }
                        }
                        if (Attachment != null) {
                            //LibraryFragment
                            // LibraryFragment.checkEmptyLib();
                            if (LibraryFragment.attachmentFileResponseModels.size() == 0) {
                                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                                Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                            } else {
                                mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                            }
                        }
                        mTextViewTitle.setText("Library - " + communityName);
                        mTextViewSignOut.setVisibility(View.GONE);
                        Prefs.setPosition(MainActivity.this, 3);
                        mImageViewInvitation.setVisibility(View.GONE);
                        mIsFromSearch = false;
                    } else if (position == 4) {
                        EventsFragment.checkEmptyEvent2();
                        if (EventsFragment.eventList.size() == 0) {
                            mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                            //   Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        } else {
                            mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                        }

                        mTextViewTitle.setText("Calendar - " + communityName);
                        mTextViewSignOut.setVisibility(View.GONE);
                        Prefs.setPosition(MainActivity.this, 4);
                        mImageViewInvitation.setVisibility(View.GONE);
                        EventsFragment.mViewEventsRecyclerView.setVisibility(View.VISIBLE);
                        mIsFromSearch = false;
                    }

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.equalsIgnoreCase("")) {
            mTextViewSearch.setVisibility(View.VISIBLE);
        } else {
            mTextViewSearch.setVisibility(View.GONE);

        }
        int limit = 150, offset = 0;
        if (newText != null || newText != "") {
            if (newText.length() > 2)
                if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                    getApiContactListForSearch(newText, limit, offset);
                } else {
                    Toast.makeText(MainActivity.this, R.string.network_error,
                            Toast.LENGTH_SHORT).show();
                }
        }


        int Position = Prefs.getPosition(this);
        String CommunityFilter = Prefs.getCommunityFilter(this);
        if (Position == 0) {
            mIsFromSearch = false;
            if (CommunityFilter == null) {
                HomeFragment homeFragment = (HomeFragment) mViewPagerAdapter.getItem(0);
                homeFragment.SearchFilterForFeed(HomeFragment.feedResponseModelList, newText);
            } else {
                Prefs.setCommunityFilter(this, null);
            }
        } else if (Position == 1) {
            mIsFromSearch = false;
            if (CommunityFilter == null) {
                int communityId = Prefs.getCommunityId(this);
                if (communityId != 0) {
                    ContactFragment contactFragment = (ContactFragment) mViewPagerAdapter.getItem(1);
                    // if()
                    contactFragment.SearchFilterForContact(mContactListResponseSearch, newText);
                    mIsFromSearch = true;
                } else {
                    ContactFragment contactFragment = (ContactFragment) mViewPagerAdapter.getItem(1);
                    contactFragment.SearchFilterForContact(mContactListResponseSearch, newText);
                    mIsFromSearch = true;
                }
            } else {
                Prefs.setCommunityFilter(this, null);
            }


        } else if (Position == 2) {
            mIsFromSearch = false;
            if (CommunityFilter == null) {
                SignUpFragment signUpFragment = (SignUpFragment) mViewPagerAdapter.getItem(2);
                signUpFragment.SearchFilterForSignUp(SignUpFragment.signUpResponseModelList, newText);
            } else {
                Prefs.setCommunityFilter(this, null);
            }

        } else if (Position == 3) {
            mIsFromSearch = false;
            if (CommunityFilter == null) {
                LibraryFragment libraryFragment = (LibraryFragment) mViewPagerAdapter.getItem(3);
                libraryFragment.SearchFilterForAlbum(LibraryFragment.photoAlbumResponseModelList, newText);
                libraryFragment.SearchFilterForAttachment(LibraryFragment.attachmentFileResponseModels, newText);
            } else {
                Prefs.setCommunityFilter(this, null);
            }

        } else if (Position == 4) {
            mIsFromSearch = false;
            if (CommunityFilter == null) {
                EventsFragment eventsFragment = (EventsFragment) mViewPagerAdapter.getItem(4);
                eventsFragment.SearchFilterForEvent(EventsFragment.eventList, newText);
            } else {
                Prefs.setCommunityFilter(this, null);
            }
        }
        return true;
    }


    public long getDifferenceTime() {
        return differenceTime;
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
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

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }


    // Get webservice to show all UpToUs member
    public void getApiContactList() {
        Log.i("MainActivity", "Requesting .... contact ");
        //Check if model is not empty and last update must once  in a while default 45411ms

        //   int contactLastUpdated = Prefs.getContactLastUpdated(this);
        //  long difference = getDifferenceTime();


        if (contactListResponseModels == null || contactListResponseModels.size() == 0 || !LoadOnce) {
            //  showProgressDialog();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show(); //add
            String mAuthenticationId = Prefs.getAuthenticationId(this);
            String mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
            APIServices service =
                    ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
            Call<List<ContactListResponseModel>> call = service.GetContactList(mLimit, mOffset);

            call.enqueue(new Callback<List<ContactListResponseModel>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<List<ContactListResponseModel>> call, Response<List<ContactListResponseModel>> response) {
                    try {
                        Log.i("MainActivity", "Got Request .... contact ");
                        if (response.body() != null && response.body().size() > 0) {
                            //     progressDialog.dismiss();
                            for (ContactListResponseModel item : response.body()) {
                                if (item.getFirstName() != null && item.getFirstName() != "" ||
                                        item.getLastName() != null && item.getLastName() != "") {
                                    MainActivity.contactListResponseModels.add(item);
                                }
                            }

                            Prefs.setContactList(MainActivity.this, contactListResponseModels.toString());
                            LoadOnce = true;
                            mTextViewSearchResult.setVisibility(View.GONE);
                            Collections.sort(MainActivity.contactListResponseModels, new CustomComparator());
                            int communityId = Prefs.getCommunityId(MainActivity.this);
                            if (communityId != 0) {
                                FilterCommunityForContact(MainActivity.contactListResponseModels, communityId);
                            }
                            ContactFragment.checkEmptyContact();
                            mViewContactRecyclerView.getAdapter().notifyDataSetChanged();
                            mViewContactRecyclerView.setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        } else {
                            // Toast.makeText(MainActivity.this, "End list - No Contact", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<ContactListResponseModel>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }


    //    // Get webservice to show all UpToUs member
    public void getApiContactListForSearch(String queryText, int limit, int offset) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show(); //add
        String mAuthenticationId = Prefs.getAuthenticationId(this);
        String mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<ContactListResponseModel>> call = service.GetContactListForSearch(queryText, limit, offset);

        call.enqueue(new Callback<List<ContactListResponseModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<ContactListResponseModel>> call, Response<List<ContactListResponseModel>> response) {
                try {
                    if (response.body() != null && response.body().size() > 0) {
                        //   progressDialog.dismiss();
                        mContactListResponseSearch.clear();
                        for (ContactListResponseModel item : response.body()) {
                            if (item.getFirstName() != null && item.getFirstName() != "" ||
                                    item.getLastName() != null && item.getLastName() != "") {
                                mContactListResponseSearch.add(item);
                            }
                        }

                        mIsQueryText = true;
                        mTextViewSearchResult.setVisibility(View.GONE);
                        Collections.sort(MainActivity.mContactListResponseSearch, new CustomComparator());
                        ContactFragment.checkEmptyContact();
                        mContactListForSearchAdapter.notifyCategoryAdapter(mContactListResponseSearch);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        //  Toast.makeText(MainActivity.this, "No Contact", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ContactListResponseModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    // Method to filter feed by community
    private List<ContactListResponseModel> FilterCommunityForContact(List<ContactListResponseModel> models, int Id) {
        MainActivity.contactListResponseModels = new ArrayList<>();
        try {
            for (ContactListResponseModel model : models) {
                List<ContactListResponseModel.CommunitiesBean> com = model.getCommunities();
                for (ContactListResponseModel.CommunitiesBean c : com) {
                    final int CommunityID = c.getId();
                    if (CommunityID == Id) {
                        MainActivity.contactListResponseModels.add(model);
                    }
                }

                mViewContactRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mContactListAdapter = new ContactListAdapter(MainActivity.this, MainActivity.contactListResponseModels);
                mViewContactRecyclerView.setAdapter(mContactListAdapter);
                mContactListAdapter.notifyDataSetChanged();
            }
            int Position = Prefs.getPosition(this);

            if (Position == 1) {
                mIsFromSearch = false;
                if (MainActivity.contactListResponseModels.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return MainActivity.contactListResponseModels;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_COMMUNITY_CHANGE) {
            if (homeFragment != null)
                homeFragment.refresh();
            LoadOnce = false;
            showProgressDialog();
            if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                getApiContactList();
            } else {
                Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 121) {
            if (homeFragment != null)
                homeFragment.refresh();
            LoadOnce = false;
            showProgressDialog();
        }

    }


    // Get webservice to show all UpToUs member
    public void getContactList() {
        try {
            if (contactListResponseModels.size() != 0) {

                String Message = Prefs.getMessage(this);
                if (Message == null) {
                    mContactListAdapter = new ContactListAdapter(this, contactListResponseModels);
                    mViewContactRecyclerView.setAdapter(mContactListAdapter);
                    Log.i("ContactFragment", "Setting Adapter .... contact ");
                }

                mViewContactRecyclerView.setVisibility(View.VISIBLE);
                int communityId = Prefs.getCommunityId(this);
                if (Message == null) {
                    if (communityId != 0) {
                        Log.i("ContactFragment", "Filtering  Adapter .... contact ");
                        FilterCommunityForContact(contactListResponseModels, communityId);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setScrollView() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mViewContactRecyclerView.setLayoutManager(layoutManager);
        mViewContactRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mViewContactRecyclerView.setAdapter(mContactListAdapter);
        mViewContactRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = layoutManager.getChildCount();
                totalItem = layoutManager.getItemCount();
                scrolledOutItem = layoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItem + scrolledOutItem == totalItem)) {
                    isScrolling = false;
                    getData();
                }
            }
        });
    }

    private void getData() {

        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);

//                ______________________ IOS Logic start_______________________________
                // first time
//                var isDataLoading:Bool=false
//                var pageNo:Int=0
//                var limit:Int=150
//                var offset:Int=0 //pageNo*limit
//                var didEndReached:Bool=false
//
                // calling time
//                self.pageNo=self.pageNo+1
//                self.limit=self.limit+150
//                self.offset=self.limit * self.pageNo
                //loadCallLogData(offset: self.offset, limit: self.limit)
                //            ______________________ IOS Logic end_______________________________


                int offset = pageN0*mLimit;
                pageN0++;
                // all again api with new limit and offset request
                if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                    getApiContactListApi(mLimit, offset);
                } else {
                    Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000); // 1 sec delay

    }

    public static void checkEmptyContact() {
        if (recycler_view_empty1 != null && mViewContactRecyclerView != null) {
            if (contactListResponseModels.size() == 0) {
                recycler_view_empty1.setVisibility(View.VISIBLE);
                mViewContactRecyclerView.setVisibility(View.GONE);
            } else {
                mViewContactRecyclerView.setVisibility(View.VISIBLE);
                recycler_view_empty1.setVisibility(View.GONE);
            }
        }
    }

    // Get webservice to show all UpToUs member
    public void getApiContactListApi(int limit, int offset) {
        if (contactListResponseModels != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show(); //add

            String mAuthenticationId = Prefs.getAuthenticationId(this);
            String mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
            APIServices service =
                    ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
            Call<List<ContactListResponseModel>> call = service.GetContactList(limit, offset);
            call.enqueue(new Callback<List<ContactListResponseModel>>() {
                @Override
                public void onResponse(Call<List<ContactListResponseModel>> call, Response<List<ContactListResponseModel>> response) {
                    try {
                        Log.i("MainActivity", "Got Request .... contact ");

                        if (response.body() != null && response.body().size() > 0) {
                            for (ContactListResponseModel item : response.body()) {
                                if (item.getFirstName() != null && item.getFirstName() != "" ||
                                        item.getLastName() != null && item.getLastName() != "") {
                                    MainActivity.contactListResponseModels.add(item);
                                }
                            }
                            mViewContactRecyclerView.getAdapter().notifyDataSetChanged();
                            Prefs.setContactList(MainActivity.this, MainActivity.contactListResponseModels.toString());
                            LoadOnce = true;
                            mTextViewSearchResult.setVisibility(View.GONE);
                            mViewContactRecyclerView.setVisibility(View.VISIBLE);
                            Collections.sort(MainActivity.contactListResponseModels, new CustomComparator());
                            int communityId = Prefs.getCommunityId(MainActivity.this);
                            if (communityId != 0) {
                                FilterCommunityForContact(MainActivity.contactListResponseModels, communityId);
                            }
                            checkEmptyContact();
                            progressDialog.dismiss();
                        } else {
                            //  Toast.makeText(MainActivity.this, "End list - No Contact", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<ContactListResponseModel>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

}
