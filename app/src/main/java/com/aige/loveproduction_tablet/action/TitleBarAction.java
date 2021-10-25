package com.aige.loveproduction_tablet.action;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;

import com.aige.loveproduction_tablet.listener.OnTitleBarListener;


/**
 *    desc   : 标题栏意图
 */
public interface TitleBarAction extends OnTitleBarListener {

    @Nullable
    Toolbar getTitleBar();
    
    /**
     * 左项被点击
     *
     * @param view     被点击的左项View
     */
    @Override
    default void onLeftClick(View view) {}

    /**
     * 标题被点击
     *
     * @param view     被点击的标题View
     */
    @Override
    default void onTitleClick(View view) {}

    /**
     * 右项被点击
     *
     * @param view     被点击的右项View
     */
    @Override
    default void onRightClick(View view) {}

    /**
     * 隐藏默认标题
     */
    default void hideTitle() {
        if (getTitleBar() != null) {
            getTitleBar().setTitle("");
        }
    }

    /**
     * 设置标题栏默认的标题
     */
    default void setTitle(@StringRes int id) {
        if (getTitleBar() != null) {
            setTitle(getTitleBar().getResources().getString(id));
        }
    }

    /**
     * 设置标题栏默认的标题
     */
    default void setTitle(CharSequence title) {
        if (getTitleBar() != null) {
            getTitleBar().setTitle(title);
        }
    }


    /**
     * 设置居中的标题，居中的标题必须在layout文件的Toolbar标签中定义一个TextView
     * @param title 标题名
     */
    default void setCenterTitle(CharSequence title) {
        if (getTitleBar() != null && getTitleBar().getChildCount() > 0) {
            TextView textView = (TextView) getTitleBar().getChildAt(0);
            textView.setText(title);
        }
    }

    /**
     * 设置左图标，一般是返回图标
     */
    default void setLeftIcon(@DrawableRes int id) {
        if (getTitleBar() != null) {
            getTitleBar().setNavigationIcon(id);
        }
    }
    default void hideLeftIcon() {
        if (getTitleBar() != null) {
            getTitleBar().setNavigationIcon(null);
        }
    }


}