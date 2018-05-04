package com.example.yinlian.collectionproject.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.example.library.base.BaseActivity;
import com.example.library.gesturelock.GestureEventListener;
import com.example.library.gesturelock.GestureLockViewGroup;
import com.example.library.gesturelock.GesturePasswordSettingListener;
import com.example.library.gesturelock.GestureUnmatchedExceedListener;
import com.example.library.utils.SPManager;
import com.example.library.utils.ToastUtils;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.testdemo.Main2Activity;

public class GestureLockActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private TextView tvState;
    private GestureLockViewGroup mGestureLockViewGroup;
    private Switch gestureTrajectoryIsVisible;
    private TextView tvForgetGestureCode;
    private boolean isReset = false;

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

    }

    private void initData() {

        initTitle("手势密码");
        setGestureWhenNoSet();
    }

    private void initListener() {
        gestureEventListener();
        gesturePasswordSettingListener();
        gestureRetryLimitListener();
        gestureTrajectoryIsVisible.setOnCheckedChangeListener(this);
        tvForgetGestureCode.setOnClickListener(this);

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
                    tvState.setText("密码错误，还可以输入" + SPManager.getRetryTimes() + "次");

                    Animation shake = AnimationUtils.loadAnimation(GestureLockActivity.this, R.anim.shake);//加载动画资源文件
                    tvState.startAnimation(shake); //给组件播放动画效果

                    mGestureLockViewGroup.resetView();

                    if (SPManager.getRetryTimes() == 0) {

                        SPManager.putGesturePassword("");
                        SPManager.putToken("");
                        SPManager.putRetryTimes(5);
                        AlertDialog.Builder builder = new AlertDialog.Builder(GestureLockActivity.this);
                        builder.setCancelable(false)
                                .setTitle(R.string.prompt)
                                .setMessage("您已连续五次输错手势，手势解锁关闭，请重新登录")
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
                    if (isReset) {
                        isReset = false;
                        ToastUtils.showShort("清除成功!");
                        resetGesturePattern();
                    } else {
                        tvState.setTextColor(Color.WHITE);
                        tvState.setText("手势密码正确");
                        SPManager.putRetryTimes(5);
                        startActivity(new Intent(GestureLockActivity.this, Main2Activity.class));
                        finish();
                    }
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
                    tvState.setText("再次绘制手势密码");
                    return true;
                } else {
                    tvState.setTextColor(Color.RED);
                    tvState.setText("最少连接4个点，请重新输入!");
                    Animation shake = AnimationUtils.loadAnimation(GestureLockActivity.this, R.anim.shake);//加载动画资源文件
                    tvState.startAnimation(shake); //给组件播放动画效果
                    return false;
                }
            }

            @Override
            public void onSuccess() {
                LogUtils.d("onSuccess");
                ToastUtils.showShort("手势密码设置成功");
                Intent intent = new Intent(GestureLockActivity.this, Main2Activity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFail() {
                LogUtils.d("onFail");
                tvState.setTextColor(Color.RED);
                tvState.setText("与上一次绘制不一致，请重新绘制");
                Animation shake = AnimationUtils.loadAnimation(GestureLockActivity.this, R.anim.shake);//加载动画资源文件
                tvState.startAnimation(shake); //给组件播放动画效果
            }
        });
    }

    private void gestureRetryLimitListener() {
        mGestureLockViewGroup.setGestureUnmatchedExceedListener(new GestureUnmatchedExceedListener() {
            @Override
            public void onUnmatchedExceedBoundary() {
                tvState.setTextColor(Color.RED);
                tvState.setText("错误次数过多，请稍后再试!");
            }
        });
    }


    private void setGestureWhenNoSet() {
        if (!mGestureLockViewGroup.isSetPassword()) {
            LogUtils.d("未设置密码，开始设置密码");
            tvState.setText("绘制手势密码");
            tvForgetGestureCode.setVisibility(View.INVISIBLE);
            gestureTrajectoryIsVisible.setVisibility(View.INVISIBLE);

        } else {
            if (SPManager.getGestureIsVisible()) {
                mGestureLockViewGroup.setPaintColorAndAlpha(0Xff3aa6e8, 50);
                gestureTrajectoryIsVisible.setChecked(true);
            } else {
                mGestureLockViewGroup.setPaintColorAndAlpha(0X00ffffff, 0);
                gestureTrajectoryIsVisible.setChecked(false);

            }
        }
    }

    private void resetGesturePattern() {
        mGestureLockViewGroup.removePassword();
        setGestureWhenNoSet();
        mGestureLockViewGroup.resetView();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        LogUtils.i("isChecked===" + isChecked);
        if (isChecked) {
            mGestureLockViewGroup.setPaintColorAndAlpha(0Xff3aa6e8, 50);
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
                .setMessage("忘记手势密码，需重新登录")
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
}
