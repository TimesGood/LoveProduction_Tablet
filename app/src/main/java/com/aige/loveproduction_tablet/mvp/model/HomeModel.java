package com.aige.loveproduction_tablet.mvp.model;

import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.bean.BaseBean;
import com.aige.loveproduction_tablet.bean.DownloadBean;
import com.aige.loveproduction_tablet.mvp.contract.HomeContract;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;

public class HomeModel implements HomeContract.Model {
    public static IBaseModel newInstance() {
        return new HomeModel();
    }

    @Override
    public Observable<BaseBean<List<DownloadBean>>> getDownloadUrl(String order_id) {
        return getApi().getDownloadUrl(order_id);
    }

    @Override
    public Observable<ResponseBody> getFile(String url) {
        return getApi().getFile(url);
    }
}
