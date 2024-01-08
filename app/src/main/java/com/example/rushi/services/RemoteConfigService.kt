package com.example.rushi.services

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.example.rushi.R
import timber.log.Timber

class RemoteConfigService {

    private var mFirebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    init {
        try {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 1
            }
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
            mFirebaseRemoteConfig.fetchAndActivate()
        } catch (e: FirebaseRemoteConfigException) {
            Timber.e(e.toString())
        }
    }

    fun isUnderMaintenanceEnabled(): Boolean {
        return mFirebaseRemoteConfig.getBoolean(UNDER_MAINTENANCE_ENABLED)
    }

    fun getUnderMaintenanceTitle(): String {
        return mFirebaseRemoteConfig.getString(UNDER_MAINTENANCE_TITLE)
    }

    fun getForceUpdateDesc(): String {
        return mFirebaseRemoteConfig.getString(FORCE_UPDATE_DESC_ANDROID)
    }

    fun getForceUpdateTitle(): String {
        return mFirebaseRemoteConfig.getString(FORCE_UPDATE_TITLE_ANDROID)
    }

    fun getForceUpdateVersionAndroid(): Int {
        val versionCode =
            mFirebaseRemoteConfig.getString(FORCE_UPDATE_VERSION_ANDROID)
        return versionCode.toInt()
    }

    fun isForceUpdateEnabled(): Boolean {
        return mFirebaseRemoteConfig.getBoolean(FORCE_UPDATE_ENABLED_ANDROID)
    }

    fun getUnderMaintenanceDesc(): String {
        return mFirebaseRemoteConfig.getString(UNDER_MAINTENANCE_DESC)
    }

    companion object {
        const val FORCE_UPDATE_DESC_ANDROID = "force_update_desc_android"
        const val FORCE_UPDATE_TITLE_ANDROID = "force_update_title_android"
        const val FORCE_UPDATE_VERSION_ANDROID = "force_update_version_android"
        const val FORCE_UPDATE_ENABLED_ANDROID = "force_update_enabled_android"
        const val UNDER_MAINTENANCE_DESC = "under_maintenance_desc_android"
        const val UNDER_MAINTENANCE_TITLE = "under_maintenance_title_android"
        const val UNDER_MAINTENANCE_ENABLED = "under_maintenance_enabled_android"

        @Volatile
        private var instance: RemoteConfigService? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: RemoteConfigService().also { instance = it }
        }
    }
}
