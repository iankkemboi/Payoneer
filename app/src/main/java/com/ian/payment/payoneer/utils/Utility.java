package com.ian.payment.payoneer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class Utility {

    public static boolean checkInternetConnection(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {

            return true;
        } else {

            return false;
        }
    }
}
