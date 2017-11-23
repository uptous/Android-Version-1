package com.uptous.sharedpreference;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.uptous.MyApplication;

public class Prefs {

    private static final String AUTHENTICATIONID = "AuthenticationId";
    private static final String AUTHENTICATIONPASSWORD = "AuthenticationPassword";
    private static final String ISLOGIN = "isLogin";
    private static final String NEWSITEMID = "NewsItemID";
    private static final String OPPORTUNITY = "OpportunityID";
    private static final String SIGNUPTYPE = "Type";
    private static final String ITEMID = "ItemId";
    private static final String NAME = "Name";
    private static final String DATE = "Date";
    private static final String FROMNAME = "FromName";
    private static final String TONAME = "ToName";

    private static final String TOTALSPOT = "Total Spot";
    private static final String NUMBEROFVOLUNTEER = "Number of volunteer";

    private static final String Detail = "Detail";
    private static final String OwnerBackground = "OwnerBackground";
    private static final String OwnerTextColor = "OwnerTextColor";
    private static final String Image = "Image";

    private static final String ImageNewsItem = "ImageNewsItem";

    private static final String OwnerName = "OwnerName";
    private static final String FeedCommunityName = "FeedCommunityName";
    private static final String NewsType = "NewsType";
    private static final String FeedId = "FeedId";
    private static final String CommunityId = "CommunityId";
    private static final String CommunityName = "CommunityName";
    private static final String Imagepath = "Imagepath";
    private static final String Path = "path";
    private static final String NewsItemName = "NewsItemName";
    private static final String NewsItemDescription = "NewsItemDescription";

    private static final String CommunityFilter = "CommunityFilter";
    private static final String Close = "Close";

    private static final String Position = "Position";
    private static final String MessagePost = "MessagePost";
    private static final String PicturePost = "PicturePost";
    private static final String AlbumDetail = "AlbumDetail";
    private static final String Message = "Message";
    private static final String SignUpDetail = "SignUpDetail";

    private static final String Attachment = "Attachment";
    private static final String Album = "Album";

    private static final String Feed = "Feed";

    private static final String FeedDetail = "FeedDetail";
    private static final String ContactList = "ContactList";
    private static final String ContactListUpdated = "ContactListDate";



    public static String getNumberofvolunteer(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(NUMBEROFVOLUNTEER, null);
    }

    public static boolean setNumberofvolunteer(Context context, String authenticationId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(NUMBEROFVOLUNTEER, authenticationId);
        return editor.commit();
    }

    public static String getTotalSpot(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(TOTALSPOT, null);
    }

    public static boolean setTotalSpot(Context context, String totalspot) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(TOTALSPOT, totalspot);
        return editor.commit();
    }


    public static String getOwnerName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(OwnerName, null);
    }

    public static boolean setOwnerName(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(OwnerName, str);
        return editor.commit();
    }

    public static String getName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(NAME, null);
    }

    public static boolean setName(Context context, String authenticationId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(NAME, authenticationId);
        return editor.commit();
    }


    public static String getToName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(TONAME, null);
    }

    public static boolean setToName(Context context, String authenticationId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(TONAME, authenticationId);
        return editor.commit();
    }

    public static String getFromName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FROMNAME, null);
    }

    public static boolean setFromName(Context context, String authenticationId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(FROMNAME, authenticationId);
        return editor.commit();
    }

    public static String getDate(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(DATE, null);
    }

    public static boolean setDate(Context context, String authenticationId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(DATE, authenticationId);
        return editor.commit();
    }

    public static String getSignUpType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SIGNUPTYPE, null);
    }

    public static boolean setSignUpType(Context context, String authenticationId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(SIGNUPTYPE, authenticationId);
        return editor.commit();
    }

    public static boolean setAuthenticationId(Context context, String authenticationId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(AUTHENTICATIONID, authenticationId);
        return editor.commit();
    }


    public static String getAuthenticationId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(AUTHENTICATIONID, null);
    }


    public static boolean setAuthenticationPassword(Context context, String authenticationPassword) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(AUTHENTICATIONPASSWORD, authenticationPassword);
        return editor.commit();
    }

    public static int getNewsItemId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(NEWSITEMID, 0);
    }

    public static boolean setNewsItemId(Context context, int newsitemId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(NEWSITEMID, newsitemId);
        return editor.commit();
    }


    public static int getItemId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(ITEMID, 0);
    }

    public static boolean setItemId(Context context, int itemId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(ITEMID, itemId);
        return editor.commit();
    }

    public static int getOpportunityId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(OPPORTUNITY, 0);
    }

    public static boolean setOpportunityId(Context context, int newsitemId) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(OPPORTUNITY, newsitemId);
        return editor.commit();
    }

    public static String getAuthenticationPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(AUTHENTICATIONPASSWORD, null);
    }

    public static boolean setIsAlreadyLogin(Context context, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(ISLOGIN, value);
        return editor.commit();
    }

    public static boolean getIsAlreadyLogin(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ISLOGIN, false);
    }
