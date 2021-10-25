package com.aige.loveproduction_tablet.mvp.contract;



import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.base.IBasePresenter;
import com.aige.loveproduction_tablet.base.IBaseView;
import com.aige.loveproduction_tablet.bean.BaseBean;
import com.aige.loveproduction_tablet.bean.UserBean;

import io.reactivex.rxjava3.core.Observable;


public interface LoginContract {
    interface Model extends IBaseModel {
        Observable<BaseBean<UserBean>> getUser(String username, String password);
    }
    interface View extends IBaseView {
        void onGetUserSuccess(BaseBean<UserBean> bean);
    }
    interface Presenter extends IBasePresenter<LoginContract.View> {
        void getUser(String username,String password);
    }
}
