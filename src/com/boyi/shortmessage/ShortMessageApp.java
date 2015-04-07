package com.boyi.shortmessage;

import java.util.ArrayList;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
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
                        } else  if (arg0.networkResponse.statusCode == 417) {
                            error = new String(arg0.networkResponse.data);
                        } else {
                            error = arg0.getLocalizedMessage();
                        }
                        Log.d(Constants.TAG, "error result: " + error);
                        if (TextUtils.isEmpty(error)) {
                            error = "操作失败";
                        }
                        CustomDialog.showMessageDialog(activity, error);
                    }
                }) {
            @Override
            public RetryPolicy getRetryPolicy() {
                RetryPolicy retryPolicy = new DefaultRetryPolicy(60 * 1000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                return retryPolicy;
            }
        };
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
