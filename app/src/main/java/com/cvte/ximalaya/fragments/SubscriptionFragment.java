package com.cvte.ximalaya.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cvte.ximalaya.DetailActivity;
import com.cvte.ximalaya.R;
import com.cvte.ximalaya.adapters.RecommendListAdapter;
import com.cvte.ximalaya.base.BaseFragment;
import com.cvte.ximalaya.interfaces.ISubscriptionCallback;
import com.cvte.ximalaya.interfaces.ISubscriptionPresenter;
import com.cvte.ximalaya.presenters.AlbumDetialPresenter;
import com.cvte.ximalaya.presenters.RecommendPresenter;
import com.cvte.ximalaya.presenters.SubscriptionPresenter;
import com.cvte.ximalaya.utils.LogUtil;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by user on 2020/9/2.
 */

public class SubscriptionFragment extends BaseFragment implements ISubscriptionCallback, RecommendListAdapter.OnRecommendItemClickListner {

    private static final String TAG = "SubscriptionFragment";
    private RecyclerView mSubList;
    private ISubscriptionPresenter mSubscriptionPresenter;
    private RecommendListAdapter mRecommendListAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        LogUtil.d(TAG,"SubscriptionFragment --> onSubViewLoaded");
        View rootView = layoutInflater.inflate(R.layout.fragment_subscription,container,false);
        TwinklingRefreshLayout refreshLayout = rootView.findViewById(R.id.over_scroll_view);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);

        mSubList = rootView.findViewById(R.id.sub_list);
        mSubList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mSubList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                /*这里是需要像素值 使用magic*/
                outRect.top = UIUtil.dip2px(view.getContext(),5);
                outRect.bottom = UIUtil.dip2px(view.getContext(),5);
                outRect.left = UIUtil.dip2px(view.getContext(),5);
                outRect.right = UIUtil.dip2px(view.getContext(),5);
            }
        });

        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendListAdapter.setOnRecommendItemClickListner(this);
        mSubList.setAdapter(mRecommendListAdapter);

        mSubscriptionPresenter = SubscriptionPresenter.getInstance();
        mSubscriptionPresenter.registerViewCallback(this);
        mSubscriptionPresenter.getSubscriptionList();
        return rootView;
    }

    @Override
    public void onAddResult(boolean isSuccess) {

    }

    @Override
    public void onDeleteResult(boolean isSuccess) {

    }

    @Override
    public void onSubscriptionsLoaded(List<Album> albums) {
        if (mRecommendListAdapter != null) {
            //Collections.reverse(albums);
            mRecommendListAdapter.setData(albums);
        }
    }

    @Override
    public void onItemClick(int cliclPosition, Album album) {
        /*将专辑数据给到detail实现类*/
        AlbumDetialPresenter.getInstance().setAlbumDetail(album);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }
}
