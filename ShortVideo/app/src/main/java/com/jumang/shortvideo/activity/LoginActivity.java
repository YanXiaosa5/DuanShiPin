package com.jumang.shortvideo.activity;

import android.os.Bundle;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends BaseActivity {

    public String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login();
    }

    public void login(){
        MobclickAgent.onEvent(getApplicationContext(),"login");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

}
