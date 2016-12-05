package com.ahiho.apps.beeenglish.my_interface;

/**
 * Created by Thep on 3/17/2016.
 */

public interface OnCallbackGrantedPermission {
    /**
     * Duoc goi khi co action callback
     */
    void onGranted(int permission);
    void onDenied(int permission);


}