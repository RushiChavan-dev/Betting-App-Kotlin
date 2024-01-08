package com.example.rushi.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class ScoreItem(
    val score: String?,
    val name: String?
) : Serializable
