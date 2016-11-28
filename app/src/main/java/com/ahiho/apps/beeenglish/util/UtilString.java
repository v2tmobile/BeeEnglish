package com.ahiho.apps.beeenglish.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 * Created by theptokim on 9/28/16.
 */
public class UtilString {
    //    public static String formatMoney(Double money){
//        return NumberFormat.getNumberInstance(Locale.US).format(money);
//    }



    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password, String confirmPassword) {
        return password.equals(password);
    }

}
