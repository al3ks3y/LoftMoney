package com.example.loftmoney

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BudgetFragment : Fragment() {
    companion object StaticFragment{
        private val REQUEST_CODE = 100
        val COLOR_ID = "colorId"
        val TYPE = "fragmentType"
        fun newInstance(colorId: Int, type: String?): BudgetFragment {
            val budgetFragment = BudgetFragment()
            val bundle = Bundle()
            bundle.putInt(COLOR_ID, colorId)
            bundle.putString(TYPE, type)
            budgetFragment.arguments = bundle
            return budgetFragment
        }
    }
    private lateinit var mAdapter: ItemsAdapter

    private lateinit var mApi: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApi = (activity!!.application as LoftApp).api
        loadItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, null)
        val callAddButton = view.findViewById<Button>(R.id.call_add_item_activity)
        callAddButton.setOnClickListener {
            startActivityForResult(
                Intent(activity, AddItemActivity::class.java),
                REQUEST_CODE
            )
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.budget_item_list)
        mAdapter = ItemsAdapter(arguments!!.getInt(COLOR_ID))
        recyclerView.adapter = mAdapter
        return view
    }
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val price: Int
        price = try {
            data!!.getStringExtra("price").toInt()
        } catch (e: NumberFormatException) {
            0
        }
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val name = data!!.getStringExtra("name")
            val token =
                PreferenceManager.getDefaultSharedPreferences(context).getString(MainActivity.TOKEN, "")
            val call: Call<Status> =
                mApi.addItem(AddItemRequest(name, arguments!!.getString(TYPE)!!, price), token.toString())
            call.enqueue(object : Callback<Status> {
                override fun onResponse(
                    call: Call<Status>, response: Response<Status>
                ) {
                    if (response.body()?.status.equals("success")) {
                        mAdapter!!.addItem(Item(name, price))
                    }
                }

                override fun onFailure(call: Call<Status>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    fun loadItems() {
        val token =
            PreferenceManager.getDefaultSharedPreferences(context).getString(MainActivity.TOKEN, "")
        val items =
            mApi.getItems(arguments?.getString(TYPE).toString(), token.toString())
        items!!.enqueue(object : Callback<List<Item?>?> {

            override fun onResponse(call: Call<List<Item?>?>, response: Response<List<Item?>?>) {
                val itemsBody = response.body()!!
                for (item in itemsBody) {
                    mAdapter.addItem(item!!)
                }
            }

            override fun onFailure(call: Call<List<Item?>?>, t: Throwable) {
                Toast.makeText(context, "Application has Crashed", Toast.LENGTH_LONG).show();
            }
        })
    }






}
