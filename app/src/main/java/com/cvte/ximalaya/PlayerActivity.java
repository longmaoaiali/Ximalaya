package com.cvte.ximalaya;

import android.app.Activity;
import android.os.Bundle;
import android.os.Trace;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.cvte.ximalaya.base.BaseActivity;
import com.cvte.ximalaya.interfaces.IPlayerCallback;
import com.cvte.ximalaya.presenters.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

/**
 * Created by user on 2020/9/10.
 */

public class PlayerActivity extends BaseActivity implements IPlayerCallback {

    private ImageView mControlBtn;
    private PlayerPresenter mPlayerPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        //测试一下播放
        mPlayerPresenter = PlayerPresenter.getInstance();
        //playerPresenter.play();
        mPlayerPresenter.registerViewCallback(this);
        initView();
        initViewListener();
        startPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayerPresenter != null) {
            mPlayerPresenter.unregisterViewCallback(this);
            mPlayerPresenter=null;
        }
    }

    private void startPlay() {
        mPlayerPresenter.play();
    }


    private void initViewListener() {
        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断状态，对点击事件进行改变
                if (mPlayerPresenter.isplay()) {
                    mPlayerPresenter.pause();
                }else {
                    mPlayerPresenter.play();
                }
            }
        });
    }

    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_stop_btn);

    }

    @Override
    public void onPlayStart() {
        //开始播放以后将图片换成暂停的图片
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.stop_normal);
        }
    }

    @Override
    public void onPlayStop() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.play_normal);
        }
    }

    @Override
    public void onPlayPause() {
        if (mControlBtn != null) {
            mControlBtn.setImageResource(R.mipmap.play_normal);
        }
    }

    @Override
    public void onPlayNext(Track track) {

    }

    @Override
    public void onPlayPre(Track track) {

    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void onListLoaded(List<Trace> list) {

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onPlayProgressChange(long currentProgress, long total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdLoadFinshed() {

    }


}
