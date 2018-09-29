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
import android.widget.Toast;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.SApplication;
import com.jumang.shortvideo.activity.ChannelDetailActivity;
import com.jumang.shortvideo.adapter.ChannelAdapter;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.api.ResultCallback;
import com.jumang.shortvideo.base.BaseFragment;
import com.jumang.shortvideo.bean.ChannelBean;
import com.jumang.shortvideo.data.AppInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelFragment extends BaseFragment {

    /**
     * 视频列表
     */
    @BindView(R.id.video_list)
    RecyclerView recyclerView;

    /**
     * 下拉刷新
     */
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    /**
     * 频道列表
     */
    List<ChannelBean> channelBeans = new ArrayList<>();

    /**
     * 布局管理器
     */
    LinearLayoutManager layoutManager;

    /**
     * 适配器
     */
    ChannelAdapter adapter;

    /**
     * 当前页码
     */
    private int currentPage = 1;

    public ChannelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChannelAdapter(this.getActivity(), channelBeans);
        adapter.setPreLoadNumber(2);
        recyclerView.setAdapter(adapter);
        adapter.setHeaderView(inflater.inflate(R.layout.channel_header, null));
        bindData();

        return view;
    }

    /**
     * 请求数据并绑定
     */
    private void bindData() {
        Api.getChannelList(AppInfo.deviceId, currentPage, 10,
                new ResultCallback<List<ChannelBean>>(this.getContext()) {
                    @Override
                    public void onSuccessString(String data, String message, int code) {
                        super.onSuccessString(data, message, code);
                    }

                    @Override
                    public void onSuccess(List<ChannelBean> response) {
                        super.onSuccess(response);
                        if (response != null && response.size() > 0) {

                            //=========  燕潇洒修改2018/9/28 09:04
                            if(currentPage == 1 && channelBeans != null){//如果不是加载更多
                                channelBeans.clear();//在请求成功之后清空原来的数据,添加新的数据
                            }
                            //=========

                            channelBeans.addAll(response);
                            adapter.loadMoreComplete();
                        } else {
                            adapter.loadMoreEnd();
                        }
                        refresh.setRefreshing(false);
                    }
                });
    }

    /**
     * 事件监听
     */
    public void setListener() {
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ChannelBean.class.getSimpleName(), channelBeans.get(position));
            startActivity(ChannelDetailActivity.class, bundle);
        });

        //刷新监听
        refresh.setOnRefreshListener(() -> {
            currentPage = 1;
//            channelBeans.clear();
            bindData();
        });

        //加载监听
        adapter.setOnLoadMoreListener(() -> {
            currentPage += 1;
            bindData();
        }, recyclerView);
    }

    private String TAG = ChannelFragment.class.getSimpleName();

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
            channelFragmentVisible();
        }
    }

    public void channelFragmentVisible(){
        MobclickAgent.onEvent(SApplication.getInstance(),"channelFragmentVisible");
    }
}
