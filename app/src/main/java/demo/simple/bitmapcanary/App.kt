package demo.simple.bitmapcanary

import android.app.Application
import me.simple.bitmapcanary.BitmapCanary

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        BitmapCanary.ignore(MainActivity::class.java, R.id.imageView3)
    }
}