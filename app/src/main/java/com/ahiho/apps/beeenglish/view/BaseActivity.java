package com.ahiho.apps.beeenglish.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.my_interface.OnCallbackSnackBar;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.util.MySnackBar;
import com.ahiho.apps.beeenglish.util.UtilString;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ahiho.apps.beeenglish.util.MyConnection.noConnectInternet;
import static com.ahiho.apps.beeenglish.util.MyConnection.turnOnWifi;

/**
 * Created by theptokim on 11/25/16.
 */

public class BaseActivity extends AppCompatActivity {
    private UtilString mUtilString;
    private MySnackBar mSnackbar;

    public void setViewShowSnackBar(View view) {
        mSnackbar = new MySnackBar(BaseActivity.this, view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Identity.REQUEST_PERMISSION_CHANGE_WIFI_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    int result = 0;
                    for (int grant : grantResults) {
                        result += grant;
                    }
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        turnOnWifi(BaseActivity.this);
                        return;
                    }
                }
                mSnackbar.showTextAction(R.string.err_permission_wifi, R.string.bt_try_connection, new OnCallbackSnackBar() {
                    @Override
                    public void onAction() {
                        ActivityCompat.requestPermissions(BaseActivity.this,
                                new String[]{Manifest.permission.CHANGE_WIFI_STATE}, Identity.REQUEST_PERMISSION_CHANGE_WIFI_STATE);
                    }
                });
                return;
            }
        }
    }

    public void showSnackBar(String text) {
        mSnackbar.showText(text);
    }


    public void actionSignIn(String email, String password) {
        if (MyConnection.isOnline(BaseActivity.this)) {
            new SignIn(email, password).execute();
        } else {
            noConnectInternet(BaseActivity.this, mSnackbar);
        }
    }

    public void actionSignUp(String username, String email, String password) {
        if (MyConnection.isOnline(BaseActivity.this)) {
            new SignUp(username, email, password).execute();
        } else {
            noConnectInternet(BaseActivity.this, mSnackbar);
        }
    }


    class SignIn extends AsyncTask<Void, Void, ResponseData> {

        private ProgressDialog mDialog;
        private String mEmail, mPassword;

        public SignIn(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(BaseActivity.this, null,
                    "Loading...", true);
        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            return MyConnection.getInstanceMyConnection().signIn(mEmail, mPassword);
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            mDialog.dismiss();
            if(responseData.isResponseState()){
                try {
                    JSONObject jsonObject = new JSONObject(responseData.getResponseData());
                } catch (JSONException e) {

                }
            }else{
                mSnackbar.showText(responseData.getResponseData());
            }
            super.onPostExecute(responseData);
        }
    }

    class SignUp extends AsyncTask<Void, Void, ResponseData> {

        private ProgressDialog mDialog;
        private String mUserName, mEmail, mPassword;

        public SignUp(String username, String email, String password) {
            mUserName = username;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            mDialog = ProgressDialog.show(BaseActivity.this, null,
                    "Loading...", true);
        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            return MyConnection.getInstanceMyConnection().signUp(mUserName,mEmail, mPassword);
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            mDialog.dismiss();
            if(responseData.isResponseState()){
                try {
                    JSONObject jsonObject = new JSONObject(responseData.getResponseData());
                } catch (JSONException e) {

                }

            }else{
                mSnackbar.showText(responseData.getResponseData());
            }
            super.onPostExecute(responseData);
        }
    }

}
