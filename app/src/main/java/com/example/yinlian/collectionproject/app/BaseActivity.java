package com.example.yinlian.collectionproject.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


/**
 * Created by DeMon on 2017/9/22.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        init();
    }


    public abstract int getLayoutId();

    public abstract void init();


}
