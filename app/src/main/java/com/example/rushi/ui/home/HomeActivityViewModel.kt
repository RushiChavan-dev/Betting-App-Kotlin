package com.example.rushi.ui.home

import com.example.rushi.common.BaseViewModel
import com.example.rushi.data.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



@HiltViewModel
class HomeActivityViewModel @Inject constructor(repository: Repository) :
    BaseViewModel() {
}