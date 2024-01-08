package com.example.rushi.ui.matchDetail.matchDetailsFragment



import com.example.rushi.common.BaseViewModel
import com.example.rushi.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MatchDetailsFragmentViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel() {

   /* fun fetchFixtures(): LiveData<Resource<ArrayList<ScoreModel>>> {
      //  return repository.getScores("soccer_brazil_campeonato", 3)
    }*/


}