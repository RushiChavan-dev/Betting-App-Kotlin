package com.example.rushi.ui.matchDetail.matchOddsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rushi.common.BaseFragment
import com.example.rushi.data.entities.MarketsItem
import com.example.rushi.data.entities.OddModel
import com.example.rushi.data.entities.BetItem
import com.example.rushi.data.entities.SelectedBetMatch
import com.example.rushi.databinding.FragmentMatchOddsBinding
import com.example.rushi.utils.AnalyticsHelper
import com.example.rushi.utils.OddUtilHelper
import com.example.rushi.utils.toast
import com.naylalabs.scorely.adapters.OddParentListener
import com.naylalabs.scorely.adapters.OddsParentAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MatchOddsFragment(val oddModel: OddModel?) :
    BaseFragment() {

    @Inject
    lateinit var oddUtilHelper: OddUtilHelper

    private lateinit var binding: FragmentMatchOddsBinding
    private val viewModel: MatchOddsFragmentViewModel by activityViewModels()

    private lateinit var adapter: OddsParentAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchOddsBinding.inflate(inflater, container, false)

        initOddsRecyclerView()
        return binding.root
    }

    private fun initOddsRecyclerView() {
        binding.oddsRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        if (!oddModel?.bookmakers.isNullOrEmpty()) {
            adapter = OddsParentAdapter(requireContext(),
                oddModel?.bookmakers?.get(0)?.markets as ArrayList<MarketsItem>,
                object : OddParentListener {
                    override fun onOddItemSelected(betItem: BetItem, marketsItem: MarketsItem) {

                        //If already match added to cart, check add / remove
                        if (oddUtilHelper.isHaveSelectedMatch(oddModel.id!!)) {
                            // If selected odd clicked remove else add
                            if (oddUtilHelper.isSameBet(betId = betItem.id!!)) {
                                oddUtilHelper.removeSelectedBet(oddModel.id)
                                requireContext().toast("Removed selection")
                            } else {
                                requireContext().toast("Update selection")
                                oddUtilHelper.updateSelectedBet(oddModel.id,betItem = betItem, marketsItem = marketsItem)
                            }

                        } else {
                            addSelectedOddModel(betItem = betItem, marketsItem = marketsItem)
                        }

                        adapter.notifyDataSetChanged()
                    }

                })
            binding.oddsRv.adapter = adapter

        } else {
            //show error widget
        }
    }

    private fun getSelectedBetMatch(betItem: BetItem, marketsItem: MarketsItem):SelectedBetMatch{
        return SelectedBetMatch(
            betItem = betItem,
            sportKey = this@MatchOddsFragment.oddModel?.sportKey,
            id = this@MatchOddsFragment.oddModel?.id,
            homeTeam = this@MatchOddsFragment.oddModel?.homeTeam,
            sportTitle = this@MatchOddsFragment.oddModel?.sportTitle,
            commenceTime = this@MatchOddsFragment.oddModel?.commenceTime,
            awayTeam = this@MatchOddsFragment.oddModel?.awayTeam,
            marketsItem = marketsItem
        )
    }
    private fun addSelectedOddModel(betItem: BetItem, marketsItem: MarketsItem) {
        val selectedBet = getSelectedBetMatch(betItem,marketsItem)
        oddUtilHelper.addSelectedBet(
            selectedBet
        )
        requireContext().toast("Added to cart")
        logToFirebase(betItem, marketsItem)
    }

    private fun logToFirebase(betItem: BetItem, marketsItem: MarketsItem) {
        val map = HashMap<String, Any?>()
        oddModel?.apply {
            map["id"] = id.toString()
            map["betId"] = betItem.id.toString()
            map["marketId"] = marketsItem.key
            map["awayTeam"] = awayTeam.toString()
            map["homeTeam"] = homeTeam.toString()
        }

        analyticsHelper.track(AnalyticsHelper.ADD_TO_CART_EVENT, map)
    }


}
