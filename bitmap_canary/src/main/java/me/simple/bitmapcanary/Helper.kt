package me.simple.bitmapcanary

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import de.robv.android.xposed.DexposedBridge

internal object Helper {

    private const val TAG = "BitmapCanary"
    val builder = Builder()

    private const val KEY_THRESHOLD_VALUE = "bitmap_canary_threshold_value"
    private const val KEY_ENABLE_LOG = "bitmap_canary_enable_log"

    fun isSupport(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false
        }
        return true
    }

    fun startHook() {
        try {
            startHookImageView()
            startHookViewBackground()
        } catch (e: Exception) {
            logE("$TAG start hook have exception")
            e.printStackTrace()
        }
    }

    private fun startHookImageView() {
        DexposedBridge.findAndHookMethod(
            ImageView::class.java,
            "setImageDrawable",
            Drawable::class.java,
            BitmapHook()
        )
    }

    private fun startHookViewBackground() {
        DexposedBridge.findAndHookMethod(
            View::class.java,
            "setBackgroundDrawable",
            Drawable::class.java,
            BitmapHook()
        )
    }

    fun log(msg: String, logE: Boolean = false) {
        if (!builder.enableLog) return

        if (logE) {
            logE(msg)
        } else {
            logI(msg)
        }
    }

    private fun logI(msg: String) {
        Log.i(TAG, msg)
    }

    private fun logE(msg: String) {
        Log.e(TAG, msg)
    }

    fun getActivity(context: Context): Activity? {
        if (context is Activity) return context

        var tmpContext = context
        while (tmpContext !is Activity && tmpContext is ContextWrapper) {
            tmpContext = (context as ContextWrapper).baseContext
        }

        if (tmpContext is Activity) return tmpContext

        return null
    }

    fun getViewNameById(view: View): String {
        if (view.id == View.NO_ID) return "no_id"

        try {
            return view.resources.getResourceEntryName(view.id)
        } catch (e: Exception) {

        }

        return "not found"
    }

    fun parseMetaData(context: Context) {
        try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            val metaData = appInfo.metaData ?: return

            if (metaData.containsKey(KEY_THRESHOLD_VALUE)) {
                val thresholdValue = metaData.get(KEY_THRESHOLD_VALUE) as Int
                builder.thresholdValue = thresholdValue
            }

            if (metaData.containsKey(KEY_ENABLE_LOG)) {
                val enableLog = metaData.get(KEY_ENABLE_LOG) as Boolean
                builder.enableLog = enableLog
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showToast(context: Context, text: String) {
        val toast = Toast.makeText(context.applicationContext, text, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    @SuppressLint("PrivateApi")
    fun getAppContext(context: Context?): Context? {
        var appContext = context?.applicationContext

        if (appContext == null) {
            try {
                val activityThread = Class.forName("android.app.ActivityThread")
                val thread = activityThread.getMethod("currentActivityThread").invoke(null)
                val app = activityThread.getMethod("getApplication").invoke(thread) as? Application
                appContext = app?.applicationContext
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return appContext
    }
}