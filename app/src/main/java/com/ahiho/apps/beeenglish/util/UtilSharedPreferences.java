package com.ahiho.apps.beeenglish.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by theptokim on 9/28/16.
 */
public class UtilSharedPreferences {
    private Context mContext;
    private SharedPreferences sharedPreferences;
    //------------------------
    final String USER_DATA_KEY = "USER_DATA";
    final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN";
    final String ACCESS_TOKEN_EXPIRED_KEY = "ACCESS_TOKEN_EXPIRED";
    private static UtilSharedPreferences utilSharedPreferences = null;

    private UtilSharedPreferences(Context context) {
        mContext = context;
        sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public static UtilSharedPreferences getInstanceSharedPreferences(Context context) {
        if (utilSharedPreferences == null) {
            utilSharedPreferences = new UtilSharedPreferences(context);
        }
        return utilSharedPreferences;
    }

    /***
     * Thong tin user dang json
     * {"id":"10000009","username":"chicken2","email":"chicken2@gmail.com","avatar":null}
     *
     * @return
     */
    public String getUserData() {
        return sharedPreferences.getString(USER_DATA_KEY, "");
    }

    public void setUserData(String data) {
        sharedPreferences.edit().putString(USER_DATA_KEY, data).apply();
    }


    public String getAccessToken() {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, "");
    }

    public void setAccessToken(String accessToken) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, accessToken).apply();
    }

    public long getAccessTokenExpired() {
        return sharedPreferences.getLong(ACCESS_TOKEN_EXPIRED_KEY, 0);
    }

    public void setAccessTokenExpired(long expired) {
        sharedPreferences.edit().putLong(ACCESS_TOKEN_EXPIRED_KEY, expired).apply();
    }


    public void signOut(){
        setUserData("");
        setAccessToken("");
        setAccessTokenExpired(0);
    }

    public int getBookLastPosition(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void setBookLastPosition(String key,int expired) {
        sharedPreferences.edit().putInt(key, expired).apply();
    }
}
