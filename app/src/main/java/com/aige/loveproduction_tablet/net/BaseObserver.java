package com.aige.loveproduction_tablet.net;



import com.aige.loveproduction_tablet.base.BaseBean;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import retrofit2.HttpException;

/**
 * 自定义Observer，对请求回来的数据进行一些业务上的处理
 */
public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {

    @Override
    public void onNext(@NonNull BaseBean<T> response) {
        if(response.getCode() != 0) {
            onError(response.getMsg());
        }else{
            onSuccess(response.getData());
        }
    }
    @Override
    public void onError(@NonNull Throwable e) {
        if(e instanceof SocketTimeoutException) {
            onError("请求超时");
        }else if(e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            if(exception.code() == 500) {
                onError("找不到数据");
            }else if(exception.code() == 404) {
                onError("服务器错误");
            }else{
                onError(e.getMessage());
            }
        }else if(e instanceof ConnectException){
            onError("请连接网络后重试");
        }else{
            onError(e.getMessage());
        }
    }


    public abstract void onSuccess(T response);
    public abstract void onError(String message);

}