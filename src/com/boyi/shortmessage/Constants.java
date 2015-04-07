package com.boyi.shortmessage;

import com.google.gson.Gson;

public class Constants {

    public static final String TAG = "ShortMessageApp";

    public static Gson mGson = new Gson();
//    public static Object fromJson(String json, Class classOfT) {
//        try {
//            return mGson.fromJson(json, classOfT);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private static final String DBG_HOST = "http://christiank.xicp.net:44061/";

    public static final String URL_LOGIN = DBG_HOST + "AppuserService.svc/Login/";
    public static final String URL_REGISTER = DBG_HOST + "AppuserService.svc/Register/";
    public static final String URL_UPDATE_SETTINGS = DBG_HOST + "AppuserService.svc/UpdateSetting/";
    public static final String URL_SELECTED_COLUMNS = DBG_HOST + "AppuserService.svc/SelectedColumns/";
    public static final String URL_RESET_PASSWORD = DBG_HOST + "AppuserService.svc/ResetPassword/";
    public static final String URL_GET_DAY_LIST = DBG_HOST + "SmsService.svc/Group/";
    public static final String URL_GET_DAY_INFO_LIST = DBG_HOST + "SmsService.svc/Download/";
    public static final String URL_SEARCH_MESSAGE = DBG_HOST + "SmsService.svc/Search/";
    public static final String URL_ADD_FEEDBACK = DBG_HOST + "FeedbackService.svc/Add/";
    public static final String URL_GET_FEEDBACK = DBG_HOST + "FeedbackService.svc/Feedback/";
    public static final String URL_GET_LATEST_VERSION = DBG_HOST + "VersionService.svc/GetLatestVersion";
}
