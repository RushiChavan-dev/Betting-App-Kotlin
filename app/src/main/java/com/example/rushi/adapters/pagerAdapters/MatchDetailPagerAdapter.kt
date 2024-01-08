package com.example.rushi.adapters.pagerAdapters
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.rushi.data.entities.OddModel
import com.example.rushi.data.entities.ScoreModel
import com.example.rushi.ui.matchDetail.matchDetailsFragment.MatchDetailsFragment
import com.example.rushi.ui.matchDetail.matchOddsFragment.MatchOddsFragment


class MatchDetailPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {


    private var matchDetail : OddModel? = null

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MatchOddsFragment(matchDetail)
            else -> MatchDetailsFragment(matchDetail)
        }
    }

    fun setScoreModel(matchDetail : OddModel?){
        this.matchDetail = matchDetail
    }
}
