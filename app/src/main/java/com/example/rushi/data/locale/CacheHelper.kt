package com.example.rushi.data.locale

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class CacheHelper(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, PREFERENCES_MODE)
    private var editor: SharedPreferences.Editor = preferences.edit()
    private var gson: Gson

    init {
        editor.apply()
        gson = Gson()
    }

    fun <T> getObject(key: String?, a: Class<T>?): T? {
        val data = preferences.getString(key, null)
        return if (data == null) {
            null
        } else {
            try {
                gson.fromJson(data, a)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun removeObject(key: String?) {
        editor.remove(key)
        editor.commit()
    }

    fun commit() {
        editor.commit()
    }

    fun saveFcmToken(token: String) {
        editor.putString(FCM_TOKEN, token)
        editor.commit()
    }

    fun getFcmToken(): String? {
        return preferences.getString(FCM_TOKEN, "")
    }

    companion object {
        @Volatile
        private var instance: CacheHelper? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: CacheHelper(context).also { instance = it }
        }

        private const val PREFERENCES_NAME: String =
            com.example.rushi.BuildConfig.APPLICATION_ID
        private const val PREFERENCES_MODE = Activity.MODE_PRIVATE

        // keys
        const val USER = "user"
        const val ANON_USER = "anonUser"
        private const val FCM_TOKEN = "fcmToken"
    }
}
