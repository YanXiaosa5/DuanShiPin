package com.jumang.shortvideo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.SApplication;
import com.jumang.shortvideo.activity.WebViewActivity;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.data.AppInfo;
import com.jumang.shortvideo.utils.DownloadUtil;
import com.jumang.shortvideo.utils.FitStateUI;
import com.jumang.shortvideo.views.MyWebView;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;
import java.util.Objects;

/**
 * Created by steven on 2017/11/2.
 * Email-songzhonghua_1987@msn.com
 */

public class BaseWebViewFragment extends BaseFragment {
    View view;
    private static BaseWebViewFragment instance;

    private String TAG = BaseWebViewFragment.class.getSimpleName();

    public static BaseWebViewFragment newInstance(String url, long adId, boolean isFit) {
        instance = new BaseWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putLong("adId", adId);
        bundle.putBoolean("isFit", isFit);
        instance.setArguments(bundle);
        return instance;
    }

    /**
     * 下载量
     */
    public void installNum(){
        MobclickAgent.onEvent(SApplication.getInstance(),"installNum");
    }

    /**
     * 加载的web地址
     */
    public void loadWebUrl(){
        MobclickAgent.onEvent(SApplication.getInstance(),"loadWebUrl");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_base_web_view, container, false);
        MyWebView webView = view.findViewById(R.id.root_view);

        if (instance != null) {
            Bundle bundle = instance.getArguments();
            assert bundle != null;
            webView.getSettings().setUserAgentString("android_webview");

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.endsWith(".apk")) {
                        installNum();
                        DownloadUtil.startApkDownLoad(url,
                                new Date().getTime() + ".apk", BaseWebViewFragment.this.getContext());
                        Api.adTrack(bundle.getLong("adId"), 4, null);
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });
            if (bundle.getBoolean("isFit", true)) {
                FitStateUI.setStatusBarHeight(Objects.requireNonNull(this.getContext()), view);
            }
            if (bundle.containsKey("url")) {
                loadWebUrl();
                String url = bundle.getString("url");
                if (url != null && url.length() > 0 && !url.startsWith("http")) {
                    url = AppInfo.LOCAL_SITE_ROOT + url;
                }
                webView.loadUrl(url);
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }
}
