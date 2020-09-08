package com.cvte.ximalaya.presenters;

import com.cvte.ximalaya.interfaces.IAlbumDetailViewCallback;
import com.cvte.ximalaya.interfaces.IAlbumDetialPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2020/9/8.
 */

public class AlbumDetialPresenter implements IAlbumDetialPresenter{
    private Album mTargetAlbum;

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
