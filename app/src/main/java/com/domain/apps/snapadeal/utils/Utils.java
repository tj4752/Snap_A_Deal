package com.domain.apps.snapadeal.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.domain.apps.snapadeal.R;
import com.domain.apps.snapadeal.appconfig.AppConfig;
import com.domain.apps.snapadeal.appconfig.Constances;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;


/**
 * Created by Droideve on 09/06/2015.
 */
public class Utils {


    public static final String DEFAULT_VALUE = "N/A";
    private static String SP_NAME = "q2sUn5aZDmL56";
    private static String SP_NAME_KEY = "q2sUn5aZDmL56tOoKeN";

    public static Bitmap flip(Bitmap src) {

        Matrix matrix = new Matrix();


        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static int dp_get_id_from_url(String url, String prefix) {

        if (!Utils.isValidURL(url))
            return 0;

        String[] list = url.split("/");

        try {

            for (int i = 0; i < list.length; i++) {

                if (prefix.equals(list[i])) {
                    String uri = list[i + 1];

                    if (uri.equals("id")) {
                        String id = list[i + 2];
                        if (AppConfig.APP_DEBUG)
                            Log.e("dp_get_id_from_url", prefix + " " + Integer.parseInt(id) + " closed");
                        return Integer.parseInt(id);
                    }

                }
            }

        } catch (Exception e) {
            if (AppConfig.APP_DEBUG)
                e.printStackTrace();
        }


        return 0;
    }

    public static boolean isValidURL(String url) {

        URL u = null;

        try {
            u = new URL(url);
        } catch (MalformedURLException e) {
            return false;
        }

        try {
            u.toURI();
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }

    public static Drawable changeDrawableIconMap(Context context, int resId) {

        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), resId, null);
        PorterDuff.Mode mode = PorterDuff.Mode.SRC_ATOP;
        drawable.setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null), mode);

        return drawable;
    }

    public static void enableEvent() {

//        for(int i=0;i<Constances.initConfig.ListCats.size();i++){
//
//            Category tab = Constances.initConfig.ListCats.get(i);
//            if(AppConfig.ENABLE_EVENTS==false){
//                if(tab.getType() == Constances.initConfig.Tabs.EVENTS){
//                    Constances.initConfig.ListCats.remove(i);
//                    Constances.initConfig.Numboftabs = Constances.initConfig.ListCats.size();
//                }
//
//            }
//        }
    }

    public static String getToken(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SP_NAME_KEY, "");
    }

    public static String getDistanceBy(double meters) {

        String FINAL_VALUE = "M";
        if (meters > 0) {

            if (meters > 1000) {

                FINAL_VALUE = "Km";

            }
        } else {
            FINAL_VALUE = "";
        }

        return FINAL_VALUE;

    }

    public static Boolean isNearMAXDistance(double meters) {

        return meters >= 0 && meters <= 100000;

    }

    public static String preparDistance(double meters) {

        String FINAL_VALUE = DEFAULT_VALUE + " ";

        if (meters >= 1000 && meters <= 100000) {

            double kilometers = 0.0;
            kilometers = meters * 0.001;

            DecimalFormat decim = new DecimalFormat("#.##");
            FINAL_VALUE = decim.format(kilometers) + "";

        } else if (meters > 100000) {

            FINAL_VALUE = "+100";

        } else if (meters < 1000) {

            FINAL_VALUE = ((int) meters) + "";

        }


        return FINAL_VALUE;
    }

    public static void setFontBold(Context context, TextView view) {

        Typeface tf = Typeface.createFromAsset(context.getAssets(), Constances.Fonts.BOLD);
        view.setTypeface(tf);

    }

    public static class Params {

        private Bundle params;


        @Override
        public String toString() {

            return params.toString();
        }
    }



    /*public static void setFont(Context context,TextView view,String name){

        Typeface tf = Typeface.createFromAsset(context.getAssets(), Constances.Fonts.REGULAR);
       view.setTypeface(tf);

    }*/

}
