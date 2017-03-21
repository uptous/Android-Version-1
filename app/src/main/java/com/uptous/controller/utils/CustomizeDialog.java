


package com.uptous.controller.utils;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.uptous.R;

/*
 * FileName : CustomizeDialog.java
 * Dependencies :
 * Description : This is show a dialog box.
 * Classes : CustomizeDialog.java
 */
public class CustomizeDialog extends Dialog {

    Context context;

    public CustomizeDialog(Context context) {

        super(context, R.style.Theme_Dialog_Translucent);
        try {
            /** 'Window.FEATURE_NO_TITLE' - Used to hide the title */

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            this.context = context;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
