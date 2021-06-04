package demo.simple.bitmapcanary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MainActivity : AppCompatActivity() {

    val imageView1 by lazy { findViewById<ImageView>(R.id.imageView1) }
    val view1 by lazy { findViewById<View>(R.id.view1) }
    val imageView2 by lazy { findViewById<ImageView>(R.id.imageView2) }
    val imageView3 by lazy { findViewById<ImageView>(R.id.imageView3) }
    val btnLoad by lazy { findViewById<Button>(R.id.btnLoad) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnLoad.setOnClickListener {
            loadResource()
            loadBackground()
            loadGlideResource()
            loadNetwork()
        }
    }

    private fun loadResource() {
        imageView1.setImageResource(R.drawable.ic_launcher)
    }

    private fun loadBackground() {
        view1.setBackgroundResource(R.drawable.ic_launcher)
    }

    private fun loadGlideResource() {
        Glide.with(this)
            .load(R.drawable.ic_launcher)
            .into(imageView2)
    }

    private fun loadNetwork() {
        val url =
            "https://huaweiyun.other.51wnl-cq.com/Mooda/DiaryPicture/a0a302b78e2f43d882b4359dc449f19f.jpg"
        val url1 =
            "https://huaweiyun.other.51wnl-cq.com/Mooda/DiaryPicture/3b296bfec2ac4824935c7c87827d3a97.jpg"
        val url2 =
            "https://huaweiyun.other.51wnl-cq.com/Mooda/DiaryPicture/61b77450a97e49009d18b84398a7efd4.jpeg"
        Glide.with(this)
            .asBitmap()
            .load(url2)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imageView3)
    }
}