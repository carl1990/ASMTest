package com.carl.asmtest.tack;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

/**
 * Created by Carl on 2018/6/13.
 */

public class TrackHelper {

    private static final String TAG = TrackHelper.class.getSimpleName();


    /**
     * 实现onClick点击事件的自动注入处理
     */
    public static void onClick(View view) {
        String path = TrackUtil.getPath(view);
        String activityName = TrackUtil.getActivityName(view);
        path = activityName + ":::::onClick:::::" + path;
        Log.d(TAG, path);
    }

    public static void onActivityResume(Activity activity) {
        Log.d(TAG, "onActivityResume()  in ::::" + activity.getClass().getCanonicalName());
    }


    public static void onActivityPause(Activity activity) {
        Log.d(TAG, "onActivityPause()  in :::  " + activity.getClass().getCanonicalName());
    }


    public static void onFragmentResume(Fragment fragment) {
        Log.d(TAG, "onFragmentResume" + fragment.getClass().getSimpleName());
    }

    public static void onFragmentPause(Fragment fragment) {
        Log.d(TAG, "onFragmentPause"  + fragment.getClass().getSimpleName());
    }

    public static void setFragmentUserVisibleHint(Fragment fragment, boolean isVisibleToUser) {
        Log.d(TAG, "setFragmentUserVisibleHint->" + isVisibleToUser + "->" + fragment.getClass().getSimpleName());
    }

    public static void onFragmentHiddenChanged(Fragment fragment, boolean hidden) {
        Log.d(TAG, "onFragmentHiddenChanged->" + hidden + "->" + fragment.getClass().getSimpleName());
    }
}
