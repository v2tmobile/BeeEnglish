package com.ahiho.apps.beeenglish.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.ahiho.apps.beeenglish.my_interface.OnCallbackSnackBar;

/**
 * Created by theptokim on 11/28/16.
 */

public class MySnackBar {
    private Snackbar snackbar;
    private View mView;
    private Activity mActivity;

    public MySnackBar(final Activity activity, View view) {
        mView = view;
        mActivity = activity;
    }

    public void showText(String text) {
        snackbar = Snackbar
                .make(mView, text, Snackbar.LENGTH_LONG);
        UtilActivity.hiddenKeyBoard(mActivity);
        snackbar.show();
    }

    public void showText(int textResource) {
        snackbar = Snackbar
                .make(mView, textResource, Snackbar.LENGTH_LONG);
        UtilActivity.hiddenKeyBoard(mActivity);
        snackbar.show();
    }

    public void showTextAction(String text, String action, final OnCallbackSnackBar onCallbackSnackBar) {
        snackbar = Snackbar.make(mView, text, Snackbar.LENGTH_LONG)
                .setAction(action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCallbackSnackBar.onAction();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        UtilActivity.hiddenKeyBoard(mActivity);
        snackbar.show();
    }

    public void showTextAction(int textResource, int actionResource, final OnCallbackSnackBar onCallbackSnackBar) {
        snackbar = Snackbar.make(mView, textResource, Snackbar.LENGTH_LONG)
                .setAction(actionResource, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCallbackSnackBar.onAction();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        UtilActivity.hiddenKeyBoard(mActivity);
        snackbar.show();
    }
}
