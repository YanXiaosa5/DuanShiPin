package com.jumang.shortvideo.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jumang.shortvideo.GlideApp;
import com.jumang.shortvideo.R;
import com.jumang.shortvideo.SApplication;
import com.jumang.shortvideo.bean.HomeVideoBean;
import com.jumang.shortvideo.utils.DensityUtils;
import com.jumang.shortvideo.utils.MUtils;
import com.jumang.shortvideo.utils.ScreenUtils;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ChannelDetailAdapter extends BaseQuickAdapter<HomeVideoBean, BaseViewHolder> {
    private Context context;

    public ChannelDetailAdapter(Context context, List<HomeVideoBean> datas) {
        super(R.layout.item_channel_detail, datas);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeVideoBean videoBean) {
        holder.setText(R.id.title, videoBean.getVideoTitle());

        GlideApp.with(context)
                .load(videoBean.getVideoImg())
                .transition(withCrossFade()).override(DensityUtils.dp2px(context,120),DensityUtils.dp2px(context,150))
                .into((ImageView) holder.getView(R.id.image));

        holder.setText(R.id.time, String.format("%s赞", MUtils.getNumWan(videoBean.getPraiseCount())));
        holder.setText(R.id.tv_play_count, String.format("%s次观看", MUtils.getNumWan(videoBean.getViewCount())));
    }
}
