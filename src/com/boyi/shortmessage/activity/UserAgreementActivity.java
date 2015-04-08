package com.boyi.shortmessage.activity;

import com.baidu.mobstat.StatService;
import com.boyi.shortmessage.R;

public class UserAgreementActivity extends WebViewActivity {

    @Override
    int getTitleId() {
        return R.string.policy_link;
    }

    @Override
    String getFileName() {
        return "UserAgreement.html";
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
