package com.cvte.ximalaya;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int sPermissionRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT>=23) {
            getUserPermission();
        }

        Map<String,String> map = new HashMap<String,String>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(@Nullable CategoryList categoryList) {
                List<Category> categories = categoryList.getCategories();
                if(categories != null){
                    int size = categories.size();
                    Log.d(TAG,"categories size --- <"+size);
                    for (Category category : categories) {
                        Log.d(TAG,"category -->" + category.getCategoryName());
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.e(TAG,"error code "+i+" error msg --->"+s);
            }
        });
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
