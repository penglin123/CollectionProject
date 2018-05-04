package com.example.library.http.progress;


import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.example.library.R;
import com.example.library.utils.ToastUtils;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * @author penglin
 * @date 2018/4/10
 */

public abstract class BaseObserver<T> implements Observer<T>, ProgressCancelListener {
    private static final String TAG = "BaseObserver";
    private ProgressDialogHandler mProgressDialogHandler;
    private Disposable d;

    protected abstract void onSuccess(T value);

    protected abstract void onFailure(String errorType, String msg);

    public abstract void onCompleted();

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        LogUtils.d( "onSubscribe: ");
        showProgressDialog();
    }

    @Override
    public void onNext(T value) {

        LogUtils.json(TAG,value.toString());
//        LogUtils.iTag("BaseObserver;onNext;code=" + value.code + ",msg=" + value.message);
        onSuccess(value);


//        if (value.isSuccess()) {
//
//        } else {
//            if (NetFlag.isDebug()) {
//                Toast.makeText(mContext,
//                        mContext.getText(R.string.networklib_error_type_server) + ":code=" + value.code + ";msg=" + value.message,
//                        Toast.LENGTH_LONG).show();
//            }
         //   onFailure(value.code, value.message);
//        }
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);

        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);

        } else if (e instanceof JsonParseException
                || e instanceof JSONException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);

        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
        LogUtils.e(TAG, "onError: ", e);
        onFailure("","");
        onComplete();
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        onCompleted();
        LogUtils.d("onComplete: ");
    }

    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅
        if (!d.isDisposed()) {
            d.dispose();
        }
    }

    protected BaseObserver(Context context) {

        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG)
                    .sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    private void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:

                ToastUtils.showShort(R.string.connect_error);
                break;

            case CONNECT_TIMEOUT:

                ToastUtils.showShort(R.string.connect_timeout);
                break;

            case BAD_NETWORK:

                ToastUtils.showShort(R.string.page_inexistence);
                break;

            case PARSE_ERROR:

              ToastUtils.showShort(R.string.parse_error);
                break;

            case UNKNOWN_ERROR:
            default:
                ToastUtils.showShort(R.string.unknown_error);
                break;
        }
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
