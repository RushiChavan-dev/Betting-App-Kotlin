package com.example.rushi.ui.splash

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.activity.viewModels
import com.example.rushi.BuildConfig
import com.example.rushi.common.BaseActivity
import com.example.rushi.databinding.ActivitySplashBinding
import com.example.rushi.services.RemoteConfigService
import com.example.rushi.ui.home.HomeActivity
import com.example.rushi.utils.PopupHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private val viewModel: SplashActivityViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var remoteConfigService: RemoteConfigService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (remoteConfigService.isUnderMaintenanceEnabled()) {
            showUnderMaintenance()
            return
        }
        if (controlVersion()) {
            showForceUpdateDialog()
            return
        }
        startActivity(HomeActivity.createSimpleIntent(this))
        finish()
    }

    private fun controlVersion(): Boolean {
        return remoteConfigService
            .isForceUpdateEnabled() && BuildConfig.VERSION_CODE < RemoteConfigService.getInstance()
            .getForceUpdateVersionAndroid()
    }

    private fun showUnderMaintenance() {
        PopupHelper.getInstance()
            .showUnderMaintenanceDialog(
                this,
                object : PopupHelper.SingleButtonClickListener {
                    override fun onPositiveClicked(dialog: Dialog?) {
                        dialog?.dismiss()
                        finish()
                    }
                }
            )
    }

    private fun showForceUpdateDialog() {
        PopupHelper.getInstance()
            .showForceUpdateDialog(
                this,
                object : PopupHelper.TwoButtonClickListener {
                    override fun onPositiveClicked(dialog: Dialog?) {
                        dialog?.dismiss()
                    }

                    override fun onNegativeClicked(dialog: Dialog?) {
                        dialog?.dismiss()
                    }
                }
            )
    }
}
