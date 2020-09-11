package com.cvte.ximalaya.presenters;

import android.net.LinkAddress;
import android.util.Log;

import com.cvte.ximalaya.base.BaseApplication;
import com.cvte.ximalaya.interfaces.IPlayerCallback;
import com.cvte.ximalaya.interfaces.IPlayerPresenter;
import com.cvte.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2020/9/11.
 */

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {


    private final XmPlayerManager mPlayerManager;
    private static final String TAG = "PlayerPresenter";
    private List<IPlayerCallback> mIPlayerCallbackList = new ArrayList<>();

    private PlayerPresenter(){
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //广告的接口
        mPlayerManager.addAdsStatusListener(this);
        //注册播放器状态的接口
        mPlayerManager.addPlayerStatusListener(this);
    }

    private static PlayerPresenter sPlayerPresenter;

    public static PlayerPresenter getInstance(){
        if (sPlayerPresenter == null) {
            synchronized (PlayerPresenter.class){
                sPlayerPresenter = new PlayerPresenter();
            }
        }
        return  sPlayerPresenter;
    }

    private boolean isPlayerListSet = false;
    public void setPlayerList(List<Track> tracks,int playerIndex){
        if (mPlayerManager != null) {
            isPlayerListSet = true;
            mPlayerManager.setPlayList(tracks,playerIndex);
        }
    }

    /*回调都是Presenter回调显示UI的*/
    @Override
    public void registerViewCallback(IPlayerCallback iPlayerCallback) {
        if (!mIPlayerCallbackList.contains(iPlayerCallback)) {
            mIPlayerCallbackList.add(iPlayerCallback);
        }

    }

    @Override
    public void unregisterViewCallback(IPlayerCallback iPlayerCallback) {

    }

    @Override
    public void play() {
        LogUtil.d(TAG,"isPlayerListSet--> "+isPlayerListSet);
        if (isPlayerListSet) {
            Log.d(TAG,Log.getStackTraceString(new Throwable()));
            //LogUtil.d(TAG.);
            mPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        if (mPlayerManager != null) {
            mPlayerManager.pause();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void switchPlayMode(int mode) {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void platByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {

    }

    @Override
    public boolean isplay() {
        return mPlayerManager.isPlaying();
    }

    /*广告物料相关的实现-->*/
    @Override
    public void onStartGetAdsInfo() {
        LogUtil.d(TAG,"onStartGetAdsInfo");
    }

    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {
        LogUtil.d(TAG,"onGetAdsInfo");
    }

    @Override
    public void onAdsStartBuffering() {
        LogUtil.d(TAG,"onAdsStartBuffering");
    }

    @Override
    public void onAdsStopBuffering() {
        LogUtil.d(TAG,"onAdsStopBuffering");
    }

    @Override
    public void onStartPlayAds(Advertis advertis, int i) {
        LogUtil.d(TAG,"onStartPlayAds");
    }

    @Override
    public void onCompletePlayAds() {
        LogUtil.d(TAG,"onCompletePlayAds");
    }

    @Override
    public void onError(int what, int extra) {
        LogUtil.d(TAG,"onError: what--> "+what+"extra msg--> "+extra);
    }

    /*播放器相关的回调接口*/
    @Override
    public void onPlayStart() {
        LogUtil.d(TAG,"onPlayStart");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbackList) {
            iPlayerCallback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {
        LogUtil.d(TAG,"onPlayPause");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbackList) {
            iPlayerCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {
        LogUtil.d(TAG,"onPlayStop");
        for (IPlayerCallback iPlayerCallback : mIPlayerCallbackList) {
            iPlayerCallback.onPlayStop();
        }
    }

    @Override
    public void onSoundPlayComplete() {
        LogUtil.d(TAG,"onSoundPlayComplete");
    }

    @Override
    public void onSoundPrepared() {
        LogUtil.d(TAG,"onSoundPrepared");
    }

    @Override
    public void onSoundSwitch(PlayableModel playableModel, PlayableModel playableModel1) {
        LogUtil.d(TAG,"onSoundSwitch");
    }

    @Override
    public void onBufferingStart() {
        LogUtil.d(TAG,"onBufferingStart");
    }

    @Override
    public void onBufferingStop() {
        LogUtil.d(TAG,"onBufferingStop");
    }

    @Override
    public void onBufferProgress(int i) {
        LogUtil.d(TAG,"onBufferProgress");
    }

    @Override
    public void onPlayProgress(int i, int i1) {
        LogUtil.d(TAG,"onPlayProgress");
    }

    @Override
    public boolean onError(XmPlayerException e) {
        LogUtil.d(TAG,"onError");
        return false;
    }


}
