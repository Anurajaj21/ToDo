package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Fade
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {

    private lateinit var left_right_anim : Animation
    private lateinit var fade_out : Animation
    private val SPLASH_TIME = 2500
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()
        val user : FirebaseUser? = auth.currentUser

        left_right_anim = AnimationUtils.loadAnimation(this, R.anim.slide_up_anim)
        fade_out = AnimationUtils.loadAnimation(this, R.anim.fade_out)

        app_name.animation = left_right_anim
        app_name.animation.duration = 1500

        app_logo.animation = fade_out
        app_logo.animation.duration = 2000

        Handler(Looper.getMainLooper()).postDelayed({
            if(user == null){
                val intent = Intent(applicationContext, Login::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            }else {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
            }
        }, SPLASH_TIME.toLong())
    }
}