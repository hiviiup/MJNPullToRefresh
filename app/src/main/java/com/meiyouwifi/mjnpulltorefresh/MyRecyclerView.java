package com.meiyouwifi.mjnpulltorefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * Created by hiviiup on 2017/1/17.
 */

public class MyRecyclerView extends RecyclerView
{
    public MyRecyclerView(Context context)
    {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    float downX = 0;
    float downY = 0;


    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        return super.onTouchEvent(e);
    }


}
