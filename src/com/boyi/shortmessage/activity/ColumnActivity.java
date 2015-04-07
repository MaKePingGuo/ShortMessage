package com.boyi.shortmessage.activity;

import com.android.volley.Response.Listener;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.boyi.shortmessage.adapter.ColumnListAdapter;
import com.boyi.shortmessage.model.UserAndClassifies;
import com.boyi.shortmessage.widget.CustomDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ColumnActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column);

        initUI();
    }

    private void initUI() {
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ColumnActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.column_title));

        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(new ColumnListAdapter(this));
        Button btn = (Button) findViewById(R.id.column_confirm);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doSelect();
            }
        });
    }

    private void doSelect() {
        String ids = "/";
        for (int i = 0; i < ShortMessageApp.mColumns.size(); i++) {
            if (ShortMessageApp.mColumns.get(i).isChecked) {
                ids += ShortMessageApp.mColumns.get(i).Column_id + "-";
            }
        }
        String url = Constants.URL_SELECTED_COLUMNS + ShortMessageApp.mUser.User_id + ids;
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
                            Intent intent = new Intent(ColumnActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            ColumnActivity.this.startActivity(intent);
                            ColumnActivity.this.finish();
                        } else {
                            CustomDialog.showMessageDialog(ColumnActivity.this, "服务器返回数据错误");
                        }
                    }
                }, "正在确认");
    }
}
