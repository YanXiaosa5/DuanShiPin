package com.jumang.shortvideo.data;

import android.os.Environment;

import com.jumang.shortvideo.bean.UserBean;
import com.jumang.shortvideo.utils.JSONUtils;

import java.io.File;
import java.util.Date;

/**
 * Created by steven on 2016/11/21.
 * Email-songzhonghua_1987@msn.com
 */

public class AppInfo {
    public static final String ROOT_PATH = "wxr/";
    public static final String RECORD_DIR = "record/";
    public static final String RECORD_PATH = ROOT_PATH + RECORD_DIR;
    public final static String DATE_FORMATE = "yyyy-MM-dd";
    public final static String DATE_TIME_FORMATE = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_TIME_FORMATE2 = "yyyy年MM月dd日 HH:mm";
    public final static String DATE_TIME_FORMATE3 = "yyyy年MM月dd日";
    public final static String TIME_FORMAT = "HH:mm";
    public final static String BASIC_REGEX = "^[A-Za-z0-9@._-]+$";
    public final static String PHONE_REGEX = "^0\\d0\\d{8}$";
    public final static String MAIL_REGEX = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public final static String APPNAME = "ShortVideo";
    private static boolean sync = false;
    public static String GPS = "";
    public static boolean isWifi = true;
    public static final String APP_FILE_ROOT = Environment.getExternalStorageDirectory() +
            File.separator + APPNAME;

    public static int AxcTotal = 0;
    public static String ChannelNO = "10000"; //为10001时是内测版本
    public static Date currentDate = new Date();
    public static String identRemark = "";

    public static String getCachePath() {
        File f = new File(APP_FILE_ROOT);
        if (!f.exists()) {
            f.mkdir();
        }
        return f.getAbsolutePath();
    }

    public static String getCourseCachePath() {
        File f = new File(APP_FILE_ROOT + "/course/");
        if (!f.exists()) {
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }

    public static String getImgCachePath() {
        File file = new File(getCachePath() +
                "/photo/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file.getAbsolutePath();
    }

    private static String docRange = "0";//病案公开范围

    public static String getDocRange() {
        return docRange;
    }

    public static void setDocRange(String docRange) {
        AppInfo.docRange = docRange;
    }

    public static UserBean getUserInfo() {
        return JSONUtils.fromJson(SharedPreference.getUserInfo(), UserBean.class);
    }

    public static void setUserInfo(String userInfo) {
        SharedPreference.setUserInfo(userInfo);
    }


    public static String deviceId;
    private static Boolean obtainGooglePermission;

    public static final String LOCAL_SITE_ROOT = "file:///android_asset/www/";

    public static String getCity() {
        return SharedPreference.getCity();
    }

    public static void setCity(String city) {
        SharedPreference.setCity(city);
    }
}
