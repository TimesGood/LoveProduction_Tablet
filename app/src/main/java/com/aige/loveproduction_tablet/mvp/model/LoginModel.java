package com.aige.loveproduction_tablet.mvp.model;

import com.aige.loveproduction_tablet.base.IBaseModel;
import com.aige.loveproduction_tablet.base.BaseBean;
import com.aige.loveproduction_tablet.bean.UserBean;
import com.aige.loveproduction_tablet.mvp.contract.LoginContract;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginModel implements LoginContract.Model {
    private final Gson gson = new Gson();
    public static IBaseModel newInstance() {
        return new LoginModel();
    }
    @Override
    public Observable<BaseBean<UserBean>> getUser(String username, String password) {
        Map<String,String> map = new HashMap<>();
        map.put("userName",username);
        map.put("password",password);
        RequestBody body = RequestBody.Companion.create(gson.toJson(map), MediaType.parse("application/json;charset=utf-8"));
        return getApi().getUser(body);
    }
}
