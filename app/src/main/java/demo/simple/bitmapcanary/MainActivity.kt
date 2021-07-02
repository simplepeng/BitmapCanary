package demo.simple.bitmapcanary

import android.content.Intent
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
    val btnFragment by lazy { findViewById<Button>(R.id.btnFragment) }
    val btnViewPager by lazy { findViewById<Button>(R.id.btnViewPager) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnLoad.setOnClickListener {
            loadResource()
            loadBackground()
            loadGlideResource()
            loadNetwork()
        }

        btnFragment.setOnClickListener {
            startActivity(Intent(this, FragmentReplaceActivity::class.java))
        }

        btnViewPager.setOnClickListener {
            startActivity(Intent(this, ViewPagerActivity::class.java))
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
        Helper.loadImage(this, imageView3)
    }
}