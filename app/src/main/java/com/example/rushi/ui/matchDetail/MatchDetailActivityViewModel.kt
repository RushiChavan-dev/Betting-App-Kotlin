package com.example.rushi.ui.matchDetail

import com.example.rushi.common.BaseViewModel
import com.example.rushi.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class MatchDetailActivityViewModel @Inject constructor(repository: Repository) :
    BaseViewModel() {
}