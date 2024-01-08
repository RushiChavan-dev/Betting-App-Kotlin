package com.example.rushi.data.remote

import com.example.rushi.data.entities.CharacterList
import com.example.rushi.data.entities.Character
import com.example.rushi.data.entities.OddModel
import com.example.rushi.data.entities.ScoreModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("character")
    suspend fun getAllCharacters(): Response<CharacterList>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<Character>

/*
    @GET("sports/{sport}/scores")
    suspend fun getScores(
        @Path("sport") sport: String,
        @Query("daysFrom") daysFrom: Int,
        @Query("apiKey") apiKey: String,
    ): Response<ArrayList<ScoreModel>>
*/

    @GET("sports/upcoming/odds")
    suspend fun getOdds(
        @Query("regions") regions: String,
        @Query("markets") markets: String,
        @Query("apiKey") apiKey: String,
    ): Response<ArrayList<OddModel>>


}