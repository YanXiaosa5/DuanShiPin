package com.jumang.shortvideo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jumang.shortvideo.R;
import com.jumang.shortvideo.activity.VideoDetailActivity;
import com.jumang.shortvideo.bean.ChannelBean;
import com.jumang.shortvideo.bean.HomeVideoBean;

import java.util.List;

public class ChannelAdapter extends BaseQuickAdapter<ChannelBean, BaseViewHolder> {

    private Activity context;

    public ChannelAdapter(Activity context, List<ChannelBean> datas) {
        super(R.layout.item_channel, datas);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, ChannelBean videoBean) {
        holder.setText(R.id.title, videoBean.getChannelName());
        RecyclerView recyclerView = holder.getView(R.id.recycler);
        if (recyclerView.getAdapter() == null) {
            ChannelChildAdapter childAdapter = new ChannelChildAdapter(context, videoBean.getVideos());
            LinearLayoutManager
                    layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(childAdapter);

            childAdapter.setOnItemClickListener((adapter, view, position) -> {
                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra(HomeVideoBean.class.getSimpleName(), videoBean.getVideos().get(position));
                context.startActivity(intent);
            });
        } else {
            ChannelChildAdapter childAdapter = (ChannelChildAdapter) recyclerView.getAdapter();
            childAdapter.setNewData(videoBean.getVideos());
        }
    }
}
