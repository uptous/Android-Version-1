package com.uptous.controller.utils.downloads;

import android.os.Environment;

/**
 * FileName : CheckForSDCard
 * Description :check SD Card available or not
 * Dependencies : No Dependency
 */

public class CheckForSDCard {

    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
