package com.meiyouwifi.mjnpulltorefresh.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.meiyouwifi.mjnpulltorefresh.R;

/**
 * Created by hiviiup on 2017/1/18.
 */

public class MeiTuanRefreshViewManager extends BaseRefreshViewManager
{

    private ImageView ivPull;
    private ImageView ivRefresh;
    private AnimationDrawable mAnimationDrawable;

    public MeiTuanRefreshViewManager(Context context)
    {
        super(context);
    }

    @Override
    protected int getRefreshViewLayout()
    {
        return R.layout.refresh_view_meituan;
    }

    @Override
    public void findChildView(View view)
    {
        ivPull = (ImageView) view.findViewById(R.id.iv_pull);
        ivRefresh = (ImageView) view.findViewById(R.id.iv_refresh);
        ivRefresh.setBackgroundResource(R.drawable.refresh_meituan_frame);
    }

    @Override
    public void changeToRefreshing()
    {}

    @Override
    public void changeToPullOnly()
    {

    }

    @Override
    public void changeToPullMore(float percent)
    {
        if (mAnimationDrawable != null)
        {
            mAnimationDrawable.stop();
        }
        ivRefresh.setVisibility(View.INVISIBLE);
        ivPull.setVisibility(View.VISIBLE);
        ivPull.setScaleX(percent);
        ivPull.setScaleY(percent);
    }

    @Override
    public void changeToRelease()
    {
        ivRefresh.setVisibility(View.VISIBLE);
        ivPull.setVisibility(View.INVISIBLE);

        mAnimationDrawable = mAnimationDrawable == null ?
                (AnimationDrawable) ivRefresh.getBackground() : mAnimationDrawable;
        mAnimationDrawable.start();
    }
}
