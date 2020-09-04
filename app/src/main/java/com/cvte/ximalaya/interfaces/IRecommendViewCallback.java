package com.cvte.ximalaya.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

/**
 * Created by user on 2020/9/4.
 */

/*回调UI显示数据*/
public interface IRecommendViewCallback {

    /*获取推荐内容的结果*/
    void onRecommendListLoaded(List<Album> result);

    /*加载更多*/
    void onLoaderMore(List<Album> result);
    void onRefreshMoreMore(List<Album> result);
}
