package com.onedevapp.unityintegrationbaseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.unity3d.player.UnityPlayer


/**
 * A simple [Fragment] subclass.
 * Use the [UnityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UnityFragment : Fragment() {
    protected var mUnityPlayer: UnityPlayer? = null
    var frameLayoutForUnity: FrameLayout? = null

    fun UnityFragment() {}

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mUnityPlayer = UnityPlayer(activity)
        val view = inflater.inflate(R.layout.fragment_unity, container, false)
        frameLayoutForUnity =
                view.findViewById<View>(R.id.frame_layout_for_unity) as FrameLayout
        frameLayoutForUnity!!.addView(
                mUnityPlayer!!.view,
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        mUnityPlayer!!.requestFocus()
        mUnityPlayer!!.windowFocusChanged(true)
        return view
    }

    /*companion object {
       *//* fun newInstance(param1: String, param2: String) =
                UnityFragment().apply {
                    arguments = Bundle().apply {
                        putString("highScore", param1)
                        putString("gameScene", param2)
                    }
                }*//*
        @JvmStatic
        private val HIGH_SCORE = "highScore"
        private val GAME_SCENE = "gameScene"

        @JvmStatic
        fun newInstance(param1: Int, param2: String): UnityFragment {
            val args: Bundle = Bundle()
            args.putInt(HIGH_SCORE, param1)
            args.putString(GAME_SCENE, param2)

            val fragment = UnityFragment()
            fragment.arguments = args
            return fragment
        }
    }*/

    override fun onDestroy() {
        mUnityPlayer!!.quit()
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mUnityPlayer!!.pause()
    }

    override fun onResume() {
        super.onResume()
        mUnityPlayer!!.resume()
    }
}