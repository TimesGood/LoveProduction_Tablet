package com.aige.loveproduction_tablet.mvp.model;

import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.mvp.contract.HomeContractBack;

public class HomeModelBack implements HomeContractBack.Model {
    public static IBaseModel newInstance() {
        return new HomeModelBack();
    }
}
