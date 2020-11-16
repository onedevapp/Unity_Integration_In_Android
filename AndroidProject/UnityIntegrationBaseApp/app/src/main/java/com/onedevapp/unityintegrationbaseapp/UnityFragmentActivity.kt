package com.onedevapp.unityintegrationbaseapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ToggleButton
import com.unity3d.player.UnityPlayer

class UnityFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unity_fragment)

        /*if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.nav_host_fragment, UnityFragment.newInstance(10, "SceneC"))
                    .commit()
        }*/

        findViewById<ToggleButton>(R.id.toggleButton).setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                UnityPlayer.UnitySendMessage("Cube", "ToggleRotation", "false")
            } else {
                UnityPlayer.UnitySendMessage("Cube", "ToggleRotation", "true")
            }
        }

        findViewById<Button>(R.id.button).setOnClickListener { view ->
            val colors = arrayOf(
                    "#FFFFFF",
                    "#000000",
                    "#FF8F00",
                    "#EF6C00",
                    "#D84315",
                    "#37474F"
            )
            val randomColor = colors.random()
            view.setBackgroundColor(Color.parseColor(randomColor))
            UnityPlayer.UnitySendMessage("Cube", "ChangeColor", randomColor)
        }

    }
}