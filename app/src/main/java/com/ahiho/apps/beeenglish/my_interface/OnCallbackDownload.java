package com.ahiho.apps.beeenglish.my_interface;

/**
 * Created by Thep on 3/17/2016.
 */

public interface OnCallbackDownload {
    /**
     * Duoc goi khi co action callback
     */
    void postProgress(float progress);
    void downloadSuccess(String uriFile);
    void downloadError(Exception exception);


}