package com.meiyouwifi.mjnpulltorefresh.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by hiviiup on 2017/1/12.
 * 加载样式管理器
 */

public abstract class BaseRefreshViewManager
{
    private Context context;
    private View mRefreshContentView;
    private TextView tv;

    public BaseRefreshViewManager(Context context)
    {
        this.context = context;
    }

    public View getRefreshContentView()
    {
        if (mRefreshContentView == null)
        {
            mRefreshContentView = View.inflate(context, getRefreshViewLayout(), null);
            mRefreshContentView.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
        findChildView(mRefreshContentView);
        return mRefreshContentView;
    }

    protected abstract int getRefreshViewLayout();

    public abstract void findChildView(View view);

    public int getRefreshContentViewHeight()
    {
        mRefreshContentView.measure(0, 0);
        return mRefreshContentView.getMeasuredHeight();
    }

    public abstract void changeToRefreshing();

    /**
     * 将控件状态改变为下拉刷新,改变状态之后仅调用一次
     * 此方法用于控件视图设置文字,图片等不随着移动而改变的固定内容,以便减少内存的消耗
     */
    public abstract void changeToPullOnly();

    /**
     * 将控件状态改变为下拉刷新,改变状态之后依旧会随着手指一动进行调用
     * 此方法期望用于给控件视图在下拉刷新状态下设置动画
     * @param percent
     */
    public abstract void changeToPullMore(float percent);

    public abstract void changeToRelease();

}
