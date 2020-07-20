package com.cory.streamline.history

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cory.streamline.R
import com.cory.streamline.setting.SettingActivity

class HistoryActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) =
            Intent(context, HistoryActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        supportActionBar?.title = "浏览记录"
    }
}