package me.simple.bitmapcanary

import android.content.Context

object BitmapCanary {

    @Synchronized
    fun install(context: Context) {
        if (!Helper.isSupport(context)) return


        Helper.startHook()
    }
}