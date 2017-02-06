package com.meiyouwifi.mjnpulltorefresh;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.meiyouwifi.mjnpulltorefresh.view.MeiTuanRefreshViewManager;
import com.meiyouwifi.mjnpulltorefresh.view.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RefreshLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        layout = (RefreshLayout) findViewById(R.id.refresh_layout);
        layout.setManager(new MeiTuanRefreshViewManager(this));
        layout.setOnRefreshListener(new RefreshLayout.OnRefrehsListener()
        {
            @Override
            public void onRefresh()
            {
                handler.sendEmptyMessageDelayed(0, 800);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++)
        {
            datas.add("条目" + i);
        }
        recyclerView.setAdapter(new MyAdapter(this, datas));
    }

    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            layout.setRefreshFinish(true);
            return true;
        }
    });

}
