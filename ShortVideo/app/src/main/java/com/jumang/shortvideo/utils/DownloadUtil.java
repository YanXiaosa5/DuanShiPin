package com.jumang.shortvideo.utils;

import android.content.Context;
import android.widget.Toast;

import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.api.OkHttpUtils.callback.FileCallBack;
import com.jumang.shortvideo.data.AppInfo;

import java.io.File;

import okhttp3.Call;

public class DownloadUtil {
    public static void startApkDownLoad(String url, String fileName, Context activity) {
        final String tempAPKPath = AppInfo.getCachePath() + File.separator;
        File file = new File(tempAPKPath + fileName);
        if (file.exists()) {
            file.delete();
        }

        Toast.makeText(activity, "开始下载...", Toast.LENGTH_SHORT).show();
        Api.downloadFile(url, new FileCallBack(tempAPKPath, fileName) {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(File t, int id) {
                if (t.exists()) {
                    MUtils.installApk(activity, t);
                }
            }
        });
    }
}
