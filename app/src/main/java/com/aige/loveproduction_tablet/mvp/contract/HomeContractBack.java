package com.aige.loveproduction_tablet.mvp.contract;

import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.base.IBasePresenter;
import com.aige.loveproduction_tablet.base.IBaseView;

public interface HomeContractBack {
    interface Model extends IBaseModel {

    }
    interface View extends IBaseView {

    }
    interface Persenter extends IBasePresenter<HomeContractBack.View> {

    }
}
