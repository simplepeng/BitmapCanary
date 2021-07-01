package me.simple.bitmapcanary

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object BitmapCanary {

    internal val ignoreMap = hashMapOf<String, List<String?>>()

    internal lateinit var mCtx: Context

    @Synchronized
    internal fun install(context: Context) {
        mCtx = context
        if (!Helper.isSupport()) return

        Helper.parseMetaData(context)

        Helper.startHook()
    }

    @Synchronized
    fun ignore(clazz: Class<*>, vararg viewIds: Int) {
        val viewNames = viewIds.map { id ->
            Helper.getViewNameById(mCtx.resources, id)
        }
        ignoreMap[clazz.name] = viewNames
    }
}