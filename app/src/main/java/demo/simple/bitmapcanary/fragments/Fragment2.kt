package demo.simple.bitmapcanary.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import demo.simple.bitmapcanary.Helper
import demo.simple.bitmapcanary.R

class Fragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(Color.GREEN)
        view.findViewById<TextView>(R.id.tvIndex).text = "2"

        val iv1 = view.findViewById<ImageView>(R.id.imageView1)
        val iv2 = view.findViewById<ImageView>(R.id.imageView2)

        Helper.loadImage(requireActivity(), iv1)
        iv2.setImageResource(R.drawable.ic_launcher)
    }

    override fun onResume() {
        super.onResume()
        Helper.debugLog("Fragment2 onResume")
    }
}