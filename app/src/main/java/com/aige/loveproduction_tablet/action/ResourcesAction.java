package com.aige.loveproduction_tablet.action;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * Context意图，资源获取封装
 */
public interface ResourcesAction {

    Context getContext();

    default Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 根据id获取String资源
     * @param id
     * @return
     */
    default String getString(@StringRes int id) {
        return getContext().getString(id);
    }

    default String getString(@StringRes int id, Object... formatArgs) {
        return getResources().getString(id, formatArgs);
    }

    /**
     * 根据id获取Drawable文件
     * @param id
     * @return
     */
    default Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(getContext(), id);
    }

    /**
     * 根据id获取Color资源
     * @param id
     * @return
     */
    @ColorInt
    default int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    default <S> S getSystemService(@NonNull Class<S> serviceClass) {
        return ContextCompat.getSystemService(getContext(), serviceClass);
    }
}