package com.cory.streamline.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.cory.streamline.R
import com.cory.streamline.about.AboutActivity
import com.cory.streamline.favorite.FavoriteActivity
import com.cory.streamline.history.HistoryActivity
import com.cory.streamline.login.data.model.LoggedInUser
import com.cory.streamline.login.ui_login.LoginActivity
import com.cory.streamline.setting.SettingActivity
import com.cory.streamline.util.user
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var loginButton: Button
    private lateinit var stateText: TextView
    private lateinit var nickText: TextView

    companion object {
        val FRAGMENT_GALLERY_TAG = "fragment gallery tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        token?.let {
            user = LoggedInUser(token)
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        stateText = navView.getHeaderView(0).findViewById(R.id.state)
        nickText = navView.getHeaderView(0).findViewById(R.id.nick)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        loginButton = navView.getHeaderView(0).findViewById(R.id.login)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        supportFragmentManager.findFragmentById(R.id.fragment_container)
            ?: supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    HomeFragment()
                )
                .commit()

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        requestPermissions(permissions, 1)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
//            R.id.action_gallery -> {
//                val intent = Intent()
//                intent.action = Intent.ACTION_VIEW
//                intent.type = "image/*"
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//                true
//            }
            R.id.action_star -> {
                startActivity(FavoriteActivity.newIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_history -> {
                startActivity(HistoryActivity.newIntent(this))
            }
            R.id.nav_setting -> {
                startActivity(SettingActivity.newIntent(this))
            }
            R.id.nav_about -> {
                startActivity(AboutActivity.newIntent(this))
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onResume() {
        super.onResume()
        val loggedUser = user
        if (loggedUser == null) {
            stateText.text = "离线"
            nickText.text = "Streamline"
            loginButton.visibility = View.VISIBLE
        } else {
            stateText.text = "在线"
            nickText.text = "Streamline"
            loginButton.visibility = View.GONE
        }
    }
}
