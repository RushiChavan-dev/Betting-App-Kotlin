package com.example.rushi.utils

import androidx.lifecycle.MutableLiveData
import com.example.rushi.data.entities.BetItem
import com.example.rushi.data.entities.MarketsItem
import com.example.rushi.data.entities.SelectedBetMatch
import timber.log.Timber


class OddUtilHelper {

    val selectedBetMatchOdds: MutableLiveData<ArrayList<SelectedBetMatch>> = MutableLiveData()


    fun addSelectedBet(betMatch: SelectedBetMatch) {
        val list = arrayListOf<SelectedBetMatch>()
        if (!selectedBetMatchOdds.value.isNullOrEmpty()) {
            list.addAll(selectedBetMatchOdds.value!!)
        }
        list.add(betMatch)
        selectedBetMatchOdds.postValue(list)
    }

    fun updateSelectedBet(id:String,betItem: BetItem,marketsItem: MarketsItem) {
        try {
            val list = selectedBetMatchOdds.value
            if (!list.isNullOrEmpty()) {
                for (item in list) {
                    if (item.id == id) {
                        item.marketsItem = marketsItem
                        item.betItem = betItem
                        selectedBetMatchOdds.postValue(list!!)
                        break
                    }
                }

            }

        } catch (e: Exception) {

        }
    }

    fun removeSelectedBet(id: String): Boolean {
        try {
            val list = selectedBetMatchOdds.value
            if (!list.isNullOrEmpty()) {
                for (item in list) {
                    if (item.id == id) {
                        list.remove(item)
                        selectedBetMatchOdds.postValue(list!!)
                        return true
                    }
                }

            }

        } catch (e: Exception) {
            return false
        }
        return false
    }


    fun isHaveSelectedMatch(id: String): Boolean {
        val list = selectedBetMatchOdds.value
        return if (list.isNullOrEmpty()) {
            false
        } else {
            val findOdd = list.find { it.id == id }
            Timber.d("isHaveSelectedOdd : $findOdd")
            findOdd != null
        }
    }

    fun isSameBet(betId: String): Boolean {
        val list = selectedBetMatchOdds.value
        return if (list.isNullOrEmpty()) {
            false
        } else {
            val findOdd = list.find { it.betItem?.id == betId }
            Timber.d("isSameBet : $findOdd")
            findOdd != null
        }
    }

    fun getMaxOdd(): Double {
        var sum = 1.0
        val list = selectedBetMatchOdds.value
        return if (list.isNullOrEmpty()) {
            sum
        } else {
            list.forEach { sum *= it.betItem?.price!! }
            sum
        }
    }

    fun getMaxPrice(price: Double): Double {
        val maxOdd = getMaxOdd()
        return maxOdd * price
    }

    companion object {
        @Volatile
        private var instance: OddUtilHelper? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: OddUtilHelper().also { instance = it }
        }
    }


}
