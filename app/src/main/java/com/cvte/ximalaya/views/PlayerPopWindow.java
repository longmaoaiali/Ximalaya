package com.cvte.ximalaya.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.cvte.ximalaya.R;
import com.cvte.ximalaya.base.BaseApplication;

/**
 * Created by user on 2020/9/16.
 */

public class PlayerPopWindow  extends PopupWindow {
    public PlayerPopWindow() {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //弹出POP界面后再点击可以收回 需要设置如下两个
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);

        View popView = LayoutInflater.from(BaseApplication.getAppContext()).inflate(R.layout.pop_play_list, null, false);

        setContentView(popView);
        //设置窗口弹出与退出的动画
        setAnimationStyle(R.style.pop_animation);
    }


}
