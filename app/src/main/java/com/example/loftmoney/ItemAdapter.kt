package com.example.loftmoney

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.lang.String
import java.util.*

class ItemsAdapter(private var colorId: Int) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    private val mItemsList: MutableList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = View.inflate(parent.context, R.layout.item_view, null)
        return ItemViewHolder(itemView, colorId)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(mItemsList[position])
    }

    fun addItem(item: Item) {
        mItemsList.add(item)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

    class ItemViewHolder(itemView: View, colorId: Int) :
        RecyclerView.ViewHolder(itemView) {
        private val mNameView: TextView
        private val mPriceView: TextView
        fun bindItem(item: Item) {
            mNameView.setText(item.name)
            mPriceView.text = mPriceView.context.resources
                .getString(R.string.price_with_currency, String.valueOf(item.price))
        }

        init {
            mNameView = itemView.findViewById(R.id.name_view)
            mPriceView = itemView.findViewById(R.id.price_view)
            val context = mPriceView.context
            mPriceView.setTextColor(ContextCompat.getColor(context, colorId))
        }
    }
}