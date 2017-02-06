package com.meiyouwifi.mjnpulltorefresh.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.meiyouwifi.mjnpulltorefresh.R;

/**
 * Created by hiviiup on 2017/1/12.
 * 加载样式管理器
 */

public class NormalRefreshViewManager extends BaseRefreshViewManager
{
    private TextView tv;

    public NormalRefreshViewManager(Context context)
    {
        super(context);
    }

    @Override
    protected int getRefreshViewLayout()
    {
        return R.layout.refresh_view_normal;
    }

    @Override
    public void findChildView(View view)
    {
        if (tv == null)
            tv = (TextView) view.findViewById(R.id.tv);

    }

    public void changeToRefreshing()
    {

        tv.setText("正在刷新");
    }

    public void changeToPullOnly()
    {
        tv.setText("下拉刷新");
    }

    @Override
    public void changeToPullMore(float percent)
    {

    }

    public void changeToRelease()
    {
        tv.setText("释放刷新");
    }
}
