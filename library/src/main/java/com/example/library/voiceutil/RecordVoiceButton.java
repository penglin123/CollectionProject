package com.example.library.voiceutil;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.library.R;

/**
 * 录音控件button
 */
public class RecordVoiceButton extends AppCompatButton implements View.OnClickListener {


    private ImageView mVolumeIv, mIvPauseContinue, mIvComplete;
    private VoiceLineView voicLine;
    private TextView mRecordHintTv;
    private Context mContext;
    private EnRecordVoiceListener enRecordVoiceListener;
    private VoiceManager voiceManager;

    public RecordVoiceButton(Context context) {
        super(context);
        init();
    }

    public RecordVoiceButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public RecordVoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        voiceManager = new VoiceManager(mContext);
        setOnClickListener(this);
    }

    /**
     * 设置监听
     *
     * @param enRecordVoiceListener
     */
    public void setEnrecordVoiceListener(EnRecordVoiceListener enRecordVoiceListener) {
        this.enRecordVoiceListener = enRecordVoiceListener;
    }

    /**
     * 启动录音dialog
     */
    private void startRecordDialog() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_record_voice, null);

        mVolumeIv = view.findViewById(R.id.iv_voice);
        voicLine = view.findViewById(R.id.voicLine);
        mRecordHintTv = view.findViewById(R.id.tv_length);
        mRecordHintTv.setText("00:00:00");
        mIvPauseContinue = view.findViewById(R.id.iv_continue_or_pause);
        mIvComplete = view.findViewById(R.id.iv_complete);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //暂停或继续
        mIvPauseContinue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voiceManager != null) {
                    voiceManager.pauseOrStartVoiceRecord();
                }
            }
        });
        //完成
        mIvComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voiceManager != null) {
                    voiceManager.stopVoiceRecord();
                }
                alertDialog.dismiss();
            }
        });

        // 有白色背景，加这句代码

        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.width = ScreenUtils.getScreenWidth() / 10 * 7;
        lp.height = ScreenUtils.getScreenHeight() / 10 * 4;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setAttributes(lp);

    }

    @Override
    public void onClick(View view) {
        startRecordDialog();
        voiceManager.setVoiceRecordListener(new VoiceManager.VoiceRecordCallBack() {
            @Override
            public void recDoing(long time, String strTime) {
                mRecordHintTv.setText(strTime);
            }

            @Override
            public void recVoiceGrade(int grade) {
                voicLine.setVolume(grade);
            }

            @Override
            public void recStart(boolean init) {
                mIvPauseContinue.setImageResource(R.drawable.icon_pause);
                voicLine.setContinue();
            }

            @Override
            public void recPause(String str) {
                mIvPauseContinue.setImageResource(R.drawable.icon_continue);
                voicLine.setPause();
            }


            @Override
            public void recFinish(long length, String strLength, String path) {
                if (enRecordVoiceListener != null) {
                    enRecordVoiceListener.onFinishRecord(length, strLength, path);
                }
            }
        });
        voiceManager.startVoiceRecord(Environment.getExternalStorageDirectory().getPath() + "/VoiceManager/audio");
    }

    /**
     * 结束回调监听
     */
    public interface EnRecordVoiceListener {
        void onFinishRecord(long length, String strLength, String filePath);
    }


}
