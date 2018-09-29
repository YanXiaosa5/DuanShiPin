package com.jumang.shortvideo.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jumang.shortvideo.utils.JavaScriptinterface;


/**
 * Created by steven on 2017/5/28.
 * Email-songzhonghua_1987@msn.com
 */

public class MyWebView extends WebView {

    public MyWebView(Context context) {
        this(context, null);
    }

    public MyWebView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        WebSettings settings = this.getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.clearCache(true);
        settings.setAppCacheEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        settings.setSupportMultipleWindows(true);
        settings.setDefaultTextEncodingName("utf-8");
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        this.getSettings().setDomStorageEnabled(true);
        //webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        this.getSettings().setAppCachePath(appCachePath);
        this.getSettings().setAllowFileAccess(true);
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                if (message != null) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }

                result.cancel();    //一定要cancel，否则会出现各种奇怪问题
                return true;
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                Log.d("test", cm.message() + " -- From line "
                        + cm.lineNumber() + " of "
                        + cm.sourceId());
                return true;
            }
        });
        this.addJavascriptInterface(new JavaScriptinterface(context), "android");

        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return !url.startsWith("http") || super.shouldOverrideUrlLoading(view, url);
            }
        });

        this.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK &&
                        MyWebView.this.canGoBack()) {//表示按返回键时的操作
                    MyWebView.this.goBack();   //后退
                    return true;
                }
            }
            return false;
        });
    }
}
