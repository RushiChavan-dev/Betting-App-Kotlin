package com.example.rushi.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class ScoreModel(

	@field:SerializedName("scores")
	val scores: List<ScoreItem>?,

	@field:SerializedName("last_update")
	val lastUpdate: String?,

	@field:SerializedName("sport_key")
	val sportKey: String?,

	@field:SerializedName("id")
	val id: String?,

	@field:SerializedName("completed")
	val completed: Boolean?,

	@field:SerializedName("home_team")
	val homeTeam: String?,

	@field:SerializedName("sport_title")
	val sportTitle: String?,

	@field:SerializedName("commence_time")
	val commenceTime: String?,

	@field:SerializedName("away_team")
	val awayTeam: String?
) : Serializable

