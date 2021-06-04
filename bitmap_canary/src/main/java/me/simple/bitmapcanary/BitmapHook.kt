package me.simple.bitmapcanary

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.View
import de.robv.android.xposed.XC_MethodHook

class BitmapHook : XC_MethodHook() {

    override fun beforeHookedMethod(param: MethodHookParam) {
        super.beforeHookedMethod(param)

        parseParam(param)
    }

    private fun parseParam(param: MethodHookParam) {
        if (param.args.isEmpty()) return

        val firstParam = param.args[0]
        if (firstParam !is BitmapDrawable) return

        val bd = firstParam as BitmapDrawable
        val bitmap: Bitmap = bd.bitmap ?: return

        Helper.log("bitmap width = ${bitmap.width}")
        Helper.log("bitmap height = ${bitmap.height}")

        val view = param.thisObject as View

        val context = view.context
        val activity = Helper.getActivity(context)
        if (activity != null) {
            Helper.log("Activity = ${activity::class.java.name}")
        }

        val viewName = Helper.getViewNameById(view)
        Helper.log("view id = $viewName")
        Helper.log("view width = ${view.width}")
        Helper.log("view height = ${view.height}")

        val byteCount = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.allocationByteCount
        } else {
            bitmap.byteCount
        }
        Helper.log("bitmap size = ${byteCount / 1024}kb")

        val m = String.format("%.02f", byteCount / 1024f / 1024f)
        Helper.log("bitmap size = ${m}M")

        val config = bitmap.config
        Helper.log("bitmap config = $config")

        Helper.log("----------------------------------------------------")
    }
}