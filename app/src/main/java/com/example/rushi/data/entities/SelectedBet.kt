package com.example.rushi.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class SelectedBet(

    val id: String?,

    val price: Double?,

    val name: String?
) : Serializable {

}

