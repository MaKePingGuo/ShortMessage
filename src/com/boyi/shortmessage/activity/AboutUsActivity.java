package com.boyi.shortmessage.activity;

import com.boyi.shortmessage.R;

public class AboutUsActivity extends WebViewActivity {

    @Override
    int getTitleId() {
        return R.string.about_us;
    }

    @Override
    String getFileName() {
        return "AboutUs.html";
    }
}
