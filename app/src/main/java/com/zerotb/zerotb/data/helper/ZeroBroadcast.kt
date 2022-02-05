package com.zerotb.zerotb.data.helper

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.zerotb.zerotb.MainActivity
import com.zerotb.zerotb.R
import java.util.*

class ZeroBroadcast : BroadcastReceiver() {

    private var NOTIF_ID = 1

    override fun onReceive(context: Context, p1: Intent?) {
        showDailyNotif(
            context,
            "Hai, udah waktunya minum obat nih",
            "Masuk ke aplikasi yuk buat lihat obat yang harus diminum",
            NOTIF_ID
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showDailyNotif(context: Context, title: String, message: String, id: Int) {
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, message)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setColorized(true)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    "channel_01",
                    "AlarmManager channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            channel.enableLights(true)
            builder.setChannelId("channel_01")
            notificationManagerCompat.createNotificationChannel(channel)
        }
        notificationManagerCompat.notify(id, builder.build())
    }

    fun setAlarm(context: Context, type: String?, time: String, message: String?) {
        cancelNotif(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ZeroBroadcast::class.java)
        intent.putExtra("message", message)
        intent.putExtra("type", type)
        val timeArray = time.split(":").toTypedArray()
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = timeArray[0].toInt()
        calendar[Calendar.MINUTE] = timeArray[1].toInt()
        calendar[Calendar.SECOND] = 0
        val pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelNotif(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ZeroBroadcast::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

}