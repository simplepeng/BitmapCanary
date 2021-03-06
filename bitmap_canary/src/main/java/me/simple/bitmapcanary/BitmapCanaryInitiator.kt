package me.simple.bitmapcanary

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class BitmapCanaryInitiator : ContentProvider() {

    override fun onCreate(): Boolean {
        val appContext = Helper.getAppContext(context) ?: return false
        BitmapCanary.install(appContext)
        Helper.mContext = appContext
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}