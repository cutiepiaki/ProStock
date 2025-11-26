package ru.dekart.prostock

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit().clear().apply()

        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            val login = findViewById<EditText>(R.id.login_field).text.toString().trim()
            if (login.isBlank()) {
                findViewById<EditText>(R.id.login_field).error = "Введите логин"
                return@setOnClickListener
            }

            val password = findViewById<EditText>(R.id.password_field).text.toString()
            if (password.isBlank()) {
                findViewById<EditText>(R.id.password_field).error = "Введите пароль"
                return@setOnClickListener
            }

            val service = Retrofit.Builder()
                .baseUrl("https://procom.dekart.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(WmsService::class.java)
            service.signIn(SignInRequest(login, password))
                .enqueue(object : Callback<SignInResponse> {
                    override fun onResponse(
                        call: Call<SignInResponse?>,
                        response: Response<SignInResponse?>
                    ) {
                        if (response.body()!!.success) {
                            prefs.edit()
                                .putString("token", response.body()!!.token)
                                .apply()

                            startActivity(
                                Intent(this@SignInActivity, MainActivity::class.java)
                                    .putExtra("fullName", response.body()?.fullName)
                            )
                        } else {
                            Toast.makeText(
                                this@SignInActivity,
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<SignInResponse?>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            this@SignInActivity,
                            "Ошибка подключения к серверу",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}