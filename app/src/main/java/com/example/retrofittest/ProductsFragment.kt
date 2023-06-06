package com.example.retrofittest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofittest.adapter.ProductAdapter
import com.example.retrofittest.databinding.FragmentProductsBinding
import com.example.retrofittest.retrofit.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private lateinit var repository: Repository
    private lateinit var adapter: ProductAdapter
    private val viewModel: LoginViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "Fragment Open")
        initRetrofit()
        Log.d("TAG", "Retrofit Initial")
        initAdapter()
        Log.d("TAG", "RC Create")
        viewModel.token.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("TAG", "Token: $it")
                val list = repository.getAllProducts(it)
                requireActivity().runOnUiThread {
                    Log.d("TAG", "Token: $it")
                    adapter.submitList(list.products)
                }
            }
        }
    }

    private fun initAdapter() = with(binding) {
        adapter = ProductAdapter()
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
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

}