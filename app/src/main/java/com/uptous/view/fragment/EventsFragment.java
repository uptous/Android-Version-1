package com.uptous.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.uptous.R;


/**
 * FileName : EventsFragment
 * Description :
 * Dependencies :
 */
public class EventsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);


        return view;
    }


}


