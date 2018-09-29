package com.jumang.shortvideo.adapter;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.jumang.shortvideo.GlideApp;
import com.jumang.shortvideo.R;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.bean.HomeVideoBean;
import com.jumang.shortvideo.utils.MUtils;
import com.jumang.shortvideo.views.CircleImageView;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainVideoAdapter extends BaseQuickAdapter<HomeVideoBean, BaseViewHolder> {
    private Activity context;
    public static String TAG = "MainVideoAdapter";

    private SparseBooleanArray mCheckStates = new SparseBooleanArray();

    public MainVideoAdapter(Activity context, List<HomeVideoBean> datas) {
        super(datas);
        this.context = context;
        setMultiTypeDelegate(new MultiTypeDelegate<HomeVideoBean>() {
            @Override
            protected int getItemType(HomeVideoBean entity) {
                return entity.getAd();
            }
        });
        getMultiTypeDelegate()
                .registerItemType(0, R.layout.item_main_video)
                .registerItemType(1, R.layout.item_main_ad);
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeVideoBean videoBean) {
        switch (holder.getItemViewType()) {
            case 0:
                setVideoControl(holder, videoBean.getVideoUrl(), videoBean.getVideoTitle(), videoBean.getVideoImg());
                CircleImageView mIvUserAvatar = holder.getView(R.id.iv_user_avatar);
                holder.setText(R.id.tv_username, videoBean.getAuthorName());
                holder.setText(R.id.tv_play_count, String.format("%s次观看", MUtils.getNumWan(videoBean.getViewCount())));
                //holder.setText(R.id.title, videoBean.getTitle());
                //        holder.setText(R.id.)

                Glide.with(context)
                        .load(videoBean.getAuthorAvatar())
                        .into(mIvUserAvatar);

                holder.getView(R.id.share).setOnClickListener(view -> {
                    UMWeb web = new UMWeb("http://www.digitaljournal.jp/");
                    web.setTitle(context.getString(R.string.app_name));//标题
                    UMImage image = new UMImage(context, R.mipmap.ic_launcher);
                    web.setThumb(image);  //缩略图
                    web.setDescription("");//描述
                    new ShareAction(context).withMedia(web)
                            .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA)
                            .setCallback(null).open();
                });
                holder.setText(R.id.heart_num, MUtils.getNumWan(videoBean.getPraiseCount()));
                holder.setText(R.id.talk_num, MUtils.getNumWan(videoBean.getCommentCount()));
                holder.getView(R.id.talk).setOnClickListener(view -> Api.addComment(videoBean.getId(), null));
                CheckBox checkBox = holder.getView(R.id.like);
                checkBox.setTag(holder.getAdapterPosition());//在最开始适配的时候，将每一个CheckBox设置一个当前的Tag值，这样每个CheckBox都有了一个固定的标识
                checkBox.setOnCheckedChangeListener((buttonView, isCheckBox) -> {
                    int pos = (int) buttonView.getTag();//得到当前CheckBox的Tag值，由于之前保存过，所以不会出现索引错乱
                    if (isCheckBox) {
                        //点击时将当前CheckBox的索引值和Boolean存入SparseBooleanArray中
                        mCheckStates.put(pos, true);
                    } else {
                        //否则将 当前CheckBox对象从SparseBooleanArray中移除
                        mCheckStates.delete(pos);
                    }
                });
                //得到CheckBox的Boolean值后，将当前索引的CheckBox状态改变
                checkBox.setChecked(mCheckStates.get(holder.getAdapterPosition(), false));
                checkBox.setChecked(videoBean.getPraised() == 1);
                checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                    Api.praise(videoBean.getId(), null);
                    videoBean.setPraised(b ? 1 : 0);
                    int t = 1;
                    if (!b) {
                        t = -1;
                    }
                    videoBean.setPraiseCount(videoBean.getPraiseCount() + t);
                    holder.setText(R.id.heart_num, MUtils.getNumWan(videoBean.getPraiseCount()));
                });
                break;
            case 1:
                if (videoBean.getAdVideoUrl() != null && !videoBean.getAdVideoUrl().trim().equals("")) {
                    holder.getView(R.id.image).setVisibility(View.GONE);
                    holder.getView(R.id.detail_player).setVisibility(View.VISIBLE);
                    setVideoControl(holder, videoBean.getAdVideoUrl(), "", videoBean.getAdImg());
                } else {
                    holder.getView(R.id.image).setVisibility(View.VISIBLE);
                    holder.getView(R.id.detail_player).setVisibility(View.GONE);
                    GlideApp.with(context)
                            .load(videoBean.getAdImg())
                            .transition(withCrossFade())
                            .into((ImageView) holder.getView(R.id.image));
                }

                if (videoBean.getRedirectUrl() != null && !videoBean.getRedirectUrl().trim().equals("")) {
                    ((Button) holder.getView(R.id.btn_upload)).setText("查看详情");
                } else {
                    ((Button) holder.getView(R.id.btn_upload)).setText("下载");
                }
                holder.setText(R.id.title_ad, String.format("广告：%s", videoBean.getAdName()));

                Api.adTrack(videoBean.getId(), 1, null);
                break;
        }
    }

    /**
     * 設置播放器相關操作
     * @param holder
     * @param videoUrl
     * @param title
     * @param videoImg
     */
    private void setVideoControl(BaseViewHolder holder, String videoUrl, String title, String videoImg) {
        StandardGSYVideoPlayer gsyVideoPlayer = holder.getView(R.id.detail_player);
        gsyVideoPlayer.setUp(videoUrl, true, null,
                null, title);
        //gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(v ->
                gsyVideoPlayer.startWindowFullscreen(context, false, true));
        //防止错位设置
        gsyVideoPlayer.setPlayTag(TAG);
        gsyVideoPlayer.setPlayPosition(holder.getAdapterPosition());
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        gsyVideoPlayer.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        gsyVideoPlayer.setReleaseWhenLossAudio(false);
        //全屏动画
        gsyVideoPlayer.setShowFullAnimation(true);
        //小屏时不触摸滑动
        gsyVideoPlayer.setIsTouchWiget(false);
        //增加封面
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.default_video_bg);

        GlideApp.with(context)
                .load(videoImg)
                .transition(withCrossFade())
                .into(imageView);
        gsyVideoPlayer.setThumbImageView(imageView);
        gsyVideoPlayer.setDismissControlTime(1000);

        gsyVideoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
            @Override
            public void onClickStop(String url, Object... objects) {
                super.onClickStop(url, objects);
//                Toast.makeText(context, "onClickStop", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
