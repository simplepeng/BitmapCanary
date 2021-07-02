package demo.simple.bitmapcanary

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import demo.simple.bitmapcanary.fragments.Fragment1
import demo.simple.bitmapcanary.fragments.Fragment2
import demo.simple.bitmapcanary.fragments.Fragment3
import java.util.*

class ViewPagerActivity : AppCompatActivity() {

    private val viewPager: ViewPager by lazy { findViewById(R.id.viewPager) }
    private val btnGet: Button by lazy { findViewById(R.id.btnGet) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager)

        viewPager.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return 100
            }

            override fun getItem(position: Int): Fragment {
                val realIndex = Random().nextInt(3)
                return when (realIndex) {
                    0 -> Fragment1()
                    1 -> Fragment2()
                    else -> Fragment3()
                }
            }
        }

        btnGet.setOnClickListener {

            findViewById<TextView>(R.id.tvIndex).text = "hello"

            Helper.debugLog("size = ${supportFragmentManager.fragments.size}")
            val fragments = supportFragmentManager.fragments
            for (fragment in fragments) {
                Helper.debugLog("${fragment::class.java.name} -- ${fragment.userVisibleHint}")
            }
        }
    }
}