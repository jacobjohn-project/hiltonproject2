package com.futurearts.hiltonnewproj.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = Context.MODE_PRIVATE;

    // Shared preferences file name
    private static final String PREF_NAME = "PROJECT";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_USER_REGISTERED = "is_user_registered";
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "user_name";
    private static final String LAST_UPDATED_TIME="last_updated_time";

    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    ///////////////////SETTERS////////////////////////////////////

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }


    public void setIsUserRegistered(Boolean setvalue) {
        editor.putBoolean(IS_USER_REGISTERED, setvalue);
        editor.commit();
    }


    public void setUserId(String setvalue) {
        editor.putString(USER_ID, setvalue);
        editor.commit();
    }
    public void setUserName(String setvalue) {
        editor.putString(USER_NAME, setvalue);
        editor.commit();
    }


    public void setLastUpdatedTime(long setvalue) {
        editor.putLong(LAST_UPDATED_TIME, setvalue);
        editor.commit();
    }


    ///////////////GETTERS//////////////////////////////////


    public boolean getIsFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public Boolean getIsUserRegistered() {
        return pref.getBoolean(IS_USER_REGISTERED, false);
    }

    public String getUserId(){
        return pref.getString(USER_ID,"");
    }

    public String getUserName(){
        return pref.getString(USER_NAME,"");
    }

    public long getLastUpdatedTime(){
        return pref.getLong(LAST_UPDATED_TIME,0);
    }

    public void resetSharedPref(){
        editor.putLong(LAST_UPDATED_TIME, 0);
        editor.putString(USER_ID, "");
        editor.putBoolean(IS_USER_REGISTERED, false);
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, true);
        editor.commit();
    }


}