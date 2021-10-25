package com.aige.loveproduction_tablet.action;


import android.view.View;

import androidx.annotation.IdRes;

/**
 * 点击事件意图
 */
public interface ClickAction extends View.OnClickListener{

    /**
     * 根据id获取View对象
     * @param id
     * @param <V>
     * @return
     */
    <V extends View> V findViewById(@IdRes int id);

    /**
     * 绑定id点击事件
     * @param ids
     */
    default void setOnClickListener(@IdRes int... ids){
        setOnClickListener(this,ids);
    }

    /**
     * 根据id绑定点击事件实现
     * @param listener
     * @param ids
     */
    default void setOnClickListener(View.OnClickListener listener, int... ids) {
        for(int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    /**
     * 根据view绑定点击事件
     * @param views
     */
    default void setOnClickListener(View... views) {
        setOnClickListener(this, views);
    }

    /**
     * 根据view对象绑定点击事件实现
     * @param listener
     * @param views
     */
    default void setOnClickListener(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * 子类实现点击事件
     * @param v
     */
    @Override
    default void onClick(View v) {}
}
