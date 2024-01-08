package com.example.rushi.ui.matchDetail

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.rushi.R
import com.example.rushi.adapters.pagerAdapters.MatchDetailPagerAdapter
import com.example.rushi.common.BaseActivity
import com.example.rushi.data.entities.OddModel
import com.example.rushi.databinding.ActivityMatchDetailBinding
import com.example.rushi.utils.Constants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class MatchDetailActivity : BaseActivity() {

    lateinit var binding: ActivityMatchDetailBinding
    private val viewModel: MatchDetailActivityViewModel by viewModels()
    private lateinit var pagerAdapter: MatchDetailPagerAdapter
    private lateinit var scoreModel: OddModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoreModel = intent.getSerializableExtra(MATCH_DETAIL_DATA) as OddModel
        initViewPager()
        adjustUI()
        listeners()
    }

    private fun listeners() {
        binding.apply {
            backIv.setOnClickListener { finish() }

            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    ResourcesCompat.getFont(this@MatchDetailActivity, R.font.roboto_700)
                        ?.let { setTabTypeface(tab, it) }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    ResourcesCompat.getFont(this@MatchDetailActivity, R.font.roboto_400)
                        ?.let { setTabTypeface(tab, it) }
                }

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

    }


    private fun initViewPager() {
        pagerAdapter = MatchDetailPagerAdapter(this)
        pagerAdapter.setScoreModel(scoreModel)
        binding.viewPager.apply {
            isUserInputEnabled = false
            offscreenPageLimit = 2
            adapter = pagerAdapter
        }
        binding.tabLayout.setSelectedTabIndicatorColor(resources.getColor(R.color.white))
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0) {
                tab.text = (getString(R.string.odds))
                ResourcesCompat.getFont(this@MatchDetailActivity, R.font.roboto_700)
                    ?.let { setTabTypeface(tab, it) }
            } else {
                tab.text = getString(R.string.Details)
                ResourcesCompat.getFont(this@MatchDetailActivity, R.font.roboto_400)
                    ?.let { setTabTypeface(tab, it) }
            }
        }.attach()
    }

    private fun adjustUI() {
        binding.apply {

            homeClubTv.text = scoreModel.homeTeam
            awayClubTv.text = scoreModel.awayTeam

          /*  if (!scoreModel.scores.isNullOrEmpty()) {
                homeScoreTv.text = scoreModel.scores!![0].score
                awayScoreTv.text = scoreModel.scores!![1].score
            }

            //need check other statuses
            if (scoreModel.completed == true) {
                matchStatusTv.text = "Finished"
            } else {
                matchStatusTv.text = "Not Started"
            }*/

            Glide.with(binding.root.context)
                .load(Constants.HOME_TEAM_LOGO)
                .transform(CircleCrop())
                .into(homeClubIv)

            Glide.with(binding.root.context)
                .load(Constants.AWAY_TEAM_LOGO)
                .transform(CircleCrop())
                .into(awayClubIv)

        }


    }


    private fun setTabTypeface(tab: TabLayout.Tab, typeface: Typeface) {
        for (i in 0 until tab.view.childCount) {
            val tabViewChild = tab.view.getChildAt(i)
            if (tabViewChild is TextView) tabViewChild.typeface = typeface
        }
    }


    companion object {
        var MATCH_DETAIL_DATA = "MATCH_DETAIL"
        fun createSimpleIntent(context: Context?, item: OddModel): Intent {
            return Intent(context, MatchDetailActivity::class.java).putExtra(
                MATCH_DETAIL_DATA,
                item
            )
        }
    }
}
