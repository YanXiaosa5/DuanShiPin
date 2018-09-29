package com.jumang.shortvideo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.jumang.shortvideo.SApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by dong on 2016/3/17.
 * 常用工具类
 * 以后我会慢慢把常用的方法优化到这个类中
 */
public class MUtils {
    private MUtils() {
    }

    /**
     * install apk
     *
     * @param aContext
     * @param apkfile
     */
    public static void installApk(Context aContext, File apkfile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //版本在7.0以上是不能直接通过uri访问的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(aContext, "com.jumang.shortvideo.install", apkfile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        }
        aContext.startActivity(intent);
    }

    /**
     * get drawable by intType
     *
     * @param aContext
     * @param res
     * @return
     */
    public static Drawable getDrawable(Context aContext, int res) {
        Drawable drawable = aContext.getResources().getDrawable(res);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    /**
     * 保存bitmap到本地文件
     *
     * @param filePath 路径
     * @param bitmap   图片
     * @return 结果
     */
    public static Boolean saveBitmap(String filePath, Bitmap bitmap) {
        File f = new File(filePath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 获取屏幕宽高
     *
     * @return
     */
    public static int getScreenWidth(Activity aActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        aActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity aActivity) {
        DisplayMetrics dm = new DisplayMetrics();
        aActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    //格式化指定Date
    public static String dateFormat(Date date, String formater) {
        SimpleDateFormat format = new SimpleDateFormat(formater);
        return format.format(date);
    }

    /**
     * bitmap to ByteArray
     *
     * @param bm
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * throw Exception
     *
     * @param error
     */
    public static void throwException(String error) {
        try {
            throw new Exception(error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get version name of app
     *
     * @param aContext
     * @return versionName
     */
    public static String getAppVersionName(Context aContext) {
        PackageManager packageManager = aContext.getPackageManager();
        String versionName = "";
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    aContext.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionName;
    }

    /**
     * get version code of app
     *
     * @param aContext
     * @return
     */
    public static int getAppVersionCode(Context aContext) {
        PackageManager manager = aContext.getPackageManager();
        int appCode = -1;
        try {
            PackageInfo info = manager.getPackageInfo(aContext
                    .getPackageName(), 0);
            appCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appCode;
    }


    //格式化余额
    public static String formatBalance(String balance) {
        try {
            DecimalFormat format = new DecimalFormat("######.##");
            String result = format.format(Double.valueOf(balance));
            if (!result.contains(".")) {
                result += ".00";
            } else if (result.endsWith(".0")) {
                result += "0";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    //格式化字符串
    public static String formatString(String str, String strFormat) {
        try {
            DecimalFormat format = new DecimalFormat(strFormat);
            return format.format(Double.valueOf(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboardFromView(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboardFromWindow(Activity activity) {
        try {
            if (activity.getWindow().getAttributes().softInputMode
                    != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                if (activity.getCurrentFocus() != null)
                    ((InputMethodManager) (activity
                            .getSystemService(Context.INPUT_METHOD_SERVICE)))
                            .hideSoftInputFromWindow(activity
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceId(Context context) {
        String deviceId = "";
        try {
            // 先获取androidid
            deviceId = Settings.Secure.getString(SApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
            // 在主流厂商生产的设备上，有一个很经常的bug，
            // 就是每个设备都会产生相同的ANDROID_ID：9774d56d682e549c
            if (TextUtils.isEmpty(deviceId) || "9774d56d682e549c".equals(deviceId)) {
                TelephonyManager telephonyManager = (TelephonyManager) SApplication.getInstance()
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return "";
                }
                deviceId = telephonyManager.getDeviceId();
            }
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = UUID.randomUUID().toString();
                deviceId = deviceId.replaceAll("-", "");
            }
        } catch (Exception e) {
            deviceId = UUID.randomUUID().toString();
            deviceId = deviceId.replaceAll("-", "");
        } finally {
            return deviceId;
        }
    }

    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static DisplayMetrics getDisplayMetrics() {
        return SApplication.getInstance().getResources().getDisplayMetrics();
    }

    public static String getNumWan(int value) {
        if (value < 10000) {
            return value + "";
        } else {
            return ((int) (value / 1000.0)) / 10.0 + "万";
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
