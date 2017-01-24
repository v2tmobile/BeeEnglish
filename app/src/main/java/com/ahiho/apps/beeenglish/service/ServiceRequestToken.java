package com.ahiho.apps.beeenglish.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by theptokim on 12/31/16.
 */

public class ServiceRequestToken extends Service {
    private UtilSharedPreferences mUtilSharedPreferences;
    private Context mContext;
    private  Runnable runnableTick;
    private Handler handler;
    private boolean isValid=true;
    private boolean isRequestRunning =false;
    // phương thức khởi tạo
    @Override
    public void onCreate() {
        mContext=getBaseContext();
        mUtilSharedPreferences= UtilSharedPreferences.getInstanceSharedPreferences(mContext);
        handler = new Handler();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
////        connectToService();
//        startTimer();
//        return S;
//    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer() {
        if (runnableTick != null) {
            handler.removeCallbacks(runnableTick);
            runnableTick = null;
        }
        runnableTick = new Runnable() {
            public void run() {
                try {
                    callRequest();
                } catch (Exception e) {
                }
                handler.postDelayed(this, 1000);
            }
        };
        runnableTick.run();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnableTick);
        runnableTick = null;

        super.onDestroy();
    }

    private void callRequest(){
        if(MyConnection.isOnline(mContext)&&!isRequestRunning) {
            long time = System.currentTimeMillis() - mUtilSharedPreferences.getAccessTokenExpired();
            if (time < 120000) {
                try {
                    JSONObject jsonObject= new JSONObject(mUtilSharedPreferences.getUserData());
                    isRequestRunning = true;
                    new RefreshToken(jsonObject.getString("username")).execute();
                } catch (JSONException e) {

                }

            }
        }
    }
    private boolean isAppRunning(){
        boolean isRunning=false;
        ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i++)
        {
            if(procInfos.get(i).processName.equals(mContext.getPackageName()))
            {
                isRunning=true;
                break;
            }
        }
        return isRunning;
    }
    class RefreshToken extends AsyncTask<Void, Void, ResponseData> {

        private String userName;
        public RefreshToken(String userName) {
            this.userName=userName;

        }



        @Override
        protected ResponseData doInBackground(Void... params) {
            return MyConnection.getInstanceMyConnection(mContext).refreshToken(userName);
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            try {
                if (responseData.isResponseState()) {
                    JSONObject jsonObject = new JSONObject(responseData.getResponseData());
                    if (jsonObject.getBoolean("success")) {
                        mUtilSharedPreferences.setAccessToken(jsonObject.getString("access_token"));
                        mUtilSharedPreferences.setAccessTokenExpired(jsonObject.getLong("expires_in"));
                    }
                }
            } catch (JSONException e) {
            }
            isRequestRunning =false;
            super.onPostExecute(responseData);
        }

    }


}