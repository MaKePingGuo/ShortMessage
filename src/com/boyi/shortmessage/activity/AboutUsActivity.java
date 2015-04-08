package com.boyi.shortmessage.activity;

import com.baidu.mobstat.StatService;
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
