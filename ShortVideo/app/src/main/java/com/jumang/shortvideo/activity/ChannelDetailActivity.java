package com.jumang.shortvideo.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.adapter.ChannelDetailAdapter;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.api.ResultCallback;
import com.jumang.shortvideo.base.BaseActivity;
import com.jumang.shortvideo.bean.ChannelBean;
import com.jumang.shortvideo.bean.HomeVideoBean;
import com.jumang.shortvideo.data.AppInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelDetailActivity extends BaseActivity {
    @BindView(R.id.video_list)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.title)
    TextView title;

    ChannelBean channelBean;
    List<HomeVideoBean> videoBeans = new ArrayList<>();
    ChannelDetailAdapter detailAdapter;
    GridLayoutManager layoutManager;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isNoStatusBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_detail);
        ButterKnife.bind(this);
        channelBean = (ChannelBean) getIntent().getSerializableExtra(ChannelBean.class.getSimpleName());
        title.setText(channelBean.getChannelName());
        layoutManager = new GridLayoutManager(this, 2);
        detailAdapter = new ChannelDetailAdapter(this, videoBeans);
        detailAdapter.setPreLoadNumber(2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(detailAdapter);
        setListener();
        bindData();

    }

    /**
     * 频道视频列表进入
     */
    public void channelDetail(){
        MobclickAgent.onEvent(getApplicationContext(),"channelDetail");
    }

    private void bindData() {
        channelDetail();
        Api.getChannelDetailList(AppInfo.deviceId, currentPage, 10, channelBean.getId(),
                new ResultCallback<List<HomeVideoBean>>(this) {
                    @Override
                    public void onSuccess(List<HomeVideoBean> response) {
                        super.onSuccess(response);
                        if (response != null && response.size() > 0) {
                            videoBeans.addAll(response);
                            detailAdapter.loadMoreComplete();
                        } else {
                            detailAdapter.loadMoreEnd();
                        }
                        refresh.setRefreshing(false);
                    }
                });
    }

    public void setListener() {
        //detailAdapter.setSpanSizeLookup((gridLayoutManager, position) -> 1);
        detailAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(HomeVideoBean.class.getSimpleName(), videoBeans.get(position));
            startActivity(VideoDetailActivity.class, bundle);
        });

        refresh.setOnRefreshListener(() -> {
            currentPage = 1;
            videoBeans.clear();
            bindData();
        });

        detailAdapter.setOnLoadMoreListener(() -> {
            currentPage += 1;
            bindData();
        }, recyclerView);

        title.setOnClickListener(view -> finish());
    }

    public String TAG = ChannelDetailActivity.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }
}
