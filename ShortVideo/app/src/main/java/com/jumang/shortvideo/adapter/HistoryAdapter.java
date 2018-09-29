package com.jumang.shortvideo.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.jumang.shortvideo.GlideApp;
import com.jumang.shortvideo.R;
import com.jumang.shortvideo.bean.HistoryBean;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class HistoryAdapter extends BaseQuickAdapter<HistoryBean, BaseViewHolder> {
    private Context context;

    public HistoryAdapter(Context context, List<HistoryBean> datas) {
        super(datas);
        this.context = context;
        setMultiTypeDelegate(new MultiTypeDelegate<HistoryBean>() {
            @Override
            protected int getItemType(HistoryBean entity) {
                return entity.getType();
            }
        });
        getMultiTypeDelegate()
                .registerItemType(HistoryBean.TITLE, R.layout.item_history_title)
                .registerItemType(HistoryBean.CONTENT, R.layout.item_history);
    }

    @Override
    protected void convert(BaseViewHolder holder, HistoryBean videoBean) {
        switch (holder.getItemViewType()) {
            case HistoryBean.TITLE:
                holder.setText(R.id.title, videoBean.getCategory());
                break;
            case HistoryBean.CONTENT:
                holder.setText(R.id.title, videoBean.getVideoTitle());
                GlideApp.with(context)
                        .load(videoBean.getVideoImg())
                        .transition(withCrossFade())
                        .into((ImageView) holder.getView(R.id.image));
                holder.setText(R.id.time, videoBean.getTimeLeft());
                break;
        }
    }
}
