package com.boyi.shortmessage.adapter;

import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ColumnListAdapter extends BaseAdapter {

    private Context mContext;

    public ColumnListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return ShortMessageApp.mColumns.size();
    }

    @Override
    public Object getItem(int position) {
        return ShortMessageApp.mColumns.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({ "ViewHolder", "InflateParams" })
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.column_item, null);
        }
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.item_checkbox);
        cb.setText(ShortMessageApp.mColumns.get(position).Column_name);
        cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ShortMessageApp.mColumns.get(position).isChecked = isChecked;
            }
        });
        return convertView;
    }
}
