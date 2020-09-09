package com.cvte.ximalaya;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvte.ximalaya.adapters.DetailListAdapter;
import com.cvte.ximalaya.base.BaseActivity;
import com.cvte.ximalaya.interfaces.IAlbumDetailViewCallback;
import com.cvte.ximalaya.presenters.AlbumDetialPresenter;
import com.cvte.ximalaya.utils.ImageBlur;
import com.cvte.ximalaya.utils.LogUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * Created by user on 2020/9/7.
 */

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private ImageView mLargeCover;
    private ImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetialPresenter mAlbumDetialPresenter;
    private static final String TAG = "DetailActivity";

    private int mCurrentPage = 1;
    private RecyclerView mRecyclerViewDeatilListView;
    private DetailListAdapter mDetailListAdapter;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        /*设置顶部透明*/
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initView();
        mAlbumDetialPresenter = AlbumDetialPresenter.getInstance();
        //
        mAlbumDetialPresenter.registerViewCallback(this);
    }

    private void initView() {
        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.iv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);
        //RecyclerView 布局管理器 适配器
        mRecyclerViewDeatilListView = this.findViewById(R.id.album_detail_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewDeatilListView.setLayoutManager(layoutManager);
        mDetailListAdapter = new DetailListAdapter();
        mRecyclerViewDeatilListView.setAdapter(mDetailListAdapter);

        //RecyclerView 设置item  间隔
        mRecyclerViewDeatilListView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = UIUtil.dip2px(view.getContext(),2);
                outRect.bottom = UIUtil.dip2px(view.getContext(),2);
                outRect.left = UIUtil.dip2px(view.getContext(),2);
                outRect.right = UIUtil.dip2px(view.getContext(),2);
            }
        });


    }


    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        mDetailListAdapter.setData(tracks);

    }

    @Override
    public void onAlbumLoaded(Album album) {

        /*根据album的ID与页码进行 */
        long id = album.getId();
        mAlbumDetialPresenter.getAlbumDetail((int)id, mCurrentPage);

        if (mAlbumTitle != null) {
            mAlbumTitle.setText(album.getAlbumTitle());
        }

        if (mAlbumAuthor != null) {
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }

        //毛玻璃效果
        if (mLargeCover != null) {
            Picasso.get().load(album.getCoverUrlLarge()).into(mLargeCover, new Callback() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onSuccess() {
                    Drawable drawable = mLargeCover.getDrawable();
                    if (drawable != null) {
                        ImageBlur.makeBlur(mLargeCover,DetailActivity.this);
                    }
                }

                @Override
                public void onError(Exception e) {
                    LogUtil.d(TAG,"Picasso onError");
                }
            });
        }

        if (mSmallCover != null) {
            Picasso.get().load(album.getCoverUrlSmall()).into(mSmallCover);
        }
    }
}
