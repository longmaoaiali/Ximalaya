package com.cvte.ximalaya;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cvte.ximalaya.base.BaseActivity;

/**
 * Created by user on 2020/9/10.
 */

public class PlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
    }
}
