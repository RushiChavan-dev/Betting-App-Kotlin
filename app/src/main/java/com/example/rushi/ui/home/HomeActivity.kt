package com.example.rushi.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.rushi.adapters.pagerAdapters.HomePagerAdapter
import com.example.rushi.common.BaseActivity
import com.example.rushi.databinding.ActivityHomeBinding
import com.example.rushi.utils.views.NavigationBar

class HomeActivity : BaseActivity(), NavigationBar.NavigationBarListener {

    private lateinit var pagerAdapter: HomePagerAdapter
    private lateinit var viewPager: ViewPager2
    lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adjustUI()
    }

    private fun adjustUI() {
        viewPager = binding.viewPager

        pagerAdapter = HomePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 4
        viewPager.isUserInputEnabled = false
        binding.navigation.setListener(this)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                try {
                    binding.navigation.selectItem(position)
                } catch (e: Exception) {
                }
            }
        })
    }

    override fun onNavigationClicked(pos: Int) {
        binding.viewPager.currentItem = pos
    }

    fun changeSelectedTab(pos : Int){
        viewPager.currentItem = pos
    }

    companion object {
        fun createSimpleIntent(context: Context?): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }
}
