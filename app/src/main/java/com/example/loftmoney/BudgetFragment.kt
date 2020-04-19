package com.example.loftmoney

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BudgetFragment : Fragment(), ItemsAdapterListener, ActionMode.Callback{
    companion object StaticFragment{
        val REQUEST_CODE = 100
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
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mApi: Api
    private var mActionMode:ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApi = (activity!!.application as LoftApp).api
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, null)
        val recyclerView: RecyclerView = view.findViewById(R.id.budget_item_list)
        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener {
            loadItems()
        }
        loadItems()
        mAdapter = ItemsAdapter(arguments!!.getInt(COLOR_ID))
        mAdapter.setListener(this)
        recyclerView.adapter = mAdapter
        return view
    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val price: Int
            price = try {
                data!!.getStringExtra("price").toInt()
            } catch (e: NumberFormatException) {
                0
            }
            val name = data!!.getStringExtra("name")
            val token = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(MainActivity.TOKEN, "")
            val type=arguments?.getString(TYPE)
            val call =
                mApi.addItem(AddItemRequest(name, type!!, price), token!!)
            call.enqueue(object : Callback<Status> {
                override fun onResponse(
                    call: Call<Status>, response: Response<Status>
                ) {
                    if (response.body()!!.status.equals("success")) {
                        loadItems()
                    }
                }

                override fun onFailure(call: Call<Status>, t: Throwable) {
                    Toast.makeText(context,t.localizedMessage,Toast.LENGTH_LONG).show()
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
        items.enqueue(object : Callback<List<Item?>?> {

            override fun onResponse(call: Call<List<Item?>?>, response: Response<List<Item?>?>) {
                swipeRefreshLayout.isRefreshing = false
                mAdapter.clearItems()
                val itemsBody = response.body()!!
                for (item in itemsBody) {
                    mAdapter.addItem(item!!)
                }
                (activity as MainActivity?)?.loadBalance()
            }

            override fun onFailure(call: Call<List<Item?>?>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(context, "Application has Crashed", Toast.LENGTH_LONG).show();
            }
        })
        swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }
    override fun onItemClick(item: Item?, position: Int) {
        mAdapter.clearItem(position)
        mActionMode?.title = getString(R.string.selected, java.lang.String.valueOf(mAdapter.selectedSize))
    }

    override fun onItemLongClick(item: Item?, position: Int) {
        if (mActionMode == null) {
            activity!!.startActionMode(this)
        }
        mAdapter.toggleItem(position)
        mActionMode?.title = getString(R.string.selected, java.lang.String.valueOf(mAdapter.selectedSize))
    }

    override fun onCreateActionMode(actionMode: ActionMode, menu: Menu?): Boolean {
        mActionMode = actionMode
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        val menuInflater = MenuInflater(activity)
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onActionItemClicked(
        actionMode: ActionMode?,
        menuItem: MenuItem
    ): Boolean {
        if (menuItem.itemId == R.id.remove) {
            AlertDialog.Builder(context)
                .setMessage(R.string.confirmation)
                .setPositiveButton(
                    android.R.string.yes
                ) { dialogInterface, i -> removeItems() }
                .setNegativeButton(
                    android.R.string.no
                ) { dialogInterface, i -> }.show()
        }
        return true
    }

    private fun removeItems() {
        val token = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(MainActivity.TOKEN, "")
        val selectedItems: List<Int> = mAdapter.selectedItemIds
        for (itemId in selectedItems) {
            val call = mApi.removeItem(itemId.toString(), token)
            call!!.enqueue(object : Callback<Status?> {
                override fun onResponse(
                    call: Call<Status?>, response: Response<Status?>
                ) {
                    loadItems()
                    mAdapter.clearSelections()
                }

                override fun onFailure(call: Call<Status?>, t: Throwable) {}
            })
        }
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        mActionMode = null
        mAdapter.clearSelections()
    }

}
