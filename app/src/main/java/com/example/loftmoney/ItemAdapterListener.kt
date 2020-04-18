package com.example.loftmoney

interface ItemsAdapterListener {
    fun onItemClick(item: Item?, position: Int)
    fun onItemLongClick(item: Item?, position: Int)
}