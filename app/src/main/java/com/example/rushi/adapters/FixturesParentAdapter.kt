package com.naylalabs.scorely.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rushi.data.entities.OddModel
import com.example.rushi.data.entities.ScoreModel
import com.example.rushi.databinding.FixtureParentItemBinding

class FixturesParentAdapter constructor(private val context: Context) : RecyclerView.Adapter<FixturesParentAdapter.FixturesViewHolder>() {

    private val items: ArrayList<String> = arrayListOf()
    private var hashList: HashMap<String, ArrayList<OddModel>> = HashMap()

    private val viewPool = RecyclerView.RecycledViewPool()

    fun setItems(hashMap: HashMap<String, ArrayList<OddModel>>) {
        this.hashList.clear()
        this.hashList = hashMap
        this.items.clear()
        this.items.addAll(hashMap.keys)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixturesViewHolder {
        val itemBinding =
            FixtureParentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FixturesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FixturesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class FixturesViewHolder(private val binding: FixtureParentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(league: String?) {
            binding.leagueTv.text = league

            if (hashList[league] != null) {
                binding.childRecyclerview.visibility = View.VISIBLE
                binding.childRecyclerview.addItemDecoration(   DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                ))
                val childLayoutManager = LinearLayoutManager(
                    binding.childRecyclerview.context,
                    RecyclerView.VERTICAL,
                    false
                )
                binding.childRecyclerview.apply {
                    layoutManager = childLayoutManager

                    adapter = hashList[league]?.let {
                        FixturesChildAdapter(it,context) }
                    setRecycledViewPool(viewPool)
                }
            } else {
                binding.childRecyclerview.visibility = View.GONE
            }

        }

    }

}