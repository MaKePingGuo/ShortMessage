package com.boyi.shortmessage.activity;

import com.baidu.mobstat.StatService;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.adapter.ClassifyListAdapter;
import com.boyi.shortmessage.utils.AppUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ClassifyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);

        initUI();
    }

    private void initUI() {
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassifyActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.classify_title));

        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(new ClassifyListAdapter(this));
        tv = (TextView) findViewById(R.id.call_number);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showDialConfirmDialog(ClassifyActivity.this);
            }
        });
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
