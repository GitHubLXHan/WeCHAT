package com.example.hany.wechat.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/13 16:43
 * @filName ScreenSizeUtil
 * @describe 测量手机屏幕宽高的工具类
 */


public class ScreenSizeUtil {
    private static ScreenSizeUtil instance = null;
    private int screenWidth, screenHeight;

    /**
     * 单例模式
     * @param context
     * @return
     */
    public static ScreenSizeUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (ScreenSizeUtil.class) {
                if (instance == null) {
                    instance = new ScreenSizeUtil(context);
                }
            }
        }
        return instance;
    }

    /**
     * 核心部分
     * 测量屏幕
     * @param context
     */
    private ScreenSizeUtil(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels; // 获取屏幕分辨率宽度
        screenHeight = displayMetrics.heightPixels; // 获取屏幕分辨率高度
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public int getScreenWidth() {
        return this.screenWidth;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public int getScreenHeight() {
        return this.getScreenHeight();
    }
}
