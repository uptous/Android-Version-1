package com.uptous.view.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.uptous.R;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.model.ContactListResponseModel;
import com.uptous.sharedpreference.Prefs;

import java.util.List;

/**
 * Created by Prakash .
 */
public class ContactListForSearchAdapter extends RecyclerView.Adapter<ContactListForSearchAdapter.VersionViewHolder> {
    List<ContactListResponseModel> listEntities;
    Activity activity;


    public ContactListForSearchAdapter(Activity a, List<ContactListResponseModel> listEntities) {
        this.listEntities = listEntities;
        this.activity = a;
    }

    public void notifyCategoryAdapter(List<ContactListResponseModel> listEntities) {
        this.listEntities = listEntities;

        notifyDataSetChanged();
    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_contact, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final VersionViewHolder versionViewHolder, int i) {
        // Set Data in your views comes from CollectionClass
        String FirstName = listEntities.get(i).getFirstName();
        String LastName = listEntities.get(i).getLastName();


//        if (FirstName != null && FirstName != "") {
//            if (LastName != null && LastName != "") {
//                versionViewHolder.mTextViewProductName.setText(LastName + ", " + FirstName);
//                versionViewHolder.mTextViewContactDetailName.setText(LastName + ", " + FirstName);
//            } else {
//                versionViewHolder.mTextViewProductName.setText(FirstName);
//                versionViewHolder.mTextViewContactDetailName.setText(FirstName);
//            }
//
//        } else {
//            versionViewHolder.mTextViewProductName.setText("- -");
//        }


        if (FirstName != null && FirstName != "" || LastName != null && LastName != "") {
            versionViewHolder.relativeLayoutMain.setVisibility(View.VISIBLE);
            versionViewHolder.mTextViewProductName.setText(LastName + ", " + FirstName);
            versionViewHolder.mTextViewContactDetailName.setText(LastName + ", " + FirstName);

            String Address = listEntities.get(i).getAddress();
            if (Address != null) {
                versionViewHolder.mTextViewContactDetailAbout.setText(Address);
            }


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                if (listEntities.get(i).getMobile() != null) {
                    versionViewHolder.TextViewPhone.setText((Html.fromHtml("<u>" + listEntities.get(i).getMobile() +
                            "</u> ", Html.FROM_HTML_MODE_LEGACY)));
                } else {
                    versionViewHolder.TextViewPhone.setText("");
                }


            } else {
                if (listEntities.get(i).getMobile() != null) {
                    versionViewHolder.TextViewPhone.setText((Html.fromHtml("<u>" + listEntities.get(i).getMobile()
                            + "</u> ")));
                } else {
                    versionViewHolder.TextViewPhone.setText("");
                }
            }
            versionViewHolder.TextViewPhone.setTag(i);
            versionViewHolder.TextViewPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();
                    final String PhoneNumber = listEntities.get(position).getMobile();

                    final CustomizeDialog customizeDialog = new CustomizeDialog(activity);

                    customizeDialog.setContentView(R.layout.dialog_phone_message);
                    TextView tetTextViewPhone = (TextView) customizeDialog.findViewById(R.id.button_phone);
                    TextView tetTextViewMessage = (TextView) customizeDialog.findViewById(R.id.button_message);
                    TextView tetTextViewCancel = (TextView) customizeDialog.findViewById(R.id.button_cancel);
                    customizeDialog.show();
                    customizeDialog.setCancelable(false);

                    tetTextViewCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customizeDialog.dismiss();
                        }
                    });
                    tetTextViewMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + PhoneNumber));
                            intent.putExtra("sms_body", "");
                            if (intent.resolveActivity(activity.getPackageManager()) != null)
                                activity.startActivity(intent);
                            else {
                                Toast.makeText(activity, activity.getString(R.string.no_app), Toast.LENGTH_SHORT).show();
                            }
                            customizeDialog.dismiss();
                        }
                    });

                    tetTextViewPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);

                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(
                                        activity,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        0);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + PhoneNumber));
                                if (intent.resolveActivity(activity.getPackageManager()) != null)
                                    activity.startActivity(intent);
                                else
                                    Toast.makeText(activity, activity.getString(R.string.no_app), Toast.LENGTH_SHORT).show();
                            }
                            customizeDialog.dismiss();
                        }
                    });
                    Prefs.setMessage(activity, "mesage");
                }
            });

            if (listEntities.get(i).getEmail() != null) {
                versionViewHolder.TextViewEmail.setText((Html.fromHtml("<u>" + listEntities.get(i).getEmail() + "</u> ")));
            } else {
                versionViewHolder.TextViewEmail.setText("");
            }

            versionViewHolder.TextViewEmail.setTag(i);
            versionViewHolder.TextViewEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (int) view.getTag();

                    Prefs.setMessage(activity, "mesage");
                    String StrEmain = listEntities.get(position).getEmail();

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{StrEmain});
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Sent from my Android");
                    final PackageManager pm = activity.getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
                    String className = null;
                    for (final ResolveInfo info : matches) {
                        if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                            className = info.activityInfo.name;

                            if (className != null && !className.isEmpty()) {
                                break;
                            }
                        }
                    }
                    emailIntent.setClassName("com.google.android.gm", className);
                    activity.startActivity(emailIntent);

                }
            });
