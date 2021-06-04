package me.simple.bitmapcanary

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import de.robv.android.xposed.DexposedBridge
import java.lang.Exception

internal object Helper {

    const val TAG = "BitmapCanary"

    fun isSupport(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false
        }
        return true
    }

    fun startHook() {
        startHookImageView()
        startHookViewBackground()
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

    fun log(msg: String) {
        logI(msg)
    }

    fun logI(msg: String) {
        Log.i(TAG, msg)
    }

    fun logE(msg: String) {
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
        if (view.id == View.NO_ID) return ""

        try {
            return view.resources.getResourceEntryName(view.id)
        } catch (e: Exception) {

        }

        return ""
    }
}