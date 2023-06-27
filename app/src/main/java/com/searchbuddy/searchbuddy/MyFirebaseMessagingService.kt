package com.searchbuddy.searchbuddy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.searchbuddy.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.searchbuddy.searchbuddy.Dashboard.Dashboard

const val  channelId="notification_channel"
const val  channelName="com.bumptech.searchbuddy"
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(Remotemessage: RemoteMessage) {
        if (Remotemessage.notification!=null){
            genrateNotification(Remotemessage.notification!!.title!!, Remotemessage.notification!!.body!!)
        }
    }

    fun getRemoteView(title: String,message: String) :RemoteViews{
        val remoteViews = RemoteViews("com.bumptech.searchbuddy", R.layout.notification)
        remoteViews.setTextViewText(R.id.notification_title,title)
        remoteViews.setTextViewText(R.id.message,message)
        remoteViews.setImageViewResource(R.id.notification_logo,R.drawable.sbs)

        return  remoteViews
    }
    fun genrateNotification(title:String,message:String){
        val intent=Intent(this, Dashboard::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent =PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        var builder:NotificationCompat.Builder= NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.sbs)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setContentIntent(pendingIntent)

        builder=builder.setContent(getRemoteView(title,message))

            val notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }
}