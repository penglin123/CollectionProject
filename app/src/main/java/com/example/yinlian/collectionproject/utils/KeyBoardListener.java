package com.example.yinlian.collectionproject.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.example.yinlian.collectionproject.activity.WriteSummaryActivity;

import java.lang.ref.WeakReference;

/**
 * 解决webView键盘遮挡问题的类
 * Created by zqy on 2016/11/14.
 */
public class KeyBoardListener {
    private WeakReference<WriteSummaryActivity> writeSummaryActivityWeakReference;


    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;


    public KeyBoardListener(Activity activity) {
        super();
        writeSummaryActivityWeakReference = new WeakReference<>((WriteSummaryActivity) activity);
    }


    public void init() {
        if (writeSummaryActivityWeakReference != null) {
            WriteSummaryActivity activity = writeSummaryActivityWeakReference.get();
            if (activity != null) {
                FrameLayout content = activity.findViewById(android.R.id.content);
                mChildOfContent = content.getChildAt(0);
                mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                possiblyResizeChildOfContent();
                            }
                        });
                frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent
                        .getLayoutParams();
            }
        }
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView()
                    .getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard
                        - heightDifference;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }


    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

}