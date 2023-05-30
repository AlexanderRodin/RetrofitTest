package com.example.retrofittest.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.retrofittest.retrofit.Product

class Comparator: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}