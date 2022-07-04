package com.dejia.anju.video;
// Created by  

/**
 * 创建描述：视频放放状态相关回调(需要哪个回调自己添加)
 * 创建人员：肖利
 * 创建时间：2021/5/13.
 **/
public interface OnVideoPlayStatusListener {
    void onStatePreparing(boolean isFirstPlay);
    void onStatePlaying();
    void onAutoPlayEnd();
}
