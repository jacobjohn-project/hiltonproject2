package com.futurearts.hiltonnewproj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getSystemDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }
}
