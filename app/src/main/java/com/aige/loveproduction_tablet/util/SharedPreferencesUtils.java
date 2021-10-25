package com.aige.loveproduction_tablet.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * SharedPreferences数据存储工具类
 */

public class SharedPreferencesUtils {
    //获取用户名
    public static String readLoginUserName(Context context) {
        SharedPreferences sp = context.getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        String userName = sp.getString("userName","");
        return userName;
    }

    /**
     * 保存文件
     * @param context 当前页面的activity
     * @param filename 保存在的文件民称
     * @param key key值，String
     * @param flag value值，boolean
     */
    public static void saveSetting(Context context,String filename,String key,boolean flag) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,flag);
        edit.apply();
    }

    /**
     * 保存文件
     * @param context 当前页面的activity
     * @param filename 保存在的文件民称
     * @param key key值，String
     * @param value value值，String
     */
    public static void saveSetting(Context context,String filename,String key,String value) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.apply();
    }
    /**
     * 根据key获取文件内的value
     * @param context 当前页面的activity
     * @param filename 要获取数据的文件民称
     * @param key key值，String
     * @return 获取的value值，boolean
     */
    public static boolean getBoolean(Context context,String filename,String key) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
    /**
     * 根据key获取文件内的value
     * @param context 当前页面的activity
     * @param filename 要获取数据的文件民称
     * @param key key值，String
     * @return 获取的value值，String
     */
    public static String getValue(Context context,String filename,String key) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 清空文件内容
     * @param context 当前页面的activity
     * @param filename 要清空的文件民称
     */
    public static void detailSetting(Context context,String filename) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    /**
     * 获取设置中单选为true的key值
     * @param context 当前页面的activity
     * @param filename 文件民称
     * @return
     */
    public static String getRadioSetting(Context context,String filename) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Map<String, ?> all = sp.getAll();
        for (Map.Entry<String,?> entry : all.entrySet()) {
            if ("true".equals(entry.getValue() + "")) {
                return entry.getKey().toString();
            }
        }
        return "";
    }

    /**
     * 获取设置多选为true的key列表
     * @param context
     * @param filename
     * @return
     */
    public static List<String> getCheckBoxSetting(Context context,String filename) {
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        Map<String, ?> all = sp.getAll();
        Iterator<? extends Map.Entry<String, ?>> iterator = all.entrySet().iterator();
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String,?> entry = iterator.next();
            if("true".equals(entry.getValue()+"")) {
                list.add(entry.getKey()+"");
            }
        }
        return list;
    }
}
