package com.jumang.shortvideo.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.SApplication;
import com.jumang.shortvideo.activity.VideoDetailActivity;
import com.jumang.shortvideo.adapter.HistoryAdapter;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.api.ResultCallback;
import com.jumang.shortvideo.base.BaseFragment;
import com.jumang.shortvideo.bean.HistoryBean;
import com.jumang.shortvideo.bean.HomeVideoBean;
import com.jumang.shortvideo.data.AppInfo;
import com.jumang.shortvideo.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {

    /**
     * 视频列表
     */
    @BindView(R.id.video_list)
    RecyclerView recyclerView;

    /**
     * 刷新
     */
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    /**
     * 历史数据列表
     */
    List<HistoryBean> videoBeans = new ArrayList<>();

    /**
     * 布局管理器
     */
    LinearLayoutManager layoutManager;

    /**
     * 历史数据适配器
     */
    HistoryAdapter adapter;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HistoryAdapter(this.getContext(), videoBeans);
        adapter.setPreLoadNumber(2);
        adapter.setEmptyView(inflater.inflate(R.layout.layout_no_network, null));
        recyclerView.setAdapter(adapter);
        bindData();

        return view;
    }

    /**
     * 请求数据
     */
    public void bindData() {
        videoBeans.clear();
        Api.getHistoryList(AppInfo.deviceId, 1, 100,
                new ResultCallback<List<HistoryBean>>(this.getContext()) {
                    @Override
                    public void onSuccess(List<HistoryBean> response) {
                        super.onSuccess(response);
                        if (response != null && response.size() > 0) {
                            setData(response);
                        }
                        refresh.setRefreshing(false);
                        adapter.setNewData(videoBeans);
                    }
                });
    }

    /**
     * 处理时间
     * @param response
     */
    private void setData(List<HistoryBean> response) {
        long today = TimeUtils.getDateStart(new Date()).getTime();
        long week = TimeUtils.getWeekStartTime();
        for (HistoryBean historyBean : response) {
            long time = Long.parseLong(historyBean.getViewTime());
            if (time >= today) {
                historyBean.setCategory("今天");
            } else if (time < today && time >= week) {
                historyBean.setCategory("本周");
            } else if (time < week) {
                historyBean.setCategory("更早");
            }
        }
        String category = "";
        for (HistoryBean historyBean : response) {
            if (!category.equals(historyBean.getCategory())) {
                category = historyBean.getCategory();
                HistoryBean title = new HistoryBean();
                title.setType(0);
                title.setCategory(category);
                videoBeans.add(title);
            }
            videoBeans.add(historyBean);
        }
    }

    /**
     * 添加监听
     */
    public void setListener() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (videoBeans.get(position).getType() == 1) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeVideoBean.class.getSimpleName(), videoBeans.get(position));
                startActivity(VideoDetailActivity.class, bundle);
            }
        });

        refresh.setOnRefreshListener(this::bindData);

        adapter.setOnLoadMoreListener(() -> adapter.loadMoreEnd(), recyclerView);
    }

    private String TAG = MineFragment.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            //对用户可见
            mineFragmentVisible();
        }
    }

    public void mineFragmentVisible(){
        MobclickAgent.onEvent(SApplication.getInstance(),"mineFragmentVisible");
    }

}
