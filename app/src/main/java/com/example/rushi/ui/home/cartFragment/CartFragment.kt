package com.example.rushi.ui.home.cartFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rushi.adapters.CartMatchAdapter
import com.example.rushi.common.BaseFragment
import com.example.rushi.data.entities.SelectedBetMatch
import com.example.rushi.databinding.FragmentCartBinding
import com.example.rushi.ui.home.HomeActivity
import com.example.rushi.utils.AnalyticsHelper
import com.example.rushi.utils.OddUtilHelper
import com.example.rushi.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment() :
    BaseFragment() {

    @Inject
    lateinit var oddUtilHelper: OddUtilHelper

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartFragmentViewModel by activityViewModels()

    private lateinit var adapter: CartMatchAdapter

    private var initialPrice: Int = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        setupObservers()
        listeners()
        drawUI(arrayListOf())
        return binding.root
    }

    private fun listeners() {
        binding.emptyBody.setOnClickListener {
            (activity as HomeActivity).changeSelectedTab(0)
        }

        binding.priceEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val char = p0.toString()
                if (char.isNotEmpty()) {
                    initialPrice = Integer.parseInt(p0.toString())
                    updateCartInfoBox()
                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }


    private fun setupObservers() {
        oddUtilHelper.selectedBetMatchOdds.observe(viewLifecycleOwner) {
            drawUI(it)
        }
    }


    private fun drawUI(list:ArrayList<SelectedBetMatch>) {
        if(list.isNullOrEmpty()){
            binding.body.visibility = View.GONE
            binding.emptyBody.visibility = View.VISIBLE
        }else{
            binding.body.visibility = View.VISIBLE
            binding.emptyBody.visibility = View.GONE
            initRecyclerView(betMatchList = list)
            updateCartInfoBox()
        }


    }
    private fun updateCartInfoBox(){
        binding.couponPriceTv.text = "$initialPrice TL"
        val df = DecimalFormat("#.##")
        binding.maxPriceTv.text = "${df.format(oddUtilHelper.getMaxPrice(initialPrice.toDouble()))} TL"
        binding.oddTv.text = df.format(oddUtilHelper.getMaxOdd())
    }


    private fun initRecyclerView(betMatchList: ArrayList<SelectedBetMatch>) {


        if (::adapter.isInitialized) {
            adapter.setItems(betMatchList)
        } else {
            val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            binding.matchRv.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            binding.matchRv.layoutManager = layoutManager
            adapter = CartMatchAdapter(requireContext(), betMatchList)
            adapter.setListener(object : CartMatchAdapter.CouponMatchItemListener {
                override fun onRemoveClicked(pos: Int, selectedBetMatch: SelectedBetMatch) {
                    val status = selectedBetMatch.id?.let { oddUtilHelper.removeSelectedBet(it) }
                    if (status == true){
                        adapter.notifyItemRemoved(pos)
                        logToFirebase(modelBet = selectedBetMatch)
                        context?.toast("Removed item")
                    }

                }

                override fun onItemClicked(pos: Int, match: SelectedBetMatch) {
                }

            })
            binding.matchRv.adapter = adapter
        }

    }
    private fun logToFirebase(modelBet: SelectedBetMatch) {
        val map = HashMap<String, Any?>()
        modelBet.apply {
            map["id"] = id.toString()
            map["betId"] = betItem?.id
            map["marketId"] = marketsItem?.key
            map["awayTeam"] = awayTeam.toString()
            map["homeTeam"] = homeTeam.toString()
        }

        analyticsHelper.track(AnalyticsHelper.REMOVE_FROM_CARD_EVENT, map)
    }

}
