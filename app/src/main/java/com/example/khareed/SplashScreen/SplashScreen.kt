package com.example.khareed.SplashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.khareed.Authentication.LoginActivity
import com.example.khareed.R

class SplashScreen : AppCompatActivity() {
    private lateinit var logo : ImageView
    private lateinit var name : TextView
    private lateinit var slogan : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        logo = findViewById(R.id.app_logo)
        name = findViewById(R.id.app_name_tv)
        slogan = findViewById(R.id.app_slogan_tv)

        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        // Set animation to elements
        logo.startAnimation(topAnim)
        name.startAnimation(bottomAnim)
        slogan.startAnimation(bottomAnim)

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}