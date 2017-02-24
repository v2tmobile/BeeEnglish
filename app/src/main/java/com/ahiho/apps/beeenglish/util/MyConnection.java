package com.ahiho.apps.beeenglish.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.my_interface.OnCallbackSnackBar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by theptokim on 11/23/16.
 */

public class MyConnection {
    private static final String BASE_URL = "https://beeenglish.mobile-backend.ahiho.com/api/";
    private static MyConnection myConnection = null;
    private final String METHOD_POST="POST";
    private final String METHOD_GET="GET";
    private Context mContext;
    public static MyConnection getInstanceMyConnection(Context context) {
        if (myConnection == null)
            myConnection = new MyConnection(context);
        return myConnection;
    }

    public MyConnection(Context context) {
        mContext=context;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public ResponseData signIn(String userName, String password) {
        String url = MyConnection.BASE_URL + "auth/login";
        HashMap hashMap = new HashMap();
        hashMap.put("username", userName);
        hashMap.put("password", password);
        return performPostCall(url, hashMap,METHOD_POST);
    }
    public ResponseData signInFacebook(String tokenKey ) {
        String url = null;
        try {
            url = MyConnection.BASE_URL + "auth/facebook-auth?access_token="+ URLEncoder.encode(tokenKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            url = MyConnection.BASE_URL + "auth/facebook-auth?access_token="+tokenKey;

        }
        HashMap hashMap = new HashMap();
//        hashMap.put("access_token", tokenKey);
        return performPostCall(url, hashMap,METHOD_POST);
    } public ResponseData signInGmail(String tokenKey ) {
        String url = null;
        try {
            url = MyConnection.BASE_URL + "auth/google-auth?access_token="+ URLEncoder.encode(tokenKey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            url = MyConnection.BASE_URL + "auth/google-auth?access_token="+tokenKey;

        }
        HashMap hashMap = new HashMap();
//        hashMap.put("access_token", tokenKey);
        return performPostCall(url, hashMap,METHOD_POST);
    }
    public ResponseData updatePassWord(String userId, String oldPassword, String password) {
        String url = MyConnection.BASE_URL + "users/change-password";
        HashMap hashMap = new HashMap();
        hashMap.put("id", userId);
        hashMap.put("password", oldPassword);
        hashMap.put("new_password", password);
        return performPostCallUseHeader(url, hashMap,METHOD_POST);
    }
    public ResponseData refreshToken(String user) {
        String url = MyConnection.BASE_URL + "auth/refresh-token";
        HashMap hashMap = new HashMap();
        hashMap.put("username", user);
        return performPostCallUseHeader(url, hashMap,METHOD_GET);
    }
    public ResponseData updateInfo(String userId, String avatar, String username, String name,String mobile) {
        String url = MyConnection.BASE_URL + "users/update";
        HashMap hashMap = new HashMap();
        hashMap.put("id", userId);
        if(!avatar.isEmpty())
            hashMap.put("avatar", avatar);
        if(!mobile.isEmpty())
            hashMap.put("mobile", mobile);
        if(!name.isEmpty()) {
            hashMap.put("first_name", name);
            hashMap.put("last_name", "");
        }
        hashMap.put("username", username);
        return performPostCallUseHeader(url, hashMap,METHOD_POST);
    }
    public ResponseData getBooks() {
        String url = MyConnection.BASE_URL + "resources/book";
        HashMap hashMap = new HashMap();
        return performPostCallUseHeader(url, hashMap,METHOD_GET);
    }

    public ResponseData activeKey(String userName,String key,String agencyId) {
        String url = MyConnection.BASE_URL +"users/active-payment";
        HashMap hashMap = new HashMap();
//        hashMap.put("username", userName);
        hashMap.put("payment_key", key);
//        hashMap.put("agency_id", agencyId);
//        hashMap.put("application_id", "10000000");
        return performPostCallUseHeader(url, hashMap,METHOD_POST);
    }
    public ResponseData getCommunications() {
        String url = MyConnection.BASE_URL + "resources/handbook";
        HashMap hashMap = new HashMap();
        return performPostCallUseHeader(url, hashMap,METHOD_GET);
    }
    public ResponseData getDictionary() {
        String url = MyConnection.BASE_URL + "resources/dictionary";
        HashMap hashMap = new HashMap();
        return performPostCallUseHeader(url, hashMap,METHOD_GET);
    }

    public ResponseData signUp(String userName,String email, String mobile, String password) {
        String url = MyConnection.BASE_URL + "auth/register";
        HashMap hashMap = new HashMap();
        hashMap.put("username", userName);
        hashMap.put("mobile", mobile);
        hashMap.put("password", password);
        hashMap.put("email", email);
        return performPostCall(url, hashMap,METHOD_POST);
    }


    private ResponseData performPostCall(String requestURL, HashMap<String, String> postDataParams,String method) {

        ResponseData responseData = new ResponseData();
        URL url;
        String response = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(requestURL);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);

            conn.setRequestProperty("User-Agent", "BeeEnglishAndroid");
            conn.setRequestProperty("Content-Type","application/json");

            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                responseData.setResponseState(true);
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
//                    response += "\n" + line;
                    response += line;
                }
            } else {
                response = conn.getResponseMessage();
            }
            responseData.setResponseData(response);
        } catch (Exception e) {
            Log.e("REQUEST EXCEPTION:", e.getMessage());
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        Log.e("RESPONSE_SERVER", response);
        return responseData;
    }

    private ResponseData performPostCallUseHeader(String requestURL, HashMap<String, String> postDataParams,String method) {

        ResponseData responseData = new ResponseData();
        URL url;
        String response = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(requestURL);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            String accessToken = UtilSharedPreferences.getInstanceSharedPreferences(mContext).getAccessToken();
            conn.setRequestProperty("User-Agent", "BeeEnglishAndroid");
            conn.setRequestProperty("Content-Type","application/json");
            if(!accessToken.isEmpty()) {
//                String basicAuth = "Bearer " +accessToken;
                String basicAuth = accessToken;
                conn.setRequestProperty("x-access-token",basicAuth);
            }
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if(postDataParams.size()>0) {
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();
            }
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                responseData.setResponseState(true);
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
//                    response += "\n" + line;
                    response += line;
                }
            } else {
                response = conn.getResponseMessage();
            }
            responseData.setResponseData(response);
        } catch (Exception e) {
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        Log.e("RESPONSE_SERVER", response);
        return responseData;
    }

    /***
     * Data encode
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        if(params.isEmpty()){
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        result.append("{");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                result.append(",");
            }
            result.append("\"" + entry.getKey() + "\"");
            result.append(":\"" + entry.getValue() + "\"");
        }
        result.append("}");
        Log.e("RESPONSE_SERVER", result.toString());
        return result.toString();
    }


    public static void noConnectInternet(final Activity activity,MySnackBar mySnackBar) {
        mySnackBar.showTextAction(R.string.err_connection_fail, R.string.bt_try_connection, new OnCallbackSnackBar() {
            @Override
            public void onAction() {
                openConnectWifi(activity);
            }
        });
    }

    public static void openConnectWifi(Activity activity) {
        int permissionWifi = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CHANGE_WIFI_STATE);
        if (permissionWifi != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CHANGE_WIFI_STATE}, Identity.REQUEST_PERMISSION_CHANGE_WIFI_STATE);
        } else {
            turnOnWifi(activity);
        }
    }

    public static void turnOnWifi(Activity activity) {
        WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }
}
