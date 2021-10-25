package com.aige.loveproduction_tablet.base;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;


/**
 * P层基类，持有V层、M层，主要对其解耦及绑定对应V层、M层
 * @param <V> 继承IBaseView的接口
 * @param <M> 继承IBaseModel的接口
 */
public abstract class BasePresenter<V extends IBaseView,M extends IBaseModel> implements IBasePresenter<V> {
    protected V mView;
    protected M mModel;
    //protected Disposable mDisposable;

    /**
     * Disposable管理容器
     */
    protected CompositeDisposable mDisposable;

    public BasePresenter() {
        this.mModel = bindModel();
        mDisposable = new CompositeDisposable();
    }
    public void setDisposable(Disposable disposable) {
        mDisposable.add(disposable);
    }
    public void dispose() {
        if(!mDisposable.isDisposed()) mDisposable.dispose();
    }

    /**
     * View是否已经绑定
     */
    public boolean isViewAttached() {
        return mView != null;
    }

    /**
     * 核查View是否已经绑定
     */
    public void checkViewAttached() {
        if (!isViewAttached()) throw new RuntimeException("未注册View");
    }

    /**
     * 绑定View
     * @param v
     */
    @Override
    public void onAttach(V v) {
        mView = v;
        if (mModel == null) {
            throw new NullPointerException("未绑定Model");
        }
    }

    /**
     * 子类重写，返回需要绑定的Model
     */
    public abstract M bindModel();

    /**
     * 解除绑定释放资源
     */
    @Override
    public void onDetach() {
        dispose();
        mView = null;
    }

    /**
     * 对请求网络异常进行统一处理
     */
    public void analysisThrowable(Throwable e){
        if(e instanceof SocketTimeoutException) {
            mView.onError( "请求超时");
        }else if(e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            if(exception.code() == 500) mView.onError("找不到数据");
        }else if(e instanceof ConnectException){
            mView.onError("请连接网络后重试");
        }else{
            mView.onError(e.getMessage());
        }
    }

    public void analysisThrowable(Throwable e,String methodName){
            if(e instanceof SocketTimeoutException) {
                mView.onError(methodName, "请求超时");
            }else if(e instanceof HttpException) {
                HttpException exception = (HttpException) e;
                if(exception.code() == 500) mView.onError("找不到数据");
            }else if(e instanceof ConnectException){
                mView.onError(methodName,"请连接网络后重试");
            }else{
                mView.onError(methodName,e.getMessage());
            }
    }


}
