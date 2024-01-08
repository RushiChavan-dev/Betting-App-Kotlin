package com.example.rushi.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import timber.log.Timber


fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun String.toHour(): String {
    if (!TextUtils.isEmpty(this)) {
        val dt = DateTime(this,DateTimeZone.getDefault())
        return try {
            dt.toDateTime()
            return dt.toString("HH:mm")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    return ""
}

fun String.toDetailCardDate(): String {
    if (!TextUtils.isEmpty(this)) {
        val dt = DateTime(this, DateTimeZone.getDefault())
        return try {
            dt.toString("h:mm , EEE")
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }
    return ""
}

