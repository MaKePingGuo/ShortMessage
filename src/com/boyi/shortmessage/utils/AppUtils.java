package com.boyi.shortmessage.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;

import com.boyi.shortmessage.R;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppUtils {

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getApplicationContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean checkAccount(String phoneNumber) {
        if (phoneNumber.length() != 11) {
            return false;
        } else {
            for (int i = 0; i < phoneNumber.length(); i++) {
                if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9') {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getAssetFileContent(Context context, String fileName) {
        String result = "";
        StringBuffer sb = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(context.getResources()
                    .getAssets().open(fileName));
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {

        } finally {
            result = sb.toString();
        }
        return result;
    }

    public static String formetFileSize(long fileS, Context context) {// Conversion
                                                                      // File
                                                                      // Size
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS)
                    + context.getString(R.string.b_text);
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024)
                    + context.getString(R.string.k_text);
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576)
                    + context.getString(R.string.m_text);
        } else {
            fileSizeString = df.format((double) fileS / 1073741824)
                    + context.getString(R.string.g_text);
        }
        return fileSizeString;
    }

    public static String getVerificationUrl(String phoneNumber, String verication) {
        String content = "";
        try {
            content = URLEncoder.encode("您的验证码是" + verication + "，请妥善保管。", "gb2312");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        String content = "您的验证码是" + verication + "，请妥善保管。";
        String url = "http://service.winic.org/sys_port/gateway/?id=bozes&pwd=82962057&to=" +
                phoneNumber + "&content=" + content + "&time=";
        return url;
    }

    public static String generateVerification() {
        String verification = "";
        while (verification.length() < 6) {
            verification += String.valueOf(((int)(Math.random() * 10)) % 10);
        }
        return verification;
    }
}
