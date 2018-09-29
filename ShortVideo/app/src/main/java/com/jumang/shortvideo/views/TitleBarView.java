package com.jumang.shortvideo.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.utils.MUtils;

/**
 * Created by steven on 15/11/7
 */
public class TitleBarView extends RelativeLayout {

    /**
     * 上下文
     */
    private Context context;

    /**
     * 中间标题
     */
    private TextView titleTextView;

    /**
     * 左侧标题(和左侧图标一起)
     */
    private TextView leftTextView;

    /**
     * 右侧标题
     */
    private TextView rightTextView;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_title_bar, this, true);
        RelativeLayout rootView = view.findViewById(R.id.title_bar_root);
        titleTextView = view.findViewById(R.id.actionbar_title);
        leftTextView = view.findViewById(R.id.actionbar_left);
        rightTextView = view.findViewById(R.id.actionbar_right);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.TitleBarView);

        // 获取自定义属性和默认值
        titleTextView.setText(mTypedArray.getString(R.styleable.TitleBarView_titleText));

        rootView.setBackgroundColor(
                mTypedArray.getColor(R.styleable.TitleBarView_titleBackgroundColor,
                        ContextCompat.getColor(context, R.color.colorPrimary)));
        int contentColor = mTypedArray.getColor(R.styleable.TitleBarView_contentBackgroundColor,
                ContextCompat.getColor(context, R.color.black));
        titleTextView.setTextColor(contentColor);
        leftTextView.setTextColor(contentColor);
        rightTextView.setTextColor(contentColor);

        if (mTypedArray.getBoolean(R.styleable.TitleBarView_leftVisible, true)) {
            leftTextView.setVisibility(View.VISIBLE);
        } else {
            leftTextView.setVisibility(View.GONE);
        }
        leftTextView.setText(mTypedArray.getString(R.styleable.TitleBarView_leftText));

        Drawable drawableLeft = mTypedArray.getDrawable(R.styleable.TitleBarView_leftDrawImage);
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());
            leftTextView.setCompoundDrawables(drawableLeft, null, null, null);
        }
        Drawable drawableTitle = mTypedArray.getDrawable(R.styleable.TitleBarView_titleDrawImage);
        if (drawableTitle != null) {
            drawableTitle.setBounds(0, 0, drawableTitle.getMinimumWidth(), drawableTitle.getMinimumHeight());
            titleTextView.setCompoundDrawables(drawableTitle, null, null, null);
        }
//        if (mTypedArray.getBoolean(R.styleable.TitleBarView_rightVisible, false)) {
//            rightTextView.setVisibility(View.VISIBLE);
//        } else {
//            rightTextView.setVisibility(View.GONE);
//        }
        rightTextView.setText(mTypedArray.getString(R.styleable.TitleBarView_rightText));
        Drawable drawableRight = mTypedArray.getDrawable(R.styleable.TitleBarView_rightDrawImage);
        if (drawableRight != null) {
            drawableRight.setBounds(0, 0, MUtils.dip2px(context, 25),
                    MUtils.dip2px(context, 25));
            rightTextView.setCompoundDrawables(null, null, drawableRight, null);
        }
        rightTextView.setTextSize(mTypedArray.getDimension(R.styleable.TitleBarView_rightTextSize, 14));
        mTypedArray.recycle();

        leftTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) TitleBarView.this.context).finish();
            }
        });
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    public TextView getLeftTextView() {
        return leftTextView;
    }
}
