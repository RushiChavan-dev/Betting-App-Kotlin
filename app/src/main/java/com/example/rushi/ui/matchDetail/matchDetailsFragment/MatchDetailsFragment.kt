package com.example.rushi.ui.matchDetail.matchDetailsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rushi.common.BaseFragment
import com.example.rushi.data.entities.OddModel
import com.example.rushi.databinding.FragmentMatchDetailBinding
import com.example.rushi.utils.AnalyticsHelper
import com.naylalabs.scorely.adapters.FixturesParentAdapter

class MatchDetailsFragment(val matchDetail: OddModel?) :
    BaseFragment() {

    private lateinit var binding: FragmentMatchDetailBinding
    private val viewModel: MatchDetailsFragmentViewModel by activityViewModels()

    private lateinit var adapter: FixturesParentAdapter
    lateinit var layoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchDetailBinding.inflate(inflater, container, false)

        adjustUI()
        logToFirebase()
        return binding.root
    }

    private fun logToFirebase() {
        val map = HashMap<String, Any?>()
        matchDetail?.apply {
            map["id"] = id.toString()
            map["awayTeam"] = awayTeam.toString()
            map["homeTeam"] = homeTeam.toString()
        }

        analyticsHelper.track(AnalyticsHelper.MATCH_DETAIL_CLICKED, map)
    }

    private fun adjustUI() {
        binding.apply {
            calendarTv.text = matchDetail?.commenceTime
        }


    }


}
