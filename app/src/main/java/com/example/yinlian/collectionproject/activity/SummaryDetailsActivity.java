package com.example.yinlian.collectionproject.activity;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import com.example.library.base.BaseActivity;
import com.example.yinlian.collectionproject.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;

/**
 * 已结单客户总结详情
 *
 * @author penglin
 * @date 2018/5/18
 */

public class SummaryDetailsActivity extends BaseActivity {
    private BridgeWebView bridgeWebView;

    @Override
    public int getLayoutId() {
        return R.layout.webview_layout;
    }

    @Override
    public void init() {
        initTitleAndToolbarBack("已接单用户",R.drawable.ic_back);
        bridgeWebView = findViewById(R.id.webView);

        bridgeWebView.loadUrl("file:///android_asset/summary_details.html");


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
