package com.jumang.shortvideo.base;

public interface  NetStateChangeObserver {

    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);
}
