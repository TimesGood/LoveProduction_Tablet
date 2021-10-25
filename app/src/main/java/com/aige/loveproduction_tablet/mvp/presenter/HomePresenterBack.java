package com.aige.loveproduction_tablet.mvp.presenter;

import com.aige.loveproduction_tablet.base.BasePresenter;
import com.aige.loveproduction_tablet.mvp.contract.HomeContractBack;
import com.aige.loveproduction_tablet.mvp.model.HomeModelBack;

public class HomePresenterBack extends BasePresenter<HomeContractBack.View, HomeContractBack.Model> implements HomeContractBack.Persenter {
    @Override
    public HomeContractBack.Model bindModel() {
        return (HomeContractBack.Model) HomeModelBack.newInstance();
    }
}
