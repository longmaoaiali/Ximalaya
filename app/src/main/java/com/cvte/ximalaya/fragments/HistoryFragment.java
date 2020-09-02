package com.cvte.ximalaya.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cvte.ximalaya.R;
import com.cvte.ximalaya.base.BaseFragment;
import com.cvte.ximalaya.utils.LogUtil;

/**
 * Created by user on 2020/9/2.
 */

public class HistoryFragment extends BaseFragment {
    private static final String TAG = "HistoryFragment";

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        LogUtil.d(TAG,"HistoryFragment --> onSubViewLoaded");
        //此处遇见大坑忘记返回rootView 返回的是一个NULL
        View rootView = layoutInflater.inflate(R.layout.fragment_history,container,false);
        return rootView;
    }
}
