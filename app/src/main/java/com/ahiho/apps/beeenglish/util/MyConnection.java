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
import android.view.View;

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
    private static final String BASE_URL = "https://beeenglish.mobile-backend.ahiho.com/";
    private static MyConnection myConnection = null;

    public static MyConnection getInstanceMyConnection() {
        if (myConnection == null)
            myConnection = new MyConnection();
        return myConnection;
    }

    public MyConnection() {
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public ResponseData signIn(String userName, String password) {
        String url = MyConnection.BASE_URL + "api/session/auth";
        HashMap hashMap = new HashMap();
        hashMap.put("username", userName);
        hashMap.put("password", password);
        return performPostCall(url, hashMap);
    }

    public ResponseData signUp(String userName,String email, String password) {
        String url = MyConnection.BASE_URL + "api/users/register";
        HashMap hashMap = new HashMap();
        hashMap.put("username", userName);
        hashMap.put("password", password);
        hashMap.put("email", email);
        hashMap.put("mobile", "");
        hashMap.put("first_name", "");
        hashMap.put("last_name", "");
        hashMap.put("avatar", "");
        return performPostCall(url, hashMap);
    }


    private ResponseData performPostCall(String requestURL, HashMap<String, String> postDataParams) {

        ResponseData responseData = new ResponseData();
        URL url;
        String response = "";
        HttpURLConnection conn = null;
        try {
            url = new URL(requestURL);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("POST");
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
                    response += "\n" + line;
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