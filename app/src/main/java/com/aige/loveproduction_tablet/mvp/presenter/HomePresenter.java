package com.aige.loveproduction_tablet.mvp.presenter;

import com.aige.loveproduction_tablet.base.BasePresenter;
import com.aige.loveproduction_tablet.bean.DownloadBean;
import com.aige.loveproduction_tablet.mvp.contract.HomeContract;
import com.aige.loveproduction_tablet.mvp.model.HomeModel;
import com.aige.loveproduction_tablet.net.BaseObserver;
import com.aige.loveproduction_tablet.net.RxScheduler;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

public class HomePresenter extends BasePresenter<HomeContract.View, HomeContract.Model> implements HomeContract.Presenter {
    @Override
    public HomeContract.Model bindModel() {
        return (HomeContract.Model) HomeModel.newInstance();
    }
    @Override
    public void getDownloadUrl(String order_id) {
        checkViewAttached();
        mModel.getDownloadUrl(order_id).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new BaseObserver<List<DownloadBean>>(){

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        setDisposable(d);
                        mView.showLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onSuccess(List<DownloadBean> response) {
                        mView.onGetDownloadUrlSuccess(response);
                    }

                    @Override
                    public void onError(String message) {
                        mView.hideLoading();
                        mView.onError(message);
                    }
                });

    }

    @Override
    public void getFile(String url) {
        checkViewAttached();
        mModel.getFile(url).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mView.showDownload();
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody body) {
                        mView.onGetFileSuccess(body);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onErrorDownload("文件下载异常");
                        mView.showDownload();
                    }

                    @Override
                    public void onComplete() {
                        mView.showDownload();
                    }
                });
    }
}
