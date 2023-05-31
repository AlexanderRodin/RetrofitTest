package com.example.retrofittest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofittest.adapter.ProductAdapter
import com.example.retrofittest.databinding.ActivityMainBinding
import com.example.retrofittest.retrofit.AuthRequest
import com.example.retrofittest.retrofit.Repository
import com.example.retrofittest.retrofit.User
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

    private val adapter by lazy {
        ProductAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


//        TODO RcView
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter


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


        var user: User? = null

        CoroutineScope(Dispatchers.IO).launch {
            user = repository.oAuth(
                AuthRequest(
                    "atuny0",
                    "9uQFF1Lh"
                )
            )
        }


        binding.sv.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                CoroutineScope(Dispatchers.IO).launch {
                    val list = newText?.let { repository.getProductByName(user?.token ?: "", it) }
                    runOnUiThread {
                        binding.apply {
                            adapter.submitList(list?.products)
                        }
                    }
                }
                return true
            }

        })

    }
}