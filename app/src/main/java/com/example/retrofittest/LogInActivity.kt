package com.example.retrofittest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.retrofittest.databinding.ContentLogInBinding

class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ContentLogInBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}