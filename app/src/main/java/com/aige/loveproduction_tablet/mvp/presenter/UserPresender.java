package com.aige.loveproduction_tablet.mvp.presenter;

import com.aige.loveproduction_tablet.base.BasePresenter;
import com.aige.loveproduction_tablet.mvp.contract.UserContract;
import com.aige.loveproduction_tablet.mvp.model.UserModel;

public class UserPresender extends BasePresenter<UserContract.View,UserContract.Model> implements UserContract.Presender{
    @Override
    public UserContract.Model bindModel() {
        return (UserContract.Model) UserModel.newInstance();
    }
}
