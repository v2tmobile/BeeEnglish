package com.ahiho.apps.beeenglish.util;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;

import com.ahiho.apps.beeenglish.my_interface.OnCallbackDownload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import io.realm.internal.IOException;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by theptokim on 11/30/16.
 */

public class MyFile {

    public static final String APP_FOLDER = "bee_english";
    public static final String PICTURE_FOLDER = "pictures";
    public static final String BOOK_FOLDER = "books";

    public static long getFolderSize(File f) {
        long size = 0;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                size += getFolderSize(file);
            }
        } else {
            size = f.length();
        }
        return size;
    }

    public static String getFolderSizeString(File f) {
        String strSize;
        long Filesize = getFolderSize(f) / 1024;//call function and convert bytes into Kb
        if (Filesize >= 1024)
            strSize = Filesize / 1024 + " Mb";
        else
            strSize = Filesize + " Kb";
        return strSize;
    }

    public static File getFileFromStringURI(String uri) {
        File file = new File(uri);
        return file;
    }

    public static String saveImageFile(Context context, String imageUrl) {
//    final String imageUrl = promtionObject.getImageURL();
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1); //promtionObject.getPromotionId() + ".png";
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            File myDir = new File(context.getFilesDir() + "/" + PICTURE_FOLDER);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            myDir = new File(myDir, fileName);
            if (!myDir.exists()) {

                FileOutputStream out = new FileOutputStream(myDir);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            }
            return Uri.fromFile(myDir).toString();
        } catch (Exception e) {
            return "";
        }
    }

    /***
     * @param context
     * @param path
     * @param fileName with extend file "yourfile.png"
     * @return
     */

    public static String downloadFile(Context context, final String path, String fileName, OnCallbackDownload callbackDownload) {
        String uriFile = "";
        try {
            URL url = new URL(path);

            URLConnection ucon = url.openConnection();
            ucon.setReadTimeout(5000);
            ucon.setConnectTimeout(10000);

            InputStream is = ucon.getInputStream();
            BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);

            File file = new File(context.getDir(PICTURE_FOLDER, Context.MODE_PRIVATE) + "/" + fileName);

            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            final long size = ucon.getContentLength();
            final int sizeBuff = 5 * 1024;

            FileOutputStream outStream = new FileOutputStream(file);
            byte[] buff = new byte[sizeBuff];

            int len;
            int total = 0;
            while ((len = inStream.read(buff)) != -1) {
                outStream.write(buff, 0, len);
                total += len;
                callbackDownload.postProgress((total * 100) / size);
            }

            outStream.flush();
            outStream.close();
            inStream.close();

            uriFile = Uri.fromFile(file).toString();
            callbackDownload.downloadSuccess(uriFile);
        } catch (Exception e) {
            callbackDownload.downloadError(e);
        }

        return uriFile;
    }

    public static String getFileName(String path) {
        String s = URLUtil.guessFileName(path, null, null);
        return s;
    }

    public static String convertUri2FileUri(String uri) {
        return uri.replace("file://", "");
    }

    public static void copy(File src, File dst) {
        InputStream in = null;
        try {
            in = new FileInputStream(src);

            OutputStream out = new FileOutputStream(dst);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
        }
    }

}
