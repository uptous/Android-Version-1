package com.uptous.controller.utils.downloads;

import android.content.Context;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * FileName : FileChache
 * Description :make Directory and file
 * Dependencies : No Dependency
 */
public class FileChache {


    private File cacheDir;

    public FileChache(Context context) {
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().
                equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),
                    DownloadTask.downloadDirectory);
        else
            cacheDir = context.getCacheDir();

        if (!cacheDir.exists())
            cacheDir.mkdirs();

    }

    public File getDirectory() {
        return cacheDir;
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename + ".jpg");
        return f;

    }

    public static String fileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy-hhmmss");
        String format = "Img-" + simpleDateFormat.format(new Date()) + ".jpg";

        return format;
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}