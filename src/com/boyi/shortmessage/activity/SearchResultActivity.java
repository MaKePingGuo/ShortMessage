package com.boyi.shortmessage.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.android.volley.Response.Listener;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.boyi.shortmessage.adapter.SearchResultListAdapter;
import com.boyi.shortmessage.model.QuickSearchResult;
import com.boyi.shortmessage.model.SearchResultItem;
import com.boyi.shortmessage.widget.CustomDialog;
import com.boyi.shortmessage.widget.XListView;
import com.boyi.shortmessage.widget.XListView.IXListViewListener;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchResultActivity extends Activity implements IXListViewListener {

    public static QuickSearchResult mResultList;
    private SearchResultListAdapter mAdapter;
    private int mPageIndex;
    private XListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        initUI();
    }

    private void initUI() {
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchResultActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.search_title));

        mAdapter = new SearchResultListAdapter(this, mResultList.SearchResult);
        mListView = (XListView) findViewById(R.id.search_result);
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);

        EditText et = (EditText) findViewById(R.id.edit_text_search);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = arg0.toString().trim();
                mAdapter.updateList(getSearchResultList(text));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    private ArrayList<SearchResultItem> getSearchResultList(String text) {
        ArrayList<SearchResultItem> list = new ArrayList<SearchResultItem>();
        for (int i = 0; i < mResultList.SearchResult.size(); i++) {
            SearchResultItem item = mResultList.SearchResult.get(i);
            if (item.Content.contains(text)) {
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public void onRefresh() {
        doRefresh();
    }

    @Override
    public void onLoadMore() {
        doLoadMore();
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        mListView.setRefreshTime(time);
    }

    private void doRefresh() {
        if (!TextUtils.isEmpty(QuickSearchActivity.mSearchText)) {
            String url = Constants.URL_SEARCH_MESSAGE +
                    URLEncoder.encode(QuickSearchActivity.mSearchText) + "/" + 0;
            ShortMessageApp.doOperation(SearchResultActivity.this, url,
                    new Listener<String>() {
                        @Override
                        public void onResponse(String arg0) {
                            try {
                                SearchResultActivity.mResultList =
                                        Constants.mGson.fromJson(arg0, QuickSearchResult.class);
                            } catch (Exception e) {
                                
                            }
                            if (SearchResultActivity.mResultList == null) {
                                CustomDialog.showMessageDialog(SearchResultActivity.this, "服务器返回数据错误");
                            } else if (SearchResultActivity.mResultList.SearchResult.size() == 0) {
                                CustomDialog.showMessageDialog(SearchResultActivity.this, "搜索结果为空");
                            } else {
                                mAdapter.updateList(SearchResultActivity.mResultList.SearchResult);
                            }
                            onLoad();
                        }
                    }, "正在搜索");
        }
    }

    private void doLoadMore() {
        if (!TextUtils.isEmpty(QuickSearchActivity.mSearchText)) {
            String url = Constants.URL_SEARCH_MESSAGE +
                    URLEncoder.encode(QuickSearchActivity.mSearchText) + "/" + ++mPageIndex;
            ShortMessageApp.doOperation(SearchResultActivity.this, url,
                    new Listener<String>() {
                        @Override
                        public void onResponse(String arg0) {
                            QuickSearchResult resultlist = null;
                            try {
                                resultlist =
                                        Constants.mGson.fromJson(arg0, QuickSearchResult.class);
                            } catch (Exception e) {
                                
                            }
                            if (resultlist == null) {
                                CustomDialog.showMessageDialog(SearchResultActivity.this, "服务器返回数据错误");
                            } else if (resultlist.SearchResult.size() == 0) {
                                Toast.makeText(SearchResultActivity.this, "已经全部加载", Toast.LENGTH_SHORT).show();
                            } else {
                                SearchResultActivity.mResultList.addResultList(resultlist.SearchResult);
                                mAdapter.updateList(SearchResultActivity.mResultList.SearchResult);
                            }
                            onLoad();
                        }
                    }, "正在搜索");
        }
    }
}
