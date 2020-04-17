package com.example.loftmoney

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.example.loftmoney.BudgetFragment.StaticFragment.newInstance
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var mApi: Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        val adapter = BudgetPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)!!.setText(R.string.expences)
        tabLayout.getTabAt(1)!!.setText(R.string.income)
        mApi = (application as LoftApp).api
        val token =
            PreferenceManager.getDefaultSharedPreferences(this).getString(TOKEN, "")
        if (TextUtils.isEmpty(token)) {
            val auth = mApi.auth(USER_ID)
            auth.enqueue(object : Callback<Status> {
                override fun onResponse(
                    call: Call<Status>, response: Response<Status>
                ) {
                    val editor = PreferenceManager.getDefaultSharedPreferences(
                        this@MainActivity
                    ).edit()
                    editor.putString(TOKEN, response.body()?.token)
                    editor.apply()
                    for (fragment in supportFragmentManager.fragments) {
                        if (fragment is BudgetFragment) {
                            fragment.loadItems()
                        }
                    }
                }

                override fun onFailure(call: Call<Status>, t: Throwable) {
                }
            })
        }
    }

    internal class BudgetPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> newInstance(R.color.dark_sky_blue, EXPENSE)
                else -> newInstance(R.color.apple_green, INCOME)
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }

    companion object {
        const val EXPENSE = "expense"
        const val INCOME = "income"
        private const val USER_ID = "zfadeev"
        const val TOKEN = "token"
    }
}
