package com.aige.loveproduction_tablet.mvp.contract;

import com.aige.loveproduction_tablet.base.BaseBean;
import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.base.IBasePresenter;
import com.aige.loveproduction_tablet.base.IBaseView;
import com.aige.loveproduction_tablet.bean.DownloadBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;

public interface ApplyContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<List<DownloadBean>>> getMPRByBatchNo(String batchNo);
        Observable<ResponseBody> getFile(String url);

    }
    interface View extends IBaseView {
        void onGetMPRByBatchNoSuccess(List<DownloadBean> beans);
        void onGetFileSuccess(ResponseBody body);
    }
    interface Presenter extends IBasePresenter<ApplyContract.View> {
        void getMPRByBatchNo(String batchNo);
        void getFile(String url);
    }
}
