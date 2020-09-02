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

public class SubscriptionFragment extends BaseFragment {

    private static final String TAG = "SubscriptionFragment";

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        LogUtil.d(TAG,"SubscriptionFragment --> onSubViewLoaded");
        View rootView = layoutInflater.inflate(R.layout.fragment_subscription,container,false);
        return rootView;
    }
}
