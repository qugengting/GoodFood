package com.common.library.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by supper on 2017/11/22.
 * UncaughtExceptionHandler做全局的catch
 * 通常来讲，会针对，某段代码做try … catch 没有catch到的代码，发生异常的时候，就会由setDefaultUncaughtExceptionHandler来处理。
 * 当程序崩溃时，由这些代码接管
 * 将报错文件报错到本地，超出日志的大小删除掉旧日志
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "APP>>CrashHandler";

    //系统默认的UncaughtException处理类
    private static Thread.UncaughtExceptionHandler mDefaultUncaughtException;
    //CarshHandler的单例实例
    private static CrashHandler instance;
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    private String errorData = "";

    private String logName = "error";

    private long FileSize = 1024 * 1024 * 10;//1个文件只保存10M的日志，超出重新创建日志文件

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (instance == null)
            instance = new CrashHandler();
        return instance;
    }

    //初始化
    public void init(Context context) {
        Log.d(TAG, "init: 初始化");
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultUncaughtException = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        cleanLog(3);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultUncaughtException != null) {
            //以对代码做try … catch处理的代码，继续让系统默认的异常处理器来处理
            mDefaultUncaughtException.uncaughtException(thread, ex);
        } else {
//            //App重启处理
//            Intent intent = new Intent(mContext, MainActivity.class);
//            PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//            //退出程序
//            AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//                    restartIntent); // 1秒钟后重启应用
//            System.exit(0);
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);
        try {
            saveCrashInfoFile(ex);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return true;
    }

    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                //获取版本
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "获取版本信息不成功》》" + e);
        }
        //获取手机的配置信息
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);//成员变量为private,故必须进行此操
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "获取配置信息不成功》》", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     * @throws Exception
     */
    private String saveCrashInfoFile(Throwable ex) throws Exception {
        Log.d(TAG, "saveCrashInfoFile:错误信息" + ex.getMessage());
        errorData = "";
        Log.d(TAG, "saveCrashInfoFile: gg了，保存报错日志到本地");
        StringBuffer sb = new StringBuffer();
        try {
            sb.append("\n\n\n-------------------------------------------我是开始的分割线---------------------------------------------------\n");
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            errorData = sDateFormat.format(new java.util.Date());
            sb.append(errorData + "\n");
            //Map.Entry<String, String> 将map里的每一个键值对取出来封装成一个Entry对象在存到一个Set里面
            for (Map.Entry<String, String> entry : infos.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.flush();
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            sb.append("-------------------------------------------我是结束的分割线---------------------------------------------------\n\n\n\n");
            final String fileName = getFileName();
            FileUtils.writeFile(fileName, sb.toString(), true);
            Log.d(TAG, "saveCrashInfoFile:日志地址" + fileName);
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
            sb.append("an error occured while writing file...\r\n");
        }
        return null;
    }


    /**
     * 获取要打印日志的文本
     * 未超出指定的文本大小在原日志末尾继续添加，否则重新建个日志文件
     *
     * @return
     */
    private String getFileName() {
        List<String> fileList = FileUtils.getFileNameList(getGlobalpath(), "log");
        int num = 0;
        String path = getGlobalpath();
        String fileName = "";
        if (fileList != null && fileList.size() > 0) {
            for (String str : fileList) {
                String numStr = str.substring(logName.length(), logName.length() + 1);
                try {
                    int m = Integer.valueOf(numStr);
                    if (m > num) {
                        num = m;
                    }
                } catch (Exception e) {
                }
            }

            long size = FileUtils.getFileSize(path + logName + num + ".log");
            if (size < FileSize) {//判断日志大小是否超过
                fileName = path + logName + num + ".log";
                return fileName;
            } else {
                fileName = path + logName + (num + 1) + ".log";
                FileUtils.createFile(fileName);
            }
        } else {
            fileName = getGlobalpath() + logName + (num + 1) + ".log";
        }
        return fileName;
    }


    /**
     * 最多可存放多少日志文件数，超出日志数删除
     *
     * @param logNum
     */
    private void cleanLog(int logNum) {
        Log.d(TAG, "cleanLog: 清除日志文件 logNum：" + logNum);
        String path = getGlobalpath();
        List<String> fileList = FileUtils.getFileNameList(path, "log");
        if (fileList == null && fileList.size() < logNum) {
            return;
        }
        Log.d(TAG, "cleanLog: 当前日志文件数" + fileList.size());
        List<Integer> intlist = new ArrayList<Integer>();
        for (String str : fileList) {
            String numStr = str.substring(logName.length(), logName.length() + 1);
            try {
                Integer m = Integer.valueOf(numStr);
                if (m != null) {
                    intlist.add(m);
                }
            } catch (Exception e) {
            }
        }
        if (intlist != null && intlist.size() > logNum) {
            Collections.sort(intlist);//重新排序
            for (int i = 0; i < intlist.size() - logNum; i++) {
                FileUtils.delete(path + logName + intlist.get(i) + ".log", null);
                Log.d(TAG, "cleanLog: 清除日志文件" + logName + intlist.get(i) + ".log");
            }
        }
    }

    /**
     * 获取存放日志的文件夹
     *
     * @return
     */
    public static String getGlobalpath() {
        String d = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "Demo" + File.separator;
        File file = new File(d);
        if (!file.exists()) {
            file.mkdir();
        }
        d = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "Demo"
                + File.separator + "Log" + File.separator;
        File f = new File(d);
        if (!f.exists()) {
            f.mkdir();
        }
        return d;
    }
}
