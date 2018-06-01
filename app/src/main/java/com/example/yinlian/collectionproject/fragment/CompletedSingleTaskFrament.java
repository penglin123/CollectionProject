package com.example.yinlian.collectionproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.example.library.base.BaseFragment;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.activity.CustomerDetailsActivity;
import com.example.yinlian.collectionproject.activity.SummaryDetailsActivity;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;

/**
 * 已结单任务
 *
 * @author penglin
 * @date 2018/5/14 15:19
 */
public class CompletedSingleTaskFrament extends BaseFragment {
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
//        bridgeWebView.getSettings().setJavaScriptEnabled(true);


//        bridgeWebView.setDefaultHandler(new DefaultHandler());
//        bridgeWebView.setWebChromeClient(new WebChromeClient());
//        bridgeWebView.setWebViewClient(new BridgeWebViewClient(bridgeWebView));

        //找到Html文件，也可以用网络上的文件
        bridgeWebView.loadUrl("file:///android_asset/completed_single_task.html");
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
                //function.onCallBack("返回给Toast的alert");
                //显示接收的消息
                LogUtils.i(data);
                startActivity(new Intent(mContext, SummaryDetailsActivity.class));
            }
        });

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
