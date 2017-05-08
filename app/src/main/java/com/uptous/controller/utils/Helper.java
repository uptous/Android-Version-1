package com.uptous.controller.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.uptous.R;

/**
 * FileName : Helper
 * Description :Some common code use more than one classes
 * Dependencies : Helper
 */
public class Helper {


    public void keyBoardHidden(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CustomizeDialog CustomizeDialogAlert(Activity activity, int layout, View.OnClickListener myClickListener) {
      CustomizeDialog  mCustomizeDialog = new CustomizeDialog(activity);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.setContentView(layout);

        TextView textViewOk = (TextView) mCustomizeDialog.findViewById(R.id.text_view_log_out);
        textViewOk.setOnClickListener(myClickListener);


        mCustomizeDialog.show();
        return mCustomizeDialog;

    }



}
