package com.meiyouwifi.mjnpulltorefresh.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static android.widget.LinearLayout.LayoutParams.*;

/**
 * Created by hiviiup on 2017/1/12.
 */

public class RefreshLayout extends LinearLayout
{

    private LinearLayout mRefreshView;
    private float refreshViewDistance = 0;
    private View mRefreshContentView;

    private RefreshStatus mRefreshStatus = RefreshStatus.IDLE;
    private boolean isRefreshFinish;

    public enum RefreshStatus
    {
        IDLE/*空闲状态*/, PULL/*下拉刷新*/, RELEASE/*释放刷新*/, REFRESHING/*正在刷新*/,
    }

    private OnRefrehsListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefrehsListener mOnRefreshListener)
    {
        this.mOnRefreshListener = mOnRefreshListener;
    }

    public interface OnRefrehsListener
    {
        void onRefresh();
    }

    public RefreshLayout(Context context)
    {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        this.setOrientation(VERTICAL);

        initRefreshView();
    }

    private BaseRefreshViewManager manager;

    public void setManager(BaseRefreshViewManager manager)
    {
        this.manager = manager;
        initSelfView();
    }

    private void initSelfView()
    {
        mRefreshContentView = manager.getRefreshContentView();
        mRefreshContentView.measure(0, 0);
        refreshViewDistance = -manager.getRefreshContentViewHeight();
        mRefreshView.setPadding(0, (int) refreshViewDistance, 0, 0);
        mRefreshView.addView(mRefreshContentView);
    }

    private void initRefreshView()
    {
        mRefreshView = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        mRefreshView.setLayoutParams(params);
        addView(mRefreshView);
    }

    /**
     * 是否加载完毕
     *
     * @param isRefreshFinish
     */
    public void setRefreshFinish(boolean isRefreshFinish)
    {
        this.isRefreshFinish = isRefreshFinish;
        if (isRefreshFinish)
            resetRefreshStatus();
    }

    float startY = 0;

    float downX = 0;
    float downY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        //返回false,触摸事件就被子view抢了,
        //返回true,触摸事件子View就无法接收到触摸事件,
        //这里考虑什么时候需要返回false,让子控件去触发,
        //什么时候返回true,让自身拦截

        switch (ev.getAction())
        {
            case ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();

                if (mRefreshStatus == RefreshStatus.IDLE)
                {
                    mRefreshStatus = RefreshStatus.PULL;
                    manager.changeToPullOnly();
                    manager.changeToPullMore(0);
                }

                break;
            case ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();

                float dy = moveY - downY;
                boolean isVerticalScroll = Math.abs(moveX - downX) > Math.abs(moveY - downY);

                //满足三个条件拦截滑动事件
                //1. 向下滑动
                //2. 偏向于纵向滑动,角度大于45度
                //3. 子控件已经滑动到了顶部
                return dy > -2 && !isVerticalScroll
                        && RefreshScrollingUtil.isScrollToTop(getChildAt(1));
            case ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case ACTION_DOWN:
                startY = event.getRawY();
                break;
            case ACTION_MOVE:
                //判断是否是正在刷新的状态,如果是,不进行处理
                if (mRefreshStatus == RefreshStatus.REFRESHING)
                    return false;

                float moveY = event.getRawY();

                if (startY == 0)
                    startY = event.getRawY();

                refreshViewDistance = refreshViewDistance + (moveY - startY) / 2;
                refreshViewDistance = Math.max(refreshViewDistance, -manager.getRefreshContentViewHeight());
                mRefreshView.setPadding(0, (int) refreshViewDistance, 0, 0);

                if (refreshViewDistance > 0 && mRefreshStatus != RefreshStatus.RELEASE)
                {
                    mRefreshStatus = RefreshStatus.RELEASE;
                    manager.changeToRelease();
                } else if (refreshViewDistance <= 0)
                {
                    if (mRefreshStatus != RefreshStatus.PULL)
                    {
                        mRefreshStatus = RefreshStatus.PULL;
                        manager.changeToPullOnly();
                    }
                    float percent = 1 + mRefreshView.getPaddingTop() * 1.0f / manager.getRefreshContentViewHeight();
                    manager.changeToPullMore(percent);
                }
                startY = moveY;

                break;
            case ACTION_UP:
                startY = 0;
                if (mRefreshStatus == RefreshStatus.RELEASE)
                {
                    resetView(0);
                    mRefreshStatus = RefreshStatus.REFRESHING;
                    mOnRefreshListener.onRefresh();
                    manager.changeToRefreshing();
                } else if (mRefreshStatus == RefreshStatus.PULL)
                {
                    resetRefreshStatus();
                }
                break;
        }
        return true;
    }

    /**
     * 将下拉刷新的状态设置为idle
     */
    private void resetRefreshStatus()
    {
        mRefreshStatus = RefreshStatus.IDLE;
        //复位refreshcontentview
        refreshViewDistance = -manager.getRefreshContentViewHeight();
        resetView((int) refreshViewDistance);
    }

    /**
     * 通过动画重置View
     *
     * @param height
     */
    public void resetView(int height)
    {
        ValueAnimator va = ValueAnimator.ofInt(mRefreshView.getPaddingTop(), height);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                mRefreshView.setPadding(0, (int) animation.getAnimatedValue(), 0, 0);
            }
        });
        va.setDuration(300);
        va.start();
    }

}
