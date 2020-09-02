package com.cvte.ximalaya;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cvte.ximalaya.adapters.IndicatorAdapter;
import com.cvte.ximalaya.adapters.MainContentAdapter;
import com.cvte.ximalaya.utils.LogUtil;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private static final String TAG = "MainActivity";
    private static final int sPermissionRequestCode = 1;
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogUtil.d(TAG, "onCreate-->");

        if (Build.VERSION.SDK_INT>=23) {
            getUserPermission();
        }

        initView();
//        Map<String,String> map = new HashMap<String,String>();
//        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
//            @Override
//            public void onSuccess(@Nullable CategoryList categoryList) {
//                List<Category> categories = categoryList.getCategories();
//                if(categories != null){
//                    int size = categories.size();
//                    Log.d(TAG,"categories size --- <"+size);
//                    for (Category category : categories) {
//                        Log.d(TAG,"category -->" + category.getCategoryName());
//                    }
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                //error code = 604注意检查真机是否有联网
//                Log.e(TAG,"error code "+i+" error msg --->"+s);
//            }
//        });
    }

    private void initView() {
        //viewPager
        mViewPager = this.findViewById(R.id.view_pager);
        //设置viewPage的适配器
        /*1. 创建ViewPager适配器
        * 2. 适配器实现返回Fragment getItem
        * 3. getItem的实现是根据index索引返回Fragment的实例 baseFragment = new RecommendFragment()*/
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);
        LogUtil.d(TAG,"ViewPager.setAdapter(mainContentAdapter)");
        mViewPager.setAdapter(mainContentAdapter);

        //indicator
        mMagicIndicator = this.findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.colorBackgroundRed));
        //创建indicator的适配器
        IndicatorAdapter adapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(adapter);


        mMagicIndicator.setNavigator(commonNavigator);
        /*将indicator与viewPager绑定，其实就是一个viewPager的监听*/
        //实现viewpager 与 indicator一起移动
        ViewPagerHelper.bind(mMagicIndicator,mViewPager);

    }

    private void getUserPermission() {
        String[] permission = new String[] {Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE};

        List<String> needPermission = new ArrayList<>();

        needPermission.clear();

        //将未授权的权限放入List中来申请权限
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permission[i]) != PackageManager.PERMISSION_GRANTED) {
                needPermission.add(permission[i]);
            }
        }

        if (needPermission.size()>0) {
            ActivityCompat.requestPermissions(this,permission,sPermissionRequestCode);
        } else {
            Log.d(TAG,"had get all permission");
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean hasPermissonDenied = false;
        if(requestCode == sPermissionRequestCode){
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_DENIED)
                    hasPermissonDenied = true;
            }
        }

        if(hasPermissonDenied){
            Log.d(TAG,"user denied permission");
            /*用户禁止权限获取处理*/
        } else {
            Log.d(TAG,"had requested get all permission");
        }
    }
}
