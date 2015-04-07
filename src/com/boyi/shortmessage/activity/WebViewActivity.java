package com.boyi.shortmessage.activity;

import com.boyi.shortmessage.R;
import com.boyi.shortmessage.utils.AppUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class WebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(getTitleId()));

        WebView wv = (WebView) findViewById(R.id.web_view);
        WebSettings settings = wv.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        wv.loadData(AppUtils.getAssetFileContent(this, getFileName()), "text/html; charset=UTF-8", null);
    }

    abstract int getTitleId();
    abstract String getFileName();
}
