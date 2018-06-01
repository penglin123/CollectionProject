package com.example.yinlian.collectionproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;

import com.blankj.utilcode.util.LogUtils;
import com.example.library.base.BaseFragment;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.activity.CustomerDetailsActivity;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;


/**
 * - @Description:  我的
 * - @Author:  penglin
 * - @Time:  2018/05/08
 */
public class MyFragment extends BaseFragment {


    private BridgeWebView bridgeWebView;

    public static MyFragment newInstance() {
        return new MyFragment();
    }


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


        bridgeWebView.loadUrl("file:///android_asset/me.html");

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

                startActivity(new Intent(mContext, CustomerDetailsActivity.class));
            }
        });
    }

}
