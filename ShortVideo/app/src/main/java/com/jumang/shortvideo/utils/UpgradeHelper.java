package com.jumang.shortvideo.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jumang.shortvideo.R;
import com.jumang.shortvideo.api.Api;
import com.jumang.shortvideo.api.OkHttpUtils.callback.FileCallBack;
import com.jumang.shortvideo.api.ResultCallback;
import com.jumang.shortvideo.bean.VersionBean;
import com.jumang.shortvideo.data.AppInfo;
import com.jumang.shortvideo.views.HorizontalProgressBarWithNumber;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by steven on 2016/11/30.
 * Email-songzhonghua_1987@msn.com
 */

public class UpgradeHelper {
    private Activity activity;
    private HorizontalProgressBarWithNumber progressBarWithNumber;
    private Button btnFullInstall;
    private File file;

    public UpgradeHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 检查Market版本升级
     */
    public void checkUpdate(final boolean isShowToast) {
        Api.getLatestVersion(MUtils.getAppVersionCode(activity) + "",
                new ResultCallback<VersionBean>(activity) {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        if (isShowToast) {
                            Toast.makeText(activity, R.string.checking_upgrade, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponseString(String response) {
                        super.onResponseString(response);
                    }

                    @Override
                    public void onSuccess(VersionBean response) {
                        super.onSuccess(response);
                        if (response != null && response.getVersion() > 0) {
                            showUpdateDialog(response);
                        } else if (isShowToast) {
                            Toast.makeText(activity, R.string.already_latest, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String strMsg) {
                        Toast.makeText(activity, R.string.already_latest, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showUpdateDialog(final VersionBean bean) {
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.dialog_upgrade, null);

        final Button okBtn = layout.findViewById(R.id.positiveButton);
        final Button cancelBtn = layout.findViewById(R.id.negativeButton);
        progressBarWithNumber = layout.findViewById(R.id.id_progressbar01);
        btnFullInstall = layout.findViewById(R.id.dialog_full_install);
        TextView title = layout.findViewById(R.id.title);
        TextView version = layout.findViewById(R.id.version);
        TextView message = layout.findViewById(R.id.message);
        TextView date = layout.findViewById(R.id.date);

        title.setText(bean.getTitle());
        version.setText(bean.getVersionName());
        message.setText(bean.getDescription());
        date.setText("");
        if (bean.getForcibly().equals("1")) {
            cancelBtn.setText(activity.getResources().getText(R.string.exit));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }

        okBtn.setOnClickListener(view -> {
            okBtn.animate().alpha(0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            okBtn.setVisibility(View.GONE);

                            progressBarWithNumber.animate().alpha(1f)
                                    .setDuration(200)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            progressBarWithNumber.setVisibility(View.VISIBLE);
                                            startDownLoad(bean.getUrl());
                                        }
                                    });

                        }
                    });

            cancelBtn.animate().alpha(0f)
                    .setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            cancelBtn.setVisibility(View.GONE);
                        }
                    });
        });
        dialog.show();
        dialog.getWindow().setContentView(layout);
        okBtn.requestFocus();
        cancelBtn.setOnClickListener(view -> {
            if (bean.getForcibly().equals("1")) {
                activity.finish();
                System.exit(0);
            }
            dialog.dismiss();
        });

        btnFullInstall.setOnClickListener(view -> {
            if (file != null && file.exists()) {
                MUtils.installApk(activity, file);
            } else {
                Toast.makeText(activity, R.string.not_found_apk, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startDownLoad(String url) {
        final String tempAPKPath = AppInfo.getCachePath() + File.separator;
        file = new File(tempAPKPath);
        if (file.exists()) {
            file.delete();
        }
        Api.downloadFile(url, new FileCallBack(tempAPKPath, "short-video.apk") {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(activity, R.string.download_failed, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(File t, int id) {
                if (t.exists()) {
                    Toast.makeText(activity, R.string.download_success, Toast.LENGTH_SHORT).show();
                    MUtils.installApk(activity, t);
                    progressBarWithNumber.setVisibility(View.GONE);
                    btnFullInstall.setVisibility(View.VISIBLE);
                    btnFullInstall.requestFocus();
                }
            }

            @Override
            public void inProgress(int progress, long total, int id) {
                super.inProgress(progress, total, id);
                progressBarWithNumber.setProgress(progress);
            }
        });
    }
}
