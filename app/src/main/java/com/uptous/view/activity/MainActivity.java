package com.uptous.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.uptous.R;
import com.uptous.controller.utils.Helper;
import com.uptous.view.fragment.LibraryFragment;
import com.uptous.view.fragment.ContactFragment;
import com.uptous.view.fragment.EventsFragment;
import com.uptous.view.fragment.HomeFragment;
import com.uptous.view.fragment.SignUpFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * FileName : MainActivity
 * Description : Show Tabs that open  different fragments like : feed, contacts, sign_ups, albums, event.
 * Dependencies : ViewPagerAdapter
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout mTabLayout;

    private static final int ICON_RES_OVER[] = {
            R.mipmap.news,
            R.mipmap.contact,
            R.mipmap.sign_up,
            R.mipmap.library,
            R.mipmap.event};


    private Helper mHelper;

    //    public SearchView SearchView;
    private LinearLayout mLinearLayoutNavigationMenu;
//    private TextView mTextViewCancel, mTextViewSearch;
//    private RelativeLayout mRelativeLayoutSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgmenuleft:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
//            case R.id.text_view_cancel:
//                SearchView.clearFocus();
//                SearchView.onActionViewCollapsed();
//                mTextViewSearch.setVisibility(View.VISIBLE);
//                break;
//            case R.id.layout_searchbar:
//                SearchView.onActionViewExpanded();
//                mTextViewSearch.setVisibility(View.GONE);
//                break;

        }
    }


    // method to initialization of views
    private void initView() {
        mHelper = new Helper();
        //Local Variables

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        RelativeLayout relativeLayout =
                (RelativeLayout) findViewById(R.id.relative);
        relativeLayout.setFocusableInTouchMode(true);
        relativeLayout.requestFocus();


        //Global Variables
//        SearchView = (SearchView) findViewById(R.id.serach_view_filter);
        mLinearLayoutNavigationMenu = (LinearLayout) findViewById(R.id.imgmenuleft);
//        mTextViewCancel = (TextView) findViewById(R.id.text_view_cancel);
//        mRelativeLayoutSearchBar = (RelativeLayout) findViewById(R.id.layout_searchbar);
//        mTextViewSearch = (TextView) findViewById(R.id.text_view_search);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

//        SearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mTextViewSearch.setVisibility(View.GONE);
//                SearchView.onActionViewExpanded();
//
//            }
////        });


//        SearchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                SearchView.clearFocus();
//                SearchView.onActionViewCollapsed();
//                mTextViewSearch.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });

        clickListenerOnViews();

        setupViewPager(viewPager);

        setupTabIcons();


    }

    private void clickListenerOnViews() {
        mLinearLayoutNavigationMenu.setOnClickListener(this);
//        mTextViewCancel.setOnClickListener(this);
//        mRelativeLayoutSearchBar.setOnClickListener(this);
    }

    // method to set tab icons
    private void setupTabIcons() {

        mTabLayout.getTabAt(0).setIcon(ICON_RES_OVER[0]);
        mTabLayout.getTabAt(1).setIcon(ICON_RES_OVER[1]);
        mTabLayout.getTabAt(2).setIcon(ICON_RES_OVER[2]);
        mTabLayout.getTabAt(3).setIcon(ICON_RES_OVER[3]);
        mTabLayout.getTabAt(4).setIcon(ICON_RES_OVER[4]);
    }

    public ViewPagerAdapter mViewPagerAdapter;

    // method to add fragment on viewpager
    private void setupViewPager(final ViewPager viewPager) {
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFrag(new HomeFragment(), "");
        mViewPagerAdapter.addFrag(new ContactFragment(), "");
        mViewPagerAdapter.addFrag(new SignUpFragment(), "");
        mViewPagerAdapter.addFrag(new LibraryFragment(), "");
        mViewPagerAdapter.addFrag(new EventsFragment(), "");
        viewPager.setAdapter(mViewPagerAdapter);

        mTabLayout.setupWithViewPager(viewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                mHelper.keyBoardHidden(MainActivity.this);

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
}
