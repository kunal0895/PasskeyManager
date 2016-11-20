package com.example.dhiren.passkeymanager.utils;

import android.content.Context;

import java.text.SimpleDateFormat;

/**
 * Created by Kunal on 11-09-2016.
 */
public class Utils {

    /**
     * Format the date with SimpleDateFormat
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Context mContext = null;


    /**
     * Public constructor that takes mContext for later use
     */
    public Utils(Context con) {
        mContext = con;
    }

}
