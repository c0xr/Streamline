package com.cory.streamline.about

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.cory.streamline.R
import com.cory.streamline.setting.SettingActivity
import com.cory.streamline.util.toast

class AboutActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) =
            Intent(context, AboutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val versionText = findViewById<TextView>(R.id.version)
        val version = packageManager.getPackageInfo(packageName, 0).versionName
        versionText.text = "版本 $version"

        val updateButton=findViewById<Button>(R.id.update)
        updateButton.setOnClickListener {
            toast("已是最新版")
        }

        supportActionBar?.title = "关于"
    }
}