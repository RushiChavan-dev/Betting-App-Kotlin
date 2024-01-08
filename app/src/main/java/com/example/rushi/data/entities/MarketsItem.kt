package com.example.rushi.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MarketsItem(

    @field:SerializedName("outcomes")
    val outcomes: List<BetItem?>?,

    @field:SerializedName("key")
    val key: String?
) : Serializable

