package com.cvte.ximalaya.interfaces;

/**
 * Created by user on 2020/9/8.
 */

public interface IAlbumDetialPresenter {
    /*下拉刷新更多内容*/
    void pull2RefreshMore();

    /*加载更多*/
    void loadMore();

    /*获取专辑详情*/
    void getAlbumDetail(int albumId,int page);

    //注册UI通知接口
    void registerViewCallback(IAlbumDetailViewCallback detailViewCallback);

    void unregisterViewCallback(IAlbumDetailViewCallback detailViewCallback);

}
