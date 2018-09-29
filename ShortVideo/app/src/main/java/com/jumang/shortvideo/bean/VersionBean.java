package com.jumang.shortvideo.bean;

import java.io.Serializable;

public class VersionBean implements Serializable {
    private String title;//标题
    private String description;//描述
    private int version;//内部版本号
    private String versionName;//版本名称
    private String forcibly;//是否强制更新
    private String url;//更新地址

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getForcibly() {
        return forcibly;
    }

    public void setForcibly(String forcibly) {
        this.forcibly = forcibly;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
