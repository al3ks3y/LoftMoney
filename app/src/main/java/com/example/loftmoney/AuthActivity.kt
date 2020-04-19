package com.example.loftmoney

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {
    private lateinit var mApi: Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        mApi = (application as LoftApp).api
        val authButton = findViewById<Button>(R.id.enter_button)
        authButton.setOnClickListener {
            finish()
            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
        }
        val token =
            PreferenceManager.getDefaultSharedPreferences(this).getString(MainActivity.TOKEN, "")
        if (TextUtils.isEmpty(token)) {
            auth()
        } else {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun auth() {
        val androidId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val auth = mApi.auth(androidId)
        auth.enqueue(object : Callback<Status> {
            override fun onResponse(
                call: Call<Status>, response: Response<Status>
            ) {
                val editor = PreferenceManager.getDefaultSharedPreferences(
                    this@AuthActivity
                ).edit()
                editor.putString(MainActivity.TOKEN, response.body()?.token)
                editor.apply()
            }

            override fun onFailure(call: Call<Status>, t: Throwable) {t.printStackTrace()}
        })
    }
}