package com.example.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.example.library.R;


/**
 * 封装的带TopBar的Activity
 * 减少在xml中的include
 */
public abstract class BaseActivity extends AppCompatActivity {
    LinearLayout rootLayout;
    Toolbar toolbar;
    AppBarLayout appbar;
    TextView title;
    FrameLayout viewContent;
    public Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_base_activity);
        // Slidr.attach(this);
        rootLayout = findViewById(R.id.rootLayout);
        appbar = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);
        title = findViewById(R.id.title);
        viewContent = findViewById(R.id.viewContent);

        //初始化设置ToolBar，必须放在toolbar事件之前
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //将继承 TopBarBaseActivity 的布局解析到 FrameLayout里面
        LayoutInflater.from(this).inflate(getLayoutId(), viewContent);

        BarUtils.setStatusBarColor(this, ContextCompat.getColor(BaseApplication.getContext(), R.color.colorPrimary), 0);
        BarUtils.addMarginTopEqualStatusBarHeight(rootLayout);
        init();
    }

    //********************tool bar start*******************************//
    protected void hideToolbar() {
        appbar.setVisibility(View.GONE);
    }

    protected void initTitle(String toolbarTitle) {
        title.setText(toolbarTitle);
    }

    protected void initTitle(int stringId) {
        title.setText(stringId);
    }

    protected void initTitleAndToolbarBack(String toolbarTitle, int navigationIconId) {
        initTitle(toolbarTitle);
        toolbar.setNavigationIcon(navigationIconId);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    protected void initToolbar(String toolbarTitle, @DrawableRes int navigationIconId, @MenuRes int menuId) {
        initTitleAndToolbarBack(toolbarTitle, navigationIconId);
        toolbar.inflateMenu(menuId);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return BaseActivity.this.onMenuItemClick(item);
            }
        });
    }

    /**
     * 复写该方法 响应ToolBar菜单项目点击事件
     *
     * @param item item
     * @return 是否消费
     */
    protected boolean onMenuItemClick(MenuItem item) {
        return false;
    }


    //获取继承的布局
    protected abstract int getLayoutId();

    //初始化继承后onCreate()业务
    protected abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 键盘监听
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }


}