//
    public static String getDetail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Detail, null);
    }

    public static boolean setDetail(Context context, String detail) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Detail, detail);
        return editor.commit();
    }
    public static String getOwnerBackground(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(OwnerBackground, null);
    }

    public static boolean setOwnerBackground(Context context, String Ownerbackground) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(OwnerBackground, Ownerbackground);
        return editor.commit();
    }
    public static String getOwnerTextColor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(OwnerTextColor, null);
    }

    public static boolean setOwnerTextColor(Context context, String OwnertextColor) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(OwnerTextColor, OwnertextColor);
        return editor.commit();
    }
    public static String getImage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Image, null);
    }
//
    public static boolean setImage(Context context, String image) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Image, image);
        return editor.commit();
    }
    public static String getimageNewsItem(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(ImageNewsItem, null);
    }

    public static boolean setimageNewsItem(Context context, String imageNewsItem) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(ImageNewsItem, imageNewsItem);
        return editor.commit();
    }
    public static String getFeedCommunityName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FeedCommunityName, null);
    }

    public static boolean setFeedCommunityName(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(FeedCommunityName, str);
        return editor.commit();
    }
    public static String getNewsType(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(NewsType, null);
    }

    public static boolean setNewsType(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(NewsType, str);
        return editor.commit();
    }
    public static int getFeedId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(FeedId, 0);
    }

    public static boolean setFeedId(Context context, int str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(FeedId, str);
        return editor.commit();
    }
    public static int getCommunityId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(CommunityId, 0);
    }

    public static boolean setCommunityId(Context context, int str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(CommunityId, str);
        return editor.commit();
    }


    public static String getImagepath(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Imagepath, null);
    }

    public static boolean setImagepath(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Imagepath ,str);
        return editor.commit();
    }

    public static String getpath(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Path, null);
    }

    public static boolean setpath(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Path ,str);
        return editor.commit();
    }

    public static String getNewsItemDescription(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(NewsItemDescription, null);
    }

    public static boolean setNewsItemDescription(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(NewsItemDescription ,str);
        return editor.commit();
    }

    public static String getNewsItemName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(NewsItemName, null);
    }

    public static boolean setNewsItemName(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(NewsItemName ,str);
        return editor.commit();
    }

    public static String getCommunityNAme(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(CommunityName, "");
    }

    public static boolean setCommunityNAme(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(CommunityName ,str);
        return editor.commit();
    }

    public static String getCommunityFilter(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(CommunityFilter, null);
    }

    public static boolean setCommunityFilter(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(CommunityFilter ,str);
        return editor.commit();
    }
    public static String getClose(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Close, null);
    }

    public static boolean setClose(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Close ,str);
        return editor.commit();
    }
    public static int getPosition(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(Position, 0);
    }

    public static boolean setPosition(Context context, int pos) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(Position, pos);
        return editor.commit();
    }
    public static String getMessagePost(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(MessagePost, null);
    }

    public static boolean setMessagePost(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(MessagePost ,str);
        return editor.commit();
    }

    public static String getPicturePost(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PicturePost, null);
    }

    public static boolean setPicturePost(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(PicturePost ,str);
        return editor.commit();
    }

    public static String getAlbumDetail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(AlbumDetail, null);
    }

    public static boolean setAlbumDetail(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(AlbumDetail ,str);
        return editor.commit();
    }

    public static String getMessage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Message, null);
    }

    public static boolean setMessage(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Message ,str);
        return editor.commit();
    }

    public static String getSignUpDetail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(SignUpDetail, null);
    }

    public static boolean setSignUpDetail(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(SignUpDetail ,str);
        return editor.commit();
    }

    public static String getAttachment(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Attachment, null);
    }

    public static boolean setAttachment(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Attachment ,str);
        return editor.commit();
    }

    public static String getAlbum(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Album, null);
    }

    public static boolean setAlbum(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Album ,str);
        return editor.commit();
    }


    public static String getFeed(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Feed, null);
    }

    public static boolean setFeed(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(Feed ,str);
        return editor.commit();
    }


    public static String getFeedDetail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(FeedDetail, null);
    }

    public static boolean setFeedDetail(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(FeedDetail ,str);
        return editor.commit();
    }


    public static boolean setContactList(Context context, String str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(ContactList ,str);
        return editor.commit();
    }

    public static String getContactList(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getString(ContactList, null);
    }

    public static boolean setContactListUpdated(Context context, int str) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(ContactListUpdated ,str);
        return editor.commit();
    }

    public static int getContactLastUpdated(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context).getInt(ContactListUpdated, 154564612);
    }

}
