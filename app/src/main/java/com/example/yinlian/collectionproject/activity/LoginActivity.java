package com.example.yinlian.collectionproject.activity;

import android.content.Intent;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.example.library.base.BaseActivity;
import com.example.library.http.api.NetManage;
import com.example.library.http.bean.LoginRequest;
import com.example.library.http.bean.LoginResponse;
import com.example.library.http.progress.BaseObserver;
import com.example.library.utils.NetStateMonitor;
import com.example.library.utils.SPManager;
import com.example.library.utils.ToastUtils;
import com.example.yinlian.collectionproject.R;
import com.example.yinlian.collectionproject.testdemo.Main2Activity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
/**
 - @Description:  登录页面
 - @Author:  penglin
 - @Time:
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, View.OnLongClickListener {

    //view
    private EditText etName;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvNetworkStatus;
    private ImageView ivSeePassword;
    private ImageView ivEmptyAccount;
    private ImageView ivEmptyPsw;

    //网络监听
    NetStateMonitor netStateMonitor;
    private Disposable disposable;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        etName = findViewById(R.id.et_account);
        btnLogin = findViewById(R.id.btn_login);
        etPassword = findViewById(R.id.et_password);
        ivSeePassword = findViewById(R.id.iv_see_password);
        tvNetworkStatus = findViewById(R.id.tv_network_status);
        ivEmptyPsw = findViewById(R.id.iv_empty_psw);
        ivEmptyAccount = findViewById(R.id.iv_empty_account);
    }

    private void initData() {
        initTitle(getString(R.string.login));
        netStateMonitor = NetStateMonitor.getInstance();

        String userName = SPManager.getUserName();
        if (!StringUtils.isEmpty(userName)) {
            etName.setText(userName);
            etName.setSelection(userName.length());
            ivEmptyAccount.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(etName.getText().toString())) {
                    ivEmptyAccount.setVisibility(View.GONE);
                } else {
                    ivEmptyAccount.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (StringUtils.isEmpty(etPassword.getText().toString())) {
                    ivEmptyPsw.setVisibility(View.GONE);
                } else {
                    ivEmptyPsw.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.setOnFocusChangeListener(this);
        etName.setOnFocusChangeListener(this);

        btnLogin.setOnClickListener(this);
        btnLogin.setOnLongClickListener(this);
        ivSeePassword.setOnClickListener(this);
        ivEmptyAccount.setOnClickListener(this);
        ivEmptyPsw.setOnClickListener(this);
        tvNetworkStatus.setOnClickListener(this);

        netStateMonitor();

    }

    //网络监听
    private void netStateMonitor() {
        netStateMonitor.observe(this)
                .subscribe(new Observer<NetStateMonitor.NetState>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(NetStateMonitor.NetState netState) {
                        if (netState == NetStateMonitor.NetState.NET_UNKNOWN
                                || netState == NetStateMonitor.NetState.NET_NO) {
                            tvNetworkStatus.setVisibility(View.VISIBLE);
                        } else {
                            tvNetworkStatus.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (!disposable.isDisposed()) {
                            disposable.dispose();
                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;

            case R.id.iv_see_password:
                setPasswordVisibility();
                break;

            case R.id.iv_empty_account:
                etName.setText("");
                ivEmptyAccount.setVisibility(View.GONE);
                break;

            case R.id.iv_empty_psw:
                etPassword.setText("");
                ivEmptyPsw.setVisibility(View.GONE);
                break;

            case R.id.tv_network_status:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
        }
    }

    // 设置密码可见和不可见的相互转换
    private void setPasswordVisibility() {
        if (ivSeePassword.isSelected()) {
            ivSeePassword.setSelected(false);
            //密码不可见
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            ivSeePassword.setSelected(true);
            //密码可见
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        }

        if (!etPassword.getText().toString().trim().isEmpty()) {
            etPassword.setSelection(etPassword.getText().toString().trim().length());
        }
    }

    //登录
    private void login() {
        final String etNameText = etName.getText().toString().trim();
        String etPasswordText = etPassword.getText().toString().trim();


        if (etNameText.isEmpty() || etPasswordText.isEmpty()) {
            ToastUtils.showShort(R.string.userName_or_password_isEmpty);
            return;
        }


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.userName = etNameText;
        loginRequest.password = etPasswordText;

        NetManage.getApiService(getApplicationContext())
                .login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<LoginResponse>(this) {
                    @Override
                    protected void onSuccess(LoginResponse value) {
                        SPManager.putToken("123456");
                        SPManager.putUserName(etNameText);
                        // TODO: 2018/5/2
                        //数据库操作
                        ToastUtils.showShort(R.string.login_successful);
                        startActivity(new Intent(LoginActivity.this, GestureLockActivity.class));
                        finish();
                    }

                    @Override
                    protected void onFailure(String errorType, String msg) {
                        onSuccess(null);
//                        ToastUtils.showShort(R.string.login_failure);
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        netStateMonitor.dispose(this, disposable);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v == etName) {
            if (hasFocus) {
                if (StringUtils.isEmpty(etName.getText().toString())) {
                    ivEmptyAccount.setVisibility(View.GONE);
                } else {
                    ivEmptyAccount.setVisibility(View.VISIBLE);
                }
            } else {
                ivEmptyAccount.setVisibility(View.GONE);
            }
        } else {
            if (hasFocus) {
                if (StringUtils.isEmpty(etPassword.getText().toString())) {
                    ivEmptyPsw.setVisibility(View.GONE);
                } else {
                    ivEmptyPsw.setVisibility(View.VISIBLE);
                }
            } else {
                ivEmptyPsw.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        startActivity(new Intent(this, Main2Activity.class));
        return true;
    }
}

