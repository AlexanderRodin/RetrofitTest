package com.example.retrofittest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.retrofittest.databinding.ActivityMainBinding
import com.example.retrofittest.retrofit.AuthRequest
import com.example.retrofittest.retrofit.Repository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val repository = retrofit.create(Repository::class.java)

        binding.btnIn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = repository.oAuth(
                    AuthRequest(
                        binding.userName.text.toString(),
                        binding.password.text.toString()
                    )
                )
                runOnUiThread {
                    binding.apply {
                        Picasso.get().load(user.image).into(iv)
                        firstName.text = user.firstName
                        lastName.text = user.lastName
                    }
                }
            }
        }

    }
}