package com.jumang.shortvideo.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.SApplication;
import com.jumang.shortvideo.adapter.RecommendAdapter;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.api.ResultCallback;
import com.jumang.shortvideo.base.BaseFragment;
import com.jumang.shortvideo.bean.HomeVideoBean;
import com.jumang.shortvideo.data.AppInfo;
import com.jumang.shortvideo.utils.DownloadUtil;
import com.jumang.shortvideo.views.viewpager.ViewPagerLayoutManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * 推荐
 */
public class RecommendFragment extends BaseFragment {

    /**
     * 视频列表
     */
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    /**
     * 刷新
     */
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    /**
     * 推荐数据适配器
     */
    private RecommendAdapter recommendAdapter;

    /**
     * viewpager布局管理器
     */
    private ViewPagerLayoutManager mLayoutManager;
    private int currentPage = 1;
    List<HomeVideoBean> videoBeans = new ArrayList<>();

    public RecommendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this, view);
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

        mLayoutManager = new ViewPagerLayoutManager(this.getContext(), OrientationHelper.VERTICAL);
        recommendAdapter = new RecommendAdapter(RecommendFragment.this.getActivity(), videoBeans);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recommendAdapter);
        bindData();
        return view;
    }

    private void bindData() {
        Api.getRecommendList(AppInfo.deviceId, currentPage, 10,
                new ResultCallback<List<HomeVideoBean>>(this.getContext()) {

                    @Override
                    public void onSuccessString(String data, String message, int code) {
                        super.onSuccessString(data, message, code);
                    }

                    @Override
                    public void onSuccess(List<HomeVideoBean> response) {
                        super.onSuccess(response);
                        if (response != null && response.size() > 0) {
                            videoBeans.addAll(response);
                            recommendAdapter.loadMoreComplete();
                        } else {
                            recommendAdapter.loadMoreEnd();
                        }
                        refresh.setRefreshing(false);
                    }
                });
    }

    /**
     * 添加监听
     */
    public void setListener() {
        //加载
        recommendAdapter.setOnLoadMoreListener(() -> {
            currentPage += 1;
            bindData();
        }, recyclerView);

        //刷新
        refresh.setOnRefreshListener(() -> {
            currentPage = 1;
            videoBeans.clear();
            bindData();
        });

        //滑动监听
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
                        default:break;
                }
            }

        });

        recommendAdapter.setOnItemClickListener((adapter, view, position) -> {
            HomeVideoBean videoBean = videoBeans.get(position);
            if (videoBean.getAd() == 1) {
                if (videoBean.getRedirectUrl() != null && !videoBean.getRedirectUrl().trim().equals("")) {
                    startWebView(videoBean.getRedirectUrl(), videoBean.getAdName(), videoBean.getId());
                    Api.adTrack(videoBean.getId(), 3, null);
                } else if (videoBean.getDownloadUrl() != null) {
                    DownloadUtil.startApkDownLoad(videoBean.getDownloadUrl(),
                            new Date().getTime() + ".apk", RecommendFragment.this.getActivity());
                    Api.adTrack(videoBean.getId(), 2, null);
                    recommendDowload();
                }
            }
        });
    }

    /**
     * 推荐页面下载量
     */
    public void recommendDowload(){
        MobclickAgent.onEvent(SApplication.getInstance(),"recommendDowload");
    }

    /**
     * 自动播放视频
     */
    private void autoPlayVideo() {
        if (mLayoutManager != null && mLayoutManager.getChildAt(0) != null &&
                Objects.requireNonNull(mLayoutManager.getChildAt(0)).findViewById(R.id.video_view) != null) {
            StandardGSYVideoPlayer homeGSYVideoPlayer = Objects.requireNonNull(mLayoutManager.getChildAt(0)).
                    findViewById(R.id.video_view);
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

            GSYVideoManager.releaseAllVideos();
        }
    }

    private String TAG = RecommendFragment.class.getSimpleName();

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        GSYVideoManager.onResume();
        Objects.requireNonNull(getView()).setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((view, i, keyEvent) -> keyEvent.getAction() == KeyEvent.ACTION_DOWN
                && i == KeyEvent.KEYCODE_BACK &&
                GSYVideoManager.backFromWindowFull(RecommendFragment.this.getContext()));
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
            recommendFragmentVisible();
        }
    }

    public void recommendFragmentVisible(){
        MobclickAgent.onEvent(SApplication.getInstance(),"recommendFragmentVisible");
    }
}
