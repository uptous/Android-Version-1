package com.uptous.controller.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * FileName : ConnectionDetector.java
 * Dependencies :
 * Description : Check Internet Connection.
 * Classes : ConnectionDetector.java
 */
public class ConnectionDetector {


    /**
     * @param ctx context of calling Activity
     * @return If internet connection is available then return true other wise false
     */
    public static boolean isConnectingToInternet(Context ctx) {
        ConnectivityManager cm =
                (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
