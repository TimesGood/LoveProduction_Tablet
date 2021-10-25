package com.aige.loveproduction_tablet.base;

import autodispose2.AutoDisposeConverter;

/**
 * V层基类
 */
public interface IBaseView {


    void showLoading();

    void hideLoading();

    void onError(String message);

    default void onError(String method,String message){}

    <T> AutoDisposeConverter<T> bindAutoDispose();

}