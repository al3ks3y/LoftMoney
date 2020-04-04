package com.example.loftmoney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.tabs) as TabLayout
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        viewPager.adapter =
            BudgetPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)!!.setText(R.string.expences)
        tabLayout.getTabAt(1)!!.setText(R.string.income)
    }
    internal class BudgetPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

        override fun getItem(position: Int): Fragment {
            return BudgetFragment()
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
