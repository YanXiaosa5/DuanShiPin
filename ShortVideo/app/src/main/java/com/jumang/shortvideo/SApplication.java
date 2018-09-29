package com.jumang.shortvideo;

import android.app.Application;

import com.jumang.shortvideo.api.OkHttpUtils.OkHttpUtils;
import com.jumang.shortvideo.base.NetStateChangeReceiver;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import okhttp3.OkHttpClient;

/**
 * Created by steven on 2017/10/23.
 * Email-songzhonghua_1987@msn.com
 */

public class SApplication extends Application {

    private static SApplication myApplication;

    public static String CHANEEL_ID;

    public synchronized static SApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        OkHttpUtils.initClient(new OkHttpClient.Builder().build());

        //获取渠道id
        final ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(this);

        if (channelInfo != null) {
            CHANEEL_ID =  channelInfo.getChannel();
        }else {
            if (BuildConfig.DEBUG) {
                CHANEEL_ID = "dev";
            } else {
                CHANEEL_ID = "default";//改为default，因为有的三方渠道，会更改channelInfo
            }
        }

        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "5ba0b68bb465f5a07500033c", CHANEEL_ID, UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        PlatformConfig.setWeixin("wx7d3588535930f2be", "dcad06c09a3c3c7c200e07bee09e8d7a");
        MobclickAgent.openActivityDurationTrack(false);

        NetStateChangeReceiver.registerReceiver(this);



    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 取消BroadcastReceiver注册
        NetStateChangeReceiver.unregisterReceiver(this);
    }

    /**
     * 添加password
     *
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
