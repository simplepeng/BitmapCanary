package me.simple.bitmapcanary

import android.app.Activity
import android.content.Context

object BitmapCanary {

    internal fun install(context: Context) {
    }

    fun ignoreActivity(
        clazz: Class<out Activity>,
        viewIds: List<Int> = emptyList()
    ) {

    }

    fun ignoreClassWithIds(
        clazzName: String,
        viewIds: List<Int> = emptyList()
    ) {

    }

    fun ignoreClassWithNames(
        clazzName: String,
        viewNames: List<String> = emptyList()
    ) {

    }
}