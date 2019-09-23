package com.qmul.partymania.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bb on 2017/6/10.
 */
public class NowTime {

    public static String getNowDate() {
        java.util.Date now_date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(now_date);
    }



    public static String getSysYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
    }


}
