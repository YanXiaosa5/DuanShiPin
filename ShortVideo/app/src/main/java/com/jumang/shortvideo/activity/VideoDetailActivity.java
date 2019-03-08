package com.jumang.shortvideo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jumang.shortvideo.R;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.base.BaseActivity;
import com.jumang.shortvideo.base.GSYVideoCallBack;
import com.jumang.shortvideo.bean.HomeVideoBean;
import com.jumang.shortvideo.utils.MUtils;
import com.jumang.shortvideo.views.CircleImageView;
import com.jumang.shortvideo.views.MyVideoView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoDetailActivity extends BaseActivity {
    private static final String TAG = VideoDetailActivity.class.getSimpleName();
    @BindView(R.id.video_view)
    MyVideoView videoPlayer;
    @BindView(R.id.iv_user_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.tv_play_count)
    TextView playCount;
    @BindView(R.id.tv_time)
    TextView time;
    @BindView(R.id.heart)
    CheckBox heart;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.share)
    TextView share;
    @BindView(R.id.back_layout)
    RelativeLayout backLayout;

    HomeVideoBean videoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        ButterKnife.bind(this);
        videoBean = (HomeVideoBean) getIntent().getSerializableExtra(HomeVideoBean.class.getSimpleName());
        if (videoBean != null) {
            play(videoBean);
        }
        MobclickAgent.onEvent(getApplicationContext(),"play");
    }

    private void play(HomeVideoBean videoBean) {
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.default_video_bg);
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this)
                .load(videoBean.getVideoImg())
                .into(imageView);
        videoPlayer.setThumbImageView(imageView);

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.INVISIBLE);
//        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置全屏按键功能
//        videoPlayer.getFullscreenButton().setOnClickListener(v ->
//                videoPlayer.startWindowFullscreen(this, false, true));
        videoPlayer.getFullscreenButton().setVisibility(View.GONE);
        //防止错位设置
        videoPlayer.setPlayTag(TAG);
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        videoPlayer.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        videoPlayer.setReleaseWhenLossAudio(false);
        //全屏动画
        videoPlayer.setShowFullAnimation(true);
        //小屏时不触摸滑动
        videoPlayer.setIsTouchWiget(false);
        //time.setText(String.format("%ss", videoBean.get()));
        playCount.setText(String.valueOf(String.format("%s次观看", MUtils.getNumWan(videoBean.getViewCount()))));

        Glide.with(this)
                .load(videoBean.getAuthorAvatar())
                .into(userAvatar);

        backLayout.bringToFront();

        videoPlayer.setUp(videoBean.getVideoUrl(), true, null,
                null, videoBean.getVideoTitle());

        /**
         * 2018/10/10  修改于燕潇洒
         */
        videoPlayer.setThumbPlay(true);//设置触摸封面播放
        videoPlayer.setLooping(true);
        videoPlayer.setVideoAllCallBack(new GSYVideoCallBack() {

            @Override
            public void onClickBlank(String url, Object... objects) {
                super.onClickBlank(url, objects);
                System.out.println(videoPlayer.getGSYVideoManager().isPlaying()+"===");
                if(videoPlayer.getGSYVideoManager().isPlaying()) {
                    videoPlayer.getGSYVideoManager().pause();
                }else{
                    videoPlayer.getGSYVideoManager().start();
                }
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }
        });


        videoPlayer.getStartButton().performClick();

        share.setOnClickListener(view -> {
            UMWeb web = new UMWeb("http://www.digitaljournal.jp/");
            web.setTitle(getString(R.string.app_name));//标题
            UMImage image = new UMImage(this, R.mipmap.ic_launcher);
            web.setThumb(image);  //缩略图
            web.setDescription("");//描述
            new ShareAction(this).withMedia(web)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                    .setCallback(null).open();
        });

        //设置点赞数量
        heart.setChecked(videoBean.getPraised() == 1);
        heart.setOnCheckedChangeListener((compoundButton, b) -> {
            Api.praise(videoBean.getId(), null);
            videoBean.setPraised(b ? 1 : 0);
            int t = 1;
            if (!b) {
                t = -1;
            }
            videoBean.setPraiseCount(videoBean.getPraiseCount() + t);
            heart.setText(MUtils.getNumWan(videoBean.getPraiseCount()));
        });
        msg.setOnClickListener(view -> Api.addComment(videoBean.getId(), null));

        heart.setText(String.format("%s", MUtils.getNumWan(videoBean.getPraiseCount())));
        msg.setText(String.format("%s", MUtils.getNumWan(videoBean.getCommentCount())));
        RelativeLayout.LayoutParams lpTop = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        videoPlayer.getTopView().setLayoutParams(lpTop);
        videoPlayer.getTopView().setPadding(0, 25, 20, 20);

//        videoPlayer.setGSYVideoProgressListener(new GSYVideoProgressListener() {
//            @Override
//            public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
//                if (progress > 0) {
//
//                }
//            }
//        });
    }

    @OnClick(R.id.back_layout)
    void backClick() {
        finish();
    }

    @Override
    public void onBackPressed() {
//        if (orientationUtils != null) {
//            orientationUtils.backToProtVideo();
//        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        videoPlayer.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        videoPlayer.getCurrentPlayer().onVideoPause();
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayer.getCurrentPlayer().release();
        String timeLeft;
        long time = videoPlayer.getGSYVideoManager().getCurrentPosition();
        long duration = videoPlayer.getGSYVideoManager().getDuration();
        if (duration - time <= 750) {
            timeLeft = "已看完";
        } else if (time < 60000) {
            timeLeft = "观看不足一分钟";
        } else {
            timeLeft = "剩余" + (duration - time) / 60000 + "分钟";
        }
        Api.addHistory(videoBean.getId(), timeLeft, null);
    }

}
