package com.uptous.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uptous.R;
import com.uptous.model.AlbumDetailResponseModel;

import java.util.List;

/**
 * Created by Prakash .
 */

public class ViewPagerAdapter extends PagerAdapter {
    // Declare Variables
    Context context;
    List<AlbumDetailResponseModel> listEntities;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, List<AlbumDetailResponseModel> listEntities) {
        this.context = context;
        this.listEntities = listEntities;

    }

    @Override
    public int getCount() {
        return listEntities.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        ImageView imgAlbum;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,
                false);


        // Locate the ImageView in viewpager_item.xml
        imgAlbum = (ImageView) itemView.findViewById(R.id.flag);
        // Capture position and set to the ImageView
        Picasso.with(context).load(listEntities.get(position).getPhoto())
                .into(imgAlbum);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
