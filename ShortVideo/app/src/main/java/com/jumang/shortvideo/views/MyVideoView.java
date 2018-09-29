package com.jumang.shortvideo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class MyVideoView extends StandardGSYVideoPlayer {
    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayout getTopView() {
        return (LinearLayout) mTopContainer;
    }
}
