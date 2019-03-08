package com.jumang.shortvideo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.base.BaseActivity;
import com.jumang.shortvideo.base.NetStateChangeObserver;
import com.jumang.shortvideo.base.NetStateChangeReceiver;
import com.jumang.shortvideo.base.NetworkType;
import com.jumang.shortvideo.data.AppInfo;
import com.jumang.shortvideo.fragment.ChannelFragment;
import com.jumang.shortvideo.fragment.MainFragment;
import com.jumang.shortvideo.fragment.MineFragment;
import com.jumang.shortvideo.fragment.RecommendFragment;
import com.jumang.shortvideo.utils.MUtils;
import com.jumang.shortvideo.utils.NetworkUtils;
import com.jumang.shortvideo.utils.UpgradeHelper;
import com.jumang.shortvideo.views.CustomViewPager;
import com.jumang.shortvideo.views.NavBottomView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NetStateChangeObserver {
    @BindView(R.id.viewpager)
    CustomViewPager viewPager;

    /**
     * 底部标签栏
     */
    @BindView(R.id.main_nav_view)
    public NavBottomView navigation;

    /**
     * fragment集合
     */
    private List<Fragment> allFragments = new ArrayList<>();

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        navigation.setPadding(0, 0, 0, getNavigationBarHeight());
        AppInfo.deviceId = MUtils.getDeviceId(this);
        instance = this;

        NetworkType networkType = NetworkUtils.getNetworkType(this);
        if (networkType != NetworkType.NETWORK_WIFI) {
            AppInfo.isWifi = false;
        }

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        NetStateChangeReceiver.registerObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetStateChangeReceiver.unregisterObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onNetDisconnected() {
        AppInfo.isWifi = false;
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        AppInfo.isWifi = networkType == NetworkType.NETWORK_WIFI;
    }

    private void initView() {

        allFragments.add(new MainFragment());
        allFragments.add(new RecommendFragment());
        allFragments.add(new ChannelFragment());
        MineFragment mineFragment = new MineFragment();
        allFragments.add(mineFragment);

        viewPager.setOffscreenPageLimit(5);
        viewPager.setSaveEnabled(false);
        viewPager.setIsCanScroll(false);
        viewPager.setAdapter(mAdapter);

        //GSYVideoManager.instance().setVideoType(this, GSYVideoType.SYSTEMPLAYER);
//        GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                GSYVideoManager.releaseAllVideos();
                if (position == 3) {
                    mineFragment.bindData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        navigation.setViewPager(viewPager);

        new UpgradeHelper(this).checkUpdate(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
    }

    private FragmentStatePagerAdapter mAdapter = new FragmentStatePagerAdapter(
            getSupportFragmentManager()) {

        /** 仅执行一次 */
        @Override
        public Fragment getItem(int position) {
            return allFragments.get(position);
        }

        @Override
        public int getCount() {
            return allFragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
            requestPermissions(new String[]{
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出" + getString(R.string.app_name),
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
