package com.example.yinlian.collectionproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.example.library.base.BaseFragment;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.activity.CustomerDetailsActivity;
import com.example.yinlian.collectionproject.activity.WriteSummaryActivity;
import com.example.yinlian.collectionproject.testdemo.BaiduMapActivity;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;

/**
 * 待访任务
 *
 * @author penglin
 * @date 2018/5/14 15:19
 */
public class WaitVisitTaskFrament extends BaseFragment implements View.OnClickListener {
    private BridgeWebView bridgeWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.webview_layout;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        //加载页面
        bridgeWebView = view.findViewById(R.id.webView);
        //允许JavaScript执行
        bridgeWebView.getSettings().setJavaScriptEnabled(true);


        bridgeWebView.setDefaultHandler(new DefaultHandler());
        bridgeWebView.setWebChromeClient(new WebChromeClient());
        bridgeWebView.setWebViewClient(new BridgeWebViewClient(bridgeWebView));

        //找到Html文件，也可以用网络上的文件
        bridgeWebView.loadUrl("file:///android_asset/wait_visit_task.html");
        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法


        /**
         * 前端发送消息给客户端  submitFromWeb 是js调用的方法名  安卓返回给js
         */
        bridgeWebView.registerHandler("initData", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //返回给html的消息
                function.onCallBack("返回给Toast的alert");

            }
        });


        bridgeWebView.registerHandler("jsTojava", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //返回给html的消息
                //                function.onCallBack("返回给Toast的alert");

                //显示接收的消息
                LogUtils.i(data);
                JsToJavaBean hahha = new Gson().fromJson(data, JsToJavaBean.class);
                LogUtils.i(hahha.tag);
                LogUtils.i(hahha.data);
                switch (hahha.tag) {
                    case 0:
                        startActivity(new Intent(mContext, CustomerDetailsActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(mContext, BaiduMapActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(mContext, WriteSummaryActivity.class));
                        break;
                    case 3:
//                        MyDialogFragment mdf = new MyDialogFragment();
//                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                        mdf.show(ft, "df");
                        showPopupWindow();
                        break;

                }


            }
        });
    }

    PopupWindow mPopWindow;

    private void showPopupWindow() {

        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popuplayout, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setBackgroundDrawable(null);
        mPopWindow.setFocusable(true);
        mPopWindow.setTouchable(true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setAnimationStyle(R.style.anim_popupw);
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        //设置各个控件的点击响应


        TextView cancel = contentView.findViewById(R.id.cancel);
        TextView confirm = contentView.findViewById(R.id.confirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);

        // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.6f;
        getActivity().getWindow().setAttributes(lp);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        View rootview = LayoutInflater.from(mContext).inflate(R.layout.webview_layout, null);
        mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

    }

    @Override
    public void onClick(View v) {
        mPopWindow.dismiss();
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

}
