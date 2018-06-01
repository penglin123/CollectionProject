package com.example.yinlian.collectionproject.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.library.base.BaseActivity;
import com.example.library.utils.FileProvider7;
import com.example.library.utils.ToastUtils;
import com.example.library.voiceutil.RecordVoiceButton;
import com.example.library.voiceutil.VoiceBean;
import com.example.library.voiceutil.VoiceManager;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.testdemo.BaiduMapActivity;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 客户详情
 *
 * @author penglin
 * @date 2018/5/18
 */

public class CustomerDetailsActivity extends BaseActivity implements View.OnClickListener {
    private BridgeWebView bridgeWebView;
    private View bg;
    private VoiceManager voiceManager;
    private RecordVoiceButton mBtRec;

    private FloatingActionButton mMenuButton;
    private FloatingActionButton mItemButton1;
    private FloatingActionButton mItemButton2;
    private FloatingActionButton mItemButton3;
    private FloatingActionButton mItemButton4;
    private FloatingActionButton mItemButton5;

    private boolean mIsMenuOpen = false;

    private boolean whetherOrNotSignIn = false;
    private static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    private String mCurrentPhotoPath;

    @Override
    public int getLayoutId() {
        return R.layout.activuty_customer_details;
    }

    @Override
    public void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        bridgeWebView = findViewById(R.id.webView);
        mMenuButton = findViewById(R.id.menu);
        mItemButton1 = findViewById(R.id.item1);
        mItemButton2 = findViewById(R.id.item2);
        mItemButton3 = findViewById(R.id.item3);
        mItemButton4 = findViewById(R.id.item4);
        mItemButton5 = findViewById(R.id.item5);
        bg = findViewById(R.id.bg);
    }

    private void initData() {
        initTitleAndToolbarBack("客户详情", R.drawable.ic_back);
        bridgeWebView.loadUrl("file:///android_asset/customer_details.html");
        voiceManager = new VoiceManager(mContext);
        mBtRec = new RecordVoiceButton(mContext);

    }

    private void initListener() {
        mMenuButton.setOnClickListener(this);
        mItemButton1.setOnClickListener(this);
        mItemButton2.setOnClickListener(this);
        mItemButton3.setOnClickListener(this);
        mItemButton4.setOnClickListener(this);
        mItemButton5.setOnClickListener(this);
        bg.setOnClickListener(this);

        //js调java ----- 录音播放
        bridgeWebView.registerHandler("recordPlay", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtils.i(data);
                if (voiceManager.isPlaying()) {
                    voiceManager.stopPlay();
                } else {
                    voiceManager.stopPlay();
                    voiceManager.startPlay(data);
                }

            }
        });

        mBtRec.setEnrecordVoiceListener(new RecordVoiceButton.EnRecordVoiceListener() {
            @Override
            public void onFinishRecord(long length, String strLength, String filePath) {
                String json = new Gson().toJson(new VoiceBean(filePath, length, strLength));
                //给Html发消息,js接收并返回数据
                LogUtils.i(json);
                bridgeWebView.callHandler("getRecord", json, new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        ToastUtils.showShort(data);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mMenuButton) {
            if (!mIsMenuOpen) {
                doAnimateOpenAll();
            } else {
                doAnimateCloseAll();
            }
        } else if (v == mItemButton1) {
            doAnimateCloseAll();
            //签到
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示")
                    .setMessage("当前位置与目标位置相差太远？确定要签到吗")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            whetherOrNotSignIn = true;
                            dialog.dismiss();
                        }
                    }).create().show();

        } else if (v == mItemButton2) {
            doAnimateCloseAll();
            //路线规划
            startActivity(new Intent(mContext, BaiduMapActivity.class));

        } else if (v == mItemButton3) {
            doAnimateCloseAll();
            //拍照
            if (whetherOrNotSignIn) {
                takePhotoNoCompress();
            } else {
                ToastUtils.showShort("请先签到再使用此功能");
            }


        } else if (v == mItemButton4) {
            doAnimateCloseAll();
            //录音
            if (whetherOrNotSignIn) {
                mBtRec.startRecord();
            } else {
                ToastUtils.showShort("请先签到再使用此功能");
            }


        } else if (v == mItemButton5) {
            doAnimateCloseAll();
            //结单
            if (whetherOrNotSignIn) {
                ToastUtils.showShort("结单");
            } else {
                ToastUtils.showShort("请先签到再使用此功能");
            }

        } else if (v == bg) {
            doAnimateCloseAll();
        }

    }

    private void doAnimateOpenAll() {
        mIsMenuOpen = true;
        mMenuButtonAnimate(0f, 135f);
        bg.setVisibility(View.VISIBLE);
        doAnimateOpen(mItemButton1, 0);
        doAnimateOpen(mItemButton2, 1);
        doAnimateOpen(mItemButton3, 2);
        doAnimateOpen(mItemButton4, 3);
        doAnimateOpen(mItemButton5, 4);
    }

    private void doAnimateCloseAll() {
        mIsMenuOpen = false;
        mMenuButtonAnimate(135f, 0f);
        bg.setVisibility(View.GONE);
        doAnimateClose(mItemButton1, 0);
        doAnimateClose(mItemButton2, 1);
        doAnimateClose(mItemButton3, 2);
        doAnimateClose(mItemButton4, 3);
        doAnimateClose(mItemButton5, 4);
    }

    /**
     * 菜单按钮旋转动画
     *
     * @param from 开始的角度
     * @param to   结束的角度
     */
    private void mMenuButtonAnimate(float from, float to) {
        ObjectAnimator iconAnim = ObjectAnimator.ofFloat(mMenuButton, "rotation", from, to);
        iconAnim.setDuration(200).start();
    }


    private void doAnimateOpen(View view, int index) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.toRadians(180) / (5 - 1) * index;//5为按钮个数
        int translationX = -(int) (300 * Math.sin(degree));//300为半径
        int translationY = -(int) (300 * Math.cos(degree));

        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1));
        //动画周期为200ms
        set.setDuration(200).start();
    }

    private void doAnimateClose(final View view, int index) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.PI * index / ((5 - 1));//5为按钮个数
        //  double degree = Math.toRadians(180)/(5 - 1) * index;//等价于上面
        int translationX = -(int) (300 * Math.sin(degree));//300为半径
        int translationY = -(int) (300 * Math.cos(degree));
        AnimatorSet set = new AnimatorSet();
        //包含平移、缩放和透明度动画
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.1f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0.1f));

        set.setDuration(200).start();
    }

    public void takePhotoNoCompress() {
        String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                .format(new Date()) + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), filename);
        mCurrentPhotoPath = file.getAbsolutePath();
        Uri fileUri = FileProvider7.getUriForFile(mContext, file);
        startActivityForResult(IntentUtils.getCaptureIntent(fileUri), REQUEST_CODE_TAKE_PHOTO);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            LogUtils.i(mCurrentPhotoPath);

            //给Html发消息,js接收并返回数据
            bridgeWebView.callHandler("getImg", mCurrentPhotoPath, new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    ToastUtils.showShort("===" + data);

                }
            });

        }
    }

    @Override
    public void onDestroy() {
        if (bridgeWebView != null) {
            bridgeWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            bridgeWebView.clearHistory();

            ((ViewGroup) bridgeWebView.getParent()).removeView(bridgeWebView);
            bridgeWebView.destroy();
            bridgeWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mIsMenuOpen) {
            doAnimateCloseAll();
        } else {
            super.onBackPressed();
        }
    }
}
