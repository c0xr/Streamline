package com.cory.streamline.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ListView
import com.cory.streamline.R

class SettingActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) =
            Intent(context, SettingActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar!!.title = "设置"
        val listView = findViewById<ListView>(R.id.listView)
        val dir=
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .path+"/Streamline/"
        val list = listOf(
            SettingListAdapter.Option("文件存储位置", dir, R.drawable.ic_back),
            SettingListAdapter.Option("自定义Wallpaper详情布局", null, R.drawable.ic_back)
        )
        listView.adapter = SettingListAdapter(this, list)
    }
}