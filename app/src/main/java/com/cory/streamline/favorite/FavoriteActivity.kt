package com.cory.streamline.favorite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cory.streamline.R
import com.cory.streamline.home.HomeActivity
import com.cory.streamline.util.SOURCE_WALLHAVEN

class FavoriteActivity : AppCompatActivity() {
    companion object {
        fun newIntent(context: Context) = Intent(context, FavoriteActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        val favoriteFragment = FavoriteFragment.newInstance(SOURCE_WALLHAVEN)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.animator.anim_fragment_start,
                0,
                0,
                R.animator.anim_fragment_end
            )
            .add(
                R.id.fragment_container,
                favoriteFragment,
                HomeActivity.FRAGMENT_GALLERY_TAG
            )
            .commit()

        supportActionBar?.title = "我的收藏"
    }
}