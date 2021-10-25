package com.aige.loveproduction_tablet.action;


import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘相关意图
 */
public interface KeyboardAction {
    /**
     * 显示软键盘
     * @param view 软键盘获得焦点的Edit对象
     */
    default void showKeyboard(View view) {
        if (view == null) return;
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(manager != null) {
            manager.showSoftInput(view,0);
        }
    }

    /**
     * 隐藏软键盘
     * @param view 软键盘获得焦点的Edit对象
     */
    default void hideKeyboard(View view){
        if(view == null) return;
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
