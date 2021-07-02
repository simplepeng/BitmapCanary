package demo.simple.bitmapcanary

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object Helper {

    const val imageUrl1 = "https://huaweiyun.other.51wnl-cq.com/Mooda/DiaryPicture/a0a302b78e2f43d882b4359dc449f19f.jpg"
    const val imageUrl2 = "https://huaweiyun.other.51wnl-cq.com/Mooda/DiaryPicture/3b296bfec2ac4824935c7c87827d3a97.jpg"
    const val imageUrl3 = "https://huaweiyun.other.51wnl-cq.com/Mooda/DiaryPicture/61b77450a97e49009d18b84398a7efd4.jpeg"

    fun debugLog(text: String) {
        Log.d("LogHelper", text)
    }

    fun loadImage(
        context: Context,
        iv: ImageView,
        url:String = imageUrl3
    ) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(iv)
    }
}