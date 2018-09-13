package com.uptous.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uptous.controller.utils.RoundedImageView;
import com.uptous.model.ContactListResponseModel;
import com.uptous.model.FeaturedProductResponseModel;
import com.uptous.R;

import java.util.List;

/**
 * Created by Prakash .
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.VersionViewHolder> {

    List<ContactListResponseModel> listEntities;
    Activity activity;



    public ContactListAdapter(Activity a, List<ContactListResponseModel> listEntities) {

        this.listEntities = listEntities;
        this.activity = a;


    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view =
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_contact, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {

        // Set Data in your views comes from CollectionClass


        versionViewHolder.mTextViewProductName.setText(listEntities.get(i).getFirstName() + " " + listEntities.get(i).getLastName());
        versionViewHolder.mTextViewContactDetailName.setText(listEntities.get(i).getFirstName() + " " + listEntities.get(i).getLastName());
        versionViewHolder.mTextViewContactDetailAbout.setText(listEntities.get(i).getCity());
        versionViewHolder.TextViewEmail.setText(listEntities.get(i).getEmail());
        versionViewHolder.TextViewPhone.setText(listEntities.get(i).getPhone());
        int color = Color.parseColor(listEntities.get(i).getMemberBackgroundColor());

        versionViewHolder.linearLayoutNameBackground.setBackgroundResource(R.drawable.circle);
        versionViewHolder.linearLayoutNameDetail.setBackgroundResource(R.drawable.circle);
        GradientDrawable gd = (GradientDrawable) versionViewHolder.linearLayoutNameBackground.getBackground().getCurrent();
        GradientDrawable gd1 = (GradientDrawable) versionViewHolder.linearLayoutNameDetail.getBackground().getCurrent();
        gd.setColor(color);
        gd1.setColor(color);
        gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
        gd1.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
//        gd.setStroke(2, Color.parseColor("#00FFFF"), 5, 6);
//        versionViewHolder.linearLayoutNameBackground.setBackgroundColor(color);


        for (int j = 0; j < listEntities.get(i).getChildren().size(); j++) {

            versionViewHolder.mTextViewKids.append(listEntities.get(i).getChildren().get(j).getFirstName() + ",");
            versionViewHolder.TextViewKidsDetail.append(listEntities.get(i).getChildren().get(j).getFirstName() + ",");
        }

        int colorText = Color.parseColor(listEntities.get(i).getMemberTextColor());


        versionViewHolder.mTextViewFirstName.setText(listEntities.get(i).getFirstName().toUpperCase());
        versionViewHolder.mTextViewFirstName.setTextColor(colorText);
        versionViewHolder.mTextViewLastName.setText(listEntities.get(i).getLastName().toUpperCase());
        versionViewHolder.mTextViewLastName.setTextColor(colorText);

        versionViewHolder.mTextViewDetailFirstName.setText(listEntities.get(i).getFirstName().toUpperCase());
        versionViewHolder.mTextViewDetailFirstName.setTextColor(colorText);
        versionViewHolder.mTextViewDetailLastName.setText(listEntities.get(i).getLastName().toUpperCase());
        versionViewHolder.mTextViewDetailLastName.setTextColor(colorText);


    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getItemCount() {

        return listEntities == null ? 0 : listEntities.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        TextView mTextViewProductModel, mTextViewProductName, mTextViewContactDetailName, mTextViewContactDetailAbout,
                TextViewEmail, TextViewPhone, TextViewKidsDetail, mTextViewKids;
        ImageView mImageViewContactDetailImage, mImageViewExpand, mImageViewCollapsd;
        private RoundedImageView imageView;
        private TextView mTextViewFirstName, mTextViewLastName, mTextViewDetailFirstName, mTextViewDetailLastName;
        private boolean mIsSelected = false;
        LinearLayout linearLayoutNameDetail, linearLayoutNameBackground;
        RelativeLayout relativeLayoutContactDetail, mRelativeLayoutContact;

        public VersionViewHolder(View itemView) {
            super(itemView);
            linearLayoutNameBackground = (LinearLayout) itemView.findViewById(R.id.layout_contact);
            linearLayoutNameDetail = (LinearLayout) itemView.findViewById(R.id.layout_contact_detail);
            mTextViewProductName = (TextView) itemView.findViewById(R.id.contact_name);
            mTextViewFirstName = (TextView) itemView.findViewById(R.id.textview_first_name);
            mTextViewLastName = (TextView) itemView.findViewById(R.id.textview_last_name);
            mTextViewDetailFirstName = (TextView) itemView.findViewById(R.id.textview_first_name_detail);
            mTextViewDetailLastName = (TextView) itemView.findViewById(R.id.textview_last_name_detail);

            mTextViewContactDetailName = (TextView) itemView.findViewById(R.id.text_view_name_contact_detail);
            mTextViewContactDetailAbout = (TextView) itemView.findViewById(R.id.text_view_contact_detail_about);
            relativeLayoutContactDetail = (RelativeLayout) itemView.findViewById(R.id.row_contact_detail);
            mRelativeLayoutContact = (RelativeLayout) itemView.findViewById(R.id.row_contact);
            mTextViewProductModel = (TextView) itemView.findViewById(R.id.text_view_kids);
            TextViewEmail = (TextView) itemView.findViewById(R.id.text_view_email);
            TextViewPhone = (TextView) itemView.findViewById(R.id.text_view_phone);
            TextViewKidsDetail = (TextView) itemView.findViewById(R.id.text_view_detail_kids);
            mTextViewKids = (TextView) itemView.findViewById(R.id.text_view_kids);
//            imageView = (RoundedImageView) itemView.findViewById(R.id.image_view_contact);
            mImageViewExpand = (ImageView) itemView.findViewById(R.id.image_view_contact_expand);
            mImageViewCollapsd = (ImageView) itemView.findViewById(R.id.image_view_contact_collpsd);
//            mImageViewContactDetailImage = (ImageView) itemView.findViewById(R.id.image_contact_detail);
            mView = itemView;
            mImageViewExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIsSelected == false) {

                        relativeLayoutContactDetail.setVisibility(View.VISIBLE);
                        mRelativeLayoutContact.setVisibility(View.GONE);
                        mIsSelected = true;
                    } else {

                        relativeLayoutContactDetail.setVisibility(View.GONE);
                        mRelativeLayoutContact.setVisibility(View.VISIBLE);
                        mIsSelected = false;
                    }

                }
            });
            mImageViewCollapsd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mIsSelected == false) {

                        relativeLayoutContactDetail.setVisibility(View.VISIBLE);
                        mRelativeLayoutContact.setVisibility(View.GONE);
                        mIsSelected = true;
                    } else {

                        relativeLayoutContactDetail.setVisibility(View.GONE);
                        mRelativeLayoutContact.setVisibility(View.VISIBLE);
                        mIsSelected = false;
                    }
                }
            });

        }
    }
}


