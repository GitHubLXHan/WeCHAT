package com.example.hany.wechat.Util;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/8 23:24
 * @filName StatusBarUtil
 * @describe ...
 */

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 制作本工具类的说明：
 *      有时候活动会涉及到浅色状态栏的需求，
 *      默认的状态栏上字体和图标颜色都是白色的。
 *      当我们想要设置状态栏为白色的时候，
 *      就必须要求使用浅色状态栏，
 *      浅色状态栏的字体和图标颜色都是黑色的。
 *
 *      比较坑爹的是能够应用浅色状态栏的系统比较少，
 *      安卓官方在6.0以后才加入了这个选项。
 *      而除此以外，
 *      小米系统MIUI V6以上和魅族系统Flyme4以上都有自己的浅色状态栏设置方案。
 *      我用自己的小米手机测试了下，
 *      我的手机是6.0的，
 *      但是官方设置浅色状态栏无效，
 *      使用针对小米的方案则成功。
 *
 *      所以现在思路比较清晰了，
 *      首先获取手机系统版本信息，
 *      然后判断是否可以使用浅色状态栏，
 *      可以的话则根据版本选择使用对应的方案。
 *      这一整个部分的代码量比较大，
 *      而且可复用性比较高，
 *      所以不直接放在活动中，
 *      而是封装在工具类的一个静态方法里。
 *
 *      说明：本类大部分内容来自http://www.th7.cn/Program/Android/201701/1079877.shtml
 */

public class StatusBarUtil {


    /**
     * 判断是否为魅族的Flyme4以上的系统
     * Flyme V4 的display格式为[Flyme OS 4.x.x.xA]
     * Flyme V5 的display格式为[Flyme OS 5.x.x.x.beta]
     * @return
     */
    private static boolean isFlymeV4OrAbove() {
        String displayId = Build.DISPLAY;
        if (!TextUtils.isEmpty(displayId) && displayId.contains("Flyme")) {
            String[] displayIdArray = displayId.split(" ");
            for (String temp : displayIdArray) {
                //版本号4以上，形如4.x.
                if (temp.matches("^[4-9]//.(//d+//.)+//S*")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否为小米的MIUI系统
     * MIUI V6对应的versionCode是4
     * MUNI V7对应的versionCode是5
     * @return
     */
    private static boolean isMIUIV6OrAbove() {
        // 调用获取系统信息函数，获取系统信息
        String miuiVersionCodeStr = getSystemProperty("ro.miui.ui.version.code");
        if (!TextUtils.isEmpty(miuiVersionCodeStr)) {
            try {
                int miuiVersionCode = Integer.parseInt(miuiVersionCodeStr);
                if (miuiVersionCode >= 4) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    //Android Api 23以上

    /**
     * 判断是否为普通Android API 23以上
     * @return
     */
    private static boolean isAndroidMOrAbove() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }


    /**
     * 获取系统信息
     * @param propName
     * @return
     */
    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }


    /**
     * 综合上面三个判断是否可以设置状态栏文字颜色的方法
     * 用作外部调用接口
     * @param activity
     * @return
     */
    public static String tryLightStatus(Activity activity){
        String systemType = null;
        if (isMIUIV6OrAbove()){
//            MIUISetStatusBarLightMode(activity);
            systemType = "MIUI";
        }
        if (isFlymeV4OrAbove()){
//            setFlymeLightStatusBar(activity);
            systemType = "Flyme";
        }
        if (isAndroidMOrAbove()){
//            setAndroidNativeLightStatusBar(activity);
            systemType = "AndroidM";
        }
        return systemType;
    }


    /**
     * MIUI6以上设置状态栏文字颜色
     * @param activity
     * @param dark 是否把状态栏文字及图标设置为深色：true表示设置为深色；false表示取消深色；
     * @return
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window=activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if(dark){
                        activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            }catch (Exception e){

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param activity 需要设置的活动
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 普通Android 6.0 以上设置状态栏文字颜色
     * @param activity
     */
    public static void AndroidMSetStatusBarLightMode(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            /*
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 是从API 16开始启用，实现效果：
            视图延伸至状态栏区域，状态栏悬浮于视图之上*/

            /*
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 是从 API 23开始启用，实现效果：
            设置状态栏图标和状态栏文字颜色为深色，为适应状态栏背景为浅色调，该Flag只有在使用了FLAG_DRWS_SYSTEM_BAR_BACKGROUNDS，
            并且没有使用FLAG_TRANSLUCENT_STATUS时才有效，即只有在透明状态栏时才有效。*/

        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }



    /**
     * 修改状态栏颜色，支持4.4以上版本
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity,int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
//      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        }
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
//            transparencyBar(activity);
//            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(colorId);
//        }
    }


    /**
     * 修改状态栏为全透明
     * 4.4以上版本
     * @param activity
     */
    public static void transparencyBar(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window =activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
