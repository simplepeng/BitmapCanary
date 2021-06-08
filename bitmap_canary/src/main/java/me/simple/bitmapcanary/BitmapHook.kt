package me.simple.bitmapcanary

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.view.View
import de.robv.android.xposed.XC_MethodHook
import java.lang.StringBuilder

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

        val view = param.thisObject as? View ?: return

        view.post {
            verifyParam(bitmap, view)
        }
    }

    private fun verifyParam(bitmap: Bitmap, view: View) {
        val config = bitmap.config
        val context = view.context
        val activity = Helper.getActivity(context)
        val activityName = if (activity != null) {
            activity::class.java.name
        } else ""
        val viewName = Helper.getViewNameById(view)
        val kb = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.allocationByteCount / 1024
        } else {
            bitmap.byteCount / 1024
        }
        val m = String.format("%.02f", kb / 1024f)

        //输出
        val isLogE = kb >= Helper.builder.thresholdValue

        Helper.log("Activity = $activityName", isLogE)

        Helper.log("view id = $viewName", isLogE)
        Helper.log("view width = ${view.width}", isLogE)
        Helper.log("view height = ${view.height}", isLogE)

        Helper.log("bitmap width = ${bitmap.width}", isLogE)
        Helper.log("bitmap height = ${bitmap.height}", isLogE)
        Helper.log("bitmap config = $config", isLogE)
        Helper.log("bitmap size = ${kb}kb", isLogE)
        Helper.log("bitmap size = ${m}M", isLogE)

        Helper.log("----------------------------------------------------", isLogE)

        if (isLogE) {
            val builder = StringBuilder()
            builder.append("Oops!!!").append("\n").append("\n")
            builder.append("Bitmap Is Too Larger").append("\n").append("\n")
            builder.append("You Can See Logcat -- BitmapCanary")
            Helper.showToast(context, builder.toString())
        }
    }
}