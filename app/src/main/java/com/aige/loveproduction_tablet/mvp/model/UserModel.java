package com.aige.loveproduction_tablet.mvp.model;

import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.mvp.contract.UserContract;

public class UserModel implements UserContract.Model {
    public static IBaseModel newInstance() {
        return new UserModel();
    }
}
