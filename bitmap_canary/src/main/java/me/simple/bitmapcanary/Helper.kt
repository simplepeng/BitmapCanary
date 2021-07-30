package me.simple.bitmapcanary

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import de.robv.android.xposed.DexposedBridge

@SuppressLint("StaticFieldLeak")
internal object Helper {

    private const val TAG = "BitmapCanary"
    val builder = Builder()

    private const val KEY_THRESHOLD_VALUE = "bitmap_canary_threshold_value"
    private const val KEY_ENABLE_LOG = "bitmap_canary_enable_log"

    var mContext: Context? = null

    val ignoreClassMap = hashMapOf<String, List<String?>>()

    /**
     * 是否支持hook
     */
    fun isSupport(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            log("不支持的系统版本 -- ${Build.VERSION.SDK_INT}", true)
            return false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            log("不支持的系统版本 -- ${Build.VERSION.SDK_INT}", true)
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

    /**
     * hook-ImageView.setImageDrawable
     */
    private fun startHookImageView() {
        DexposedBridge.findAndHookMethod(
            ImageView::class.java,
            "setImageDrawable",
            Drawable::class.java,
            DrawableHook()
        )
    }

    /**
     * hook-View.setBackgroundDrawable
     */
    private fun startHookViewBackground() {
        DexposedBridge.findAndHookMethod(
            View::class.java,
            "setBackgroundDrawable",
            Drawable::class.java,
            DrawableHook()
        )
    }

    /**
     * 输出日志
     */
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

    /**
     * 从Context中获取Activity
     */
    fun getActivity(context: Context): Activity? {
        if (context is Activity) return context

        var tmpContext = context
        while (tmpContext !is Activity && tmpContext is ContextWrapper) {
            tmpContext = (context as ContextWrapper).baseContext
        }

        if (tmpContext is Activity) return tmpContext

        return null
    }

    /**
     * 获取当前显示的Fragment
     */
    fun getVisibleFragment(activity: Activity?): Fragment? {
        if (activity == null) return null

        if (activity is FragmentActivity) {
            val fragments = activity.supportFragmentManager.fragments
            if (fragments.isEmpty()) return null
            for (fragment in fragments) {
                if (fragment.userVisibleHint) {
                    return fragment
                }
            }
        }

        return null
    }

    /**
     * 获取Activity中的fragments
     */
    fun getFragmentsInActivity(activity: Activity?): List<Fragment> {
        if (activity == null) return emptyList()

        if (activity is FragmentActivity) {
            return activity.supportFragmentManager.fragments
        }

        return emptyList()
    }

    /**
     * 获取已经Resumed了的Fragments
     */
    fun getResumedFragments(activity: Activity?): List<Fragment> {
        return getFragmentsInActivity(activity).filter {
            it.isResumed
        }
    }

    /**
     * 获取Activity中的fragments
     */
    fun getViewAttachedFragment(id: Int, fragments: List<Fragment>): Fragment? {
        var view: View? = null
        for (fragment in fragments) {
            view = fragment.view?.findViewById<View>(id)
            if (view != null) return fragment
        }
        return null
    }

    /**
     * 获取Fragment的父类Fragments
     */
    fun getParentFragments(f: Fragment): List<Fragment> {
        val pfs = mutableListOf<Fragment>()
        var curFragment = f
        while (curFragment.parentFragment != null) {
            val pf = curFragment.parentFragment!!
            pfs.add(pf)
            curFragment = pf
        }
        return pfs
    }

    /**
     * 用id获取View的名称
     */
    fun getViewNameById(view: View): String {
        return getViewNameById(view.resources, view.id)
    }

    /**
     * 用id获取View的名称
     */
    fun getViewNameById(
        resources: Resources,
        id: Int
    ): String {
        if (id == View.NO_ID) return "no_id"

        try {
            return resources.getResourceEntryName(id)
        } catch (e: Exception) {

        }

        return "not found"
    }

    /**
     * 解析MetaData
     */
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

    /**
     * 显示Toast
     */
    fun showToast(context: Context, text: String) {
        val toast = Toast.makeText(context.applicationContext, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 获取ApplicationContext
     */
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

    /**
     * 可以被忽略不监控
     */
    fun canIgnore(clazz: Class<*>?, viewName: String?): Boolean {
        return canIgnore(clazz?.name, viewName)
    }

    /**
     * 可以被忽略不监控
     */
    fun canIgnore(clazzName: String?, viewName: String?): Boolean {
        if (clazzName.isNullOrEmpty()) return false

        val viewNames = Helper.ignoreClassMap[clazzName]
        if (viewNames != null && viewNames.isEmpty()) return true

        return viewNames?.contains(viewName) == true
    }

    /**
     * 获取类名
     */
    fun getClassName(any: Any?): String {
        if (any == null) return ""

        return any::class.java.name ?: ""
    }
}