package com.ahiho.apps.beeenglish.util;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by theptokim on 9/28/16.
 */
public class UtilString {

    private static String removeAccent(String str) {

        str= str.toLowerCase();
        str= str.replaceAll("\\à|\\á|\\ạ|\\ả|\\ã|\\â|\\ầ|\\ấ|\\ậ|\\ẩ|\\ẫ|\\ă|\\ằ|\\ắ|\\ặ|\\ẳ|\\ẵ","a");
        str= str.replaceAll("\\è|\\é|\\ẹ|\\ẻ|\\ẽ|\\ê|\\ề|\\ế|\\ệ|\\ể|\\ễ","e");
        str= str.replaceAll("\\ì|\\í|\\ị|\\ỉ|\\ĩ","i");
        str= str.replaceAll("\\ò|\\ó|\\ọ|\\ỏ|\\õ|\\ô|\\ồ|\\ố|\\ộ|\\ổ|\\ỗ|\\ơ|\\ờ|\\ớ|\\ợ|\\ở|\\ỡ","o");
        str= str.replaceAll("\\ù|\\ú|\\ụ|\\ủ|\\ũ|\\ư|\\ừ|\\ứ|\\ự|\\ử|\\ữ","u");
        str= str.replaceAll("\\ỳ|\\ý|\\ỵ|\\ỷ|\\ỹ","y");
        str= str.replace("đ","d");
        //cắt bỏ ký tự - ở đầu và cuối chuỗi
        return str;
    }

    public static boolean compareStringSearch(String s, String search){
        if(removeAccent(s).contains(removeAccent(search)))
            return true;
        return false;
    }


    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password, String confirmPassword) {
        return password.equals(password);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }
}
