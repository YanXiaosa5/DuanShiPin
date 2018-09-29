package com.jumang.shortvideo.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jumang.shortvideo.SApplication;
import com.jumang.shortvideo.api.OkHttpUtils.OkHttpUtils;
import com.jumang.shortvideo.api.OkHttpUtils.callback.Callback;
import com.jumang.shortvideo.bean.AdTrackReqBean;
import com.jumang.shortvideo.bean.AddReqBean;
import com.jumang.shortvideo.bean.ChannelDetailReqBean;
import com.jumang.shortvideo.bean.SimpleRequestBean;
import com.jumang.shortvideo.data.AppInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * http网络访问
 * Created by steven on 2016/11/15.
 * Email-songzhonghua_1987@msn.com
 */
public class Api {
    /**
     * 用户登陆
     */
    public static void login(String userName, String pwd, Callback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userid", userName);
        params.put("pswd", pwd);
        HttpTaskUtil.doJsonTask("/login/user/login", params, callback);
    }

    /**
     * 首页列表
     */
    public static void getHomeList(String deviceId, int page, int size, Callback callback) {
        SimpleRequestBean simpleRequestBean = new SimpleRequestBean();
        simpleRequestBean.setDeviceId(deviceId);
        simpleRequestBean.setPage(page);
        simpleRequestBean.setSize(size);
        HttpTaskUtil.doJsonTask("/api/video/home/list/v1", new Gson().toJson(simpleRequestBean), callback);
    }

    /**
     * 推荐列表
     */
    public static void getRecommendList(String deviceId, int page, int size, Callback callback) {
        SimpleRequestBean simpleRequestBean = new SimpleRequestBean();
        simpleRequestBean.setDeviceId(deviceId);
        simpleRequestBean.setPage(page);
        simpleRequestBean.setSize(size);
        HttpTaskUtil.doJsonTask("/api/video/recommend/list/v1", new Gson().toJson(simpleRequestBean), callback);
    }

    /**
     * 频道列表
     */
    public static void getChannelList(String deviceId, int page, int size, Callback callback) {
        SimpleRequestBean simpleRequestBean = new SimpleRequestBean();
        simpleRequestBean.setDeviceId(deviceId);
        simpleRequestBean.setPage(page);
        simpleRequestBean.setSize(size);
        HttpTaskUtil.doJsonTask("/api/channel/list/v1", new Gson().toJson(simpleRequestBean), callback);
    }

    /**
     * 频道视频列表
     */
    public static void getChannelDetailList(String deviceId, int page, int size, long typeId, Callback callback) {
        ChannelDetailReqBean reqBean = new ChannelDetailReqBean();
        reqBean.setDeviceId(deviceId);
        reqBean.setPage(page);
        reqBean.setSize(size);
        reqBean.setVideoType(typeId);
        HttpTaskUtil.doJsonTask("/api/channel/video/list/v1", new Gson().toJson(reqBean), callback);
    }

    /**
     * Add 观看历史记录
     */
    public static void addHistory(long videoId, String timeLeft, Callback callback) {
        AddReqBean reqBean = new AddReqBean();
        reqBean.setDeviceId(AppInfo.deviceId);
        reqBean.setVideoId(videoId);
        reqBean.setTimeLeft(timeLeft);
        HttpTaskUtil.doJsonTask("/api/video/history/add/v1",
                new GsonBuilder().serializeNulls().create().toJson(reqBean), callback);
    }

    /**
     * 点赞/取消点赞
     */
    public static void praise(long videoId, Callback callback) {
        MobclickAgent.onEvent(SApplication.getInstance(),"praise");
        AddReqBean reqBean = new AddReqBean();
        reqBean.setDeviceId(AppInfo.deviceId);
        reqBean.setVideoId(videoId);
        HttpTaskUtil.doJsonTask("/api/video/praise/v1",
                new GsonBuilder().serializeNulls().create().toJson(reqBean), callback);
    }

    /**
     * 评论
     */
    public static void addComment(long videoId, Callback callback) {
        MobclickAgent.onEvent(SApplication.getInstance(),"addComment");
        AddReqBean reqBean = new AddReqBean();
        reqBean.setDeviceId(AppInfo.deviceId);
        reqBean.setVideoId(videoId);
        HttpTaskUtil.doJsonTask("/api/video/comment/v1", new Gson().toJson(reqBean), callback);
    }

    /**
     * 广告统计
     */
    public static void adTrack(long adId, int statisticsType, Callback callback) {
        AdTrackReqBean reqBean = new AdTrackReqBean();
        reqBean.setDeviceId(AppInfo.deviceId);
        reqBean.setAdId(adId);
        reqBean.setStatisticsType(statisticsType);
        HttpTaskUtil.doJsonTask("/api/ad/track/v1", new Gson().toJson(reqBean), callback);
    }

    /**
     * 历史记录列表
     */
    public static void getHistoryList(String deviceId, int page, int size, Callback callback) {
        SimpleRequestBean simpleRequestBean = new SimpleRequestBean();
        simpleRequestBean.setDeviceId(deviceId);
        simpleRequestBean.setPage(page);
        simpleRequestBean.setSize(size);
        HttpTaskUtil.doJsonTask("/api/my/history/list/v1", new Gson().toJson(simpleRequestBean), callback);
    }

    /**
     * 下载文件
     */
    public static void downloadFile(String url, Callback callback) {
        OkHttpUtils.get().url(url).build().execute(callback);
    }

    /**
     * 检查版本
     */
    public static void getLatestVersion(String versionCode, Callback callback) {
//        Map<String, String> params = new HashMap<>();
//        params.put("mpos", "apk");
//        params.put("version", versionCode);
//        HttpTaskUtil.doJsonTask("/health/app/version/get", params, callback);
    }
}