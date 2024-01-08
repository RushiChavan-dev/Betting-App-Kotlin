package com.example.rushi.ui.home.cartFragment


import androidx.lifecycle.LiveData
import com.example.rushi.common.BaseViewModel
import com.example.rushi.data.entities.OddModel
import com.example.rushi.data.repositories.Repository
import com.example.rushi.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartFragmentViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel() {


    fun fetchFixtures(): LiveData<Resource<ArrayList<OddModel>>> {
        return repository.getOdds("eu","h2h,spreads,totals")
    }


}