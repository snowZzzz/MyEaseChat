package com.trade.beauty.mylibrary.utils;

import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.trade.beauty.mylibrary.base.BaseApplication;


/**
 * Created by zzz on 2017/7/12.
 */
public class ToastUtils {

    /**
     * Toast 开关<br>
     * true为开启<br>
     * false为关闭
     */
    public static boolean SHOW_DEBUG = true;

    /** 之前显示的内容 */
    private static String oldMsg ;
    /** Toast对象 */
    private static Toast toast = null ;
    /** 第一次时间 */
    private static long oneTime = 0 ;
    /** 第二次时间 */
    private static long twoTime = 0 ;

    /**
     * 防止重复显示Toast
     * @param context
     * @param message
     */
    public static void showOneToastCenter(Context context, String message){
        if(toast == null){
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show() ;
            oneTime = System.currentTimeMillis() ;
        }else{
            twoTime = System.currentTimeMillis() ;
            if(message.equals(oldMsg)){
                if(twoTime - oneTime > Toast.LENGTH_SHORT){
                    toast.show() ;
                }
            }else{
                oldMsg = message ;
                toast.setText(message) ;
                toast.show() ;
            }
        }
        oneTime = twoTime ;
    }



    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, String message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (SHOW_DEBUG)
            Toast.makeText(context, message, duration).show();
    }



    public static void showCenter(Context context, int message, int duration) {
        if (SHOW_DEBUG) {
            Toast toast=  Toast.makeText(BaseApplication.getContext(), message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    public static void showCenter(Context context, int message) {
        if (SHOW_DEBUG) {
            Toast toast=  Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
  public static void showCenter(Context context, String message) {
        if (SHOW_DEBUG) {
            Toast toast=  Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public static void showCenter(Context context, String message, int duration) {
        if (SHOW_DEBUG) {
            Toast toast=  Toast.makeText(BaseApplication.getContext(), message, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }




}
