package com.uptous.ui.adapter;

import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.uptous.R;
import com.uptous.controller.utils.SlidingTabLayout;
import com.uptous.ui.fragment.EventsFragment;
import com.uptous.ui.fragment.HomeFragment;
import com.uptous.ui.fragment.ContactFragment;
import com.uptous.ui.fragment.LibraryFragment;
import com.uptous.ui.fragment.SignUpFragment;


/**
 * Created by Prakash .
 */
public class TabAdapter extends FragmentStatePagerAdapter implements SlidingTabLayout.TabIconProvider {
    private static final String TAG = TabAdapter.class.getSimpleName();
    public static final int iconRes[] = {
            R.mipmap.news,
            R.mipmap.contact,
            R.mipmap.sign_up,

            R.mipmap.library,
            R.mipmap.event
    };
    public static final int iconRes1[] = {
            R.mipmap.news,
            R.mipmap.contact,
            R.mipmap.sign_up,

            R.mipmap.library,
            R.mipmap.event
    };


    public TabAdapter(AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new HomeFragment();

            case 1:
                return new ContactFragment();
            case 2:
                return new SignUpFragment();
            case 3:
                return new LibraryFragment();
            case 4:
                return new EventsFragment();


        }
        return null;
    }

    @Override
    public int getCount() {
        return iconRes.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public int getPageIconResId(int position) {

        return iconRes[position];
        // return iconRes_over[position];
    }

}

