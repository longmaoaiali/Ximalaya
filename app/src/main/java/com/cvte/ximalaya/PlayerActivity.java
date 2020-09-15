package com.cvte.ximalaya;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cvte.ximalaya.adapters.PlayerTrackPageViewAdapter;
import com.cvte.ximalaya.base.BaseActivity;
import com.cvte.ximalaya.interfaces.IPlayerCallback;
import com.cvte.ximalaya.presenters.PlayerPresenter;
import com.cvte.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by user on 2020/9/10.
 */

public class PlayerActivity extends BaseActivity implements IPlayerCallback, ViewPager.OnPageChangeListener {

    private ImageView mControlBtn;
    private PlayerPresenter mPlayerPresenter;
    private SimpleDateFormat mMinFormat = new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat = new SimpleDateFormat("hh:mm:ss");
    private TextView mTotalTime;
    private TextView mCurrentTime;
    private SeekBar mPlayerSeekBar;
    private int mCurrentProgress = 0;
    private boolean mIsUserTouchProgressBar = false;
    private ImageView mPlayerPre;
    private ImageView mPlayerNext;
    private TextView mTitleView;
    private String mTrackTitleText;
    private ViewPager mTrackPageView;
    private static final String TAG = "PlayerActivity";
    private PlayerTrackPageViewAdapter mPlayerTrackPageViewAdapter;
    private boolean mIsUserSlidePager = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        //测试一下播放
        mPlayerPresenter = PlayerPresenter.getInstance();
        //playerPresenter.play();
        mPlayerPresenter.registerViewCallback(this);
        initView();
        //界面初始化以后再获取数据
        mPlayerPresenter.getPlayList();
        initViewListener();
        /*大坑 有的歌曲会出现无法播放 需要等待播放器准备好才能再播放*/
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

        mPlayerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    //如果是用户点击
                    mCurrentProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar = false;
                //手离开进度条更新进度
                mPlayerPresenter.seekTo(mCurrentProgress);

            }
        });

        mPlayerPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerPresenter.playNext();
            }
        });

        mPlayerNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerPresenter.playPre();
            }
        });

        //ViewPager 滑动监听事件
        mTrackPageView.addOnPageChangeListener(this);

        //mIsUserSlidePager
        mTrackPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mIsUserSlidePager = true;
                        break;
                }
                return false;
            }
        });
    }

    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_stop_btn);
        mCurrentTime = this.findViewById(R.id.current_position);
        mTotalTime = this.findViewById(R.id.totalTime);
        mPlayerSeekBar = this.findViewById(R.id.player_progress);
        mPlayerPre = this.findViewById(R.id.play_pre);
        mPlayerNext = this.findViewById(R.id.play_next);
        mTitleView = this.findViewById(R.id.track_title);
        if (mTitleView != null) {
            mTitleView.setText(mTrackTitleText);
        }

        mTrackPageView = this.findViewById(R.id.track_pager_view);
        //ViewPage显示需要创建适配器，设置适配器
        mPlayerTrackPageViewAdapter = new PlayerTrackPageViewAdapter();
        mTrackPageView.setAdapter(mPlayerTrackPageViewAdapter);

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
    public void onListLoaded(List<Track> list) {
        LogUtil.d(TAG,"list size is -->"+list.size());
        //Track List回传给UI
        if (mPlayerTrackPageViewAdapter != null) {
            mPlayerTrackPageViewAdapter.setData(list);
        }
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onPlayProgressChange(int currentProgress, int total) {
        mPlayerSeekBar.setMax(total);
        //更新进度条 更新时间
        String totalDuration;
        String currentDuration;

        if (total>1000*60*60) {
            totalDuration = mHourFormat.format(total);
            currentDuration = mHourFormat.format(currentProgress);
        }else{
            totalDuration = mMinFormat.format(total);
            currentDuration = mMinFormat.format(currentProgress);
        }

        if (mTotalTime != null && "00:00".equals(mTotalTime.getText())) {
            mTotalTime.setText(totalDuration);
        }

        if (mCurrentTime != null) {
            mCurrentTime.setText(currentDuration);
        }
        //随着播放自动更新进度条
        if (!mIsUserTouchProgressBar) {
            //int present = (int) (currentProgress / total * 100);
            mPlayerSeekBar.setProgress(currentProgress);
        }


    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdLoadFinshed() {

    }

    @Override
    public void onTrackUpdate(Track track, int index) {
        this.mTrackTitleText = track.getTrackTitle();
        if (mTitleView != null) {
            mTitleView.setText(mTrackTitleText);
        }
        //上下切换歌曲时，需要联动切换图片
        if (mTrackPageView != null) {
            mTrackPageView.setCurrentItem(index,true);
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        /*ViewPager页面切换时都会调用到这里来*/
        /* 切歌来切换图片 或者切换图片来切歌都会调用这里 */
        //页面选中时播放切换的内容
        if (mPlayerPresenter != null && mIsUserSlidePager) {
            mPlayerPresenter.playByIndex(position);
        }

        mIsUserSlidePager=false;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
