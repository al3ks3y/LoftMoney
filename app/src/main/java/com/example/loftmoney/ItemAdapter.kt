package com.example.loftmoney

import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.lang.String
import java.util.*

class ItemsAdapter(private val colorId: Int) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {
    private val mItemsList: MutableList<Item> = ArrayList()
    private var mListener: ItemsAdapterListener? = null
    private val mSelectedItems = SparseBooleanArray()
    fun clearSelections() {
        mSelectedItems.clear()
        notifyDataSetChanged()
    }

    fun toggleItem(position: Int) {
        mSelectedItems.put(position, !mSelectedItems[position])
        notifyDataSetChanged()
    }

    fun clearItem(position: Int) {
        mSelectedItems.put(position, false)
        notifyDataSetChanged()
    }

    val selectedSize: Int
        get() {
            var result = 0
            for (i in mItemsList.indices) {
                if (mSelectedItems[i]) {
                    result++
                }
            }
            return result
        }

    val selectedItemIds: List<Int>
        get() {
            val result: MutableList<Int> = ArrayList()
            var i = 0
            for (item in mItemsList) {
                if (mSelectedItems[i]) {
                    result.add(item.id)
                }
                i++
            }
            return result
        }

    fun setListener(listener: ItemsAdapterListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = View.inflate(parent.context, R.layout.item_view, null)
        return ItemViewHolder(itemView, colorId)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(mItemsList[position], mSelectedItems[position])
        holder.setListener(mListener, mItemsList[position], position)
    }

    fun addItem(item: Item) {
        mItemsList.add(item)
        notifyDataSetChanged()
    }

    fun clearItems() {
        mItemsList.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

    class ItemViewHolder(private val mItemView: View, colorId: Int) :
        RecyclerView.ViewHolder(mItemView) {
        private val mNameView: TextView
        private val mPriceView: TextView
        fun bindItem(item: Item, isSelected: Boolean) {
            mItemView.isSelected = isSelected
            mNameView.setText(item.name)
            mPriceView.text = mPriceView.context.resources
                .getString(R.string.price_with_currency, String.valueOf(item.price))
        }

        fun setListener(listener: ItemsAdapterListener?, item: Item?, position: Int) {
            mItemView.setOnClickListener { listener!!.onItemClick(item, position) }
            mItemView.setOnLongClickListener {
                listener!!.onItemLongClick(item, position)
                false
            }
        }

        init {
            mNameView = mItemView.findViewById(R.id.name_view)
            mPriceView = mItemView.findViewById(R.id.price_view)
            val context = mPriceView.context
            mPriceView.setTextColor(ContextCompat.getColor(context, colorId))
        }
    }

}