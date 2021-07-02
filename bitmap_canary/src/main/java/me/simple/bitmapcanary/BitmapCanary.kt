package me.simple.bitmapcanary

import android.app.Activity
import android.content.Context

object BitmapCanary {

    @Synchronized
    internal fun install(context: Context) {
        if (!Helper.isSupport()) return

        Helper.parseMetaData(context)

        Helper.startHook()
    }

    /**
     * 忽略一个Activity的某些view
     * viewIds不传就是全部忽略
     */
    @Synchronized
    fun ignoreActivity(
        clazz: Class<out Activity>,
        viewIds: List<Int> = emptyList()
    ) {
        ignoreClassWithIds(clazz.name, viewIds)
    }

    /**
     * 忽略一个类的某些View
     * viewIds不传就是全部忽略
     */
    fun ignoreClassWithIds(
        clazzName: String,
        viewIds: List<Int> = emptyList()
    ) {
        if (Helper.mContext == null) return

        val viewNames = viewIds.map { id ->
            Helper.getViewNameById(Helper.mContext!!.resources, id)
        }

        ignoreClassWithNames(clazzName, viewNames)
    }

    /**
     * 忽略一个类的某些View
     * viewNames不传就是全部忽略
     */
    fun ignoreClassWithNames(
        clazzName: String,
        viewNames: List<String> = emptyList()
    ) {
        if (Helper.mContext == null) return

        Helper.ignoreClassMap[clazzName] = viewNames
    }
}