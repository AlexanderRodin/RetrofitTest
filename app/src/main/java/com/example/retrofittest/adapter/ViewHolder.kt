package com.example.retrofittest.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofittest.databinding.ListItemBinding
import com.example.retrofittest.retrofit.Product


class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = ListItemBinding.bind(view)

    fun bind(product: Product) = with(binding){
        listItemTitle.text = product.title
        listItemDescription.text = product.description
    }
}