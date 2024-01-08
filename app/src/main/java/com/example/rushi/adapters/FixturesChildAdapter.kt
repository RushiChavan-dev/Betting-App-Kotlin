package com.naylalabs.scorely.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.rushi.data.entities.OddModel
import com.example.rushi.data.entities.ScoreModel
import com.example.rushi.databinding.FixtureChildItemBinding
import com.example.rushi.ui.matchDetail.MatchDetailActivity
import com.example.rushi.utils.Constants
import com.example.rushi.utils.GeneralUtils
import com.example.rushi.utils.toHour

class FixturesChildAdapter(private val fixtures: List<OddModel>, private val context: Context) :
    RecyclerView.Adapter<FixturesChildAdapter.FixturesChildViewHolder>() {


    //private lateinit var oddUtilHelper: OddUtilHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixturesChildViewHolder {
       // oddUtilHelper = OddUtilHelper.getInstance()
        val fixtureBinding =
            FixtureChildItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FixturesChildViewHolder(fixtureBinding)
    }

    override fun onBindViewHolder(holder: FixturesChildViewHolder, position: Int) {
        holder.bind(fixtures[position])
    }

    override fun getItemCount(): Int = fixtures.size

    inner class FixturesChildViewHolder(val binding: FixtureChildItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OddModel) {
            binding.homeClubTv.text = item.homeTeam
            binding.awayClubTv.text = item.awayTeam


            Glide.with(binding.root.context)
                .load(Constants.HOME_TEAM_LOGO)
                .transform(CircleCrop())
                .into(binding.homeClubIv)

            Glide.with(binding.root.context)
                .load(Constants.AWAY_TEAM_LOGO)
                .transform(CircleCrop())
                .into(binding.awayClubIv)

            binding.root.setOnClickListener {
               context.startActivity(MatchDetailActivity.createSimpleIntent(context,item))


            }
        }

    }


}