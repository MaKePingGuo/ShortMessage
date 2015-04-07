package com.boyi.shortmessage.activity;

import java.net.URLEncoder;

import com.android.volley.Response.Listener;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.boyi.shortmessage.adapter.FeedbackListAdapter;
import com.boyi.shortmessage.model.AddFeedbackResult;
import com.boyi.shortmessage.model.FeedbackList;
import com.boyi.shortmessage.widget.CustomDialog;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FeedbackActivity extends Activity {

    private FeedbackListAdapter mAdapter;
    private ListView mListView;
    private String mFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initUI();
        doGetFeedback();
    }

    private void initUI() {
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.feedback));

        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(new FeedbackListAdapter(this));
        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new FeedbackListAdapter(FeedbackActivity.this);
        mListView.setAdapter(mAdapter);
        EditText et = (EditText) findViewById(R.id.edit_text_feedback);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFeedback = s.toString().trim();
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
                    doAddFeedback();
                    return false;
                }
                return false;
            }
        });
        Button btn = (Button) findViewById(R.id.feedback_btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mFeedback)) {
                    doAddFeedback();
                }
            }
        });
    }

    private void doGetFeedback() {
        String url = Constants.URL_GET_FEEDBACK + ShortMessageApp.mUser.User_id;
        ShortMessageApp.doOperation(this, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        FeedbackList feedbacklist = null;
                        try {
                            feedbacklist = Constants.mGson.fromJson(arg0, FeedbackList.class);
                        } catch (Exception e) {
                        }
                        if (feedbacklist != null) {
                            mAdapter.updateList(feedbacklist.FeedbackResult);
                        } else {
                            CustomDialog.showMessageDialog(FeedbackActivity.this, "服务器返回数据错误");
                        }
                    }
                }, "正在获取");
    }

    private void doAddFeedback() {
        String url = Constants.URL_ADD_FEEDBACK + ShortMessageApp.mUser.User_id + "/" +
                URLEncoder.encode(mFeedback);
        ShortMessageApp.doOperation(this, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        AddFeedbackResult addfeedbacklist = null;
                        try {
                            addfeedbacklist = Constants.mGson.fromJson(arg0, AddFeedbackResult.class);
                        } catch (Exception e) {
                        }
                        if (addfeedbacklist != null) {
                            mAdapter.updateList(addfeedbacklist.AddResult);
                        } else {
                            CustomDialog.showMessageDialog(FeedbackActivity.this, "服务器返回数据错误");
                        }
                    
                        mFeedback = "";
                        EditText et = (EditText) findViewById(R.id.edit_text_feedback);
                        et.setText("");
                    }
                }, "正在发送");
    }
}
