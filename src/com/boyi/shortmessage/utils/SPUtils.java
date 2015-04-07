package com.boyi.shortmessage.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.boyi.shortmessage.model.MessageList;
import com.boyi.shortmessage.model.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

public class SPUtils {
    public static final String TEXT_SIZE_SMALL = "small";
    public static final String TEXT_SIZE_MEDIUM = "medium";
    public static final String TEXT_SIZE_LARGE = "large";

    private static final String SP_NAME = "shortmessage";
    private static final String SP_SETTINGS = "settings";
    private static final String SP_DAY_COUNT = "daycount";
    private static final String SP_TEXT_SIZE = "textsize";

    public static void saveSettings(Context context, Settings settings) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(settings);

            String personBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(SP_SETTINGS, personBase64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings getSettings(Context context) {
        try {
            SharedPreferences mSharedPreferences = context
                    .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            String personBase64 = mSharedPreferences.getString(
                    SP_SETTINGS, "");
            byte[] base64Bytes = Base64.decode(personBase64.getBytes(), Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Settings settings = (Settings) ois.readObject();
            return settings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getDayCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getInt(SP_DAY_COUNT, 7);
    }

    public static void setDayCount(Context context, int daycount) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        Editor et = sp.edit();
        et.putInt(SP_DAY_COUNT, daycount);
        et.commit();
    }

    public static String getTextSize(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(SP_TEXT_SIZE, TEXT_SIZE_SMALL);
    }

    public static void setTextSize(Context context, String textsize) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        Editor et = sp.edit();
        et.putString(SP_TEXT_SIZE, textsize);
        et.commit();
    }

    public static void setMessageListForDate(Context context, String date, String messageListJson) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        Editor et = sp.edit();
        et.putString(date, messageListJson);
        et.commit();
    }

    public static String getMessageListForDate(Context context, String date) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(date, "");
    }

    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        Editor et = sp.edit();
        et.clear();
        et.commit();
    }
}
