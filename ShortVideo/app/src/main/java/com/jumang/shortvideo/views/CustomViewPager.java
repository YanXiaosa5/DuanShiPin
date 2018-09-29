package com.jumang.shortvideo.views;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 类说明
 */
public class CustomViewPager extends ViewPager {

	private boolean isCanScroll = false;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setIsCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

//	@Override
//	public void scrollTo(int x, int y) {
//		if (isCanScroll) {
//			super.scrollTo(x, y);
//		}
//	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		try {
			return isCanScroll && super.onInterceptTouchEvent(arg0);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return isCanScroll && super.onTouchEvent(arg0);

	}
}
