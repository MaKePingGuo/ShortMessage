package com.boyi.shortmessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.boyi.shortmessage.model.Classify;
import com.boyi.shortmessage.model.Column;
import com.boyi.shortmessage.model.Settings;
import com.boyi.shortmessage.model.User;
import com.boyi.shortmessage.utils.SPUtils;
import com.boyi.shortmessage.widget.CustomDialog;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;
import android.util.Log;

public class ShortMessageApp extends Application {

    public static RequestQueue mRequestQueue;
    public static User mUser;
    public static ArrayList<Column> mColumns;
    public static ArrayList<Classify> mClassifies;
    public static Settings mSettings;

    private static CustomDialog mCD;

    @Override
    public void onCreate() {
        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(this);
        mSettings = SPUtils.getSettings(this);
    }

    public static void doOperation(final Activity activity,
            String url, final Listener<String> listener, String title) {
        Log.d(Constants.TAG, "url: " + url);
        final StringRequest sr = new StringRequest(Method.GET, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.d(Constants.TAG, "result: " + arg0);
                        dismissLoadingDialog();
                        if (listener != null) {
                            listener.onResponse(arg0);
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        dismissLoadingDialog();
                        String error = "";
                        if (arg0.networkResponse == null) {
                            error = arg0.getMessage();
                        } else {
                            int statuscode = arg0.networkResponse.statusCode;
                            switch (statuscode) {
                            case 500:
                                error = "用户不存在";
                                break;
                            case 501:
                                error = "用户名或密码错误";
                                break;
                            case 502:
                                error = "用户已过期";
                                break;
                            case 503:
                                error = "用户名已经存在";
                                break;
                            case 504:
                                error = "密码不能为空";
                                break;
                            default:
                                break;
                            }
                        }
                        if (TextUtils.isEmpty(error)) {
                          error = arg0.getLocalizedMessage();
                        }
                        if (TextUtils.isEmpty(error)) {
                            error = "操作失败";
                        }
                        Log.d(Constants.TAG, "error result: " + error);
                        CustomDialog.showMessageDialog(activity, error);
                    }
                });
        sr.setTag(activity);
        ShortMessageApp.mRequestQueue.add(sr);
        mCD = CustomDialog.showLoading(activity, title, new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ShortMessageApp.mRequestQueue.cancelAll(this);
                Log.d(Constants.TAG, "cancel");
            }
        });
    }

    public static void dismissLoadingDialog() {
        if (mCD != null) {
            mCD.dismiss();
            mCD = null;
        }
    }
}
