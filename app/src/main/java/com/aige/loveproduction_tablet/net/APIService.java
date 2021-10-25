package com.aige.loveproduction_tablet.net;




import com.aige.loveproduction_tablet.bean.BaseBean;
import com.aige.loveproduction_tablet.bean.DownloadBean;
import com.aige.loveproduction_tablet.bean.UserBean;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface APIService {

    //登录
    @POST("/api/UserLogin")
    Observable<BaseBean<UserBean>> getUser(@Body RequestBody body);

    //获取下载链接
    @GET("/api/Paperless/Folder/GetOrderFolderByOrderId")
    Observable<BaseBean<List<DownloadBean>>> getDownloadUrl(@Query("orderId") String orderId);

    //下载
    @Streaming
    @GET()
    Observable<ResponseBody> getFile(@Url String url);


}
