package com.cvte.ximalaya.presenters;

import android.support.annotation.Nullable;
import android.util.Log;

import com.cvte.ximalaya.interfaces.IAlbumDetailViewCallback;
import com.cvte.ximalaya.interfaces.IAlbumDetialPresenter;
import com.cvte.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2020/9/8.
 */

public class AlbumDetialPresenter implements IAlbumDetialPresenter{
    private Album mTargetAlbum;
    private static final String TAG="AlbumDetialPresenter";

    private AlbumDetialPresenter(){}

    private static AlbumDetialPresenter sInstance = null;

    public static AlbumDetialPresenter getInstance(){
        synchronized (AlbumDetialPresenter.class){
            if (sInstance==null) {
                sInstance = new AlbumDetialPresenter();
            }
        }
        return sInstance;
    }

    private List<IAlbumDetailViewCallback> mCallbacks = new ArrayList<>();

    @Override
    public void pull2RefreshMore() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumId, int page) {
        /*根据album ID获得专辑详情*/
        Map<String,String> map = new HashMap<String,String>();
        map.put(DTransferConstants.ALBUM_ID,albumId+"");
        map.put(DTransferConstants.SORT,"asc");
        map.put(DTransferConstants.PAGE,page+"");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(@Nullable TrackList trackList) {
                if (trackList != null) {
                    List<Track> tracks = trackList.getTracks();
                    LogUtil.d(TAG,"专辑详情 size --> "+tracks.size());
                    LogUtil.d(TAG,"current thread --> "+Thread.currentThread().getName());
                    handlerAlbumDetailResult(tracks);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"errorCode = "+i+" errorMsg = "+s);
                handlerError(i,s);
            
            }
        });


    }

    private void handlerError(int errorCode, String errorMsg) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onNetworkError(errorCode,errorMsg);
        }
    }

    private void handlerAlbumDetailResult(List<Track> tracks) {
        for (IAlbumDetailViewCallback callback : mCallbacks) {
            callback.onDetailListLoaded(tracks);
        }
    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        /*UI注册回调*/
        if (!mCallbacks.contains(detailViewCallback)) {
            mCallbacks.add(detailViewCallback);
            if (mTargetAlbum != null) {
                detailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unregisterViewCallback(IAlbumDetailViewCallback detailViewCallback) {
        mCallbacks.remove(detailViewCallback);
    }

    public void setAlbumDetail(Album targetAlbum){
        this.mTargetAlbum = targetAlbum;
    }
}
