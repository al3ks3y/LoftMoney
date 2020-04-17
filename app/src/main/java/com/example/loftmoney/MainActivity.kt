package com.example.loftmoney

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.loftmoney.BudgetFragment.StaticFragment.newInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXPENSE = "expense"
        const val INCOME = "income"
        const val TOKEN = "token"
    }
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
        val floatingActionButton = findViewById<FloatingActionButton>(R.id.fab)
        floatingActionButton.setOnClickListener {
            val activeFragmentIndex = viewPager.currentItem
            val activeFragment =
                supportFragmentManager.fragments[activeFragmentIndex]
            activeFragment.startActivityForResult(
                Intent(this@MainActivity, AddItemActivity::class.java),
                BudgetFragment.REQUEST_CODE
            )
            overridePendingTransition(R.anim.from_rigth_in, R.anim.from_left_out)
        }
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)!!.setText(R.string.expences)
        tabLayout.getTabAt(1)!!.setText(R.string.income)
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BudgetFragment) {
                fragment.loadItems()
            }
        }
    }

    internal class BudgetPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> newInstance(R.color.fire_brick, EXPENSE)
                else -> newInstance(R.color.apple_green, INCOME)
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
