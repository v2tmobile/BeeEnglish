package com.ahiho.apps.beeenglish.controller;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyDownloadManager;

/**
 * Created by theptokim on 12/10/16.
 */

public class MyDownloadBroadcastReceiver extends BroadcastReceiver {
    private final String TAG="RESPONSE_DOWNLOAD";
    private MyDownloadManager mDownloadManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        mDownloadManager= new MyDownloadManager(context);
        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        int status=mDownloadManager.statusDownloading(referenceId);
//        if(status==  DownloadManager.STATUS_SUCCESSFUL) {
//            String uri = mDownloadManager.getStringUriFileDownload(referenceId);
//            Log.e(TAG, "STATUS_SUCCESS" +referenceId + "_"+ uri);
//        }else if(status==DownloadManager.STATUS_FAILED){
//            Log.e(TAG, "STATUS_FAILED");
//        }
        Intent i = new Intent(Identity.DOWNLOAD_STATUS_BROADCAST);
        i.putExtra(Identity.EXTRA_ID_DOWNLOAD,referenceId);
        context.sendBroadcast(intent);
    }
}
