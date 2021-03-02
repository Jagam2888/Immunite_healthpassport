package com.cmg.vaccine.switchprofileblurbackground

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.WindowManager


class BlurUtils {
    fun isFullScreen(activity: Activity): Boolean {
        return (WindowManager.LayoutParams.FLAG_FULLSCREEN and activity.window.attributes.flags
                == WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun isTranslucentStatusBar(activity: Activity): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT and (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS and activity.window.attributes.flags)
                 WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
    }

    fun getStatusBarHeight(context: Context): Int {
        val heightResId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (heightResId > 0) context.resources.getDimensionPixelSize(heightResId) else 0
    }
}