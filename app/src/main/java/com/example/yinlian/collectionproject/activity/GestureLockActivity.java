package com.example.yinlian.collectionproject.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.example.library.base.BaseActivity;
import com.example.library.gesturelock.GestureEventListener;
import com.example.library.gesturelock.GestureLockViewGroup;
import com.example.library.gesturelock.GesturePasswordSettingListener;
import com.example.library.utils.SPManager;
import com.example.library.utils.ToastUtils;
import com.example.yinlian.collectionproject.R;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

/**
 * - @Description:  手势密码设置与解锁，还包含指纹解锁
 * - @Author:  penglin
 * - @Time:  2018/4/20
 */
public class GestureLockActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private TextView tvState;
    private GestureLockViewGroup mGestureLockViewGroup;
    private Switch gestureTrajectoryIsVisible;
    private TextView tvForgetGestureCode;
    private ImageView ivFingerprint;

    //指纹解锁目前存在内存泄露，所以注释掉了
    private FingerprintIdentify mFingerprintIdentify;

    @Override
    public int getLayoutId() {
        return R.layout.activity_gesture_lock;
    }

    @Override
    public void init() {
        initView();
        initData();
        initListener();


    }

    private void initView() {
        tvState = findViewById(R.id.tv_state);
        mGestureLockViewGroup = findViewById(R.id.gesturelock);
        gestureTrajectoryIsVisible = findViewById(R.id.gesture_trajectory_is_visible);
        tvForgetGestureCode = findViewById(R.id.tv_forget_gesture_code);
        ivFingerprint = findViewById(R.id.iv_fingerprint);

    }

    private void initData() {

        initTitle(getString(R.string.gesture_password));


        setGestureWhenNoSet();

    }

    private void initListener() {

        gestureEventListener();//手势监听
        gesturePasswordSettingListener();//手势设置监听
        gestureTrajectoryIsVisible.setOnCheckedChangeListener(this);
        tvForgetGestureCode.setOnClickListener(this);

    }

    //指纹识别监听
    private void initVerify() {

        mFingerprintIdentify.startIdentify(5, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                LogUtils.i("onSucceed");
                tvState.setTextColor(Color.parseColor("#888888"));
                tvState.setText(R.string.fingerprint_verification_success);
                startActivity(new Intent(GestureLockActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onNotMatch(int availableTimes) {

                tvState.setTextColor(Color.RED);
                tvState.setText(R.string.fingerprint_verification_failed);
                startAnimation();
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                LogUtils.i("onFailed");

            }

            @Override
            public void onStartFailedByDeviceLocked() {
                LogUtils.i("onStartFailedByDeviceLocked");
            }

        });
    }

    private void gestureEventListener() {
        mGestureLockViewGroup.setGestureEventListener(new GestureEventListener() {
            @Override
            public void onDown() {
                gestureTrajectoryIsVisible.setEnabled(false);
                tvForgetGestureCode.setEnabled(false);
            }

            @Override
            public void onGestureEvent(boolean matched) {
                LogUtils.d("onGestureEvent matched: " + matched);
                gestureTrajectoryIsVisible.setEnabled(true);
                tvForgetGestureCode.setEnabled(true);
                if (!matched) {
                    tvState.setTextColor(Color.RED);
                    tvState.setText(String.format(getString(R.string.number_of_password_errors), SPManager.getRetryTimes()));
                    startAnimation();

                    mGestureLockViewGroup.resetView();

                    if (SPManager.getRetryTimes() == 0) {

                        SPManager.putGesturePassword("");
                        SPManager.putToken("");
                        SPManager.putRetryTimes(5);
                        AlertDialog.Builder builder = new AlertDialog.Builder(GestureLockActivity.this);
                        builder.setCancelable(false)
                                .setTitle(R.string.prompt)
                                .setMessage(R.string.gesture_unlock_close)
                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        startActivity(new Intent(GestureLockActivity.this, LoginActivity.class));
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .show();
                    }

                } else {
                    tvState.setTextColor(Color.parseColor("#888888"));
                    tvState.setText(R.string.correct_gesture_password);
                    SPManager.putRetryTimes(5);
                    startActivity(new Intent(GestureLockActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void gesturePasswordSettingListener() {
        mGestureLockViewGroup.setGesturePasswordSettingListener(new GesturePasswordSettingListener() {
            @Override
            public boolean onFirstInputComplete(int len) {
                LogUtils.d("onFirstInputComplete");
                if (len > 3) {
                    tvState.setTextColor(Color.parseColor("#888888"));
                    tvState.setText(R.string.again_draw_the_password);
                    return true;
                } else {
                    tvState.setTextColor(Color.RED);
                    tvState.setText(R.string.least_connect_4_points);
                    startAnimation();
                    return false;
                }
            }

            @Override
            public void onSuccess() {
                LogUtils.d("onSuccess");
                ToastUtils.showShort(R.string.gestures_password_successfully_set);
                Intent intent = new Intent(GestureLockActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail() {
                LogUtils.d("onFail");
                tvState.setTextColor(Color.RED);
                tvState.setText(R.string.repaint);
                startAnimation();
            }
        });
    }

    //判断是否设置过手势密码
    private void setGestureWhenNoSet() {

        if (!mGestureLockViewGroup.isSetPassword()) {
            tvState.setText(R.string.draw_the_password);
            tvForgetGestureCode.setVisibility(View.INVISIBLE);
            gestureTrajectoryIsVisible.setVisibility(View.INVISIBLE);
            ivFingerprint.setVisibility(View.GONE);

        } else {
            //开启指纹识别
            //  mFingerprintIdentify = new FingerprintIdentify(this);
            //判断指纹识别是否可用
//            if (mFingerprintIdentify.isFingerprintEnable()) {
//
//                tvState.setText(R.string.please_use_fingerprint_or_manual_password_unlock);
//                ivFingerprint.setVisibility(View.VISIBLE);
//
//                initVerify(); //初始化指纹识别监听
//            } else {
            tvState.setText(R.string.please_use_manual_password_unlock);
//                ivFingerprint.setVisibility(View.GONE);
//                mFingerprintIdentify.cancelIdentify();
//            }

            if (SPManager.getGestureIsVisible()) {
                mGestureLockViewGroup.setPaintColorAndAlpha(0Xff7990ff, 50);
                gestureTrajectoryIsVisible.setChecked(true);
            } else {
                mGestureLockViewGroup.setPaintColorAndAlpha(0X00ffffff, 0);
                gestureTrajectoryIsVisible.setChecked(false);

            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            mGestureLockViewGroup.setPaintColorAndAlpha(0Xff7990ff, 50);
            SPManager.putGestureIsVisible(true);
        } else {
            mGestureLockViewGroup.setPaintColorAndAlpha(0X00ffffff, 0);
            SPManager.putGestureIsVisible(false);
        }
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(GestureLockActivity.this);
        builder.setTitle(R.string.prompt)
                .setMessage(R.string.forget_gesture_code)
                .setPositiveButton(R.string.login_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPManager.putGesturePassword("");
                        SPManager.putToken("");
                        SPManager.putRetryTimes(5);
                        startActivity(new Intent(GestureLockActivity.this, LoginActivity.class));
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void startAnimation() {
        //加载动画资源文件
        Animation shake = AnimationUtils.loadAnimation(GestureLockActivity.this, R.anim.shake);
        //给组件播放动画效果
        tvState.startAnimation(shake);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mFingerprintIdentify = null;
    }
}
