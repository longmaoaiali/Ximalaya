package com.cvte.ximalaya.interfaces;

import android.os.Trace;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * Created by user on 2020/9/11.
 */

public interface IPlayerCallback  {


    void onPlayStart();
    void onPlayStop();
    void onPlayPause();
    void onPlayNext(Track track);
    void onPlayPre(Track track);
    void onPlayError();

    //播放列表
    void onListLoaded(List<Trace> list);
    void onPlayModeChange(XmPlayListControl.PlayMode playMode);

    void onPlayProgressChange(long currentProgress,long total);

    //广告加载与完成
    void onAdLoading();
    void onAdLoadFinshed();



}
