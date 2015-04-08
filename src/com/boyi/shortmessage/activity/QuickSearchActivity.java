package com.boyi.shortmessage.activity;

import java.net.URLEncoder;

import com.android.volley.Response.Listener;
import com.baidu.mobstat.StatService;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.boyi.shortmessage.model.QuickSearchResult;
import com.boyi.shortmessage.utils.AppUtils;
import com.boyi.shortmessage.widget.CustomDialog;
import com.igexin.sdk.PushManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class QuickSearchActivity extends Activity {

    public static String mSearchText;

    private static Activity mInstance = null;

    public static void finishQuickSearchActivity() {
        if (mInstance != null) {
            mInstance.finish();
            mInstance = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search);

        PushManager.getInstance().initialize(this.getApplicationContext());

        mInstance = this;
        initUI();
    }

    private void initUI() {
        mSearchText = "";
        TextView tv = (TextView) findViewById(R.id.version);
        tv.setText("V" + AppUtils.getVersionName(this));
        EditText et = (EditText) findViewById(R.id.edit_text_search);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mSearchText = arg0.toString().trim();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
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
        Button bt = (Button) findViewById(R.id.login);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickSearchActivity.this, LoginActivity.class);
                QuickSearchActivity.this.startActivity(intent);
            }
        });
        bt = (Button) findViewById(R.id.register);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuickSearchActivity.this, RegisterActivity.class);
                QuickSearchActivity.this.startActivity(intent);
            }
        });
    }

    private void doSearch() {
        if (!TextUtils.isEmpty(mSearchText)) {
            String url = Constants.URL_SEARCH_MESSAGE + URLEncoder.encode(mSearchText) + "/" + 0;
            ShortMessageApp.doOperation(QuickSearchActivity.this, url,
                    new Listener<String>() {
                        @Override
                        public void onResponse(String arg0) {
                            try {
                                SearchResultActivity.mResultList =
                                        Constants.mGson.fromJson(arg0, QuickSearchResult.class);
                            } catch (Exception e) {
                                
                            }
                            if (SearchResultActivity.mResultList == null) {
                                CustomDialog.showMessageDialog(QuickSearchActivity.this, "服务器返回数据错误");
                            } else if (SearchResultActivity.mResultList.SearchResult.size() == 0) {
                                CustomDialog.showMessageDialog(QuickSearchActivity.this, "搜索结果为空");
                            } else {
                                Intent intent = new Intent(
                                        QuickSearchActivity.this, SearchResultActivity.class);
                                QuickSearchActivity.this.startActivity(intent);
                            }
                        }
                    }, "正在搜索");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        StatService.onPageStart(this, this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();

        StatService.onPageEnd(this, this.getClass().getSimpleName());
    }
}
