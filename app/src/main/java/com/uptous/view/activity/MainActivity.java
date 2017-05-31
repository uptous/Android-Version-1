package com.uptous.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.uptous.MyApplication;
import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.Helper;
import com.uptous.model.ContactListResponseModel;
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
public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

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

                MyApplication.editor.putString("CommunityFilter", "community");
                MyApplication.editor.putString("Message", null);
                MyApplication.editor.commit();
                Intent intentCommunity = new Intent(MainActivity.this, CommunityActivity.class);
                startActivity(intentCommunity);
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
        int Position = MyApplication.mSharedPreferences.getInt("Position", 0);
        int CommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);
        String communityName = MyApplication.mSharedPreferences.getString("CommunityName", null);

        if (CommunityId != 0) {
            if (Position == 0) {
                if (HomeFragment.feedResponseModelList.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
                mTextViewTitle.setText("Feed - " + communityName);
            } else if (Position == 1) {
                if (contactListResponseModels.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
                mTextViewTitle.setText("Contacts - " + communityName);
            } else if (Position == 2) {
                if (SignUpFragment.signUpResponseModelList.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
                mTextViewTitle.setText("Sign-Ups - " + communityName);
            } else if (Position == 3) {
                String Album = MyApplication.mSharedPreferences.getString("Album", null);
                String Attachment = MyApplication.mSharedPreferences.getString("Attachment", null);

                if (Album != null) {
                    if (LibraryFragment.photoAlbumResponseModelList.size() == 0) {
                        mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                        Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    } else {
                        mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                    }
                }
                if (Attachment != null) {
                    if (LibraryFragment.attachmentFileResponseModels.size() == 0) {
                        mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                        Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    } else {
                        mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                    }
                }

                mTextViewTitle.setText("Library - " + communityName);
            } else if (Position == 4) {
                if (EventsFragment.eventList.size() == 0) {
                    mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                    Toast.makeText(MainActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                } else {
                    mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                }
                mTextViewTitle.setText("Calendar - " + communityName);
            }
        } else {
            if (Position == 0) {
                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                mTextViewTitle.setText(R.string.feed_all_communities);
            } else if (Position == 1) {
                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                mTextViewTitle.setText(R.string.contacts_all_communities);

                String Message = MyApplication.mSharedPreferences.getString("Message", null);
                String Close = MyApplication.mSharedPreferences.getString("Close", null);
                if (Message == null) {


                    if (Close == null) {
                        if (contactListResponseModels != null) {
                            contactListResponseModels.clear();
                        }
                        if(ConnectionDetector.isConnectingToInternet(MainActivity.this)){
                            getApiContactList();
                        }else {
                            Toast.makeText(MainActivity.this,R.string.network_error,Toast.LENGTH_SHORT).show();
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
        MyApplication.editor.putString("CommunityFilter", null);
        MyApplication.editor.putString("CommunityName", null);
        MyApplication.editor.putInt("CommunityId", 0);
        MyApplication.editor.putString("Attachment", null);
        MyApplication.editor.putString("Album", null);
        MyApplication.editor.putString("Message", null);
        MyApplication.editor.putString("Detail", null);
        MyApplication.editor.putString("FeedDetail", null);
        MyApplication.editor.putString("SignUpDetail", null);
        MyApplication.editor.putString("Close", null);
        MyApplication.editor.commit();


    }

    @Override
    protected void onStop() {
        super.onStop();
        mSearchView.onActionViewCollapsed();
        mSearchView.clearFocus();


    }

    // method to initialization of views
    private void initView() {
        mHelper = new Helper();

        //Local Variables Initialization
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

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

        mTabLayout.getTabAt(0).setIcon(ICON_RES_OVER[0]);
        mTabLayout.getTabAt(1).setIcon(ICON_RES_OVER[1]);
        mTabLayout.getTabAt(2).setIcon(ICON_RES_OVER[2]);
        mTabLayout.getTabAt(3).setIcon(ICON_RES_OVER[3]);
        mTabLayout.getTabAt(4).setIcon(ICON_RES_OVER[4]);
    }


    // method to add fragment on viewpager
    private void setupViewPager(final ViewPager viewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFrag(new HomeFragment(), "");
        mViewPagerAdapter.addFrag(new ContactFragment(), "");
        mViewPagerAdapter.addFrag(new SignUpFragment(), "");
        mViewPagerAdapter.addFrag(new LibraryFragment(), "");
        mViewPagerAdapter.addFrag(new EventsFragment(), "");
        viewPager.setAdapter(mViewPagerAdapter);
        int mCommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);
        if (mCommunityId == 0) {
            mTextViewTitle.setText(R.string.feed_all_communities);
            MyApplication.editor.putInt("Position", 0);
            MyApplication.editor.commit();
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

                MyApplication.editor.putString("Message", null);
                MyApplication.editor.putString("AlbumDetail", null);
                MyApplication.editor.putString("SignUpDetail", null);
                MyApplication.editor.putString("Detail", null);
                MyApplication.editor.putString("FeedDetail", null);
                MyApplication.editor.commit();
                mHelper.keyBoardHidden(MainActivity.this);

                int mCommunityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);
                String communityName = MyApplication.mSharedPreferences.getString("CommunityName", null);
                int position = tab.getPosition();
                if (mCommunityId == 0) {
                    if (position == 0) {
                        HomeFragment.mFabPost.setVisibility(View.VISIBLE);
                        mImageViewInvitation.setVisibility(View.VISIBLE);
                        mTextViewSignOut.setVisibility(View.GONE);
                        mTextViewTitle.setText(R.string.feed_all_communities);
                        MyApplication.editor.putInt("Position", 0);
                        MyApplication.editor.commit();
                        HomeFragment.mViewHomeRecyclerView.setVisibility(View.VISIBLE);
                    } else if (position == 1) {

                        mTextViewTitle.setText(R.string.contacts_all_communities);
                        mTextViewSignOut.setVisibility(View.VISIBLE);
                        mTextViewSignOut.setText("Top");
                        MyApplication.editor.putInt("Position", 1);
                        MyApplication.editor.commit();
                        mImageViewInvitation.setVisibility(View.GONE);

                        if (ConnectionDetector.isConnectingToInternet(MainActivity.this)) {
                            if (contactListResponseModels.size() == 0) {
                                getApiContactList();
                            } else {
                                ContactFragment.mViewContactRecyclerView.setVisibility(View.VISIBLE);


                                String CommunityFilter = MyApplication.mSharedPreferences.getString("CommunityFilter", null);

                                if (CommunityFilter != null) {
                                    MyApplication.editor.putString("CommunityFilter", null);
                                    MyApplication.editor.commit();
                                    if (contactListResponseModels != null) {
                                        contactListResponseModels.clear();
                                    }


                                    if(ConnectionDetector.isConnectingToInternet(MainActivity.this)){
                                        getApiContactList();
                                    }else {
                                        Toast.makeText(MainActivity.this,R.string.network_error,Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        } else {
                            Toast.makeText(MainActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        }


                    } else if (position == 2) {

                        mTextViewTitle.setText(R.string.sign_ups_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        MyApplication.editor.putInt("Position", 2);
                        MyApplication.editor.commit();
                        mImageViewInvitation.setVisibility(View.GONE);
                        SignUpFragment.mViewSignUpRecyclerView.setVisibility(View.VISIBLE);
                    } else if (position == 3) {

                        mTextViewTitle.setText(R.string.library_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        MyApplication.editor.putInt("Position", 3);
                        MyApplication.editor.commit();
                        mImageViewInvitation.setVisibility(View.GONE);
                        LibraryFragment.mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                        LibraryFragment.mLinearLayoutAlbumFile.setVisibility(View.VISIBLE);
                    } else if (position == 4) {
                        mTextViewTitle.setText(R.string.event_all_communities);
                        mTextViewSignOut.setVisibility(View.GONE);
                        MyApplication.editor.putInt("Position", 4);
                        MyApplication.editor.commit();
                        mImageViewInvitation.setVisibility(View.GONE);
                        EventsFragment.mViewEventsRecyclerView.setVisibility(View.VISIBLE);

                    }

                } else {
                    if (position == 0) {
                        HomeFragment.mFabPost.setVisibility(View.VISIBLE);
                        HomeFragment.mViewHomeRecyclerView.setVisibility(View.VISIBLE);
                        if (HomeFragment.feedResponseModelList.size() == 0) {
                            mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                            Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        } else {
                            mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                        }
                        mImageViewInvitation.setVisibility(View.VISIBLE);
                        mTextViewTitle.setText("Feed - " + communityName);
                        mTextViewSignOut.setVisibility(View.GONE);
                        MyApplication.editor.putInt("Position", 0);
                        MyApplication.editor.commit();
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
                            Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        } else {
                            mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                        }
                        mTextViewTitle.setText("Contacts - " + communityName);
                        mTextViewSignOut.setVisibility(View.VISIBLE);
                        mTextViewSignOut.setText("Top");
                        MyApplication.editor.putInt("Position", 1);

                        MyApplication.editor.commit();
                        mImageViewInvitation.setVisibility(View.GONE);
                    } else if (position == 2) {
                        SignUpFragment.mViewSignUpRecyclerView.setVisibility(View.VISIBLE);
                        if (SignUpFragment.signUpResponseModelList.size() == 0) {
                            mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                            Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        } else {
                            mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                        }
                        mTextViewTitle.setText("Sign-Ups - " + communityName);
                        mTextViewSignOut.setVisibility(View.GONE);
                        MyApplication.editor.putInt("Position", 2);
                        MyApplication.editor.commit();
                        mImageViewInvitation.setVisibility(View.GONE);
                    } else if (position == 3) {
                        LibraryFragment.mViewAlbumsRecyclerView.setVisibility(View.VISIBLE);
                        LibraryFragment.mLinearLayoutAlbumFile.setVisibility(View.VISIBLE);
                        String Album = MyApplication.mSharedPreferences.getString("Album", null);
                        String Attachment = MyApplication.mSharedPreferences.getString("Attachment", null);
                        if (Album != null) {
                            if (LibraryFragment.photoAlbumResponseModelList.size() == 0) {
                                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                                Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                            } else {
                                mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                            }
                        }
                        if (Attachment != null) {
                            if (LibraryFragment.attachmentFileResponseModels.size() == 0) {
                                mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                                Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                            } else {
                                mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                            }
                        }
                        mTextViewTitle.setText("Library - " + communityName);
                        mTextViewSignOut.setVisibility(View.GONE);
                        MyApplication.editor.putInt("Position", 3);
                        mImageViewInvitation.setVisibility(View.GONE);
                        MyApplication.editor.commit();
                    } else if (position == 4) {

                        if (EventsFragment.eventList.size() == 0) {
                            mImageViewSorting.setBackgroundResource(R.mipmap.down_sorting_arrow);
                            Toast.makeText(MainActivity.this, R.string.no_record_found, Toast.LENGTH_SHORT).show();
                        } else {
                            mImageViewSorting.setBackgroundResource(R.mipmap.up_sorting_arrow);
                        }

                        mTextViewTitle.setText("Calendar - " + communityName);
                        mTextViewSignOut.setVisibility(View.GONE);
                        MyApplication.editor.putInt("Position", 4);
                        MyApplication.editor.commit();
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

        int Position = MyApplication.mSharedPreferences.getInt("Position", 0);
        String CommunityFilter = MyApplication.mSharedPreferences.getString("CommunityFilter", null);
        if (Position == 0) {


            if (CommunityFilter == null) {
                HomeFragment homeFragment = (HomeFragment) mViewPagerAdapter.getItem(0);
                homeFragment.SearchFilterForFeed(HomeFragment.feedResponseModelList, newText);
            } else {
                MyApplication.editor.putString("CommunityFilter", null);
                MyApplication.editor.commit();
            }
        } else if (Position == 1) {


            if (CommunityFilter == null) {

                int communityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);
                if (communityId != 0) {
                    ContactFragment contactFragment = (ContactFragment) mViewPagerAdapter.getItem(1);
                    contactFragment.SearchFilterForContact(ContactFragment.contactListResponseModels, newText);

                } else {
                    ContactFragment contactFragment = (ContactFragment) mViewPagerAdapter.getItem(1);
                    contactFragment.SearchFilterForContact(contactListResponseModels, newText);
                }

            } else {
                MyApplication.editor.putString("CommunityFilter", null);
                MyApplication.editor.commit();
            }


        } else if (Position == 2) {
            if (CommunityFilter == null) {
                SignUpFragment signUpFragment = (SignUpFragment) mViewPagerAdapter.getItem(2);
                signUpFragment.SearchFilterForSignUp(SignUpFragment.signUpResponseModelList, newText);
            } else {
                MyApplication.editor.putString("CommunityFilter", null);
                MyApplication.editor.commit();
            }


        } else if (Position == 3) {
            if (CommunityFilter == null) {
                LibraryFragment libraryFragment = (LibraryFragment) mViewPagerAdapter.getItem(3);
                libraryFragment.SearchFilterForAlbum(LibraryFragment.photoAlbumResponseModelList, newText);
                libraryFragment.SearchFilterForAttachment(LibraryFragment.attachmentFileResponseModels, newText);
            } else {
                MyApplication.editor.putString("CommunityFilter", null);
                MyApplication.editor.commit();
            }


        } else if (Position == 4) {
            if (CommunityFilter == null) {
                EventsFragment eventsFragment = (EventsFragment) mViewPagerAdapter.getItem(4);
                eventsFragment.SearchFilterForEvent(EventsFragment.eventList, newText);
            } else {
                MyApplication.editor.putString("CommunityFilter", null);
                MyApplication.editor.commit();
            }

        }
        return true;
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

    //Method to logout from app
    public void logOut() {
        if (contactListResponseModels != null) {
            contactListResponseModels.clear();
        }
        MyApplication.editor.putString("CommunityName", null);
        MyApplication.editor.putInt("CommunityId", 0);
        MyApplication.editor.putString("Attachment", null);
        MyApplication.editor.putString("Album", null);
        MyApplication.editor.putString("Detail", null);
        MyApplication.editor.putString("AuthenticationId", null);
        MyApplication.editor.putString("AuthenticationPassword", null);
        MyApplication.editor.putString("CommunityFilter", null);
        MyApplication.editor.putString("Close", null);
        MyApplication.editor.putBoolean(MyApplication.ISLOGIN, false);
        MyApplication.editor.commit();


    }

    // Get webservice to show all UpToUs member
    public void getApiContactList() {


        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait..");

        if (contactListResponseModels.size() == 0)
            progressDialog.show();
        progressDialog.setCancelable(false);
        String mAuthenticationId = MyApplication.mSharedPreferences.getString("AuthenticationId", null);
        String mAuthenticationPassword = MyApplication.mSharedPreferences.getString("AuthenticationPassword", null);
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<ContactListResponseModel>> call = service.GetContactList();

        call.enqueue(new Callback<List<ContactListResponseModel>>() {
            @Override
            public void onResponse(Call<List<ContactListResponseModel>> call, Response<List<ContactListResponseModel>> response) {
                try {
                    ContactFragment.mViewContactRecyclerView.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    if (response.body() != null) {

                        contactListResponseModels = response.body();
                        mContactListAdapter = new ContactListAdapter(MainActivity.this, contactListResponseModels);
                        ContactFragment.mViewContactRecyclerView.setAdapter(mContactListAdapter);
                        ContactFragment.mTextViewSearchResult.setVisibility(View.GONE);
                        Collections.sort(contactListResponseModels, new CustomComparator());
                        int communityId = MyApplication.mSharedPreferences.getInt("CommunityId", 0);
                        if (communityId != 0) {

                            FilterCommunityForContact(MainActivity.contactListResponseModels, communityId);
                        }
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

                ContactFragment.mViewContactRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mContactListAdapter = new ContactListAdapter(MainActivity.this, MainActivity.contactListResponseModels);
                ContactFragment.mViewContactRecyclerView.setAdapter(mContactListAdapter);
                mContactListAdapter.notifyDataSetChanged();


            }
            int Position = MyApplication.mSharedPreferences.getInt("Position", 0);

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

}
