package com.example.picktheperfectwatermelon.loading;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Util {
    public static Boolean isNetworkConnected(Context context) {
        try {
            context = context.getApplicationContext();
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}
