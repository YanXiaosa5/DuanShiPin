package com.jumang.shortvideo.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.SApplication;
import com.jumang.shortvideo.adapter.MainVideoAdapter;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.api.ResultCallback;
import com.jumang.shortvideo.base.BaseFragment;
import com.jumang.shortvideo.bean.HomeVideoBean;
import com.jumang.shortvideo.data.AppInfo;
import com.jumang.shortvideo.utils.DownloadUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static com.umeng.socialize.shareboard.widgets.SocializeViewPager.SCROLL_STATE_DRAGGING;
import static com.umeng.socialize.shareboard.widgets.SocializeViewPager.SCROLL_STATE_SETTLING;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment {

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
     * 视频数据
     */
    List<HomeVideoBean> videoBeans = new ArrayList<>();

    //    Items items;
//    MultiTypeAdapter adapter;
    /**
     * 记录滑动时第一条，最后一条，和可视条目数量
     */
    public int firstVisibleItem, lastVisibleItem, visibleCount;

    /**
     * 布局管理器
     */
    LinearLayoutManager layoutManager;

    /**
     * 适配器
     */
    MainVideoAdapter adapter;

    /**
     * 当前页码
     */
    private int currentPage = 1;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainVideoAdapter(this.getActivity(), videoBeans);
        recyclerView.setAdapter(adapter);
        bindData(false);
        return view;
    }

    /**
     * 请求数据
     * @param isLoadMore
     */
    private void bindData(boolean isLoadMore) {

        Api.getHomeList(AppInfo.deviceId, currentPage, 10,
                new ResultCallback<List<HomeVideoBean>>(this.getContext()) {
                    @Override
                    public void onSuccessString(String data, String message, int code) {
                        super.onSuccessString(data, message, code);
                    }

                    @Override
                    public void onSuccess(List<HomeVideoBean> response) {
                        super.onSuccess(response);
                        if (response != null && response.size() > 0) {

                            //=========  燕潇洒修改2018/9/26 14:29
                            if(currentPage == 1 && videoBeans != null){//如果不是加载更多
                                videoBeans.clear();//在请求成功之后清空原来的数据,添加新的数据
                            }
                            //=========
                            videoBeans.addAll(response);
                            if (isLoadMore) {
                                System.out.println("加载完成");
                                adapter.loadMoreComplete();
                            }
                        } else {
                            adapter.loadMoreEnd();
                        }
                        refresh.setRefreshing(false);

                        adapter.disableLoadMoreIfNotFullPage();
                    }

                    @Override
                    public void onFailure(String strMsg) {
                        super.onFailure(strMsg);
                    }
                });
    }

    /**
     * 列表滑动监听,刷新监听,加载监听
     */
    public void setListener() {
        refresh.setOnRefreshListener(() -> {
            currentPage = 1;
//            videoBeans.clear();
            bindData(false);
        });

        adapter.setOnLoadMoreListener(() -> {
            currentPage += 1;
            bindData(true);
        }, recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean scrollState = false;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE: //滚动停止
                        scrollState = false;
                        if (AppInfo.isWifi) {
                            autoPlayVideo();
                        }
                        break;
                    case SCROLL_STATE_DRAGGING: //手指拖动
                        scrollState = true;
                        break;
                    case SCROLL_STATE_SETTLING: //惯性滚动
                        scrollState = true;
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;

                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(MainVideoAdapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
                        GSYVideoManager.onPause();
                    }
                }
            }

        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            HomeVideoBean videoBean = videoBeans.get(position);
            if (videoBean.getAd() == 1) {
                if (videoBean.getRedirectUrl() != null && !videoBean.getRedirectUrl().trim().equals("")) {
                    startWebView(videoBean.getRedirectUrl(), videoBean.getAdName(), videoBean.getId());
                    Api.adTrack(videoBean.getId(), 3, null);
                } else if (videoBean.getDownloadUrl() != null) {
                    mainDowload();
                    DownloadUtil.startApkDownLoad(videoBean.getDownloadUrl(),
                            new Date().getTime() + ".apk", MainFragment.this.getActivity());
                    Api.adTrack(videoBean.getId(), 2, null);
                }

            }
        });
    }

    /**
     * 主页下载
     */
    public void mainDowload(){
        MobclickAgent.onEvent(SApplication.getInstance(),"mainDowload");
    }

    /**
     * 滑动时自动播放视频
     */
    private void autoPlayVideo() {
        for (int i = 0; i < visibleCount; i++) {
            if (layoutManager != null && layoutManager.getChildAt(i) != null &&
                    Objects.requireNonNull(layoutManager.getChildAt(i)).findViewById(R.id.detail_player) != null) {
                StandardGSYVideoPlayer homeGSYVideoPlayer = Objects.requireNonNull(layoutManager.getChildAt(i)).
                        findViewById(R.id.detail_player);
                Rect rect = new Rect();
                homeGSYVideoPlayer.getLocalVisibleRect(rect);
                int videoHeight = homeGSYVideoPlayer.getHeight();
                if (rect.top == 0 && rect.bottom == videoHeight) {
                    if (homeGSYVideoPlayer.getCurrentState() == StandardGSYVideoPlayer.CURRENT_STATE_NORMAL
                            || homeGSYVideoPlayer.getCurrentState() == StandardGSYVideoPlayer.CURRENT_STATE_ERROR) {
                        homeGSYVideoPlayer.getStartButton().performClick();
                    }
                    return;
                }
            }
        }
        GSYVideoManager.releaseAllVideos();
    }

    private String TAG = MainFragment.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        GSYVideoManager.onResume();
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((view, i, keyEvent) -> keyEvent.getAction() == KeyEvent.ACTION_DOWN
                && i == KeyEvent.KEYCODE_BACK &&
                GSYVideoManager.backFromWindowFull(MainFragment.this.getContext()));
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        GSYVideoManager.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            //对用户可见
            mainFragmentVisible();
        }
    }

    public void mainFragmentVisible(){
        MobclickAgent.onEvent(SApplication.getInstance(),"mainFragmentVisible");
    }

}
