package com.example.yinlian.collectionproject.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;
import com.example.library.base.BaseActivity;
import com.example.library.utils.BottomNavigationViewHelper;
import com.example.library.utils.ToastUtils;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.fragment.MyFragment;
import com.example.yinlian.collectionproject.fragment.OutboundTaskFragment;
import com.example.yinlian.collectionproject.fragment.StatisticalFragment;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private long mExitTime = 0;
    BottomNavigationView navigation;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mViewPager = findViewById(R.id.viewPager);
        navigation = findViewById(R.id.navigation);
    }

    private void initData() {
        initTitle(R.string.navigation_one);

        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_one);

        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(2);
    }

    private void initListener() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mViewPager.addOnPageChangeListener(onPageChangeListener);
    }


    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.navigation_one);
                    initTitle(R.string.navigation_one);
                    break;
//                case 1:
//                    navigation.setSelectedItemId(R.id.navigation_two);
//                    initTitle(R.string.navigation_two);
//                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.navigation_three);
                    initTitle(R.string.navigation_three);
                    break;
                case 2:
                    navigation.setSelectedItemId(R.id.navigation_four);
                    initTitle(R.string.navigation_four);
                    break;

                default:
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_one:
                    mViewPager.setCurrentItem(0);
                    initTitle(R.string.navigation_one);
                    return true;
//                case R.id.navigation_two:
//                    mViewPager.setCurrentItem(1);
//                    initTitle(R.string.navigation_two);
//                    return true;
                case R.id.navigation_three:
                    mViewPager.setCurrentItem(1);
                    initTitle(R.string.navigation_three);
                    return true;
                case R.id.navigation_four:
                    mViewPager.setCurrentItem(2);
                    initTitle(R.string.navigation_four
                    );
                    return true;
            }
            return false;
        }
    };

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private Fragment[] mFragments = new Fragment[]{OutboundTaskFragment.newInstance()
                , StatisticalFragment.newInstance(), MyFragment.newInstance()};

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtils.showShort(getString(R.string.app_exit_hint));
                mExitTime = System.currentTimeMillis();
            } else {
                try {
                    //杀死后台进程需要在AndroidManifest中声明android.permission.KILL_BACKGROUND_PROCESSES；
                    ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                    assert activityManager != null;
                    activityManager.killBackgroundProcesses(this.getPackageName());
                    finish();
                } catch (Exception e) {

                    LogUtils.e("ActivityManager", "app exit" + e.getMessage());
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
