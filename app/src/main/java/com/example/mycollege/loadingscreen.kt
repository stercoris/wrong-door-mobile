package com.example.mycollege

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_loadingscreen.*


class loadingscreen : AppCompatActivity() {
    var mSettings: SharedPreferences? = null
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadingscreen)
        supportActionBar?.hide() //hide the title bar
        var androidid = Settings.Secure.getString(
            applicationContext?.contentResolver,
            Settings.Secure.ANDROID_ID)

        var req  = IntCom(androidid)

        //Проверка на доступ и онлайн сервера
        Thread {//код гавно
            Thread.sleep(2000)
            when {
                req.getAccess() and  req.isOnline() -> gotoMain("All OK",1000)
                req.getAccess() and !req.isOnline() -> gotoMain("TPCOL currently offline",5000)
                else -> runOnUiThread { errors.text = "leatherman club is two blocks down" }
            }
        }.start()

    }

    fun gotoMain(message: String,delay: Long)
    {
        runOnUiThread {
            errors.text = message
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, delay)
        }
    }
}
