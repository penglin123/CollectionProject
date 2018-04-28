package com.example.yinlian.collectionproject.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.testdemo.Main2Activity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author lenovo
 */
public class WelcomeActivity extends AppCompatActivity {

    private MyHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarVisibility(this, false);
        setContentView(R.layout.activity_welcome);

        mHandler = new MyHandler(this);

        PermissionUtils.permission(Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO, Manifest.permission_group.CAMERA)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        if (!permissionsDeniedForever.isEmpty()) {

                            new AlertDialog.Builder(WelcomeActivity.this)
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
                                            finish();

                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();

                        } else {
                            //  Toast.makeText(WelcomeActivity.this, "必须同意所有权限才能使用本程序", Toast.LENGTH_LONG).show();
                            mHandler.sendEmptyMessageDelayed(0, 1000);
                        }

                    }
                }).request();

    }


    private static class MyHandler extends Handler {
        private WeakReference<WelcomeActivity> mActivity;

        MyHandler(WelcomeActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivity.get().getHome();
        }
    }

    public void getHome() {
        String token = SPUtils.getInstance().getString("token");
        LogUtils.i(token);
        if (!StringUtils.isEmpty(token)) {
            Intent intent = new Intent(WelcomeActivity.this, Main2Activity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  If null, all callbacks and messages will be removed.
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {

    }
}
