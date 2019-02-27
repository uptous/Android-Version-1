package com.uptous.controller.utils.downloads;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

//this import for R file for string file
/**
 * FileName : Permission
 * Description :Permission Class Used for given Permission api level 23 [marshmallow] or upper
 * Dependencies : No Dependency
 */

public class Permission {

    public static     final int PERMISSION_CALLBACK_CONSTANT = 100;
    private Activity mActivity;

    public Permission(Activity activity) {
        mActivity = activity;
        }

    String[] permissionsRequired = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public void requestPermission() {

        if (ActivityCompat.checkSelfPermission(mActivity, permissionsRequired[0])
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(mActivity, permissionsRequired[1])
                != PackageManager.PERMISSION_GRANTED)

        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequired[1])
                    ) {
                }
            else {
                //just request the permission
                ActivityCompat.requestPermissions(mActivity, permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT);
            }
            }

    }

    public void stillNotHavePermissions() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequired[0])
                || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequired[1])
                )
        {
            ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);

            }
    }

}