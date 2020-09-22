package com.cvte.ximalaya;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvte.ximalaya.adapters.IndicatorAdapter;
import com.cvte.ximalaya.adapters.MainContentAdapter;
import com.cvte.ximalaya.interfaces.IPlayerCallback;
import com.cvte.ximalaya.presenters.PlayerPresenter;
import com.cvte.ximalaya.utils.LogUtil;
import com.cvte.ximalaya.views.RoundRectImageView;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements IPlayerCallback {

    private static final String TAG = "MainActivity";
    private static final int sPermissionRequestCode = 1;
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private IndicatorAdapter mIndicatorAdapter;
    private RoundRectImageView mRoundRectImageView;
    private TextView mTrackTitle;
    private TextView mTrackAuthor;
    private ImageView mSubPlayerControl;
    private PlayerPresenter mPlayerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtil.d(TAG, "onCreate-->");

        if (Build.VERSION.SDK_INT>=23) {
            getUserPermission();
        }

        initView();
        initEvent();
        initPresenter();
    }

    private void initPresenter() {
        mPlayerPresenter = PlayerPresenter.getInstance();
        mPlayerPresenter.registerViewCallback(this);
    }

    private void initEvent() {
        /*设置Indicator的监听器 */
        mIndicatorAdapter.setOnIndicatorTabClickListener(new IndicatorAdapter.OnIndicatorTabClickListener() {
            @Override
            public void onTabClick(int index) {
                LogUtil.d(TAG,"IndicatorAdapter Listener click index "+index);
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(index);
                }
            }
        });
    }

    private void initView() {
        //viewPager
        mViewPager = this.findViewById(R.id.view_pager);
        //设置viewPage的适配器
        /*1. 创建ViewPager适配器
        * 2. 适配器实现返回Fragment getItem
        * 3. getItem的实现是根据index索引返回Fragment的实例 baseFragment = new RecommendFragment()*/
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);
        LogUtil.d(TAG,"ViewPager.setAdapter(mainContentAdapter)");
        mViewPager.setAdapter(mainContentAdapter);

        //indicator
        mMagicIndicator = this.findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.colorBackgroundRed));
        //创建indicator的适配器
        mIndicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //自动平分indicator
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatorAdapter);


        mMagicIndicator.setNavigator(commonNavigator);
        /*将indicator与viewPager绑定，其实就是一个viewPager的监听*/
        //实现viewpager 与 indicator一起移动
        ViewPagerHelper.bind(mMagicIndicator,mViewPager);

        //track_cover track_little_title track_author sub_play_control
        mRoundRectImageView = this.findViewById(R.id.track_cover);
        mTrackTitle = this.findViewById(R.id.track_little_title);
        mTrackAuthor = this.findViewById(R.id.track_author);
        mSubPlayerControl = this.findViewById(R.id.sub_play_control);


    }

    private void getUserPermission() {
        String[] permission = new String[] {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
            Manifest.permission.PROCESS_OUTGOING_CALLS };

        List<String> needPermission = new ArrayList<>();

        needPermission.clear();

        //将未授权的权限放入List中来申请权限
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                needPermission.add(permission[i]);
            }
        }

        if (needPermission.size()>0) {
            ActivityCompat.requestPermissions(this,permission,sPermissionRequestCode);
        } else {
            Log.d(TAG,"had get all permission");
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermissonDenied = false;
        if(requestCode == sPermissionRequestCode){
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_DENIED)
                    hasPermissonDenied = true;
            }
        }

        if(hasPermissonDenied){
            Log.d(TAG,"user denied permission");
            /*用户禁止权限获取处理*/
        } else {
            Log.d(TAG,"had requested get all permission");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerPresenter.unregisterViewCallback(this);
    }

    @Override
    public void onPlayStart() {

    }

    @Override
    public void onPlayStop() {

    }

    @Override
    public void onPlayPause() {

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

    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onPlayProgressChange(int currentProgress, int total) {

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdLoadFinshed() {

    }

    @Override
    public void onTrackUpdate(Track track, int index) {
        //todo：回调设置UI显示
        if (track != null) {
            String trackTitle = track.getTrackTitle();
            LogUtil.d(TAG,"trackTitle is --> "+trackTitle);
        }
    }

    @Override
    public void updateListOrder(boolean isReverse) {

    }
}
