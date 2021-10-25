package com.aige.loveproduction_tablet.mvp.presenter;

import com.aige.loveproduction_tablet.base.BasePresenter;
import com.aige.loveproduction_tablet.bean.BaseBean;
import com.aige.loveproduction_tablet.bean.UserBean;
import com.aige.loveproduction_tablet.mvp.contract.LoginContract;
import com.aige.loveproduction_tablet.mvp.model.LoginModel;
import com.aige.loveproduction_tablet.net.RxScheduler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class LoginPresenter extends BasePresenter<LoginContract.View,LoginContract.Model> implements LoginContract.Presenter {

    @Override
    public LoginContract.Model bindModel() {
        return (LoginContract.Model) LoginModel.newInstance();
    }

    @Override
    public void getUser(String username, String password) {
        checkViewAttached();
        mModel.getUser(username, password).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<BaseBean<UserBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(@NonNull BaseBean<UserBean> bean) {
                        mView.onGetUserSuccess(bean);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        analysisThrowable(e);
                        mView.hideLoading();

                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
    @Override
    public void analysisThrowable(Throwable e){
        if(e instanceof SocketTimeoutException) {
            mView.onError( "登录超时");
        }else if(e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            if(exception.code() == 500) mView.onError("没有该用户");
        }else if(e instanceof ConnectException){
            mView.onError("请连接网络后重试");
        }else{
            mView.onError(e.getMessage());
        }
    }
}
