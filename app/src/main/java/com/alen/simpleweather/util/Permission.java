package com.alen.simpleweather.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/12/4.
 */

public class Permission {
    private Activity activity;
    public boolean start(final Activity activity){
        this.activity = activity;
        final List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity, permissions, 1);
        }else {
            return true;
        }
        return false;
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Utility.showToast(activity, "你拒绝此权限");
                            return;
                        }
                    }
                }else {
                    Utility.showToast(activity, "发生未知错误");
                }
                break;
            default:
        }
    }
}
