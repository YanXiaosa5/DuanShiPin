package com.jumang.shortvideo.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jumang.shortvideo.R;
import com.jumang.shortvideo.bean.HomeVideoBean;
import com.jumang.shortvideo.utils.DensityUtils;
import com.jumang.shortvideo.utils.MUtils;

import java.util.List;


//https://blog.csdn.net/god_sunht/article/details/79150716  glide三级缓存
/**
 * 主页频道的横向列表
 */
public class ChannelChildAdapter extends BaseQuickAdapter<HomeVideoBean, BaseViewHolder> {
    private Context context;

    ChannelChildAdapter(Context context, List<HomeVideoBean> datas) {
        super(R.layout.item_channel_child, datas);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeVideoBean videoBean) {
        holder.setText(R.id.title, videoBean.getVideoTitle());

        Glide.with(context)
                .load(videoBean.getVideoImg())
                .override(DensityUtils.dp2px(context,120),DensityUtils.dp2px(context,150)).centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) holder.getView(R.id.image));
        holder.setText(R.id.time, String.format("%s赞", MUtils.getNumWan(videoBean.getPraiseCount())));
        holder.setText(R.id.tv_play_count, String.format("%s次观看", MUtils.getNumWan(videoBean.getViewCount())));
    }
}
