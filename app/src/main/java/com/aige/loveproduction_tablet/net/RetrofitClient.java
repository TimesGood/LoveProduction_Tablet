package com.aige.loveproduction_tablet.net;



import com.aige.loveproduction_tablet.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Retrofit的配置、初始化、创建实例
 */
public class RetrofitClient {
    private static volatile RetrofitClient instance;
    private APIService apiService;
    private static final String baseUrl = BuildConfig.SERVER;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;

    private RetrofitClient() {
    }

    /**
     * 获创建单一实例，对该对象上锁，防止多线程创建多个对象
     *
     * @return
     */
    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }
    /**
     * 创建Retrofit实例与网络接口实例
     * @return
     */
    public APIService getApi() {
        //创建Retrofit实例
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //设置网络请求的Url地址
                    .baseUrl(baseUrl)
                    //设置数据解析器
                    .addConverterFactory(GsonConverterFactory.create())
                    //设置网络请求适配器，使其支持RxJava与RxAndroid
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        //创建网络请求接口实例
        if (apiService == null) {
            apiService = retrofit.create(APIService.class);
        }
        return apiService;
    }

    /**
     * 创建OkHttp实例
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            //对debug和release分别做不同处理
            if (BuildConfig.DEBUG) {
                okHttpClient = new OkHttpClient().newBuilder()
                        //默认重试一次，若需要重试N次，则要实现拦截器
                        .retryOnConnectionFailure(true)
                        //请求头拦截
                        .addInterceptor(new HeaderInterceptor())
                        //设置日志拦截
                        .addInterceptor(getInterceptor())
                        //设置dns
                        .dns(new ApiDns())
                        .build();
            } else {
                okHttpClient = new OkHttpClient().newBuilder()
                        .retryOnConnectionFailure(true)
                        //请求头拦截
                        .addInterceptor(new HeaderInterceptor())
                        //设置dns
                        .dns(new ApiDns())
                        .build();
            }
        }
        return okHttpClient;
    }
    /**
     * 设置Header拦截器，统一请求头
     *
     * @return
     */
    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {

                //拿到请求的实例对象
                Request original = chain.request();
                //添加请求头
                Request request = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build();
                return chain.proceed(request);
            }
        };

    }

    /**
     * 自定义请求头拦截
     */
    public class HeaderInterceptor implements Interceptor {


        public static final String CONNECT_TIMEOUT = "CONNECT_TIMEOUT";
        public static final String READ_TIMEOUT = "READ_TIMEOUT";
        public static final String WRITE_TIMEOUT = "WRITE_TIMEOUT";

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {

            int connectTimeout = chain.connectTimeoutMillis();
            int readTimeout = chain.readTimeoutMillis();
            int writeTimeout = chain.writeTimeoutMillis();
            //获取request对象并统一请求头
            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build();
            String connectNew = request.header(CONNECT_TIMEOUT);
            String readNew = request.header(READ_TIMEOUT);
            String writeNew = request.header(WRITE_TIMEOUT);
            if(connectNew != null) connectTimeout = Integer.parseInt(connectNew);

            if(readNew != null) readTimeout = Integer.parseInt(readNew);

            if(writeNew != null) writeTimeout = Integer.parseInt(writeNew);

            return chain
                    .withConnectTimeout(connectTimeout, TimeUnit.SECONDS)
                    .withReadTimeout(readTimeout, TimeUnit.SECONDS)
                    .withWriteTimeout(writeTimeout, TimeUnit.SECONDS)
                    .proceed(request);
        }
    }
    /**
     * 设置日志拦截器
     */
    private Interceptor getInterceptor() {
        //获取日志拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //更改拦截器的输出级别，只输出Body
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }





}
