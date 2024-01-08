package com.naylalabs.scorely.ui.main.home.fixturesFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rushi.common.BaseFragment
import com.example.rushi.data.entities.OddModel
import com.example.rushi.databinding.FragmentFixturesBinding
import com.example.rushi.utils.Resource
import com.naylalabs.scorely.adapters.FixturesParentAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FixturesFragment() :
    BaseFragment() {

    private lateinit var binding: FragmentFixturesBinding
    private val viewModel: FixturesViewModel by activityViewModels()

    private lateinit var adapter: FixturesParentAdapter
    lateinit var layoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixturesBinding.inflate(inflater, container, false)


        setupObservers()
        listeners()

        return binding.root
    }

    private fun listeners() {
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = p0.toString()

                if (input.length > 2) {
                    val groupList = viewModel.fixtures.filter {
                        it.homeTeam?.contains(
                            input,
                            true
                        ) == true || it.awayTeam?.contains(input, true) == true
                    }.groupBy { it.sportTitle }
                    initRecyclerView(groupList as HashMap<String, ArrayList<OddModel>>)
                } else {
                    initRecyclerView(viewModel.fixtures.groupBy { it.sportTitle } as HashMap<String, ArrayList<OddModel>>)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }


    private fun setupObservers() {
        viewModel.fetchFixtures().observe(viewLifecycleOwner) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    val fixtureList = it.data
                    onFixtureListFetched(fixtureList)
                    hideLoading()
                }
                Resource.Status.ERROR -> {
                    onFixtureListFetched(null)
                    hideLoading()
                }
                Resource.Status.LOADING -> {
                    showLoading()
                }
            }
        }

    }

    private fun onFixtureListFetched(fixtureList: ArrayList<OddModel>?) {

        if (!fixtureList.isNullOrEmpty()) {
            viewModel.fixtures = fixtureList
            val manipulateData = manipulateData(fixtureList)
            binding.body.visibility = View.VISIBLE
            binding.emptyList.visibility = View.GONE
            val groupList = manipulateData.groupBy { it.sportTitle }
            initRecyclerView(groupList as HashMap<String, ArrayList<OddModel>>)
        } else {
            binding.body.visibility = View.GONE
            binding.emptyList.visibility = View.VISIBLE
        }

    }

    //TODO TEMPORARY SOLUTION : We need manipulate data because  we need to bet ıtem ıd and backend not giving to us?
    private fun manipulateData(fixtureList: ArrayList<OddModel>): ArrayList<OddModel> {
        for (item in fixtureList) {
            item.bookmakers?.forEach { it ->
                it?.markets?.forEach { marketItem ->
                    marketItem?.outcomes?.forEach { betItem ->
                        betItem?.id = UUID.randomUUID().toString()
                    }
                }
            }
        }
        return fixtureList
    }


    private fun initRecyclerView(hashMap: HashMap<String, ArrayList<OddModel>>) {
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.fixtureRv.layoutManager = layoutManager
        adapter = FixturesParentAdapter(requireContext())
        binding.fixtureRv.adapter = adapter
        adapter.setItems(hashMap)

        //TODO Have a problem when search , check later

       /*   if (::adapter.isInitialized) {
              adapter.setItems(hashMap)
          } else {
              adapter = FixturesParentAdapter(requireContext())
              binding.fixtureRv.adapter = adapter
              adapter.setItems(hashMap)
          }*/

    }

}
