package com.tronlink.autoinstall;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.app.ActivityCompat;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

public class MiAccessibilityService extends AccessibilityService {
    private static final String TAG = "Source";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"Event: " + event.toString());
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
                event.getPackageName().equals("com.miui.securitycenter")) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo != null){
                installConfirm(nodeInfo);
            }
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED &&
                event.getPackageName().toString().contains("systemui")) {
            backButton();
        }
    }
//检测当系统进入任务管理或者下拉刷新页面后，点击返回键
    private void backButton() {
        if (Build.VERSION.SDK_INT >= 16) {
            if (ActivityCompat.checkSelfPermission(MiAccessibilityService.this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                performGlobalAction(GLOBAL_ACTION_BACK);
            } else {
                return;
            }
            }
    }
//自动点击"继续安装"
    private void installConfirm(AccessibilityNodeInfo rootNode) {
        Log.d(TAG,"Source: " + rootNode.toString());
        List<AccessibilityNodeInfo> nodeInfoList = new ArrayList<>();
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("继续安装"));
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    @Override
    public void onInterrupt() {

    }
}
