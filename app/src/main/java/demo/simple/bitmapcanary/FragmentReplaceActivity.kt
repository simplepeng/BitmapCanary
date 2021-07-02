package demo.simple.bitmapcanary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import demo.simple.bitmapcanary.fragments.Fragment1
import demo.simple.bitmapcanary.fragments.Fragment2
import demo.simple.bitmapcanary.fragments.Fragment3
import java.util.*

class FragmentReplaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_fragment)

        changeFragment(0)

        findViewById<View>(R.id.btnReplace).setOnClickListener {
            val index = Random().nextInt(3)
            changeFragment(index)
        }
    }

    private fun changeFragment(index: Int) {
        val fragment = when (index) {
            0 -> Fragment1()
            1 -> Fragment2()
            else -> Fragment3()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameContainer, fragment)
            .commitAllowingStateLoss()

        Helper.debugLog("size = ${supportFragmentManager.fragments.size}")
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            Helper.debugLog("${fragment::class.java.name} -- ${fragment.userVisibleHint}")
        }
    }

    override fun onResume() {
        super.onResume()

    }
}