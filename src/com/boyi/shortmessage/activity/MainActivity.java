package com.boyi.shortmessage.activity;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.android.volley.Response.Listener;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.boyi.shortmessage.model.DayList;
import com.boyi.shortmessage.model.MessageList;
import com.boyi.shortmessage.model.QuickSearchResult;
import com.boyi.shortmessage.utils.SPUtils;
import com.boyi.shortmessage.widget.CustomDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity {
    private DayList mDayList = null;
    private MessageList mMessageList = null;
    private String mSearchText;
    private static ArrayList<LinearLayout> mContainerList = new ArrayList<LinearLayout>();

    private static Activity mInstance = null;

    public static void finishMainActivity() {
        if (mInstance != null) {
            mInstance.finish();
            mInstance = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInstance = this;
        QuickSearchActivity.finishQuickSearchActivity();
        initUI();
        doGetList();
    }

    public static void removeAllViews() {
        for (LinearLayout ll : mContainerList) {
            ll.removeAllViews();
        }
    }

    private void initUI() {
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.boyi_shortmessage));
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setImageResource(R.drawable.classify_icon);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClassifyActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        iv = (ImageView) findViewById(R.id.right_btn);
        iv.setVisibility(View.VISIBLE);
        iv.setImageResource(R.drawable.settings_icon);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        EditText et = (EditText) findViewById(R.id.edit_text_search);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchText = s.toString().trim();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                    KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return false;
                }
                return false;
            }
        });
    }

    private void showDayList() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.container);
        for (int i = 0; i < mDayList.GroupResult.size(); i++) {
            View view = (View) MainActivity.this.getLayoutInflater().inflate(R.layout.day_item, null);
            TextView tv = (TextView) view.findViewById(R.id.day);
            tv.setText(mDayList.GroupResult.get(i).Week);
            tv = (TextView) view.findViewById(R.id.date);
            tv.setText(mDayList.GroupResult.get(i).Date);
            tv = (TextView) view.findViewById(R.id.message_count);
            tv.setText(mDayList.GroupResult.get(i).Count);
            ll.addView(view, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            final LinearLayout container = (LinearLayout) view.findViewById(R.id.container);
            mContainerList.add(container);
            final int index = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String messagelistString = SPUtils.getMessageListForDate(MainActivity.this,
                            mDayList.GroupResult.get(index).Date);
                    if (TextUtils.isEmpty(messagelistString)) {
                        showDownloadConfirm(index);
                    } else {
                        expendList(container,
                                Constants.mGson.fromJson(messagelistString, MessageList.class),
                                mDayList.GroupResult.get(index).Date);
                    }
                }
            });
        }
    }

    private void expendList(LinearLayout container, MessageList messagelist, String date) {
        if (container.getChildCount() == 0) {
            for (int i = 0; i < messagelist.DownloadResult.size(); i++) {
                View view = (View) MainActivity.this.getLayoutInflater().inflate(R.layout.message_item, null);
                TextView tv = (TextView) view.findViewById(R.id.classify);
                tv.setText(messagelist.DownloadResult.get(i).Classify_name);
                tv = (TextView) view.findViewById(R.id.date);
                tv.setText(date);
                tv = (TextView) view.findViewById(R.id.content);
                tv.setText(messagelist.DownloadResult.get(i).Content);
                String textSize = SPUtils.getTextSize(MainActivity.this);
                if (textSize.equals(SPUtils.TEXT_SIZE_SMALL)) {
                    tv.setTextSize(12);
                } else if (textSize.equals(SPUtils.TEXT_SIZE_MEDIUM)) {
                    tv.setTextSize(16);
                } else {
                    tv.setTextSize(20);
                }
                container.addView(view, new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                container.setVisibility(View.VISIBLE);
            }
        } else {
            container.setVisibility(container.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        }
    }

    private void doGetList() {
        String url = Constants.URL_GET_DAY_LIST +
                ShortMessageApp.mUser.User_id + "/" + SPUtils.getDayCount(MainActivity.this);
        ShortMessageApp.doOperation(MainActivity.this, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        try {
                            mDayList = Constants.mGson.fromJson(arg0, DayList.class);
                        } catch (Exception e) {
                            
                        }
                        if (mDayList != null) {
                            showDayList();
                            doGetTodayInfoList();
                        } else {
                            CustomDialog.showMessageDialog(MainActivity.this, "服务器返回数据错误");
                        }
                    }
                }, "正在获取短讯列表");
    }

    private void doGetDayInfoList(final int index) {
        String url = Constants.URL_GET_DAY_INFO_LIST +
                ShortMessageApp.mUser.User_id + "/" + mDayList.GroupResult.get(index).Date;
        ShortMessageApp.doOperation(MainActivity.this, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        try {
                            mMessageList = Constants.mGson.fromJson(arg0, MessageList.class);
                        } catch (Exception e) {
                            
                        }
                        if (mMessageList != null) {
                            Log.d(Constants.TAG, "get mMessageList size: " + mMessageList.DownloadResult.size());
                            SPUtils.setMessageListForDate(MainActivity.this,
                                    mDayList.GroupResult.get(index).Date,
                                    Constants.mGson.toJson(mMessageList));
                        } else {
                            CustomDialog.showMessageDialog(MainActivity.this, "服务器返回数据错误");
                        }
                    }
                }, "正在获取列表");
    }

    private void doGetTodayInfoList() {
        doGetDayInfoList(0);
    }

    private CustomDialog showDownloadConfirm(final int index) {
        CustomDialog cd = new CustomDialog(MainActivity.this);
        cd.setTitle("下载咨询");
        cd.setMessage("您尚未下载" + mDayList.GroupResult.get(index).Date +
                "的最新资讯，是否立即下载查看？");
        cd.setPositiveButton("下载",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doGetDayInfoList(index);
                    }
                });
        cd.setNegativeButton("暂不下载",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        cd.setCanceledOnTouchOutside(false);
        cd.show();
        return cd;
    }

    private void doSearch() {
        if (!TextUtils.isEmpty(mSearchText)) {
            String url = Constants.URL_SEARCH_MESSAGE + URLEncoder.encode(mSearchText) + "/" + 0;
            ShortMessageApp.doOperation(MainActivity.this, url,
                    new Listener<String>() {
                        @Override
                        public void onResponse(String arg0) {
                            try {
                                SearchResultActivity.mResultList =
                                        Constants.mGson.fromJson(arg0, QuickSearchResult.class);
                            } catch (Exception e) {
                                
                            }
                            if (SearchResultActivity.mResultList == null) {
                                CustomDialog.showMessageDialog(MainActivity.this, "服务器返回数据错误");
                            } else if (SearchResultActivity.mResultList.SearchResult.size() == 0) {
                                CustomDialog.showMessageDialog(MainActivity.this, "搜索结果为空");
                            } else {
                                Intent intent = new Intent(
                                        MainActivity.this, SearchResultActivity.class);
                                MainActivity.this.startActivity(intent);
                            }
                        }
                    }, "正在搜索");
        }
    }
}
