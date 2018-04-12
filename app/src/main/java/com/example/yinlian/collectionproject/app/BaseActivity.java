package com.example.yinlian.collectionproject.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


/**
 * @author penglin
 * @date 2018/4/10
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        init();
    }


    public abstract int getLayoutId();

    public abstract void init();


}
