package com.cvte.ximalaya.interfaces;

/**
 * Created by user on 2020/9/4.
 */

/*逻辑层的接口*/
public interface IRecommendPresenter {

    /*获得推荐内容*/
    void getRecommendList();

    /*下拉刷新更多内容*/
    void pull2RefreshMore();

    /*加载更多*/
    void loadMore();

    /*注册回调的方法*/
    void registerViewCallback(IRecommendViewCallback callback);
    void unRegisterViewCallback(IRecommendViewCallback callback);



}
