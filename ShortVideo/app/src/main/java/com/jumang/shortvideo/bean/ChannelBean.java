package com.jumang.shortvideo.bean;

import java.io.Serializable;
import java.util.List;

public class ChannelBean implements Serializable {
    private String channelName;
    private long id;
    private int sortNum;
    private List<HomeVideoBean> videos;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public List<HomeVideoBean> getVideos() {
        return videos;
    }

    public void setVideos(List<HomeVideoBean> videos) {
        this.videos = videos;
    }

}
