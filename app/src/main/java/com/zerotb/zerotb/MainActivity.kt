package com.zerotb.zerotb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.asLiveData
import com.zerotb.zerotb.data.helper.launchActivity
import com.zerotb.zerotb.data.store.UserStore
import com.zerotb.zerotb.ui.auth.AuthActivity
import com.zerotb.zerotb.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userStore: UserStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler(Looper.getMainLooper()).postDelayed({
            userStore.authToken.asLiveData().observe(this, {
                val activity =
                    if (it == null) AuthActivity::class.java else HomeActivity::class.java
                launchActivity(activity)
            })
        }, 2000)
    }
}