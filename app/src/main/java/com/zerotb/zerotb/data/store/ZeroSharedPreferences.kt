package com.zerotb.zerotb.data.store

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class ZeroSharedPreferences constructor(
    context: Context
) {

    private val editor: SharedPreferences.Editor

    fun setTimeDaily(time: String?) {
        editor.putString(KEY_DAILY, time)
        Log.i("Time", "$time")
        editor.commit()
    }

    fun setDailyMessage(message: String?) {
        editor.putString(KEY_MESSAGE_DAILY, message)
    }

    companion object {
        private const val PREFERENCE = "reminderPreferences"
        private const val KEY_DAILY = "DailyReminder"
        private const val KEY_MESSAGE_DAILY = "messageDaily"
    }

    init {
        val sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

}