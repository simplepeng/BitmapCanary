package demo.simple.bitmapcanary

import android.app.Application
import demo.simple.bitmapcanary.fragments.Fragment1
import me.simple.bitmapcanary.BitmapCanary

class App : Application() {

    override fun onCreate() {
        super.onCreate()

//        BitmapCanary.ignoreActivity(
//            MainActivity::class.java,
//            mutableListOf(R.id.imageView1, R.id.imageView3)
//        )

//        BitmapCanary.ignoreActivity(MainActivity::class.java, mutableListOf(R.id.imageView3))
    }
}