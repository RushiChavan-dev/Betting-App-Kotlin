package com.example.rushi.utils


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics

class GeneralUtils(val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: GeneralUtils? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: GeneralUtils(context).also { instance = it }
        }
    }


    /**
     * This method converts device specific pixels to density independent pixels.
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics. If you don't have
     * access to Context, just pass null.
     * @return A float value to represent dp equivalent to px value
     */
    fun convertPixelsToDp(px: Int, context: Context?): Float {
        return if (context != null) {
            val resources = context.resources
            val metrics = resources.displayMetrics
            px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        } else {
            val metrics = Resources.getSystem().displayMetrics
            px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }


    fun convertDpToPixel(dp: Int, context: Context): Int {
        val dip: Float = dp.toFloat()
        return run {
            val resources = context.resources
            val metrics = resources.displayMetrics
            (dip * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
        }
    }

}
