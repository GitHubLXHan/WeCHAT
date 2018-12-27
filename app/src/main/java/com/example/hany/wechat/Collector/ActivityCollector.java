package com.example.hany.wechat.Collector;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/12/19 22:59
 * @filName ActivityCollector
 * @describe Activity活动管理类。
 */
public class ActivityCollector {

    public static List<Activity> activityList = new ArrayList<>();

    /**
     * 每启动一个Activity就将添加到列表中
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移出指定的Activity
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activity.finish();
        activityList.remove(activity);
    }

    /**
     * 强制结束所有活动，相当于强制关闭应用
     */
    public static void finishAll() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        activityList.clear();
    }

}
