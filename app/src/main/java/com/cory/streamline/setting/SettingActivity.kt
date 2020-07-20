package com.cory.streamline.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.cory.streamline.R
import com.cory.streamline.detail.SubsampleActivity

class SettingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) =
            Intent(context, SettingActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }
}