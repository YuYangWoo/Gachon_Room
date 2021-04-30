package com.cookandroid.gachon_study_room.ui.main.view.activity

import android.content.Intent
import androidx.annotation.Nullable
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.cookandroid.gachon_study_room.R
import com.cookandroid.gachon_study_room.databinding.ActivityMainBinding
import com.cookandroid.gachon_study_room.ui.base.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    // Fragment Attach
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    // Fragment appBar연결
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
                setOf(R.id.mainFragment)
        )
    }

    override fun init() {
        super.init()
        initToolbar()
    }

    // Toolbar Set
    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // 뒤로가기 이벤트
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}