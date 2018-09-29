package com.jumang.shortvideo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.base.BaseWebViewFragment;
import com.jumang.shortvideo.views.TitleBarView;
import com.jumang.shortvideo.views.swipebackview.app.SwipeBackActivity;
import com.umeng.analytics.MobclickAgent;

public class WebViewActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isNoStatusBar = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
        String title = getIntent().getStringExtra("title");
        boolean isFit = true;
        if (title != null && title.length() > 0) {
            titleBarView.setTitle(title);
            isFit = false;
        } else {
            titleBarView.setVisibility(View.GONE);
        }
        BaseWebViewFragment fragment =
                BaseWebViewFragment.newInstance(getIntent().getStringExtra("url"),
                        getIntent().getLongExtra("adId", 0), isFit);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
        comeInWeb();
    }

    public void comeInWeb(){
        MobclickAgent.onEvent(getApplicationContext(),"comeInWeb");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
