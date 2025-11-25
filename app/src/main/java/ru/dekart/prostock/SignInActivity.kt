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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body

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
        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            val login = findViewById<EditText>(R.id.login_field).text.toString()
            val password = findViewById<EditText>(R.id.password_field).text.toString().toInt()
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
                        println(response.body())
                        if (response.body()?.success == true) {
                            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
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
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}