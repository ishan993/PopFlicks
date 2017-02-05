package com.ishanvadwala.nanodegree01.Utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by ishanvadwala on 10/24/16.
 */
public class Utility {

    private static float dpWidth;


    public static boolean isTabletWidth(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = activity.getResources().getDisplayMetrics().density;
        dpWidth = outMetrics.widthPixels / density;
        if (dpWidth>735)
            return true;
        else
            return false;
    }

}