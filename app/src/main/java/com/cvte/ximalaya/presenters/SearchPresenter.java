package com.cvte.ximalaya.presenters;

import android.support.annotation.Nullable;

import com.cvte.ximalaya.api.XimalayaDataApi;
import com.cvte.ximalaya.interfaces.ISearchCallback;
import com.cvte.ximalaya.interfaces.ISearchPresenter;
import com.cvte.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;
import com.ximalaya.ting.android.opensdk.model.word.SuggestWords;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2020/9/25.
 */

public class SearchPresenter implements ISearchPresenter {

    /*当前的搜索关键字*/
    private String mCurrentKeyword = null;

    private final XimalayaDataApi mXimalayaDataApi;
    private static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private static final String TAG = "SearchPresenter";

    private SearchPresenter(){
        mXimalayaDataApi = XimalayaDataApi.getXimalayaDataApi();
    }

    private static SearchPresenter sSearchPresenter = null;

    public static SearchPresenter getInstance(){
        if (sSearchPresenter == null) {
            synchronized (SearchPresenter.class){
                if (sSearchPresenter == null) {
                    sSearchPresenter = new SearchPresenter();
                }
            }
        }

        return sSearchPresenter;
    }


    private List<ISearchCallback> mCallbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ISearchCallback iSearchCallback) {
        if (!mCallbacks.contains(iSearchCallback)) {
            mCallbacks.add(iSearchCallback);
        }
    }

    @Override
    public void unregisterViewCallback(ISearchCallback iSearchCallback) {
        mCallbacks.remove(iSearchCallback);
    }

    @Override
    public void doSearch(String keyword) {
        this.mCurrentKeyword = keyword;
        search(keyword);

    }

    private void search(String keyword) {
        mXimalayaDataApi.searchByKeyword(keyword, mCurrentPage, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(@Nullable SearchAlbumList searchAlbumList) {
                List<Album> albums = searchAlbumList.getAlbums();
                if (albums != null) {
                    LogUtil.d(TAG,"albums size-->"+albums.size());
                    for (ISearchCallback callback : mCallbacks) {
                        callback.onSearchResultLoaded(albums);
                    }
                }

            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG,"errorCode-->"+errorCode+" errorMsg -->" + errorMsg);
                for (ISearchCallback callback : mCallbacks) {
                    callback.onError(errorCode,errorMsg);
                }
            }
        });
    }

    @Override
    public void reSearch() {
        search(this.mCurrentKeyword);
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getHotWord() {
        mXimalayaDataApi.getHotWords(new IDataCallBack<HotWordList>() {
            @Override
            public void onSuccess(@Nullable HotWordList hotWordList) {
                if (hotWordList != null) {
                    List<HotWord> hotWords = hotWordList.getHotWordList();
                    LogUtil.d(TAG,"hotWords size--> " + hotWords.size());
                    for (ISearchCallback callback : mCallbacks) {
                        callback.onHotWordLoaded(hotWords);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG,"getHotWord errorCode-->"+errorCode+" errorMsg -->" + errorMsg);
                for (ISearchCallback callback : mCallbacks) {
                    callback.onError(errorCode,errorMsg);
                }
            }
        });
    }

    @Override
    public void getRecommendWord(String keyword) {
        mXimalayaDataApi.getSuggestWord(keyword, new IDataCallBack<SuggestWords>() {
            @Override
            public void onSuccess(@Nullable SuggestWords suggestWords) {
                if (suggestWords != null) {
                    List<QueryResult> keyWordList = suggestWords.getKeyWordList();
                    LogUtil.d(TAG,"keyWordList-->size()"+ keyWordList.size());
                }
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                LogUtil.d(TAG,"getHotWord errorCode-->"+errorCode+" errorMsg -->" + errorMsg);
                for (ISearchCallback callback : mCallbacks) {
                    callback.onError(errorCode,errorMsg);
                }
            }
        });
    }
}
