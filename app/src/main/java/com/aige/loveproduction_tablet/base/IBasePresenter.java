package com.aige.loveproduction_tablet.base;

/**
 * P层接口
 * @param <V>
 */
public interface IBasePresenter<V extends IBaseView> {

    void onAttach(V v);

    void onDetach();
    
}