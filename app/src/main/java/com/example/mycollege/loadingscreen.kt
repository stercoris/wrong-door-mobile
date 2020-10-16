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

        var request = IntCom(androidid)


        Thread {
            Thread.sleep(2000)
            if(request.getAccess())
            {
                runOnUiThread(java.lang.Runnable {
                    Handler().postDelayed({
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        startActivity(Intent(this,MainActivity::class.java))
                        // close this activity
                        finish()
                    },0)
                })
            }
            else{
                runOnUiThread(java.lang.Runnable {
                    errors.text = "leatherman club is two blocks down"
                })
            }
        }.start()
    }
}
