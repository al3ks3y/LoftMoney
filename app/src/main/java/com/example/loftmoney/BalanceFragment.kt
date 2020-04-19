package com.example.loftmoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BalanceFragment : Fragment() {
    private lateinit var mApi: Api
    private var myExpences: TextView? = null
    private var myIncome: TextView? = null
    private var totalFinances: TextView? = null
    private lateinit var mDiagramView: DiagramView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mApi = (activity!!.application as LoftApp).api
        loadBalance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_balance, null)
        myExpences = view.findViewById(R.id.my_expences)
        myIncome = view.findViewById(R.id.my_income)
        totalFinances = view.findViewById(R.id.total_finances)
        mDiagramView = view.findViewById(R.id.diagram_view)
        mSwipeRefreshLayout = view.findViewById(R.id.balance_swipe_refresh_layout)
        mSwipeRefreshLayout.setOnRefreshListener { loadBalance() }
        return view
    }

    override fun onResume() {
        super.onResume()
        loadBalance()
    }

    fun loadBalance() {
        val token = PreferenceManager.getDefaultSharedPreferences(activity)
            .getString(MainActivity.TOKEN, "")
        val responceCall: Call<BalanceResponce> = mApi.getBalance(token)
        responceCall.enqueue(object : Callback<BalanceResponce> {
            override fun onResponse(
                call: Call<BalanceResponce>, response: Response<BalanceResponce>
            ) {
                val totalExpences: Float = response.body()!!.totalExpences
                val totalIncome: Float = response.body()!!.totalIncome
                myExpences!!.text = totalExpences.toString()
                myIncome!!.text = totalIncome.toString()
                totalFinances!!.text = (totalIncome - totalExpences).toString()
                mDiagramView.update(totalExpences, totalIncome)
                mSwipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<BalanceResponce>, t: Throwable) {
                mSwipeRefreshLayout.isRefreshing = false
            }
        })
    }

    companion object {
        fun newInstance(): BalanceFragment {
            return BalanceFragment()
        }
    }
}