//        gd.setStroke(2, Color.parseColor("#00FFFF"), 5, 6);
//        versionViewHolder.linearLayoutNameBackground.setBackgroundColor(color);


            String Image1 = listEntities.get(i).getPhoto();

            if (Image1 != null) {
                String result1 = Image1.substring(Image1.lastIndexOf(".") + 1);
                if (result1.equalsIgnoreCase("gif")) {
                    String BackgroundColor = listEntities.get(i).getMemberBackgroundColor();

                    if (BackgroundColor != null) {

                        int colorTextView = Color.parseColor(listEntities.get(i).getMemberTextColor());

                        versionViewHolder.imageView.setVisibility(View.GONE);
                        versionViewHolder.linearLayoutNameBackground.setVisibility(View.VISIBLE);
                        int color = Color.parseColor(listEntities.get(i).getMemberBackgroundColor());

                        versionViewHolder.linearLayoutNameBackground.setBackgroundResource(R.drawable.circle);
                        versionViewHolder.linearLayoutNameDetail.setBackgroundResource(R.drawable.circle);
                        GradientDrawable gd = (GradientDrawable) versionViewHolder.linearLayoutNameBackground.getBackground().getCurrent();
                        GradientDrawable gd1 = (GradientDrawable) versionViewHolder.linearLayoutNameDetail.getBackground().getCurrent();
                        gd.setColor(color);
                        gd1.setColor(color);
                        gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
                        gd1.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});

                        String FirstNameDetail = listEntities.get(i).getFirstName();
                        String LastNameDetail = listEntities.get(i).getLastName();

                        if (FirstNameDetail != null) {
                            versionViewHolder.mTextViewFirstName.setText(listEntities.get(i).getFirstName().trim().toUpperCase());
                            versionViewHolder.mTextViewFirstName.setTextColor(colorTextView);
                            versionViewHolder.mTextViewDetailFirstName.setText(listEntities.get(i).getFirstName().trim().toUpperCase());
                            versionViewHolder.mTextViewDetailFirstName.setTextColor(colorTextView);
                        }


                        if (LastNameDetail != null) {
                            versionViewHolder.mTextViewLastName.setText(listEntities.get(i).getLastName().trim().toUpperCase());
                            versionViewHolder.mTextViewLastName.setTextColor(colorTextView);


                            versionViewHolder.mTextViewDetailLastName.setText(listEntities.get(i).getLastName().trim().toUpperCase());
                            versionViewHolder.mTextViewDetailLastName.setTextColor(colorTextView);
                        }

                    }
                } else {
                    versionViewHolder.linearLayoutNameBackground.setVisibility(View.GONE);
                    versionViewHolder.linearLayoutNameDetail.setVisibility(View.GONE);
                    versionViewHolder.imageView.setVisibility(View.VISIBLE);
                    versionViewHolder.mImageViewContactDetailImage.setVisibility(View.VISIBLE);


                    Glide.with(activity).load(listEntities.get(i).getPhoto())
                            .into(versionViewHolder.imageView);

                    Glide.with(activity).load(listEntities.get(i).getPhoto())
                            .into(versionViewHolder.mImageViewContactDetailImage);

//                    Glide.with(activity)
//                            .load(listEntities.get(i).getPhoto())
//                            .listener(new RequestListener<String, GlideDrawable>() {
//                                @Override
//                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                    // TODO: 08/11/16 handle failure
//                                    versionViewHolder.mImageViewProgress.setVisibility(View.GONE);
//                                    return false;
//                                }
//
//                                @Override
//                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                    // image ready, hide progress now
//                                    versionViewHolder.mImageViewProgress.setVisibility(View.GONE);
//                                    return false;   // return false if you want Glide to handle everything else.
//                                }
//                            })
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
//                            .centerCrop()
//                            .crossFade()
//                            .into(versionViewHolder.imageView);


                }

            } else {
                String BackgroundColor = listEntities.get(0).getMemberBackgroundColor();

                if (BackgroundColor != null) {
                    versionViewHolder.linearLayoutNameBackground.setVisibility(View.VISIBLE);
                    versionViewHolder.imageView.setVisibility(View.GONE);
                    int color = Color.parseColor(listEntities.get(i).getMemberBackgroundColor());
                    int colorTextView = Color.parseColor(listEntities.get(i).getMemberTextColor());

                    versionViewHolder.linearLayoutNameBackground.setBackgroundResource(R.drawable.circle);
                    versionViewHolder.linearLayoutNameDetail.setBackgroundResource(R.drawable.circle);
                    GradientDrawable gd = (GradientDrawable) versionViewHolder.linearLayoutNameBackground.getBackground().getCurrent();
                    GradientDrawable gd1 = (GradientDrawable) versionViewHolder.linearLayoutNameDetail.getBackground().getCurrent();
                    gd.setColor(color);
                    gd1.setColor(color);
                    gd.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});
                    gd1.setCornerRadii(new float[]{30, 30, 30, 30, 0, 0, 30, 30});

                    String FirstNameDetail = listEntities.get(i).getFirstName();
                    String LastNameDetail = listEntities.get(i).getLastName();

                    if (FirstNameDetail != null) {
                        versionViewHolder.mTextViewFirstName.setText(listEntities.get(i).getFirstName().trim().toUpperCase());
                        versionViewHolder.mTextViewFirstName.setTextColor(colorTextView);
                        versionViewHolder.mTextViewDetailFirstName.setText(listEntities.get(i).getFirstName().trim().toUpperCase());
                        versionViewHolder.mTextViewDetailFirstName.setTextColor(colorTextView);
                    } else {
                        versionViewHolder.mTextViewFirstName.setText("-");
                        versionViewHolder.mTextViewDetailFirstName.setText("-");
                    }


                    if (LastNameDetail != null) {
                        versionViewHolder.mTextViewLastName.setText(listEntities.get(i).getLastName().trim().toUpperCase());
                        versionViewHolder.mTextViewLastName.setTextColor(colorTextView);


                        versionViewHolder.mTextViewDetailLastName.setText(listEntities.get(i).getLastName().trim().toUpperCase());
                        versionViewHolder.mTextViewDetailLastName.setTextColor(colorTextView);
                    } else {
                        versionViewHolder.mTextViewLastName.setText("-");
                        versionViewHolder.mTextViewDetailLastName.setText("-");
                    }
                }
            }

            versionViewHolder.mTextViewKids.setText("");
            versionViewHolder.TextViewKidsDetail.setText("");
            try {
                for (int j = 0; j < listEntities.get(i).getChildren().size(); j++) {
                    versionViewHolder.mTextViewKids.append(listEntities.get(i).getChildren().get(j).getFirstName());
                    versionViewHolder.TextViewKidsDetail.append(listEntities.get(i).getChildren().get(j).getFirstName());
                    if (j < listEntities.get(i).getChildren().size() - 1) {
                        versionViewHolder.mTextViewKids.append(", ");
                        versionViewHolder.TextViewKidsDetail.append(", ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


//        int colorText = Color.parseColor(listEntities.get(i).getMemberTextColor());

        } else {
            versionViewHolder.relativeLayoutMain.setVisibility(View.GONE);
        }
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
        TextView mTextViewProductName, mTextViewContactDetailName, mTextViewContactDetailAbout,
                TextViewEmail, TextViewPhone, TextViewKidsDetail, mTextViewKids;
        ImageView mImageViewContactDetailImage, mImageViewExpand, mImageViewCollapsd, imageView;
        private TextView mTextViewFirstName, mTextViewLastName, mTextViewDetailFirstName, mTextViewDetailLastName;
        private boolean mIsSelected = false;
        LinearLayout linearLayoutNameDetail, linearLayoutNameBackground;
        RelativeLayout relativeLayoutContactDetail, mRelativeLayoutContact, relativeLayoutMain;
        private ProgressBar mImageViewProgress;

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
            TextViewEmail = (TextView) itemView.findViewById(R.id.text_view_email);
            TextViewPhone = (TextView) itemView.findViewById(R.id.text_view_phone);
            TextViewKidsDetail = (TextView) itemView.findViewById(R.id.text_view_detail_kids);
            mTextViewKids = (TextView) itemView.findViewById(R.id.text_view_kids);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_contact);
            mImageViewExpand = (ImageView) itemView.findViewById(R.id.image_view_contact_expand);
            mImageViewCollapsd = (ImageView) itemView.findViewById(R.id.image_view_contact_collpsd);
            mImageViewContactDetailImage = (ImageView) itemView.findViewById(R.id.image_view_contact_detail);
            mImageViewProgress = (ProgressBar) itemView.findViewById(R.id.image_view_contact_progress);
            relativeLayoutMain = (RelativeLayout) itemView.findViewById(R.id.main_layout);
            mView = itemView;
            mRelativeLayoutContact.setOnClickListener(new View.OnClickListener() {
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
            relativeLayoutContactDetail.setOnClickListener(new View.OnClickListener() {
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


