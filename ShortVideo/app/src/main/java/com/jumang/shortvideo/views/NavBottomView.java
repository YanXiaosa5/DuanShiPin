package com.jumang.shortvideo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jumang.shortvideo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 2017/6/27
 */
public class NavBottomView extends LinearLayout implements View.OnClickListener {

    /**
     * 上下文
     */
    Context context;

    /**
     * 自定义属性
     */
    AttributeSet attrs;

    /**
     * 获取自定义属性
     */
    TypedArray mTypedArray;

    /**
     * viewpager
     */
    ViewPager viewPager;

    /**
     * 首页-文本
     */
    TextView text1;

    /**
     * 首页-图标
     */
    ImageView image1;

    /**
     * 推荐-文本
     */
    TextView text2;

    /**
     * 推荐-图标
     */
    ImageView image2;

    /**
     * 频道-文本
     */
    TextView text3;

    /**
     * 频道-图标
     */
    ImageView image3;

    /**
     * 我的-文本
     */
    TextView text4;

    /**"
     * 我的-图标
     */
    ImageView image4;
//    View redPoint;
//    TextView text5;
//    ImageView image5;

    /**
     * 保存底部标签的TextView
     */
    List<TextView> textViews = new ArrayList<>();

    /**
     * 保存底部标签的ImageView图标
     */
    List<ImageView> imageViews = new ArrayList<>();

    /**
     * 记录当前页面位置,切换tab标签
     */
    int currentIndex = 0;

    public NavBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.NavBottomView);
        initControl();
    }

    private void initControl() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_nav_bottom_view, this);
        text1 = view.findViewById(R.id.nav_text1);
        image1 = view.findViewById(R.id.nav_image1);
        text2 = view.findViewById(R.id.nav_text2);
        image2 = view.findViewById(R.id.nav_image2);
        text3 = view.findViewById(R.id.nav_text3);
        image3 = view.findViewById(R.id.nav_image3);
        text4 = view.findViewById(R.id.nav_text4);
        image4 = view.findViewById(R.id.nav_image4);
//        redPoint = view.findViewById(R.id.nav_discovery_red_point);
//        text5 = view.findViewById(R.id.nav_text5);
//        image5 = view.findViewById(R.id.nav_image5);

        textViews.add(text1);
        textViews.add(text2);
        textViews.add(text3);
        textViews.add(text4);
//        textViews.add(text5);

        imageViews.add(image1);
        imageViews.add(image2);
        imageViews.add(image3);
        imageViews.add(image4);
//        imageViews.add(image5);

        findViewById(R.id.nav_ll1).setOnClickListener(this);
        findViewById(R.id.nav_ll2).setOnClickListener(this);
        findViewById(R.id.nav_ll3).setOnClickListener(this);
        findViewById(R.id.nav_ll4).setOnClickListener(this);
//        findViewById(R.id.nav_ll5).setOnClickListener(this);

        refreshControl();
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                refreshControl();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void showRedPoint() {
        //redPoint.setVisibility(View.VISIBLE);
    }

    public void dismissRedPoint() {
        //redPoint.setVisibility(View.GONE);
    }

    private void refreshControl() {
        for (int index = 0; index < textViews.size(); index++) {
            if (currentIndex == index) {
                textViews.get(index).setTextColor(
                        mTypedArray.getColor(R.styleable.NavBottomView_textCheckedColor,
                                ContextCompat.getColor(context, R.color.color_theme_nav)));
            } else {
                textViews.get(index).setTextColor(
                        mTypedArray.getColor(R.styleable.NavBottomView_textUnCheckedColor,
                                ContextCompat.getColor(context, R.color.color_999)));
            }
            switch (index) {
                case 0:
//                    textViews.get(index).setText(
//                            mTypedArray.getText(R.styleable.NavBottomView_navLeftText));
                    if (currentIndex == 0) {
                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
                                R.styleable.NavBottomView_navTab0CheckedImage, R.mipmap.icon_nav1_checked));
                    } else {
                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
                                R.styleable.NavBottomView_navTabOUnCheckedImage, R.mipmap.icon_nav1));
                    }
                    break;
                case 1:
//                    textViews.get(index).setText(
//                            mTypedArray.getText(R.styleable.NavBottomView_navRightText));
                    if (currentIndex == 1) {
                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
                                R.styleable.NavBottomView_navTab2CheckedImage, R.mipmap.icon_nav2_checked));
                    } else {
                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
                                R.styleable.NavBottomView_navTab2UnCheckedImage, R.mipmap.icon_nav2));
                    }
                    break;
                case 2:
//                    textViews.get(index).setText(
//                            mTypedArray.getText(R.styleable.NavBottomView_navRightText));
                    if (currentIndex == 2) {
                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
                                R.styleable.NavBottomView_navTab3CheckedImage, R.mipmap.icon_nav3_checked));
                    } else {
                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
                                R.styleable.NavBottomView_navTab3UnCheckedImage, R.mipmap.icon_nav3));
                    }
                    break;
                case 3:
//                    textViews.get(index).setText(
//                            mTypedArray.getText(R.styleable.NavBottomView_navRightText));
                    if (currentIndex == 3) {
                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
                                R.styleable.NavBottomView_navTab5CheckedImage, R.mipmap.icon_nav5_checked));
                    } else {
                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
                                R.styleable.NavBottomView_navTab5UnCheckedImage, R.mipmap.icon_nav5));
                    }
                    break;
//                case 4:
////                    textViews.get(index).setText(
////                            mTypedArray.getText(R.styleable.NavBottomView_middleText));
//                    if (currentIndex == 1) {
//                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
//                                R.styleable.NavBottomView_navTab5CheckedImage,
//                                R.mipmap.icon_nav5_checked));
//                    } else {
//                        imageViews.get(index).setImageResource(mTypedArray.getResourceId(
//                                R.styleable.NavBottomView_navTab1UnCheckedImage,
//                                R.mipmap.icon_nav5));
//                    }
//                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        viewPager.setCurrentItem(Integer.valueOf(view.getTag().toString()));
    }
}
