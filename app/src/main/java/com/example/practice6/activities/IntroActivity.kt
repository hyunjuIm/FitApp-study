package com.example.practice6.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.practice6.utils.transparentStatusAndNavigation
import android.os.Bundle
import android.os.Handler
import com.example.practice6.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        this.transparentStatusAndNavigation()

        var handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 1000)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}