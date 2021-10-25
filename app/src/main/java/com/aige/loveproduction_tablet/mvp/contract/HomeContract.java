package com.aige.loveproduction_tablet.mvp.contract;

import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.base.IBasePresenter;
import com.aige.loveproduction_tablet.base.IBaseView;
import com.aige.loveproduction_tablet.bean.BaseBean;
import com.aige.loveproduction_tablet.bean.DownloadBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;

public interface HomeContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<List<DownloadBean>>> getDownloadUrl(String order_id);
        Observable<ResponseBody> getFile(String url);
    }
    interface View extends IBaseView {
        void showDownload();
        void hideDownload();
        void onErrorDownload(String msg);
        void onGetDownloadUrlSuccess(List<DownloadBean> bean);
        void onGetFileSuccess(ResponseBody body);
    }
    interface Presenter extends IBasePresenter<HomeContract.View> {
        void getDownloadUrl(String order_id);
        void getFile(String url);
    }
}
