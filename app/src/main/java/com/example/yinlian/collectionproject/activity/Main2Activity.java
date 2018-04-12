package com.example.yinlian.collectionproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.app.BaseTopBarActivity;
import com.example.yinlian.collectionproject.http.api.NetManage;
import com.example.yinlian.collectionproject.http.bean.BooksByCatsBean;
import com.example.yinlian.collectionproject.http.progress.ProgressObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author penglin
 * @date 2018/4/10
 */

public class Main2Activity extends BaseTopBarActivity {
    private Button aaa;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void init() {
        aaa = findViewById(R.id.aaa);
        aaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetManage.getApiService().getBooksByCats("male", "hot",
                        "玄幻", "东方玄幻", 0, 20)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ProgressObserver<BooksByCatsBean>(Main2Activity.this) {
                            @Override
                            public void onSuccess(Object o) {
                                Log.i("wokk", o.toString());
                            }

                            @Override
                            public void onFailure(Throwable msg) {

                            }

                            @Override
                            public void onCompleted() {

                            }
                        });
            }
        });
    }
}
