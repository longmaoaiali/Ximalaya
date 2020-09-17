package com.cvte.ximalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cvte.ximalaya.R;
import com.cvte.ximalaya.adapters.PlayListAdatpter;
import com.cvte.ximalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

/**
 * Created by user on 2020/9/16.
 */

public class PlayerPopWindow  extends PopupWindow {

    private final View mPopView;
    private View mCloseBtn;
    private RecyclerView mTrackList;
    private PlayListAdatpter mPlayListAdatpter;

    public PlayerPopWindow() {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //弹出POP界面后再点击可以收回 需要设置如下两个
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);

        mPopView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null, false);

        setContentView(mPopView);
        //设置窗口弹出与退出的动画
        setAnimationStyle(R.style.pop_animation);

        //初始化view的控件
        initView();
        initEventListener();
    }

    private void initEventListener() {
        //点击以后调用dismiss 窗口消失
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerPopWindow.this.dismiss();
            }
        });
    }

    private void initView() {
        mCloseBtn = mPopView.findViewById(R.id.play_list_close_btn);
        mTrackList = mPopView.findViewById(R.id.play_list_recycler);
        //找到控件 设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(BaseApplication.getAppContext());
        mTrackList.setLayoutManager(layoutManager);
        //设置适配器
        mPlayListAdatpter = new PlayListAdatpter();
        mTrackList.setAdapter(mPlayListAdatpter);
    }

    public void setListData(List<Track> data){
        if (mPlayListAdatpter != null) {
            mPlayListAdatpter.setData(data);
        }
    }

    public void setCurrentPlayPosition(int position){
        if (mPlayListAdatpter != null) {
            mPlayListAdatpter.setCurrentPlayPosition(position);
            //当前item移动至范围之类
            mTrackList.scrollToPosition(position);
        }
    }

    public void setPlayListItemClickListener(PlayListItemClickListener listener){
        mPlayListAdatpter.setOnItemClickListener(listener);
    }

    public interface PlayListItemClickListener{
        void onItemClick(int position);
    }

}
