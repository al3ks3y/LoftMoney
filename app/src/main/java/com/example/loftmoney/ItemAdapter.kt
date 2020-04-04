package com.example.loftmoney

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    private val mItemsList = ArrayList<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = View.inflate(parent.context, R.layout.item_view, null)

        return ItemViewHolder(itemView)
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

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mNameView: TextView
        private val mPriceView: TextView

        init {

            mNameView = itemView.findViewById(R.id.name_view)
            mPriceView = itemView.findViewById(R.id.price_view)
        }

        fun bindItem(item: Item) {
            mNameView.setText(item.name)
            mPriceView.text =
                mPriceView.context.resources.getString(R.string.price_with_currency, item.price.toString() )
        }
    }
}