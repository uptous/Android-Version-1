package com.uptous.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    public SearchView mSearchView;
    private long differenceTime;
    private String text;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


        // Get run time permission if device version N
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            } else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
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
            } else if (Position == 4) {
                EventsFragment.checkEmptyEvent();
                if (EventsFragment.eventList.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    EventsFragment.checkEmptyEvent();
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

        RelativeLayout relativeLayout =
                (RelativeLayout) findViewById(R.id.relative);
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

                if (HomeFragment.mViewHomeRecyclerView != null) {
                    HomeFragment.mFabPost.setVisibility(View.GONE);
                }
                ContactFragment.mViewContactRecyclerView.setVisibility(View.GONE);


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
                        HomeFragment.checkEmptyView();
                        HomeFragment.mFabPost.setVisibility(View.VISIBLE);
                        mImageViewInvitation.setVisibility(View.VISIBLE);
                        mTextViewSignOut.setVisibility(View.GONE);
                        mTextViewTitle.setText(R.string.feed_all_communities);
                        Prefs.setPosition(MainActivity.this, 0);
                        HomeFragment.mViewHomeRecyclerView.setVisibility(View.VISIBLE);
                    } else if (position == 1) {

                        mTextViewTitle.setText(R.string.contacts_all_communities);
                        mTextViewSignOut.setVisibility(View.VISIBLE);
                        mTextViewSignOut.setText("Top");
                        Prefs.setPosition(MainActivity.this, 1);
                        mImageViewInvitation.setVisibility(View.GONE);

                        if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                            if (contactListResponseModels.size() == 0) {
                                getApiContactList();
                            } else {
                                ContactFragment.mViewContactRecyclerView.setVisibility(View.VISIBLE);
                                ContactFragment.checkEmptyContact();

                                String CommunityFilter = Prefs.getCommunityFilter(MainActivity.this);

                                if (CommunityFilter != null) {
                                    Prefs.setCommunityFilter(MainActivity.this, null);
                                    if (contactListResponseModels != null) {
                                        //e  contactListResponseModels.clear();
                                    }


                                    if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                                        getApiContactList();
                                    } else {
                                        Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }


                    } else if (position == 2) {
                        SignUpFragment.checkEmptySignUp();
                        mTextViewTitle.setText(R.string.sign_ups_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        Prefs.setPosition(MainActivity.this, 2);
                        mImageViewInvitation.setVisibility(View.GONE);
                        SignUpFragment.mViewSignUpRecyclerView.setVisibility(View.VISIBLE);
                    } else if (position == 3) {
                        LibraryFragment.checkEmptyAlbum();
                        mTextViewTitle.setText(R.string.library_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        Prefs.setPosition(MainActivity.this, 3);
                        mImageViewInvitation.setVisibility(View.GONE);
                        LibraryFragment.mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                        LibraryFragment.mLinearLayoutAlbumFile.setVisibility(View.VISIBLE);
                    } else if (position == 4) {
                        mTextViewTitle.setText(R.string.event_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        EventsFragment.checkEmptyEvent();
                        Prefs.setPosition(MainActivity.this, 4);
                        mImageViewInvitation.setVisibility(View.GONE);
                        EventsFragment.mViewEventsRecyclerView.setVisibility(View.VISIBLE);

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
                    } else if (position == 1) {

                        if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                            if (contactListResponseModels.size() == 0) {
                                getApiContactList();
                            } else {
                                ContactFragment.mViewContactRecyclerView.setVisibility(View.VISIBLE);
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
                    } else if (position == 3) {

                        LibraryFragment.mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                        LibraryFragment.mLinearLayoutAlbumFile.setVisibility(View.VISIBLE);
                        String Album = Prefs.getAlbum(MainActivity.this);
                        String Attachment = Prefs.getAttachment(MainActivity.this);
                        if (Album != null) {
                            LibraryFragment.checkEmptyAlbum();
                            if (LibraryFragment.photoAlbumResponseModelList.size() == 0) {
                                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
//                                Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                            } else {
                                mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                            }
                        }
                        if (Attachment != null) {
                            //LibraryFragment
                            LibraryFragment.checkEmptyLib();
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
                    } else if (position == 4) {
                        EventsFragment.checkEmptyEvent();
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

        int Position = Prefs.getPosition(this);
        String CommunityFilter = Prefs.getCommunityFilter(this);
        if (Position == 0) {


            if (CommunityFilter == null) {
                HomeFragment homeFragment = (HomeFragment) mViewPagerAdapter.getItem(0);
                homeFragment.SearchFilterForFeed(HomeFragment.feedResponseModelList, newText);
            } else {
                Prefs.setCommunityFilter(this, null);
            }
        } else if (Position == 1) {


            if (CommunityFilter == null) {

                int communityId = Prefs.getCommunityId(this);
                if (communityId != 0) {
                    ContactFragment contactFragment = (ContactFragment) mViewPagerAdapter.getItem(1);
                    contactFragment.SearchFilterForContact(contactListResponseModels, newText);

                } else {
                    ContactFragment contactFragment = (ContactFragment) mViewPagerAdapter.getItem(1);
                    contactFragment.SearchFilterForContact(contactListResponseModels, newText);
                }

            } else {
                Prefs.setCommunityFilter(this, null);
            }


        } else if (Position == 2) {
            if (CommunityFilter == null) {
                SignUpFragment signUpFragment = (SignUpFragment) mViewPagerAdapter.getItem(2);
                signUpFragment.SearchFilterForSignUp(SignUpFragment.signUpResponseModelList, newText);
            } else {
                Prefs.setCommunityFilter(this, null);
            }


        } else if (Position == 3) {
            if (CommunityFilter == null) {
                LibraryFragment libraryFragment = (LibraryFragment) mViewPagerAdapter.getItem(3);
                libraryFragment.SearchFilterForAlbum(LibraryFragment.photoAlbumResponseModelList, newText);
                libraryFragment.SearchFilterForAttachment(LibraryFragment.attachmentFileResponseModels, newText);
            } else {
                Prefs.setCommunityFilter(this, null);
            }


        } else if (Position == 4) {
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

    static Boolean LoadOnce;

    // Get webservice to show all UpToUs member
    public void getApiContactList() {

        Log.i("MainActivity", "Requesting .... contact ");
        //Check if model is not empty and last update must once  in a while default 45411ms
//        int contactLastUpdated = Prefs.getContactLastUpdated(this);
//        long difference = getDifferenceTime();
        if (contactListResponseModels == null || contactListResponseModels.size() == 0 || !LoadOnce) {
            showProgressDialog();
            String mAuthenticationId = Prefs.getAuthenticationId(this);
            String mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
            APIServices service =
                    ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
            Call<List<ContactListResponseModel>> call = service.GetContactList();

            call.enqueue(new Callback<List<ContactListResponseModel>>() {
                @Override
                public void onResponse(Call<List<ContactListResponseModel>> call, Response<List<ContactListResponseModel>> response) {
                    try {
                        Log.i("MainActivity", "Got Request .... contact ");

                        ContactFragment.mViewContactRecyclerView.setVisibility(View.VISIBLE);
                        hideProgressDialog();
                        if (response.body() != null) {

                            contactListResponseModels = response.body();
                            Prefs.setContactList(MainActivity.this, contactListResponseModels.toString());
                            LoadOnce = true;
                            mContactListAdapter = new ContactListAdapter(MainActivity.this, contactListResponseModels);
                            ContactFragment.mViewContactRecyclerView.setAdapter(mContactListAdapter);
                            ContactFragment.mTextViewSearchResult.setVisibility(View.GONE);
                            Collections.sort(contactListResponseModels, new CustomComparator());
                            int communityId = Prefs.getCommunityId(MainActivity.this);
                            if (communityId != 0) {

                                FilterCommunityForContact(MainActivity.contactListResponseModels, communityId);
                            }
                            ContactFragment.checkEmptyContact();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<List<ContactListResponseModel>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();

                    hideProgressDialog();
                }

            });
        }
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

                ContactFragment.mViewContactRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mContactListAdapter = new ContactListAdapter(MainActivity.this, MainActivity.contactListResponseModels);
                ContactFragment.mViewContactRecyclerView.setAdapter(mContactListAdapter);
                mContactListAdapter.notifyDataSetChanged();


            }
            int Position = Prefs.getPosition(this);

            if (Position == 1) {
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
            getApiContactList();
        } else if (requestCode == 121) {
            if (homeFragment != null)
                homeFragment.refresh();
            LoadOnce = false;
            showProgressDialog();
        }

    }
}
