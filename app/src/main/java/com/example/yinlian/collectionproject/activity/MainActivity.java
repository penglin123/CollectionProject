package com.example.yinlian.collectionproject.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.app.BaseTopBarActivity;
import com.example.yinlian.collectionproject.db.helper.DBHelper;
import com.example.yinlian.collectionproject.db.gen.UserDao;
import com.example.yinlian.collectionproject.http.api.NetManage;
import com.example.yinlian.collectionproject.http.bean.BooksByCatsBean;
import com.example.yinlian.collectionproject.http.progress.ProgressObserver;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author penglin
 * @date 2018/4/10
 */

public class MainActivity extends BaseTopBarActivity {


    private TextView text;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        BarUtils.setStatusBarColor(this, Color.parseColor("#ff0000"));
        setTopBarText("测试");
        text = findViewById(R.id.text);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NetManage.getApiService().getBooksByCats("male", "hot",
                        "玄幻", "东方玄幻", 0, 20)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ProgressObserver<BooksByCatsBean>(MainActivity.this) {
                            @Override
                            public void onSuccess(Object o) {
                                if (o instanceof BooksByCatsBean) {
                                    text.setText(o.toString());
                                    BooksByCatsBean booksByCatsBean= (BooksByCatsBean) o;
                                    LogUtils.json(booksByCatsBean.books.toString());
                                }


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
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

    }


}
