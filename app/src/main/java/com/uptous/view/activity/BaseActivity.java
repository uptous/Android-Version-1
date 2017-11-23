package com.uptous.view.activity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uptous.R;
import com.uptous.controller.utils.CustomizeDialog;
import com.uptous.sharedpreference.Prefs;

import static com.uptous.view.activity.MainActivity.viewPager;


public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    public void showProgressDialog()
    {
        showProgressDialog("Please wait..");
    }
    public void showProgressDialog(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);

            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(title);
            if(viewPager!= null&&viewPager.getCurrentItem()==1)
            {

            }else
                mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void showToast(String msg){
        Toast.makeText(BaseActivity.this,msg,Toast.LENGTH_SHORT).show();

    }


    public void showLogOutDialog(){
        final CustomizeDialog customizeDialog = new CustomizeDialog(BaseActivity.this);
        customizeDialog.setCancelable(false);
        customizeDialog.setContentView(R.layout.dialog_password_change);
        TextView textViewOk = (TextView) customizeDialog.findViewById(R.id.text_view_log_out);
        customizeDialog.show();
        textViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customizeDialog.dismiss();
                logOut();
            }
        });
    }
    //Method to logout from app
    public void logOut() {
        if (MainActivity.contactListResponseModels != null) {
            MainActivity.contactListResponseModels.clear();
        }

        Application app = getApplication();
        Intent intent = new Intent(app, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(intent);


        Prefs.setIsAlreadyLogin(this, false);
        Prefs.setCommunityNAme(this, null);
        Prefs.setCommunityId(this, 0);
        Prefs.setDetail(this, null);
        Prefs.setAuthenticationId(this, null);
        Prefs.setAuthenticationPassword(this, null);
        Prefs.setClose(this, null);
        Prefs.setCommunityFilter(this, null);

        Prefs.setAttachment(this,null);
        Prefs.setAlbum(this,null);
        Prefs.setIsAlreadyLogin(this,false);
        Prefs.setIsAlreadyLogin(this,false);



    }


}
