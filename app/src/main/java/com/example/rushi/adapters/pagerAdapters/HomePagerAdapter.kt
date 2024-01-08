package com.example.rushi.adapters.pagerAdapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rushi.ui.home.cartFragment.CartFragment
import com.naylalabs.scorely.ui.main.home.fixturesFragment.FixturesFragment
import com.naylalabs.scorely.ui.main.home.moreFragment.AuthFragment

class HomePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FixturesFragment()
            1 -> CartFragment()
            else -> AuthFragment()
        }
    }
}
