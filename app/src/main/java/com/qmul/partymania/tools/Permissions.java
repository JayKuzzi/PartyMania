package com.qmul.partymania.tools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class Permissions{
    public static boolean checkNetwork(Activity activity) {
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
