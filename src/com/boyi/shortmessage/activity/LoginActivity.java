package com.boyi.shortmessage.activity;

import com.android.volley.Response.Listener;
import com.baidu.mobstat.StatService;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.model.UserAndClassifies;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.boyi.shortmessage.utils.AppUtils;
import com.boyi.shortmessage.widget.CustomDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends Activity {

    private String mAccount = "";
    private String mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
    }

    private void initUI() {
        TextView tv = (TextView) findViewById(R.id.version);
        tv.setText("V" + AppUtils.getVersionName(this));
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.login_boyi));
        tv = (TextView) findViewById(R.id.call_number);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showDialConfirmDialog(LoginActivity.this);
            }
        });

        EditText et = (EditText) findViewById(R.id.edit_text_account);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mAccount = arg0.toString().trim();
                if (TextUtils.isEmpty(mAccount)) {
                    ((ImageView) findViewById(R.id.account_icon)).setImageResource(
                            R.drawable.icon_account_empty);
                } else {
                    ((ImageView) findViewById(R.id.account_icon)).setImageResource(
                            R.drawable.icon_account_not_empty);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        et = (EditText) findViewById(R.id.edit_text_password);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mPassword = arg0.toString().trim();
                if (TextUtils.isEmpty(mPassword)) {
                    ((ImageView) findViewById(R.id.password_icon)).setImageResource(
                            R.drawable.icon_password_empty);
                } else {
                    ((ImageView) findViewById(R.id.password_icon)).setImageResource(
                            R.drawable.icon_password_not_empty);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        Button bt = (Button) findViewById(R.id.login);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mAccount) || !AppUtils.checkAccount(mAccount)) {
                    CustomDialog.showMessageDialog(LoginActivity.this, R.string.account_warning);
                } else if (TextUtils.isEmpty(mPassword)) {
                    CustomDialog.showMessageDialog(LoginActivity.this, R.string.password_warning);
                } else {
                    doLogin();
                }
            }
        });
//        bt = (Button) findViewById(R.id.register);
//        bt.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
        bt = (Button) findViewById(R.id.reset_password);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }

    private void doLogin() {
        String url = Constants.URL_LOGIN + mAccount + "/" + mPassword;
        ShortMessageApp.doOperation(this, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        try {
                            UserAndClassifies uac = Constants.mGson.fromJson(arg0, UserAndClassifies.class);
                            ShortMessageApp.mUser = uac.Appuser;
                            ShortMessageApp.mClassifies = uac.Classifies;
                        } catch (Exception e) {
                        }
                        if (ShortMessageApp.mUser != null) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            LoginActivity.this.startActivity(intent);
                            LoginActivity.this.finish();
                        } else {
                            CustomDialog.showMessageDialog(LoginActivity.this, "服务器返回数据错误");
                        }
                    }
                }, "正在登录");
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
