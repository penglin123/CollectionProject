package com.example.yinlian.collectionproject.testdemo;

import android.view.View;
import android.widget.Button;

import com.example.library.base.BaseActivity;
import com.example.library.voiceutil.RecordVoiceButton;
import com.example.library.voiceutil.VoiceBean;
import com.example.library.voiceutil.VoiceManager;
import com.example.yinlian.collectionproject.R;

public class VoiceActivity extends BaseActivity {

    private Button play;
    private VoiceBean voiceBean;
    private VoiceManager voiceManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_voice;
    }

    @Override
    public void init() {
        RecordVoiceButton mBtRec = findViewById(R.id.button_rec);
        play = findViewById(R.id.play);
        voiceManager = new VoiceManager(this);

        mBtRec.setEnrecordVoiceListener(new RecordVoiceButton.EnRecordVoiceListener() {
            @Override
            public void onFinishRecord(long length, String strLength, String filePath) {
                // adapter.add(new VoiceBean(length, strLength, filePath) )
                play.setVisibility(View.VISIBLE);
                play.setText("播放时长：" + strLength);
                voiceBean = new VoiceBean();
                voiceBean.filePath = filePath;
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceManager.isPlaying()) {
                    voiceManager.stopPlay();
                } else {
                    voiceManager.stopPlay();
                    voiceManager.startPlay(voiceBean.filePath);
                }
            }
        });
    }
}
