package com.jumang.shortvideo.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.jumang.shortvideo.SApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异常信息捕获类
 */
public class CrashHandler /*implements Thread.UncaughtExceptionHandler*/ {

//    private static CrashHandler INSTANCE;
//    private static Context mContext;
//
//    private static PendingIntent restartIntent;
//
//    private Thread.UncaughtExceptionHandler mDefaultHandler;
//    private CrashUploader crashUploader;
//
//    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//
//    public static final String EXCEPTION_INFO = "EXCEPTION_INFO";
//    public static final String PACKAGE_INFO = "PACKAGE_INFO";
//    public static final String DEVICE_INFO = "DEVICE_INFO";
//    public static final String SYSTEM_INFO = "SYSTEM_INFO";
//    public static final String SECURE_INFO = "SECURE_INFO";
//    public static final String MEM_INFO = "MEM_INFO";
//
//
//
//    private String mExceptionInfo;
//    private String mMemInfo;
//
//    private ConcurrentHashMap<String, String> mPackageInfo = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<String, String> mDeviceInfo = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<String, String> mSystemInfo = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<String, String> mSecureInfo = new ConcurrentHashMap<>();
//
//    private ConcurrentHashMap<String, Object> totalInfo = new ConcurrentHashMap<>();
//
//
//
//    private CrashHandler() {
//
//    }
//
//    public static CrashHandler getInstance() {
//        if (INSTANCE == null) {
//            synchronized (CrashHandler.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new CrashHandler();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//    public void init(Context context, CrashUploader crashUploader, PendingIntent pendingIntent) {
//        mContext = context;
//        this.crashUploader = crashUploader;
//        this.restartIntent = pendingIntent;
//        //保存一份系统默认的CrashHandler
//        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//        //使用我们自定义的异常处理器替换程序默认的
//        Thread.setDefaultUncaughtExceptionHandler(this);
//    }
//
//    /**
//     * @param t 出现未捕获异常的线程
//     * @param e 未捕获的异常，有了这个ex，我们就可以得到异常信息
//     */
//    @Override
//    public void uncaughtException(Thread t, Throwable e) {
//        if (!catchCrashException(e) && mDefaultHandler != null) {
//            //没有自定义的CrashHandler的时候就调用系统默认的异常处理方式
//            mDefaultHandler.uncaughtException(t, e);
//        } else {
//            //退出应用
//            killProcess();
//        }
//    }
//
//
//    /**
//     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
//     *
//     * @param ex 未捕获的异常
//     * @return true:如果处理了该异常信息;否则返回false.
//     */
//    private boolean catchCrashException(Throwable ex) {
//        if (ex == null) {
//            return false;
//        }
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                Toast.makeText(SApplication.getInstance().getApplicationContext(), "程序异常,即将退出", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }.start();
//        collectInfo(ex);
//        //保存日志文件
//        saveCrashInfo2File();
//        //上传崩溃信息
//        uploadCrashMessage(totalInfo);
//
//        return true;
//    }
//
//    /**
//     * 退出应用
//     */
//    private static void killProcess() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            Log.e("application", "error : ", e);
//        }
//        // 退出程序
//        AlarmManager mgr = (AlarmManager) SApplication.getInstance().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restartIntent); // 2秒钟后重启应用
//        Process.killProcess(Process.myPid());
//        System.exit(1);
//    }
//
//
//    /**
//     * 获取异常信息和设备参数信息
//     */
//    private void collectInfo(Throwable ex) {
//        mExceptionInfo = collectExceptionInfo(ex);
//        collectPackageInfo();
//        collectBuildInfos();
//        collectSystemInfos();
//        collectSecureInfo();
//        mMemInfo = collectMemInfo();
//
//        totalInfo.put(EXCEPTION_INFO, mExceptionInfo);
//        totalInfo.put(PACKAGE_INFO, mPackageInfo);
//        totalInfo.put(DEVICE_INFO, mDeviceInfo);
//        totalInfo.put(SYSTEM_INFO, mSecureInfo);
//        totalInfo.put(SECURE_INFO, mSecureInfo);
//        totalInfo.put(MEM_INFO, MEM_INFO);
//    }
//
//    /**
//     * 获取捕获异常的信息
//     */
//    private String collectExceptionInfo(Throwable ex) {
//        Writer mWriter = new StringWriter();
//        PrintWriter mPrintWriter = new PrintWriter(mWriter);
//        ex.printStackTrace(mPrintWriter);
//        ex.printStackTrace();
//        Throwable mThrowable = ex.getCause();
//        // 迭代栈队列把所有的异常信息写入writer中
//        while (mThrowable != null) {
//            mThrowable.printStackTrace(mPrintWriter);
//            // 换行 每个个异常栈之间换行
//            mPrintWriter.append("\r\n");
//            mThrowable = mThrowable.getCause();
//        }
//        // 记得关闭
//        mPrintWriter.close();
//        return mWriter.toString();
//    }
//
//    /**
//     * 获取应用包参数信息
//     */
//    private void collectPackageInfo() {
//        try {
//            // 获得包管理器
//            PackageManager mPackageManager = mContext.getPackageManager();
//            // 得到该应用的信息，即主Activity
//            PackageInfo mPackageInfo = mPackageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
//            if (mPackageInfo != null) {
//                String versionName = mPackageInfo.versionName == null ? "null" : mPackageInfo.versionName;
//                String versionCode = mPackageInfo.versionCode + "";
//                this.mPackageInfo.put("VersionName", versionName);
//                this.mPackageInfo.put("VersionCode", versionCode);
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 从系统属性中提取设备硬件和版本信息
//     */
//    private void collectBuildInfos() {
//        // 反射机制
//        Field[] mFields = Build.class.getDeclaredFields();
//        // 迭代Build的字段key-value 此处的信息主要是为了在服务器端手机各种版本手机报错的原因
//        for (Field field : mFields) {
//            try {
//                field.setAccessible(true);
//                mDeviceInfo.put(field.getName(), field.get("").toString());
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 获取系统常规设定属性
//     */
//    private void collectSystemInfos() {
//        Field[] fields = Settings.System.class.getFields();
//        for (Field field : fields) {
//            if (!field.isAnnotationPresent(Deprecated.class)
//                    && field.getType() == String.class) {
//                try {
//                    String value = Settings.System.getString(mContext.getContentResolver(), (String) field.get(null));
//                    if (value != null) {
//                        mSystemInfo.put(field.getName(), value);
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 获取系统安全设置信息
//     */
//    private void collectSecureInfo() {
//        Field[] fields = Settings.Secure.class.getFields();
//        for (Field field : fields) {
//            if (!field.isAnnotationPresent(Deprecated.class)
//                    && field.getType() == String.class
//                    && field.getName().startsWith("WIFI_AP")) {
//                try {
//                    String value = Settings.Secure.getString(mContext.getContentResolver(), (String) field.get(null));
//                    if (value != null) {
//                        mSecureInfo.put(field.getName(), value);
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 获取内存信息
//     */
//    private String collectMemInfo() {
//        BufferedReader br = null;
//        StringBuffer sb = new StringBuffer();
//
//        ArrayList<String> commandLine = new ArrayList<>();
//        commandLine.add("cat");
//        commandLine.add("/proc/meminfo");
//        commandLine.add(Integer.toString(Process.myPid()));
//        try {
//            java.lang.Process process = Runtime.getRuntime()
//                    .exec(commandLine.toArray(new String[commandLine.size()]));
//            br = new BufferedReader(new InputStreamReader(process.getInputStream()), 1024);
//
//            while (true) {
//                String line = br.readLine();
//                if (line == null) {
//                    break;
//                }
//                sb.append(line);
//                sb.append("\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * 将崩溃日志信息写入本地文件
//     */
//    private String saveCrashInfo2File() {
//        StringBuffer mStringBuffer = getInfoStr(mPackageInfo);
//        mStringBuffer.append(mExceptionInfo);
//        // 保存文件，设置文件名
//        String mTime = formatter.format(new Date());
//        String mFileName = "crash-" + mTime + ".log";
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            try {
//                File mDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/hive-crash");
//                if (!mDirectory.exists()) {
//                    boolean success = mDirectory.mkdirs();
//                }
//                FileOutputStream mFileOutputStream = new FileOutputStream(mDirectory + File.separator + mFileName);
//                mFileOutputStream.write(mStringBuffer.toString().getBytes());
//                mFileOutputStream.close();
//                return mFileName;
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 将HashMap遍历转换成StringBuffer
//     */
//    @NonNull
//    private static StringBuffer getInfoStr(ConcurrentHashMap<String, String> info) {
//        StringBuffer mStringBuffer = new StringBuffer();
//        for (Map.Entry<String, String> entry : info.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            mStringBuffer.append(key + "=" + value + "\r\n");
//        }
//        return mStringBuffer;
//    }
//
//    /**
//     * 上传崩溃信息到服务器
//     */
//    private void uploadCrashMessage(ConcurrentHashMap<String, Object> info) {
//        crashUploader.uploadCrashMessage(info);
//    }
//
//    /**
//     * 崩溃信息上传接口回调
//     */
//    public interface CrashUploader {
//        void uploadCrashMessage(ConcurrentHashMap<String, Object> info);
//    }

    /**
     *    在Application中注册使用
     */
    /*
    public class ClientApp extends Application {

            private static ClientApp INSTANCE;
            private PendingIntent restartIntent;

            @Override
            public void onCreate() {
                super.onCreate();
                CrashHandler.CrashUploader crashUploader = new CrashHandler.CrashUploader() {

                    @Override
                    public void uploadCrashMessage(ConcurrentHashMap<String, Object> info) {
                        //这个地方自定义崩溃信息的上传
                    }
                };
                INSTANCE = this;
                // 以下用来捕获程序崩溃异常
                Intent intent = new Intent();
                intent.setClassName("com.owen.crashhandler", "com.owen.crashhandler.main.MainActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                CrashHandler.getInstance().init(this, crashUploader, restartIntent);
            }


            public synchronized static ClientApp getInstance() {
                return INSTANCE;
            }
    }
     */
}