package com.cory.streamline.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cory.streamline.R
import com.cory.streamline.util.globalContext
import com.cory.streamline.util.log
import com.sackcentury.shinebuttonlib.ShineButton
import kotlinx.android.synthetic.main.activity_layout_custom.*

class LayoutCustomActivity : AppCompatActivity(), ShineButton.OnCheckedChangeListener {
    private lateinit var button: ShineButton
    private lateinit var button2: ShineButton
    private lateinit var button3: ShineButton
    private lateinit var button4: ShineButton

    companion object {
        val SP_KEY = "prefer layout share preference"
        val ELEMENT_KEY = "prefer layout element"
        fun newIntent(context: Context) =
            Intent(context, LayoutCustomActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_custom)
        supportActionBar!!.title = "自定义Wallpaper详情布局"
        button = findViewById(R.id.shineButton)
        button2 = findViewById(R.id.shineButton2)
        button3 = findViewById(R.id.shineButton3)
        button4 = findViewById(R.id.shineButton4)
        button.init(this)
        button2.init(this)
        button3.init(this)
        button4.init(this)
        button.setOnCheckStateChangeListener(this)
        button2.setOnCheckStateChangeListener(this)
        button3.setOnCheckStateChangeListener(this)
        button4.setOnCheckStateChangeListener(this)
        setPreferState()
    }

    override fun onCheckedChanged(view: View?, checked: Boolean) {
        view as ShineButton
        if (!checked) {
            view.isChecked = true
            return
        }
        when (view.id) {
            R.id.shineButton -> {
                button2.isChecked = false
                button3.isChecked = false
                button4.isChecked = false
                savePreferLayout(R.layout.fragment_detail)
            }
            R.id.shineButton2 -> {
                button.isChecked = false
                button3.isChecked = false
                button4.isChecked = false
                savePreferLayout(R.layout.fragment_detail_2)
            }
            R.id.shineButton3 -> {
                button.isChecked = false
                button2.isChecked = false
                button4.isChecked = false
                savePreferLayout(R.layout.fragment_detail_3)
            }
            R.id.shineButton4 -> {
                button.isChecked = false
                button2.isChecked = false
                button3.isChecked = false
                savePreferLayout(R.layout.fragment_detail_4)
            }
        }
    }

    private fun savePreferLayout(layoutId: Int) {
        val sp = globalContext.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
        sp.edit()
            .putInt(ELEMENT_KEY, layoutId)
            .apply()
    }

    private fun setPreferState() {
        val sp = globalContext.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
        when (sp.getInt(ELEMENT_KEY, R.layout.fragment_detail)) {
            R.layout.fragment_detail -> button.isChecked = true
            R.layout.fragment_detail_2 -> button2.isChecked = true
            R.layout.fragment_detail_3 -> button3.isChecked = true
            R.layout.fragment_detail_4 -> button4.isChecked = true
        }
    }
}