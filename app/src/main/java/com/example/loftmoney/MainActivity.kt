package com.example.loftmoney

import android.content.Intent
import android.os.Bundle
import android.view.ActionMode
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.loftmoney.BudgetFragment.StaticFragment.newInstance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    companion object {
        const val EXPENSE = "expense"
        const val INCOME = "income"
        const val TOKEN = "token"
    }
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar
    private lateinit var floatingActionButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       tabLayout = findViewById(R.id.tabs)
        toolbar=findViewById(R.id.toolbar)
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        val adapter = BudgetPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    floatingActionButton.hide()
                } else {
                    floatingActionButton.show()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        floatingActionButton = findViewById(R.id.fab)
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
        tabLayout.getTabAt(2)!!.setText(R.string.balance)
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BudgetFragment) {
                fragment.loadItems()
            }
        }
    }

    fun loadBalance() {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BalanceFragment) {
                fragment.loadBalance()
            }
        }
    }

    override fun onActionModeStarted(mode: ActionMode?) {
        super.onActionModeStarted(mode)
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue))
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue))
        floatingActionButton.hide()
    }

    override fun onActionModeFinished(mode: ActionMode?) {
        super.onActionModeFinished(mode)
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        floatingActionButton.show()
    }

    internal class BudgetPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> newInstance(R.color.dark_sky_blue, EXPENSE)
                1 -> newInstance(R.color.apple_green, INCOME)
                else -> BalanceFragment.newInstance()
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
