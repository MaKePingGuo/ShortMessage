package com.boyi.shortmessage.widget;

import com.boyi.shortmessage.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class XListViewFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context mContext;

    private View mContentView;
    private View mProgressBar;
    private TextView mHintView;
    private ListView mParentList;

    public XListViewFooter(Context context, ListView parentList) {
        super(context);
        initView(context);
        mParentList = parentList;
    }

    public XListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void updateTotalCount() {
        if (mParentList.getAdapter() != null
                && mParentList.getAdapter().getCount()
                        - mParentList.getFooterViewsCount()
                        - mParentList.getHeaderViewsCount() > 0) {
            int count = mParentList.getAdapter().getCount()
                    - mParentList.getFooterViewsCount()
                    - mParentList.getHeaderViewsCount();
            mHintView.setText(
            // mContext
            // .getString(R.string.xlistview_footer_hint_normal)
            // + " ("+
                    getResources().getString(R.string.loaded) + ": "
                            + String.valueOf(count)
                    // + ")"
                    );
        } else if (mParentList.getAdapter() != null
                && mParentList.getAdapter().getCount()
                        - mParentList.getFooterViewsCount()
                        - mParentList.getHeaderViewsCount() <= 0) {
            mHintView.setText(
            // mContext
            // .getString(R.string.xlistview_footer_hint_normal)
            // + " ("+
                    getResources().getString(R.string.loaded) + ": "
                            + String.valueOf(0)
                    // + ")"
                    );
        } else {
            mHintView.setText("上拉加载更多");
        }
    }

    public void setState(int state) {
        mHintView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mHintView.setVisibility(View.INVISIBLE);
        if (state == STATE_READY) {
            mHintView.setVisibility(View.VISIBLE);
            mHintView.setText("上拉加载更多");
        } else if (state == STATE_LOADING) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mHintView.setVisibility(View.VISIBLE);
            // mHintView.setText(R.string.xlistview_footer_hint_normal
            // + R.string.xlistview_footer_hint_ready_kuo_left
            // + SolutionActivity.listCount
            // + R.string.xlistview_footer_hint_ready_kuo_right);

            updateTotalCount();
        }
    }

    public void setBottomMargin(int height) {
        if (height < 0)
            return;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
                .getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.xlistview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.xlistview_footer_content);
        mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
        mHintView = (TextView) moreView
                .findViewById(R.id.xlistview_footer_hint_textview);
    }

}
