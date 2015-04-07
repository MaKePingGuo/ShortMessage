package com.boyi.shortmessage.adapter;

import com.android.volley.Response.Listener;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.zcw.togglebutton.ToggleButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClassifyListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ToggleButton mToggleButton;

    public ClassifyListAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return ShortMessageApp.mClassifies.size();
    }

    @Override
    public Object getItem(int position) {
        return ShortMessageApp.mClassifies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({ "ViewHolder", "InflateParams" })
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.classify_item, null);
        TextView tv = (TextView) view.findViewById(R.id.item_text);
        tv.setText(ShortMessageApp.mClassifies.get(position).Classify_name);
        final ToggleButton tb = (ToggleButton) view.findViewById(R.id.item_button);
        if (ShortMessageApp.mClassifies.get(position).IsSelected.equals("1")) {
            tb.setToggleOn();
        } else {
            tb.setToggleOff();
        }
        tb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mToggleButton = tb;
                doModify(tb, position);
            }
        });
    
        return view;
    }

    private void doModify(final ToggleButton tb, final int index) {
        final String status = ShortMessageApp.mClassifies.get(index).IsSelected.equals("1") ? "0" : "1";
        String url = Constants.URL_UPDATE_SETTINGS + ShortMessageApp.mUser.User_id + "/"
                + ShortMessageApp.mClassifies.get(index).Classify_id + "/" + status;
        ShortMessageApp.doOperation(mActivity, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        ShortMessageApp.mClassifies.get(index).IsSelected = status;
                        if (status.equals("1")) {
                            mToggleButton.toggleOn();
                        } else {
                            mToggleButton.toggleOff();
                        }
                    }
                }, "正在修改");
    }
}
