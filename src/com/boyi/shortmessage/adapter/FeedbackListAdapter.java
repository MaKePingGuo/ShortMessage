package com.boyi.shortmessage.adapter;

import java.util.ArrayList;

import com.boyi.shortmessage.R;
import com.boyi.shortmessage.model.Feedback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedbackListAdapter extends BaseAdapter {

    private ArrayList<Feedback> mList = new ArrayList<Feedback>();
    private Context mContext;

    public FeedbackListAdapter(Context context) {
        mContext = context;
    }

    public void updateList(ArrayList<Feedback> list) {
        if (list != null) {
            mList.clear();
            for (Feedback f : list) {
                if (TextUtils.isEmpty(f.Reply)) {
                    mList.add(f);
                } else {
                    Feedback b = new Feedback();
                    b.Content = "";
                    b.Mobile = new String(f.Mobile);
                    b.Reply = new String(f.Reply);
                    b.id = new String(f.id);
                    f.Reply = "";
                    mList.add(f);
                    mList.add(b);
                }
            }
        } else {
            mList = new ArrayList<Feedback>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({ "ViewHolder", "InflateParams" })
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.feedback_item, null);
        }
        TextView left = (TextView) convertView.findViewById(R.id.left);
        TextView right = (TextView) convertView.findViewById(R.id.right);
        TextView content = (TextView) convertView.findViewById(R.id.content);
        Feedback f = mList.get(position);
        if (!TextUtils.isEmpty(f.Content)) {
            left.setText("我");
            right.setText("时间");
            content.setText(f.Content);
        } else {
            right.setText("博易短讯");
            left.setText("时间");
            content.setText(f.Reply);
        }
        return convertView;
    }
}
