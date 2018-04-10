package com.example.yinlian.collectionproject.http.progress;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * @author DeMon
 * @date 2017/9/6
 */

public abstract class ProgressObserver<T> implements Observer<T>, ProgressCancelListener {
    private static final String TAG = "ProgressObserver";
    //    private ObserverOnNextListener listener;
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;
    private Disposable d;


    public abstract void onSuccess(Object o);

    public abstract void onFailure(Throwable msg);

    public abstract void onCompleted();

//    public ProgressObserver(Context context, ObserverOnNextListener listener) {
//        this.listener = listener;
//        this.context = context;
//        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
//    }

    public ProgressObserver(Context context) {

        this.context = context;
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

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        Log.d(TAG, "onSubscribe: ");
        showProgressDialog();

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
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
        Log.e(TAG, "onError: ", e);
        onFailure(e);
        onComplete();
    }

    @Override
    public void onComplete() {
        dismissProgressDialog();
        onCompleted();
        Log.d(TAG, "onComplete: ");
    }

    @Override
    public void onCancelProgress() {
        //如果处于订阅状态，则取消订阅
        if (!d.isDisposed()) {
            d.dispose();
        }
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
                //   ToastUtils.show(R.string.connect_error, Toast.LENGTH_SHORT);

                break;

            case CONNECT_TIMEOUT:
                Toast.makeText(context, "22222", Toast.LENGTH_SHORT).show();
                //    ToastUtils.show(R.string.connect_timeout, Toast.LENGTH_SHORT);
                break;

            case BAD_NETWORK:
                Toast.makeText(context, "33333", Toast.LENGTH_SHORT).show();
                // ToastUtils.show(R.string.bad_network, Toast.LENGTH_SHORT);
                break;

            case PARSE_ERROR:
                Toast.makeText(context, "44444", Toast.LENGTH_SHORT).show();
                //  ToastUtils.show(R.string.parse_error, Toast.LENGTH_SHORT);
                break;

            case UNKNOWN_ERROR:
            default:
                Toast.makeText(context, "55555", Toast.LENGTH_SHORT).show();
                //  ToastUtils.show(R.string.unknown_error, Toast.LENGTH_SHORT);
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
