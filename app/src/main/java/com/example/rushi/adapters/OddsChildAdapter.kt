package com.naylalabs.scorely.adapters

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rushi.R
import com.example.rushi.data.entities.MarketsItem
import com.example.rushi.data.entities.BetItem
import com.example.rushi.databinding.ItemMatchOddChildBinding
import com.example.rushi.utils.OddUtilHelper


class OddsChildAdapter(
    private val market: MarketsItem,
    private var itemListener: OddsChildAdapter.OddItemListener
) :
    RecyclerView.Adapter<OddsChildAdapter.MyViewHolder>() {

    private var oddUtilHelper: OddUtilHelper = OddUtilHelper.getInstance()

    private var lastSelectedItem: Int = -1

    interface OddItemListener {
        fun onOddItemSelected(oddModel: BetItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemBinding =
            ItemMatchOddChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val values = market.outcomes

        values?.get(position)?.let { holder.bind(it, itemListener) }
    }

    override fun getItemCount(): Int = market.outcomes?.size!!

    inner class MyViewHolder(val binding: ItemMatchOddChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemListener: OddItemListener? = null

        fun bind(betItem: BetItem, itemListener: OddItemListener) {
            this.itemListener = itemListener

            binding.apply {
                parentTv.text = betItem.name
                oddTv.text = betItem.price.toString()

                //TODO FIX  INITIAL VALUE PROBLEM ON PARCELIZE CLASSES
                if (betItem.id.let { oddUtilHelper.isSameBet(betId = it.orEmpty()) } || lastSelectedItem == adapterPosition) {
                    binding.oddsContainer.setCardBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.color_secondary
                        )
                    )
                } else {
                    binding.oddsContainer.setCardBackgroundColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.odd_background_gray
                        )
                    )
                }


                root.setOnClickListener {
                 /*   val copyLastCheck = lastSelectedItem
                    lastSelectedItem = adapterPosition
                    notifyItemChanged(copyLastCheck)
                    notifyItemChanged(lastSelectedItem)*/
                    itemListener.onOddItemSelected(betItem)

                }
            }


        }


    }

}