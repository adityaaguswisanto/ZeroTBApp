package com.zerotb.zerotb.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zerotb.zerotb.R
import com.zerotb.zerotb.data.helper.gone
import com.zerotb.zerotb.data.helper.launchActivity
import com.zerotb.zerotb.data.helper.visible
import com.zerotb.zerotb.data.store.UserStore
import com.zerotb.zerotb.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    @Inject
    lateinit var userStore: UserStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navigationHost.navController

        userStore.userRole.asLiveData().observe(this, {
            if (it == "1") {

                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.admin)

                val navAdmin = navController.navInflater.inflate(R.navigation.nav_admin)
                navigationHost.navController.graph = navAdmin

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.adminFragment -> bottomNavigationView.visible()
                        R.id.consultFragment -> bottomNavigationView.visible()
                        R.id.reportFragment -> bottomNavigationView.visible()
                        R.id.settingsFragment -> bottomNavigationView.visible()
                        else -> bottomNavigationView.gone()
                    }
                }

                bottomNavigationView.setupWithNavController(navController)

            } else {

                bottomNavigationView.menu.clear()
                bottomNavigationView.inflateMenu(R.menu.user)

                val navUser = navController.navInflater.inflate(R.navigation.nav_user)
                navigationHost.navController.graph = navUser

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.userFragment -> bottomNavigationView.visible()
                        R.id.settingsFragment -> bottomNavigationView.visible()
                        else -> bottomNavigationView.gone()
                    }
                }

                bottomNavigationView.setupWithNavController(navController)
            }
        })

    }

    fun performLogout() = lifecycleScope.launch {
        userStore.clear()
        launchActivity(AuthActivity::class.java)
    }
}