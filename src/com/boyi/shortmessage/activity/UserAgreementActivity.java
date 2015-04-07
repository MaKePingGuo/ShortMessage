package com.boyi.shortmessage.activity;

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
}
