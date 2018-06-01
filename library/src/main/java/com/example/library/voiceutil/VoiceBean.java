package com.example.library.voiceutil;


public class VoiceBean {
    public VoiceBean(String filePath, long length, String strLength) {
        this.filePath = filePath;
        this.length = length;
        this.strLength = strLength;
    }

    public String filePath;
    public long length;
    public String strLength;
}
