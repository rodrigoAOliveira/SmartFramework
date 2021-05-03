package com.arcthos.arcthosmart.shared.tabadapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.arcthos.arcthosmart.shared.tabadapter.SmartTabAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList

class SmartTabAdapterHandler {

    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()

    fun setupTabAdapter(
        activity: AppCompatActivity,
        tabLayout: TabLayout,
        viewPager: ViewPager2
    ){

        val tabAdapter = SmartTabAdapter(activity, fragments)
        viewPager.adapter = tabAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    fun addTab(fragment: Fragment, title: String){
        fragments.add(fragment)
        titles.add(title)
    }
}