package com.aige.loveproduction_tablet.mvp.model;

import com.aige.loveproduction_tablet.base.BaseBean;
import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.bean.DownloadBean;
import com.aige.loveproduction_tablet.mvp.contract.ApplyContract;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;

public class ApplyModel implements ApplyContract.Model {
    public static IBaseModel newInstance(){
        return new ApplyModel();
    }

    @Override
    public Observable<BaseBean<List<DownloadBean>>> getMPRByBatchNo(String batchNo) {
        return getApi().getMPRByBatchNo(batchNo);
    }

    @Override
    public Observable<ResponseBody> getFile(String url) {
        return getApi().getFile(url);
    }
}
