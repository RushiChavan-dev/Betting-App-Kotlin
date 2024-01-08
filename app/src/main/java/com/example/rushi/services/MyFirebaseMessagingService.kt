package com.example.rushi.services

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.rushi.MainApplication
import com.example.rushi.R
import com.example.rushi.data.locale.CacheHelper
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var data: Map<String, String>? = null
        if (remoteMessage.data.isNotEmpty()) {
            data = remoteMessage.data
        }
        if (data != null) {
            remoteMessage.notification?.let {
                Timber.d("Message Notification Body: %s", remoteMessage.notification?.body)
                showNotification(data, it)
            }
        }
    }

    private fun showNotification(
        data: Map<String, String>,
        notification: RemoteMessage.Notification
    ) {
        val id = (Date().time / 1000L % Int.MAX_VALUE).toInt()
        var body = data["body"]
        var title = data["title"]
        val imageUrl = data["imageUrl"]
        val channel: String =
            MainApplication.appContext.getString(R.string.default_notification_channel_id)
        if (TextUtils.isEmpty(body) && TextUtils.isEmpty(title)) {
            title = notification.title
            body = notification.body
        }

        // TODO Customize large and small icon for your app
        val largeIcon =
            BitmapFactory.decodeResource(resources, R.drawable.ic_stat_ic_notification)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channel)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentInfo(title)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setDefaults(Notification.DEFAULT_VIBRATE)
        if (!TextUtils.isEmpty(imageUrl)) {
            notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle().setBigContentTitle(title).setSummaryText(body)
            )
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(getImage(imageUrl))
                    .setSummaryText(body)
            )
        }
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channel, channel, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        notificationManager?.notify(id, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CacheHelper.getInstance(MainApplication.appContext).saveFcmToken(token)
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    private fun getImage(strURL: String?): Bitmap? {
        return try {
            val url = URL(strURL)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val TAG = "CLOUD MESSAGING"

        /**
         * Method checks if the app is in background or not
         */
        fun isAppIsInBackground(context: Context): Boolean {
            var isInBackground = true
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningProcesses = am.runningAppProcesses
            for (processInfo in runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (activeProcess in processInfo.pkgList) {
                        if (activeProcess == context.packageName) {
                            isInBackground = false
                        }
                    }
                }
            }
            return isInBackground
        }
    }
}
