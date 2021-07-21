package com.example.newsbreeze.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.newsbreeze.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
          Intent(this,MainActivity::class.java).also {
              startActivity(it)
              this@SplashActivity.finish()
              overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
          }
        },3000)
    }
}