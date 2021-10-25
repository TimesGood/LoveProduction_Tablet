package com.aige.loveproduction_tablet.base;


import com.aige.loveproduction_tablet.net.APIService;
import com.aige.loveproduction_tablet.net.RetrofitClient;

/**
 * Model主要对网络请求结果进行封装
 */
public interface IBaseModel {
    /**
     * 获取网络请求Api实例
     * @return APIService实例对象
     */
    default APIService getApi() {
       return RetrofitClient.getInstance().getApi();
    }

}