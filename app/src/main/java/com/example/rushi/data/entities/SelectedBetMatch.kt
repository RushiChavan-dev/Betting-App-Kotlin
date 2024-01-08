package com.example.rushi.data.entities


data class SelectedBetMatch(

    var betItem : BetItem?,

    val sportKey: String?,

    val id: String?,

    val homeTeam: String?,

    val sportTitle: String?,

    val commenceTime: String?,

    val awayTeam: String?,

    var marketsItem:MarketsItem?
)

