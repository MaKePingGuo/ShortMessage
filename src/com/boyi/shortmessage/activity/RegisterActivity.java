package com.boyi.shortmessage.activity;

import com.android.volley.Response.Listener;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
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

public class RegisterActivity extends Activity {

    private String mAccount = "";
    private String mVerification = "";
    private String mRealVerification = "";
    private String mPassword = "";

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
        setContentView(R.layout.activity_register);

        initUI();
    }

    private void initUI() {
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.register));
        Button bt = (Button) findViewById(R.id.register);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerCheck()) {
                    doRegister();
                }
            }
        });

        EditText et = (EditText) findViewById(R.id.edit_text_phone_number);
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

        et = (EditText) findViewById(R.id.edit_text_verification);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mVerification = arg0.toString().trim();
                if (TextUtils.isEmpty(mVerification)) {
                    ((ImageView) findViewById(R.id.verification_icon)).setImageResource(
                            R.drawable.icon_verification_empty);
                } else {
                    ((ImageView) findViewById(R.id.verification_icon)).setImageResource(
                            R.drawable.icon_verification_not_empty);
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
        Button btn = (Button) findViewById(R.id.get_verification);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetVerification();
            }
        });
    }

    private void doGetVerification() {
        if (!AppUtils.checkAccount(mAccount)) {
            CustomDialog.showMessageDialog(RegisterActivity.this, "请输入正确的电话号码");
            return;
        }
        mRealVerification = AppUtils.generateVerification();
        String url = AppUtils.getVerificationUrl(mAccount, mRealVerification);
        ShortMessageApp.doOperation(RegisterActivity.this, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        String result = arg0.substring(0, arg0.indexOf('/'));
                        if ("000".equals(result)) {
                            CustomDialog.showMessageDialog(RegisterActivity.this, "请求发送成功");
                            ((Button) findViewById(R.id.get_verification)).setEnabled(false);
                            mTimernHandler.sendMessageDelayed(
                                    mTimernHandler.obtainMessage(UPDATE_TIMER, VERIFICATION_WAIT_TIME), 1000);
                        } else {
                            CustomDialog.showMessageDialog(RegisterActivity.this, "请求发送失败");
                        }
                    }
                }, "正在获取验证码");
    }

    private boolean registerCheck() {
        if (TextUtils.isEmpty(mAccount) || !AppUtils.checkAccount(mAccount)) {
            CustomDialog.showMessageDialog(RegisterActivity.this, R.string.account_warning);
            return false;
        } else if (TextUtils.isEmpty(mVerification)) {
            CustomDialog.showMessageDialog(RegisterActivity.this, R.string.verification_warning);
            return false;
        } else if (!mVerification.equals(mRealVerification)) {
            CustomDialog.showMessageDialog(RegisterActivity.this, "验证码输入错误");
            return false;
        } else if (TextUtils.isEmpty(mPassword)) {
            CustomDialog.showMessageDialog(RegisterActivity.this, R.string.password_warning);
            return false;
        }
        return true;
    }

    private void doRegister() {
        String url = Constants.URL_REGISTER + mAccount + "/" + mPassword;
        ShortMessageApp.doOperation(this, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        try {
                            UserAndColumns uac = Constants.mGson.fromJson(arg0, UserAndColumns.class);
                            ShortMessageApp.mUser = uac.Appuser;
                            ShortMessageApp.mColumns = uac.Columns;
                        } catch (Exception e) {
                        }
                        if (ShortMessageApp.mUser != null) {
                            Intent intent = new Intent(RegisterActivity.this, ColumnActivity.class);
                            RegisterActivity.this.startActivity(intent);
                            RegisterActivity.this.finish();
                        } else {
                            CustomDialog.showMessageDialog(RegisterActivity.this, "服务器返回数据错误");
                        }
                    }
                }, "正在注册");
    }
}
