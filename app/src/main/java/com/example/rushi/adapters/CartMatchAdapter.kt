package com.example.rushi.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.rushi.data.entities.SelectedBetMatch
import com.example.rushi.data.locale.MarketType
import com.example.rushi.databinding.CartMatchItemBinding
import com.example.rushi.utils.Constants
import com.example.rushi.utils.OddUtilHelper
import com.example.rushi.utils.toDetailCardDate


class CartMatchAdapter(val context: Context, private val items: ArrayList<SelectedBetMatch>
) : ListAdapter<SelectedBetMatch,CartMatchAdapter.CouponsChildViewHolder>(REPO_COMPARATOR) {


    private var oddutilHelper:OddUtilHelper = OddUtilHelper.getInstance()

    private var listener : CouponMatchItemListener? = null

    fun setListener(listener : CouponMatchItemListener){
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: ArrayList<SelectedBetMatch>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponsChildViewHolder {
        val itemBinding =
            CartMatchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CouponsChildViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: CouponsChildViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class CouponsChildViewHolder( val binding: CartMatchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SelectedBetMatch) {

            binding.apply {
                homeClubTv.text = item.homeTeam
                awayClubTv.text = item.awayTeam
                oddTv.text = item.betItem?.price.toString()
                priceTv.text = item.betItem?.name.toString()

                dateTv.text = item.commenceTime?.toDetailCardDate().toString()

                var labelTv = ""
                when (item.marketsItem?.key) {
                    MarketType.H2H -> labelTv = "Match Winner"
                    MarketType.SPREADS -> labelTv = "Handicap"
                    MarketType.TOTALS -> labelTv = "Goals Over/Under"
                    MarketType.OUTRIGHTS -> labelTv = "Outrights, Futures"
                    MarketType.H2H_LAY -> labelTv = "Head to head, Moneyline"
                    MarketType.OUTRIGHTS_LAY ->labelTv =  "Outrights, Futures"
                    else -> { // Note the block
                    }
                }
                binding.marketNameTv.text = labelTv

                Glide.with(binding.root.context)
                    .load(Constants.HOME_TEAM_LOGO)
                    .transform(CircleCrop())
                    .into(homeClubIv)

                Glide.with(binding.root.context)
                    .load(Constants.AWAY_TEAM_LOGO)
                    .transform(CircleCrop())
                    .into(awayClubIv)

                removeIv.setOnClickListener {
                    listener?.onRemoveClicked(pos = bindingAdapterPosition, match = item )

                }
            }



            itemView.setOnClickListener {
             /*   itemView.context.startActivity(
                    MatchDetailActivity.createSimpleIntent(
                        itemView.context,
                        fixture
                    )
                )*/
            }

        }
    }

    interface CouponMatchItemListener {
        fun onRemoveClicked(pos: Int, match: SelectedBetMatch)

        fun onItemClicked(pos:Int,match: SelectedBetMatch)
    }
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<SelectedBetMatch>() {
            override fun areItemsTheSame(oldItem: SelectedBetMatch, newItem: SelectedBetMatch): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SelectedBetMatch, newItem: SelectedBetMatch): Boolean =
                oldItem == newItem
        }


    }
}
