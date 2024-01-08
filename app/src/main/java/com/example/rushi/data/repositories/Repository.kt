package com.example.rushi.data.repositories

import com.example.rushi.BuildConfig
import com.example.rushi.common.BaseDataSource
import com.example.rushi.data.remote.ApiService
import com.example.rushi.utils.performOperation
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiService: ApiService
) : BaseDataSource() {
    fun getCharacters() = performOperation { getResult { apiService.getAllCharacters() } }

    fun getCharacterDetail(id: Int) = performOperation { getResult { apiService.getCharacter(id) } }


/*
    fun getScores(sport: String, daysFrom: Int) =
        performOperation {
            getResult {
                apiService.getScores(
                    sport,
                    daysFrom,
                    BuildConfig.API_KEY
                )
            }
        }
*/



    fun getOdds( regions: String, markets : String) =
        performOperation {
            getResult {
                apiService.getOdds(
                    regions,
                    markets,
                    BuildConfig.API_KEY
                )
            }
        }


}