package com.ahiho.apps.beeenglish.view;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.my_interface.OnCallbackSnackBar;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.util.MySnackBar;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.ahiho.apps.beeenglish.util.UtilString;
import com.ahiho.apps.beeenglish.view.dialog.StatusDialog;

import static com.ahiho.apps.beeenglish.util.MyConnection.noConnectInternet;
import static com.ahiho.apps.beeenglish.util.MyConnection.turnOnWifi;

/**
 * Created by theptokim on 11/25/16.
 */

public class BaseActivity extends AppCompatActivity {
    private UtilString mUtilString;
    public MySnackBar mSnackbar;
    private UtilSharedPreferences mUtilSharedPreferences;
    private MyConnection mMyConnection;

    private final int ACTION_SIGN_IN = 0;
    private final int ACTION_SIGN_UP = 1;
    private final int ACTION_GET_BOOKS = 2;
    private ProgressDialog progressDialog;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        mSnackbar = new MySnackBar(BaseActivity.this, viewGroup);
        mMyConnection = MyConnection.getInstanceMyConnection(BaseActivity.this);
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(BaseActivity.this);
    }


    public void showDialogLoading() {
        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = ProgressDialog.show(BaseActivity.this, null,
                getString(R.string.loading), true);
    }
    public void showDialogLoading(int textResource) {
        try {
            if ((progressDialog != null) && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = ProgressDialog.show(BaseActivity.this, null,
                    getString(textResource), true);
        }catch (Exception e){

        }
    }

    public void dismissDialog() {
        try {
            if ((progressDialog != null) && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
        }catch (Exception e){}
    }

    public void showDialogStatus(String title, String text, boolean status) {
        Intent intent = new Intent(BaseActivity.this, StatusDialog.class);
        intent.putExtra(Identity.EXTRA_STATUS_TITLE,title);
        intent.putExtra(Identity.EXTRA_STATUS_TEXT,text);
        intent.putExtra(Identity.EXTRA_STATUS_BOOLEAN,status);
        startActivity(intent);

    }




    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        dismissDialog();
        super.onPause();
    }


    public void signOut() {
        mUtilSharedPreferences.signOut();
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
            case Identity.REQUEST_PERMISSION_READ_WRITE_FILE:
                if (grantResults.length > 0) {
                    int result = 0;
                    for (int grant : grantResults) {
                        result += grant;
                    }
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mSnackbar.showTextAction(R.string.err_permission_file, R.string.bt_try_connection, new OnCallbackSnackBar() {
                    @Override
                    public void onAction() {
                        grantPermissionReadWriteFile();
                    }
                });
                break;
        }
    }

    public boolean grantPermissionReadWriteFile() {
        boolean result = false;
        int permissionReadFile = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteFile = ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionReadFile != PackageManager.PERMISSION_GRANTED || permissionWriteFile != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Identity.REQUEST_PERMISSION_READ_WRITE_FILE);
        } else {
            result = true;
        }
        return result;
    }

    public void showSnackBar(String text) {
        mSnackbar.showText(text);
    }

    public void showSnackBar(int textResource) {
        mSnackbar.showText(textResource);
    }

    public boolean isOnline() {
        if (MyConnection.isOnline(BaseActivity.this)) {
            return true;
        } else {
            noConnectInternet(BaseActivity.this, mSnackbar);
        }
        return false;
    }


}
