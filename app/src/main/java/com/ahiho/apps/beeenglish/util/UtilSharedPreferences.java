package com.ahiho.apps.beeenglish.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by theptokim on 9/28/16.
 */
public class UtilSharedPreferences {
    private Context mContext;
    private SharedPreferences sharedPreferences;
    //------------------------
    final String USER_DATA_KEY = "USER_DATA";
    final String ACTIVE_DATA_KEY = "ACTIVE_DATA";
    final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN";
    final String ACCESS_TOKEN_EXPIRED_KEY = "ACCESS_TOKEN_EXPIRED";
    final String STATUS_UPDATE_FIRST="STATUS_UPDATE_FIRST";
    final String STATUS_ACTIVE="STATUS_ACTIVE";
    final String LIST_UPDATE_FAIL="LIST_UPDATE_FAIL";
    final String TIME_EXPIRED="TIME_EXPIRED";
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
        setActiveData("");
        setTrialTimeExpired(0);
        setAccessToken("");
        setAccessTokenExpired(0);
    }

    public int getBookLastPosition(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void setBookLastPosition(String key,int expired) {
        sharedPreferences.edit().putInt(key, expired).apply();
    }
    public long getBookDownloadId(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public void setBookDownloadId(String key,long bookDownloadId) {
        sharedPreferences.edit().putLong(key, bookDownloadId).apply();
    }

    public int getIdDictionaryDownload(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void setIdDictionaryDownload(String key,int id) {
        sharedPreferences.edit().putInt(key, id).apply();
    }

    public String getUriDownloadWithIdDownload(String key) {
        return sharedPreferences.getString("LINK_"+key, "");
    }

    public void setUriDownloadWithIdDownload(String key,String uri) {
        sharedPreferences.edit().putString("LINK_"+key, uri).apply();
    }

    public String getListDownloadFail() {
        return sharedPreferences.getString(LIST_UPDATE_FAIL, "");
    }

    public void setListDownloadFail(int id) {
        String list =  getListDownloadFail();
        if(list.isEmpty()){
            list =String.valueOf(id);
        }else{
            list+=","+id;
        }
        sharedPreferences.edit().putString(LIST_UPDATE_FAIL, list).apply();
    }
    public boolean isUpdateFirst() {
        return sharedPreferences.getBoolean(STATUS_UPDATE_FIRST, false);
    }

    public void setUpdateFirst(boolean state) {
        sharedPreferences.edit().putBoolean(STATUS_UPDATE_FIRST, state).apply();
    }


    public long getTrialTimeExpired() {
        return sharedPreferences.getLong(TIME_EXPIRED, 0);
    }

    public void setTrialTimeExpired(long time) {
        sharedPreferences.edit().putLong(TIME_EXPIRED, time).apply();
    }
    public void setTrialTimeExpired(String dateTime) {
        long time = UtilString.convertString2Date(dateTime);
        sharedPreferences.edit().putLong(TIME_EXPIRED, time).apply();
    }

    /***
     * Thong tin active
     * {"application_id": "10000000","key": "10914588-2a62-490a-8541-cd361805c9c1","expired_time": "2017-03-24 19:55:37","created": "2016-12-25 02:55:37","modified": "2016-12-25 02:55:37"}
     *
     * @return
     */
    public String getActiveData() {
        return sharedPreferences.getString(ACTIVE_DATA_KEY, "");
    }

    /***
     * Neu da active va thoi gian het han con >3 ngay
     * @return
     */
    public boolean isActiveValid() {
        String activeInfo = getActiveData();
        if (!activeInfo.isEmpty()) {
            long time = 0;
            //start
            try {
                time = UtilString.convertString2Date(new JSONObject(activeInfo).getString("expired_time"))-System.currentTimeMillis();
            } catch (JSONException e) {
            }
            time = time - 3600000 * 3;
            if (time < 0) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }
    }

    public void setActiveData(String data) {
        sharedPreferences.edit().putString(ACTIVE_DATA_KEY, data).apply();
        if(data.isEmpty()){
            setTrialTimeExpired(0);

        }else {
            try {
                JSONObject jsonObjectActive = new JSONObject(data);
                setTrialTimeExpired(jsonObjectActive.getString("expired"));
            } catch (JSONException e) {

            }
        }

    }
}
