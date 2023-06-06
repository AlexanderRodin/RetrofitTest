package com.example.retrofittest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.retrofittest.databinding.FragmentLoginBinding
import com.example.retrofittest.retrofit.AuthRequest
import com.example.retrofittest.retrofit.Repository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        binding.apply {
            nextButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
            }
            signIn.setOnClickListener {
                auth(
                    AuthRequest(
                        etLogin.text.toString(),
                        etPassword.text.toString()
                    )
                )
            }
        }

    }

    private fun initRetrofit() {
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

        repository = retrofit.create(Repository::class.java)
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.oAuth(authRequest)
            val message = response.errorBody()?.string()?.let {
                JSONObject(it).getString("message")
            }
            requireActivity().runOnUiThread {
                binding.tvError.text = message
                val user = response.body()
                if (user != null) {
                    Picasso.get().load(user.image).into(binding.iv)
                    binding.textView.text = user.firstName
                    binding.nextButton.visibility = View.VISIBLE

                    viewModel.token.value = user.token
                    Log.d("TAG", "Token: ${viewModel.token.value.toString()}")
                }
            }
        }
    }
}