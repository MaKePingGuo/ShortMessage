package com.boyi.shortmessage.activity;

import com.android.volley.Response.Listener;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.boyi.shortmessage.model.UserAndClassifies;
import com.boyi.shortmessage.model.UserAndColumns;
import com.boyi.shortmessage.utils.AppUtils;
import com.boyi.shortmessage.widget.CustomDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ResetPasswordActivity extends Activity {

    private String mAccount = "";
    private String mVerification = "";
    private String mRealVerification = "";
    private String mPassword = "";
//    private String mPasswordConfirm = "";

    private static final int VERIFICATION_WAIT_TIME = 60;
    private static final int UPDATE_TIMER = 1;

    private Handler mTimernHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case UPDATE_TIMER:
                int left = (Integer) msg.obj;
                if (left != 0) {
                    ((Button) findViewById(R.id.get_verification)).setText(
                            String.valueOf(left) + "秒后重新获取");
                    mTimernHandler.sendMessageDelayed(
                            mTimernHandler.obtainMessage(UPDATE_TIMER, left - 1), 1000);
                } else {
//                    mVerification = "";
                    ((Button) findViewById(R.id.get_verification)).setText(R.string.get_verification);
                    ((Button) findViewById(R.id.get_verification)).setEnabled(true);
                }
                break;
            default:
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initUI();
    }

    private void initUI() {
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.forget_password));
        Button btn = (Button) findViewById(R.id.register);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resetCheck()) {
                    doReset();
                }
            }
        });
        btn = (Button) findViewById(R.id.get_verification);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetVerification();
            }
        });

        EditText et = (EditText) findViewById(R.id.edit_text_phone_number);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mAccount = arg0.toString().trim();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        et = (EditText) findViewById(R.id.edit_text_verification);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mVerification = arg0.toString().trim();
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
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private boolean resetCheck() {
        if (TextUtils.isEmpty(mAccount) || !AppUtils.checkAccount(mAccount)) {
            CustomDialog.showMessageDialog(ResetPasswordActivity.this, R.string.account_warning);
            return false;
        } else if (TextUtils.isEmpty(mVerification)) {
            CustomDialog.showMessageDialog(ResetPasswordActivity.this, R.string.verification_warning);
            return false;
        } else if (!mVerification.equals(mRealVerification)) {
            CustomDialog.showMessageDialog(ResetPasswordActivity.this, "验证码输入错误");
            return false;
        } else if (TextUtils.isEmpty(mPassword)) {
            CustomDialog.showMessageDialog(ResetPasswordActivity.this, R.string.password_warning);
            return false;
        }
        return true;
    }

    private void doReset() {
        String url = Constants.URL_RESET_PASSWORD + mAccount + "/" + mPassword;
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
                            Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            ResetPasswordActivity.this.startActivity(intent);
                            ResetPasswordActivity.this.finish();
                        } else {
                            CustomDialog.showMessageDialog(ResetPasswordActivity.this, "服务器返回数据错误");
                        }
                    }
                }, "正在重置");
    }

    private void doGetVerification() {
        if (!AppUtils.checkAccount(mAccount)) {
            CustomDialog.showMessageDialog(ResetPasswordActivity.this, "请输入正确的电话号码");
            return;
        }
        mRealVerification = AppUtils.generateVerification();
        String url = AppUtils.getVerificationUrl(mAccount, mRealVerification);
        ShortMessageApp.doOperation(ResetPasswordActivity.this, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        String result = arg0.substring(0, arg0.indexOf('/'));
                        if ("000".equals(result)) {
                            CustomDialog.showMessageDialog(ResetPasswordActivity.this, "请求发送成功");
                            ((Button) findViewById(R.id.get_verification)).setEnabled(false);
                            mTimernHandler.sendMessageDelayed(
                                    mTimernHandler.obtainMessage(UPDATE_TIMER, VERIFICATION_WAIT_TIME), 1000);
                        } else {
                            CustomDialog.showMessageDialog(ResetPasswordActivity.this, "请求发送失败");
                        }
                    }
                }, "正在获取验证码");
    }
}
