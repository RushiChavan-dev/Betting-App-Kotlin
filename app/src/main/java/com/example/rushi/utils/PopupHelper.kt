package com.example.rushi.utils

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import com.example.rushi.R

class PopupHelper() {
    private var dialog: Dialog? = null

    fun showForceUpdateDialog(activity: Activity, action: TwoButtonClickListener) {
        if (dialog != null && dialog!!.isShowing) {
            return
        }
        dialog = Dialog(activity, R.style.MyAlertDialogStyle)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_force_update)
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        dialog?.show()

        dialog!!.findViewById<View>(R.id.cancel_button).setOnClickListener {
            action.onNegativeClicked(
                dialog
            )
        }
        dialog!!.findViewById<View>(R.id.confirm_button).setOnClickListener {
            action.onPositiveClicked(
                dialog
            )
        }
    }

    fun showUnderMaintenanceDialog(activity: Activity, action: SingleButtonClickListener) {
        if (dialog != null && dialog!!.isShowing) {
            return
        }
        dialog = Dialog(activity, R.style.MyAlertDialogStyle)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_under_maintenance)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        dialog?.show()

        dialog!!.findViewById<View>(R.id.ok_button).setOnClickListener {
            action.onPositiveClicked(
                dialog
            )
        }
    }

    companion object {
        @Volatile
        private var instance: PopupHelper? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: PopupHelper().also { instance = it }
        }
    }

    interface SingleButtonClickListener {
        fun onPositiveClicked(dialog: Dialog?)
    }

    interface TwoButtonClickListener {
        fun onPositiveClicked(dialog: Dialog?)
        fun onNegativeClicked(dialog: Dialog?)
    }
}
