package com.example.yinlian.collectionproject.testdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.PermissionUtils;
import com.example.library.base.BaseActivity;
import com.example.library.base.BaseApplication;
import com.example.library.http.api.NetManage;
import com.example.library.http.bean.BooksByCatsResponse;
import com.example.library.http.progress.BaseObserver;
import com.example.library.utils.SPManager;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.activity.LoginActivity;
import com.example.yinlian.collectionproject.activity.MainActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author penglin
 * @date 2018/4/10
 */

public class Main2Activity extends BaseActivity implements View.OnClickListener {


    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void init() {
        initTitle("11111");
        // initToolbar(R.drawable.ic_back);
        final TextView text = findViewById(R.id.text);
        text.setOnClickListener(this);
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                NetManage.getApiService(BaseApplication.getContext())
                        .getBooksByCats("male", "hot", "玄幻", "东方玄幻", 0, 10)
                        .subscribeOn(Schedulers.io())//指定Observable（被观察者）自身在哪个调度器上执行
                        .observeOn(AndroidSchedulers.mainThread())//指定一个观察者在哪个调度器上观察这个Observable
                        .subscribe(new BaseObserver<BooksByCatsResponse>(Main2Activity.this) {
                            @Override
                            protected void onSuccess(BooksByCatsResponse value) {

                                text.setText(value.books.toString());
                            }

                            @Override
                            protected void onFailure(String errorType, String msg) {

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
                startActivity(new Intent(Main2Activity.this, GreedaoActivity.class));
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, VoiceActivity.class));
            }
        });
        findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setTitle(R.string.prompt)
                        .setMessage("确定却出当前账号？")
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPManager.putToken("");
                                startActivity(new Intent(Main2Activity.this, LoginActivity.class));

                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PermissionUtils.permission(Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(new PermissionUtils.FullCallback() {
                            @Override
                            public void onGranted(List<String> permissionsGranted) {
                                startActivity(new Intent(Main2Activity.this, BaiduMapActivity.class));
                            }

                            @Override
                            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                                if (!permissionsDeniedForever.isEmpty()) {

                                    new AlertDialog.Builder(Main2Activity.this)
                                            .setTitle(android.R.string.dialog_alert_title)
                                            .setMessage("去设置权限？")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //  shouldRequest.again(true);
                                                    PermissionUtils.launchAppDetailsSettings();
                                                }
                                            })
                                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // shouldRequest.again(false);

                                                }
                                            })
                                            .setCancelable(false)
                                            .create()
                                            .show();

                                } else {
                                    Toast.makeText(Main2Activity.this, "必须同意所有权限才能使用本程序", Toast.LENGTH_LONG).show();

                                }

                            }
                        }).request();

            }
        });

    }


    @Override
    public void onClick(View v) {

        startActivity(new Intent(Main2Activity.this, MainActivity.class));

    }
}
