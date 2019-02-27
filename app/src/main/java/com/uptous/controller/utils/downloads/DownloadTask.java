package com.uptous.controller.utils.downloads;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * FileName : DownloadTask
 * Description : download image from url  and also scan the file for showing in gallery
 * Dependencies :  FileChache, CheckForSDCard, DownloadStatusListener
 */

public class DownloadTask {
    private static final String TAG = "Download Task";
    private Context context;
    private String downloadUrl = "",
            downloadFileName = "";
    public static final String downloadDirectory = "Uptous";

    DownloadStatusListener directoryListener;

    public DownloadTask(Context context, String downloadUrl,
                        DownloadStatusListener directoryListener) {
        this.context = context;
        this.directoryListener = directoryListener;
        this.downloadUrl = downloadUrl;

//Create file name by picking download file name from URL
        downloadFileName = new FileChache(context).fileName();
        //   Log.e(TAG, downloadFileName);
        //Start Downloading Task
        new DownloadingTask().execute();
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile != null) {
                    //if download success
                    directoryListener.directoryStatus(true);
                    }
                    else {
                    // if download fail
                    directoryListener.directoryStatus(false);

                }
            } catch (Exception e) { }

            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);//Create Download URl
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    /*Log.e(TAG, "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());*/

                }


                //Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStorageDirectory() + "/"
                                    + downloadDirectory);
                } else
                    // Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();

                    //If File is not present create directory
                    if (!apkStorage.exists()) {
                        apkStorage.mkdir();
                      /*  Log.e(TAG, "Directory Created.");*/
                    }

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                // this also for image refresh need to call
                fos.flush();
                fos.getFD().sync();

                //Close all connection after doing task
                fos.close();
                is.close();

                // call file scan function
                fileScan(context, outputFile);


            } catch (Exception e) {

                //Read exception if something went wrong
               // e.printStackTrace();
                outputFile = null;
            }

            return null;
        }
    }

    // scan for gallery
    public static void fileScan(Context context, File outputFile) {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                final Uri contentUri = Uri.fromFile(outputFile);
                scanIntent.setData(contentUri);
                context.sendBroadcast(scanIntent);
            } else {
                final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                context.sendBroadcast(intent);
            }

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
}