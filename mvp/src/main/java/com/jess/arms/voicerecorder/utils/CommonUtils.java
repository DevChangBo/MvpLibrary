package com.jess.arms.voicerecorder.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * ================================================================
 * 创建时间：2018/11/16 10:38
 * 创 建 人：Mr.常
 * 文件描述：录音公共方法类
 * 码    农：你不是我记忆中Bug、但致命的程度没两样！
 * ================================================================
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";


    /**
     * check if sdcard exist
     *
     * @return
     */
    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }


    static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * get top activity
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.getClassName();
        else
            return "";
    }


    //根据时间长短计算语音条宽度:220dp
    public synchronized static int getVoiceLineWight(Context context, int seconds) {
        //1-2s是最短的。2-10s每秒增加一个单位。10-60s每10s增加一个单位。
        if (seconds <= 2) {
            return dip2px(context, 90);
        } else if (seconds <= 10) {
            //90~170
            return dip2px(context, 90 + 8 * seconds);
        } else {
            //170~220
            return dip2px(context, 170 + 10 * (seconds / 10));

        }
    }


    //根据时间长短计算语音条宽度:220dp
    public synchronized static int getVoiceLineWight2(Context context, int seconds) {
        //1-2s是最短的。2-10s每秒增加一个单位。10-60s每10s增加一个单位。
        if (seconds <= 2) {
            return dip2px(context, 60);
        } else if (seconds > 2 && seconds <= 10) {
            //90~170
            return dip2px(context, 60 + 8 * seconds);
        } else {
            //170~220
            return dip2px(context, 140 + 10 * (seconds / 10));
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @return px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @return dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
