package com.aige.loveproduction_tablet.net;

import org.reactivestreams.Publisher;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeSource;
import io.reactivex.rxjava3.core.MaybeTransformer;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 统一线程处理
 */
public class RxScheduler {

    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return FlowableTransformer
     */
    public static <T> FlowableTransformer< T, T> Flo_io_main() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())//切换到io()线程执行请求
                        .observeOn(AndroidSchedulers.mainThread());//切换到主线程，处理请求结果
            }
        };
    }

    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> Obs_io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply( Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return MaybeTransformer
     */
    public static <T> MaybeTransformer<T,T> May_io_main() {
        return new MaybeTransformer<T, T>() {
            @Override
            public @NonNull MaybeSource<T> apply(@NonNull Maybe<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型类型
     * @return SingleTransformer
     */
    public static <T>SingleTransformer<T,T> Sin_io_main() {
        return new SingleTransformer<T, T>() {
            @Override
            public @NonNull SingleSource<T> apply(@NonNull Single<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
