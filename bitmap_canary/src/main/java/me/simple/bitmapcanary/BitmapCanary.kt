package me.simple.bitmapcanary

import android.content.Context

object BitmapCanary {

    @Synchronized
    fun install(context: Context) {
        if (!Helper.isSupport()) return

        Helper.parseMetaData(context)

        Helper.startHook()
    }
}