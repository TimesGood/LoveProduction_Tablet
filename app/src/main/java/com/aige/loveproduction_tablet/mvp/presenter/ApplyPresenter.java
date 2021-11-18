package com.aige.loveproduction_tablet.mvp.presenter;

import com.aige.loveproduction_tablet.base.BasePresenter;
import com.aige.loveproduction_tablet.bean.DownloadBean;
import com.aige.loveproduction_tablet.mvp.contract.ApplyContract;
import com.aige.loveproduction_tablet.mvp.model.ApplyModel;
import com.aige.loveproduction_tablet.net.BaseObserver;
import com.aige.loveproduction_tablet.net.RxScheduler;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;

public class ApplyPresenter extends BasePresenter<ApplyContract.View, ApplyContract.Model> implements ApplyContract.Presenter{
    @Override
    public ApplyContract.Model bindModel() {
        return (ApplyContract.Model) ApplyModel.newInstance();
    }

    @Override
    public void getMPRByBatchNo(String batchNo) {
        mModel.getMPRByBatchNo(batchNo).compose(RxScheduler.Obs_io_main())
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
                        mView.onGetMPRByBatchNoSuccess(response);
                        DownloadBean downloadBean = response.get(0);
                        getFile(downloadBean.getFileUrl());
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
        mModel.getFile(url).compose(RxScheduler.Obs_io_main())
                .to(mView.bindAutoDispose())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        setDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody body) {
                        mView.onGetFileSuccess(body);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mView.onError("文件下载异常");
                        mView.hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
}
