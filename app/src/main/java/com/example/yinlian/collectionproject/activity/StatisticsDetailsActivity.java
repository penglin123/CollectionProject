package com.example.yinlian.collectionproject.activity;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import com.example.library.base.BaseActivity;
import com.example.yinlian.collectionproject.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;

/**
 * 统计详情（包含日报，考勤，还款统计）
 *
 * @author penglin
 * @date 2018/5/18 14:47
 */
public class StatisticsDetailsActivity extends BaseActivity {
    private BridgeWebView bridgeWebView;

    @Override
    public int getLayoutId() {
        return R.layout.webview_layout;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void init() {
        int tag = getIntent().getIntExtra("tag", 0);
        //加载页面
        bridgeWebView = findViewById(R.id.webView);
        //允许JavaScript执行
        bridgeWebView.getSettings().setJavaScriptEnabled(true);
        switch (tag) {

            case 1:
                initTitleAndToolbarBack("日报/月报", R.drawable.ic_back);
                bridgeWebView.loadUrl("file:///android_asset/daily_statistics.html");
                break;
            case 2:
                initTitleAndToolbarBack("个人考勤", R.drawable.ic_back);
                bridgeWebView.loadUrl("file:///android_asset/attendance_statistics.html");
                break;
            case 3:
                initTitleAndToolbarBack("实时还款", R.drawable.ic_back);
                bridgeWebView.loadUrl("file:///android_asset/reimbursement_statistics.html");
                break;
            default:
                break;
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

}
