package com.example.loftmoney

import android.content.ClipData

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class BudgetFragment : Fragment() {
    private var mAdapter: ItemsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, null)

        val callAddButton = view.findViewById<Button>(R.id.call_add_item_activity)
        callAddButton.setOnClickListener(View.OnClickListener {
            startActivityForResult(
                Intent(activity, AddItemActivity::class.java),
                REQUEST_CODE
            )
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.budget_item_list)

        mAdapter = ItemsAdapter()
        recyclerView.setAdapter(mAdapter)

        mAdapter!!.addItem(Item("Молоко", 70))
        mAdapter!!.addItem(Item("Зубная щетка", 70))
        mAdapter!!.addItem(Item("Новый телевизор", 20000))

        return view
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        var price: Int
        try {
            price = Integer.parseInt(data!!.getStringExtra("price")!!)
        } catch (e: NumberFormatException) {
            price = 0
        }

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mAdapter!!.addItem(Item(data!!.getStringExtra("name"), price))
        }
    }

    companion object {

        private val REQUEST_CODE = 100
    }


}
