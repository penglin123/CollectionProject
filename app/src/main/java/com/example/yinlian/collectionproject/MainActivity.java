package com.example.yinlian.collectionproject;

import com.example.yinlian.collectionproject.app.BaseActivity;
import com.example.yinlian.collectionproject.http.api.NetManage;
import com.example.yinlian.collectionproject.http.progress.ObserverOnNextListener;
import com.example.yinlian.collectionproject.http.progress.ProgressObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void init() {


        NetManage.getApiService().getLocation("aaa")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressObserver<String>(this, new ObserverOnNextListener() {
                    @Override
                    public void onNext(Object o) {

                    }
                }));
//        .subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }


}
