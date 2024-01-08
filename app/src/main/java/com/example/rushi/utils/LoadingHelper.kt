package com.example.rushi.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.example.rushi.R


class LoadingHelper : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        isCancelable = false

        return  inflater.inflate(R.layout.progress_dialog, container, false)
    }

    //over rid  this due to some issues that occur when trying to show a the dialog after onSaveInstanceState
    override fun show(manager: FragmentManager, tag: String?) {
        try {

            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
            manager.executePendingTransactions()
        } catch (ignored: IllegalStateException) {

        }
    }



}

/*@SuppressLint("InflateParams")
class LoadingHelper(private var context: Context) {
    private var dialog: Dialog = Dialog(context, R.style.MyAlertDialogStyle)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(LayoutInflater.from(context).inflate(R.layout.progress_dialog, null))
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
    }

    fun showDialog() {
        try {
            if (!dialog.isShowing) {
                dialog.show()
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun hideDialog() {
        try {
            if (context is Activity) {
                if (!(context as Activity).isFinishing) {
                    dialog.dismiss()
                }
            } else {
                dialog.dismiss()
            }
        } catch (e: java.lang.Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }


}*/
