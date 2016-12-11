package com.ahiho.apps.beeenglish.util;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ahiho.apps.beeenglish.R;

/**
 * Created by theptokim on 12/10/16.
 */

public class MyDownloadManager {
    private static DownloadManager downloadManager;
    private Context mContext;
    public  MyDownloadManager(Context context){
        getDownloadManager(context);
        this.mContext =context;
    }
    private DownloadManager getDownloadManager(Context context) {
        if (downloadManager == null)
            downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return downloadManager;
    }
    public DownloadManager getDownloadManager() {
        return getDownloadManager(mContext);
    }

    public int statusDownloading(long downloadId) {
        int status = DownloadManager.STATUS_FAILED;
        Cursor cursor = null;
        try {
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(downloadId);
            cursor = downloadManager.query(q);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                status = cursor.getInt(columnIndex);
                cursor.close();
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null)
                cursor.close();
            return status;
        }
    }

    public long downloadData( String filePath, String name, Uri destinationUri) {

        long downloadReference;
        Uri uri = Uri.parse(filePath);
        // Create request for android download manager
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //Setting title of request
        request.setTitle(mContext.getString(R.string.app_name) + " " + mContext.getString(R.string.download));

        //Setting description of request
        request.setDescription(mContext.getString(R.string.download_file) + " " + name);

        //Set the local destination for the downloaded file to a path
        //within the application's external files directory
//        request.setDestinationInExternalFilesDir(mContext,
//                Environment.DIRECTORY_DOWNLOADS, fileName);
//            Uri destinationUri = Uri.fromFile(fileDestionation);
        request.setDestinationUri(destinationUri);
        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);
        return downloadReference;
    }

    public String getStringUriFileDownload( long referenceId){
        return MyFile.convertUri2FileUri(downloadManager.getUriForDownloadedFile(referenceId).toString());
    }
    public Uri getUriFileDownload(long referenceId){
        return downloadManager.getUriForDownloadedFile(referenceId);
    }
}
