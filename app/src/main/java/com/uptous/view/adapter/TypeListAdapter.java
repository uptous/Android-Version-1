package com.uptous.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.model.CommnunitiesResponseModel;

import java.util.List;

/**
 * Created by Prakash .
 */
public class TypeListAdapter extends BaseAdapter implements View.OnClickListener {


    Activity activity;
    private List<CommnunitiesResponseModel> mData;
    private static LayoutInflater sLayoutInflater = null;

    /**
     * CustomAdapter Constructor *******
     */
    public TypeListAdapter(Activity a, List<CommnunitiesResponseModel> d) {

        activity = a;
        mData = d;
        sLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * What is the size of Passed Arraylist Size *******
     */
    public int getCount() {
        if (null != mData)
            return mData.size()-1;
        else
            return 0;
    }


      public CommnunitiesResponseModel getItem(int position) {
        return mData.get(position);
    }
//    public Object getItem(int position) {
//        return position;
//    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * Depends upon data size called for each row , Create each ListView row **
     */
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        if (vi == null) {
            vi = sLayoutInflater.inflate(R.layout.row_spinner, null);
        }



        holder = new ViewHolder();
        holder.mTextViewName = (TextView) vi.findViewById(R.id.text_view_item);
//
        if (position == getCount()){
            holder.mTextViewName.setText("SELECT COMMUNITY");
            holder.mTextViewName.setTextColor(Color.GRAY);
            holder.mTextViewName.setGravity(Gravity.LEFT);
            holder.mTextViewName.setHint(mData.get(position).getName());
        }
        else {
            holder.mTextViewName.setText(mData.get(position).getName());

        }

        vi.setTag(holder);

        return vi;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    public static class ViewHolder {
        private TextView mTextViewName;

    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

}