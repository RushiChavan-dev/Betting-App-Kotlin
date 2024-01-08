package com.example.rushi.data.entities

import com.google.gson.annotations.SerializedName
import java.io.Serializable

import java.util.*



 class BetItem(
    //TODO PARCELIZE OBJECT NOT SUPPORTING DEFAULT VALUES THROWING ERROR FIND A SOLUTION
    var id:String?  = UUID.randomUUID().toString(),

    @field:SerializedName("price")
    val price: Double?,

    @field:SerializedName("name")
    val name: String?
) : Serializable{

}

