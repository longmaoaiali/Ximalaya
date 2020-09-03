package com.cvte.ximalaya.fragments;

import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cvte.ximalaya.R;
import com.cvte.ximalaya.adapters.RecommendListAdapter;
import com.cvte.ximalaya.base.BaseFragment;
import com.cvte.ximalaya.utils.Constants;
import com.cvte.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2020/9/2.
 */

public class RecommendFragment extends BaseFragment{

    private static final String TAG = "RecommendFragment";
    private View mRootView;
    private RecyclerView mRecommendRv;
    private RecommendListAdapter mRecommendListAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        LogUtil.d(TAG,"RecommendFragment --> onSubViewLoaded");
        /*加载View*/
        mRootView = layoutInflater.inflate(R.layout.fragment_recommand,container,false);

        /*1.找到控件
        * 2,设置布局管理器 垂直方向
        * 3.创建适配器
        * 4.向适配器设置数据
        * */
        mRecommendRv = mRootView.findViewById(R.id.recommend_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecommendRv.setLayoutManager(linearLayoutManager);

        mRecommendRv.addItemDecoration(new RecyclerView.ItemDecoration() {
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
        mRecommendRv.setAdapter(mRecommendListAdapter);

        /*使用XAMALAYA SDK拿数据 SDK 3.10.6 获取猜你喜欢专辑*/
        /*view绘制里面请求远程数据可能会出现，后面在处理*/
        getRecommendData();


        //返回View
        return mRootView;
    }

    private void getRecommendData() {
        LogUtil.d(TAG,"request Recommend Data");
        //封装map参数，实现回调函数
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMAND_COUNT+"");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(@Nullable GussLikeAlbumList gussLikeAlbumList) {
                LogUtil.d(TAG,"thread name --> "+Thread.currentThread().getName());
                if (gussLikeAlbumList != null) {
                    List<Album> albumList = gussLikeAlbumList.getAlbumList();
                    if (albumList != null) {
                        LogUtil.d(TAG,"size -->" + albumList.size());
                        //得到数据以后，更新UI
                        updateRecommendUI(albumList);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.d(TAG,"errorCode --> "+i+" errorMsg -->"+s );
            }
        });
    }

    private void updateRecommendUI(List<Album> albumList) {
        mRecommendListAdapter.setData(albumList);

    }
}
