package com.example.yinlian.collectionproject.activity;

import android.annotation.SuppressLint;
import android.webkit.WebView;

import com.example.library.base.BaseActivity;
import com.example.yinlian.collectionproject.utils.KeyBoardListener;
import com.example.yinlian.collectionproject.R;

/**
 * 写总结页面
 * @author penglin
 * @date 2018/5/22 16:29
 */
public class WriteSummaryActivity extends BaseActivity {
    private WebView bridgeWebView;
    KeyBoardListener keyBoardListener;

    @Override
    public int getLayoutId() {
        return R.layout.webview_layout;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void init() {
        //解决webView键盘遮挡问题
        keyBoardListener=new KeyBoardListener(this);
        keyBoardListener.init();
        initTitleAndToolbarBack("写总结",R.drawable.ic_back);

        bridgeWebView = findViewById(R.id.webView);
        bridgeWebView.loadUrl("file:///android_asset/write_summary.html");


    }
}
