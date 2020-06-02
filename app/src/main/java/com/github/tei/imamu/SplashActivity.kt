package com.github.tei.imamu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity()
{
    private val handler = Handler()

    private val runnable = Runnable {
        if (!isFinishing)
        {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    override fun onResume()
    {
        super.onResume()
        handler.postDelayed(runnable, 200)
    }

    override fun onPause()
    {
        super.onPause()
        handler.removeCallbacks(runnable)
    }
}