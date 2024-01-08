package com.example.rushi.ui.matchDetail.matchOddsFragment


import androidx.lifecycle.MutableLiveData
import com.example.rushi.common.BaseViewModel
import com.example.rushi.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchOddsFragmentViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel() {


 /*   fun fetchFixtures(): LiveData<Resource<ArrayList<ScoreModel>>> {
        return repository.getScores("soccer_brazil_campeonato", 3)
    }*/


}