package com.cvte.ximalaya;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cvte.ximalaya.adapters.RecommendListAdapter;
import com.cvte.ximalaya.base.BaseActivity;
import com.cvte.ximalaya.interfaces.ISearchCallback;
import com.cvte.ximalaya.presenters.SearchPresenter;
import com.cvte.ximalaya.utils.LogUtil;
import com.cvte.ximalaya.views.FlowTextLayout;
import com.cvte.ximalaya.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import java.util.ArrayList;
import java.util.Collections;
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
    private UILoader mUILoader;
    private RecyclerView mResultListView;
    private RecommendListAdapter mRecommendListAdapter;
    //private FlowTextLayout mFlowTextLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        initView();

        initPresenter();

        initViewListener();
    }

    private void initPresenter() {
        mSearchPresenter = SearchPresenter.getInstance();
        //注册回调接口
        mSearchPresenter.registerViewCallback(this);

        //获取热词
        mSearchPresenter.getHotWord();
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
        if (mUILoader != null) {
            mUILoader.setOnRetryClickListener(new UILoader.OnRetryClickListener() {
                @Override
                public void onRetryClick() {
                    if (mSearchPresenter != null) {
                        mSearchPresenter.reSearch();
                        mUILoader.updateStatus(UILoader.UIStatus.LOADING);
                    }
                }
            });
        }


        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mInputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //搜索逻辑
//                String keyword = mInputText.getText().toString().trim();
//                if (mSearchPresenter != null) {
//                    mSearchPresenter.doSearch(keyword);
//                    if (mUILoader != null) {
//                        mUILoader.updateStatus(UILoader.UIStatus.LOADING);
//                    }
                //}
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索逻辑
                String keyword = mInputText.getText().toString().trim();
                if (mSearchPresenter != null) {
                    mSearchPresenter.doSearch(keyword);
                    if (mUILoader != null) {
                        mUILoader.updateStatus(UILoader.UIStatus.LOADING);
                    }
                }
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

//        mFlowTextLayout.setClickListener(new FlowTextLayout.ItemClickListener() {
//            @Override
//            public void onItemClick(String text) {
//                Toast.makeText(SearchActivity.this,text,Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    private void initView() {
        mBackBtn = this.findViewById(R.id.search_back);
        mInputText = this.findViewById(R.id.search_edit);
        mSearchBtn = this.findViewById(R.id.search_btn);
        mSearchResult = this.findViewById(R.id.search_container);

        //mFlowTextLayout = this.findViewById(R.id.flow_text_layout);
        if (mUILoader == null) {
            mUILoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView();
                }
            };

            //Fragment添加View之前需要删除View
            if (mUILoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) mUILoader.getParent()).removeView(mUILoader);
            }
            mSearchResult.addView(mUILoader);
        }

    }

    /**
     * 创建搜索成功的VIew
     * @return
     */
    private View createSuccessView() {
        View resultView = LayoutInflater.from(this).inflate(R.layout.search_result_layout,null);
        mResultListView = resultView.findViewById(R.id.result_list_view);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mResultListView.setLayoutManager(layoutManager);

        //设置适配器 直接复用RecommendList的Adapter
        mRecommendListAdapter = new RecommendListAdapter();
        mResultListView.setAdapter(mRecommendListAdapter);




        return resultView;
    }

    @Override
    public void onSearchResultLoaded(List<Album> result) {
        LogUtil.d(TAG,"liuyu onSearchResultLoaded");
        if (result != null) {
            if (result.size() == 0) {
                if (mUILoader != null) {
                    mUILoader.updateStatus(UILoader.UIStatus.EMPTY);
                }
            }else{
                if (mUILoader != null && mRecommendListAdapter != null) {
                    LogUtil.d(TAG,"mUILoader != null && mRecommendListAdapter != null");
                    mRecommendListAdapter.setData(result);
                    mUILoader.updateStatus(UILoader.UIStatus.SUCCESS);
                }
            }
        }
    }

    @Override
    public void onHotWordLoaded(List<HotWord> hotWordList) {
        LogUtil.d(TAG,"hotWorldList-- >"+hotWordList.size());
        List<String> hotwords = new ArrayList<>();
        hotwords.clear();
        for (HotWord hotWord : hotWordList) {
            String serachWord = hotWord.getSearchword();
            hotwords.add(serachWord);
        }
//        Collections.sort(hotwords);
//        mFlowTextLayout.setTextContents(hotwords);
    }

    @Override
    public void onLoadMoreResult(List<Album> result, boolean isOkay) {

    }

    @Override
    public void onRecommendWordLoaded(List<QueryResult> keyWordList) {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        if (mUILoader != null) {
            mUILoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
        }
    }
}
