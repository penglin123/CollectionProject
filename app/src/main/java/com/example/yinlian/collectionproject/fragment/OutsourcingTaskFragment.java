package com.example.yinlian.collectionproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.library.base.BaseFragment;
import com.example.yinlian.collectionproject.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;


/**
 * - @Description:委外任务（已经没用了，有外访模块就够了）
 * - @Author:  penglin
 * - @Time:  2018/05/08
 */
@Deprecated
public class OutsourcingTaskFragment extends BaseFragment {

    public static OutsourcingTaskFragment newInstance() {

       return new OutsourcingTaskFragment();

    }

    private BridgeWebView bridgeWebView;

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


      //  bridgeWebView.setDefaultHandler(new DefaultHandler());
     //   bridgeWebView.setWebChromeClient(new WebChromeClient());
      //  bridgeWebView.setWebViewClient(new BridgeWebViewClient(bridgeWebView));

        //找到Html文件，也可以用网络上的文件
        bridgeWebView.loadUrl("file:///android_asset/customer_details.html");
        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法

//        /**
//         * 前端发送消息给客户端  submitFromWeb 是js调用的方法名  安卓返回给js
//         */
//        bridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {
//            @Override
//            public void handler(String data, CallBackFunction function) {
//                //显示接收的消息
//                ToastUtils.showShort(data);
//                //返回给html的消息
//                function.onCallBack("返回给Toast的alert");
//            }
//        });
//
//        /**
//         * 给Html发消息,js接收并返回数据
//         */
//        bridgeWebView.callHandler("functionInJs", "调用js的方法", new CallBackFunction() {
//
//            @Override
//            public void onCallBack(String data) {
//                ToastUtils.showShort("===" + data);
//            }
//        });
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
