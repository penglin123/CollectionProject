package com.example.yinlian.collectionproject.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.app.BaseTopBarActivity;
import com.example.yinlian.collectionproject.http.api.NetManage;
import com.example.yinlian.collectionproject.http.bean.BooksByCatsBean;
import com.example.yinlian.collectionproject.http.bean.BooksByCatsBean1;
import com.example.yinlian.collectionproject.http.progress.ProgressObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseTopBarActivity {

    private Button button1;
    private Button button2;
    private TextView text;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        setTopBarText("hahaha");
        text=findViewById(R.id.text);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetManage.getApiService().getBooksByCats("male", "hot", "玄幻", "东方玄幻", 0, 20)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ProgressObserver<BooksByCatsBean>(MainActivity.this) {
                            @Override
                            public void onSuccess(Object o) {
                                text.setText(o.toString());
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


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetManage.getApiService().getBooksByCats1(new BooksByCatsBean1())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ProgressObserver<BooksByCatsBean>(MainActivity.this) {
                            @Override
                            public void onSuccess(Object o) {
                                text.setText(o.toString());
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
