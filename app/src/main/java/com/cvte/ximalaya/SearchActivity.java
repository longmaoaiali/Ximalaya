package com.cvte.ximalaya;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cvte.ximalaya.base.BaseActivity;
import com.cvte.ximalaya.interfaces.ISearchCallback;
import com.cvte.ximalaya.presenters.SearchPresenter;
import com.cvte.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.List;

/**
 * Created by user on 2020/9/25.
 */

public class SearchActivity extends BaseActivity implements ISearchCallback {

    private View mBackBtn;
    private EditText mInputText;
    private TextView mSearchBtn;
    private FrameLayout mSearchResult;
    private static final String TAG="SearchActivity";
    private SearchPresenter mSearchPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        initView();
        initViewListener();
        initPresenter();
    }

    private void initPresenter() {
        mSearchPresenter = SearchPresenter.getInstance();
        //注册回调接口
        mSearchPresenter.registerViewCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchPresenter != null) {
            mSearchPresenter.registerViewCallback(this);
            mSearchPresenter = null;
        }
    }

    private void initViewListener() {

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo；搜索逻辑
            }
        });

        mInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.d(TAG,"content-->" +s );
                LogUtil.d(TAG,"start-->" +start );
                LogUtil.d(TAG,"before-->" +before );
                LogUtil.d(TAG,"count-->" +count );
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        mBackBtn = this.findViewById(R.id.search_back);
        mInputText = this.findViewById(R.id.search_edit);
        mSearchBtn = this.findViewById(R.id.search_btn);
        mSearchResult = this.findViewById(R.id.search_container);
    }

    @Override
    public void onSearchResultLoaded(List<Album> result) {

    }

    @Override
    public void onHotWordLoaded(List<HotWord> hotWordList) {

    }

    @Override
    public void onLoadMoreResult(List<Album> result, boolean isOkay) {

    }

    @Override
    public void onRecommendWordLoaded(List<QueryResult> keyWordList) {

    }
}
