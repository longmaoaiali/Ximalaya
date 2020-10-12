package com.cvte.ximalaya.presenters;

import com.cvte.ximalaya.base.BaseApplication;
import com.cvte.ximalaya.data.ISubDaoCallback;
import com.cvte.ximalaya.data.SubscriptionDao;
import com.cvte.ximalaya.interfaces.ISubscriptionCallback;
import com.cvte.ximalaya.interfaces.ISubscriptionPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.live.schedule.Schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by user on 2020/10/12.
 */

public class SubscriptionPresenter implements ISubscriptionPresenter, ISubDaoCallback {

    private final SubscriptionDao mSubscriptionDao;
    private Map<Long,Album> mDatas = new HashMap<>();
    private List<ISubscriptionCallback> mCallbacks = new ArrayList<>();

    private SubscriptionPresenter(){
        mSubscriptionDao = SubscriptionDao.getInstance();
        mSubscriptionDao.setCallback(this);
        //加载数据表里面保存的订阅数据
        //mSubscriptionDao.listAlbums();
        LoadListAlbumFromDB();
    }

    /*RXJava 实现子线程加载订阅数据*/
    private void LoadListAlbumFromDB(){
        io.reactivex.rxjava3.core.Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Throwable {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.listAlbums();
                }
            }

        }).subscribeOn(Schedulers.io()).subscribe();
    }


    private static SubscriptionPresenter sInatnce = null;

    public static SubscriptionPresenter getInstance(){
        if (sInatnce != null) {
            synchronized (SubscriptionPresenter.class){
                sInatnce = new SubscriptionPresenter();
            }
        }
        return sInatnce;
    }

    @Override
    public void registerViewCallback(ISubscriptionCallback iSubscriptionCallback) {
        if (!mCallbacks.contains(iSubscriptionCallback)) {
            mCallbacks.add(iSubscriptionCallback);
        }
    }

    @Override
    public void unregisterViewCallback(ISubscriptionCallback iSubscriptionCallback) {
        mCallbacks.remove(iSubscriptionCallback);
    }

    @Override
    public void addSubscription(final Album album) {
        /*RXJava*/
        io.reactivex.rxjava3.core.Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Throwable {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.addAlbum(album);
                }
            }

        }).subscribeOn(Schedulers.io()).subscribe();

    }

    @Override
    public void deleteSubscription(final Album album) {
        /*RXJava*/
        io.reactivex.rxjava3.core.Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Throwable {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.delAlbum(album);
                }
            }

        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void getSubscriptionList() {
        LoadListAlbumFromDB();
    }

    @Override
    public boolean isSub(Album album) {
        Album result = mDatas.get(album.getId());
        //返回true 代表不包含
        return result==null;

    }

    @Override
    public void onAddResult(final boolean isSuccess) {
        /*添加数据表数据结果回调*/
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onAddResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onDeleteResult(final boolean isSuccess) {
        /*删除数据表数据结果回调*/
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onDeleteResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onSubListLoaded(final List<Album> result) {
        /*读取数据表所有数据结果回调*/
        for (Album album : result) {
            //保存albumID 和 album 在HashMap中
            mDatas.put(album.getId(),album);
        }

        //todo 通知UI回调
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onSubscriptionsLoaded(result);
                }
            }
        });


    }
}
