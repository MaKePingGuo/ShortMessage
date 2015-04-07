package com.boyi.shortmessage.adapter;

import java.util.ArrayList;

import com.boyi.shortmessage.R;
import com.boyi.shortmessage.model.SearchResultItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchResultListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SearchResultItem> mList;

    public SearchResultListAdapter(Context context, ArrayList<SearchResultItem> list) {
        mContext = context;
        if (list == null) {
            mList = new ArrayList<SearchResultItem>();
        } else {
            mList = list;
        }
    }

    public void updateList(ArrayList<SearchResultItem> list) {
        if (list == null) {
            mList = new ArrayList<SearchResultItem>();
        } else {
            mList = list;
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_list_item, null);
        TextView tv = (TextView) view.findViewById(R.id.content);
        tv.setText(mList.get(position).Content);
        tv = (TextView) view.findViewById(R.id.time);
        tv.setText(mList.get(position).Edit_time);
        return view;
    }

}
