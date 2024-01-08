package com.example.rushi.data.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable


data class OddModel(

	@field:SerializedName("bookmakers")
	val bookmakers: List<BookmakersItem?>? ,

	@field:SerializedName("sport_key")
	val sportKey: String?,

	@field:SerializedName("id")
	val id: String? ,

	@field:SerializedName("home_team")
	val homeTeam: String? ,

	@field:SerializedName("sport_title")
	val sportTitle: String?,

	@field:SerializedName("commence_time")
	val commenceTime: String? ,

	@field:SerializedName("away_team")
	val awayTeam: String?
) : Serializable

