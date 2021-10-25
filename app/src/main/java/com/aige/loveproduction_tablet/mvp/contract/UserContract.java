package com.aige.loveproduction_tablet.mvp.contract;

import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.base.IBasePresenter;
import com.aige.loveproduction_tablet.base.IBaseView;

public interface UserContract {
    interface Model extends IBaseModel{

    }
    interface View extends IBaseView {

    }
    interface Presender extends IBasePresenter<UserContract.View> {

    }
}
