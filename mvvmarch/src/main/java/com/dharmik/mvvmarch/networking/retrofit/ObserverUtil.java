package com.dharmik.mvvmarch.networking.retrofit;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ObserverUtil {

    /**
     * @param observable make it via ApiClient.getClient().create(Class_Name).method()
     * @return Single which helps in adding other functions like flatMap(), filter()
     */
    public static <T> Single<T> makeSingle(Observable<T> observable) {
        return Single.fromObservable(observable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    /**
     * @return SingleObserver with which observable will subscribe
     */
    public static <T> SingleObserver<T> getSingleObserver(final CompositeDisposable compositeDisposable,
                                                          final WebserviceBuilder.ApiNames apiNames,
                                                          final SingleCallback tSingleCallback) {
        return new SingleObserver<T>() {

            @Override
            public void onSubscribe(Disposable d) {
                if (compositeDisposable != null) compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(@NonNull T t) {
                if (tSingleCallback != null) tSingleCallback.onSingleSuccess(t, apiNames);
            }


            @Override
            public void onError(Throwable e) {
                if (tSingleCallback != null) tSingleCallback.onFailure(e, apiNames);
            }
        };
    }
}