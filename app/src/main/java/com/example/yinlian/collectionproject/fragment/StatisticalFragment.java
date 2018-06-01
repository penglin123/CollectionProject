package com.example.yinlian.collectionproject.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

import com.blankj.utilcode.util.LogUtils;
import com.example.library.base.BaseFragment;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.activity.StatisticsDetailsActivity;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.google.gson.Gson;

/**
 * - @Description:  统计
 * - @Author:  penglin
 * - @Time:  2018/05/08
 */
public class StatisticalFragment extends BaseFragment {

    private BridgeWebView bridgeWebView;

    public static StatisticalFragment newInstance() {
        return new StatisticalFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.webview_layout;
    }

    @SuppressLint("JavascriptInterface")
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
        bridgeWebView.loadUrl("file:///android_asset/statistics.html");
        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法


        /**
         * 前端发送消息给客户端  submitFromWeb 是js调用的方法名  安卓返回给js
         */
        bridgeWebView.registerHandler("jsTojava", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

                JsToJavaBean bean = new Gson().fromJson(data, JsToJavaBean.class);
                LogUtils.i(bean.tag);
                LogUtils.i(bean.data);
                Intent intent = new Intent(mContext, StatisticsDetailsActivity.class);
                intent.putExtra("tag", bean.tag);
                startActivity(intent);


            }
        });





        bridgeWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String message = consoleMessage.message();
                int lineNumber = consoleMessage.lineNumber();
                String sourceID = consoleMessage.sourceId();
                String messageLevel = consoleMessage.message();

                Log.i("wokk", String.format("[%s] sourceID: %s lineNumber: %n message: %s",
                        messageLevel, sourceID, lineNumber, message));

                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("wokk", message + " -- From line " + lineNumber + " of " + sourceID);
                super.onConsoleMessage(message, lineNumber, sourceID);
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
