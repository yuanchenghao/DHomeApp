package com.dejia.anju.model;

public class VoiceMessage {
    private String voiceTime;
    private String playerUrl;
    private boolean isPlay = false;

    public VoiceMessage(String voiceTime, String playerUrl) {
        this.voiceTime = voiceTime;
        this.playerUrl = playerUrl;
    }

    public String getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(String voiceTime) {
        this.voiceTime = voiceTime;
    }

    public String getPlayerUrl() {
        return playerUrl;
    }

    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean click) {
        isPlay = click;
    }
}
