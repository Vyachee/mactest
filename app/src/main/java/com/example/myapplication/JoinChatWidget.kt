package com.example.myapplication

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */
class JoinChatWidget : AppWidgetProvider() {

    val joinVoiceButton = "android.appwidget.action.JOIN_VOICE_BUTTON"
    val timer = "android.appwidget.action.TIMER"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        Log.d("DEBUG", "update")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @SuppressLint("ShortAlarm", "UnspecifiedImmutableFlag")
    override fun onEnabled(context: Context) {

        Log.d("DEBUG", "enabled")

        Toast.makeText(context, "asdasd", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, JoinChatWidget::class.java)
        intent.action = timer
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarm: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarm.cancel(pendingIntent)
        val interval: Long = 1000 // 1 sec or 10 maybe??
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval, pendingIntent)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val action = intent?.action

        Log.d("DEBUG", "action: $action")

        when(action) {
            joinVoiceButton -> {
                val intent = Intent(context, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(context, 1, intent, 0)
                context?.startActivity(Intent(context, MainActivity::class.java))
            }

            timer -> {
                val sharedPrefs = context?.getSharedPreferences("widget_settings", MODE_PRIVATE)
                val editor = sharedPrefs?.edit()

                val currentCount = sharedPrefs?.getInt("count", 5)
                var nextCount = currentCount?.minus(1)

                if(nextCount == 0) {
                    nextCount = 5
                }

                if (nextCount != null) {
                    editor?.putInt("count", nextCount)
                }

                editor?.apply()

                val avatarIds = arrayListOf (R.id.ivp1, R.id.ivp2, R.id.ivp3, R.id.ivp4, R.id.ivp5)

                val views = RemoteViews(context?.packageName, R.layout.join_chat_widget)

                for(id in avatarIds) {
                    Log.d("DEBUG", "hiding: $nextCount")
//                    views.setInt(id, "setImageAlpha", 0)
                    views.setViewVisibility(id, View.INVISIBLE)
                }

                for(index in 0 until nextCount!!) {
                    Log.d("DEBUG", "showing: $nextCount")
//                    views.setInt(avatarIds.get(index), "setImageAlpha", 1)
                    views.setViewVisibility(avatarIds.get(index), View.VISIBLE)
                }

                AppWidgetManager.getInstance(context).updateAppWidget(
                    context?.let { ComponentName(it, JoinChatWidget::class.java) },
                    views
                )

                Log.d("DEBUG", "next count: $nextCount")
            }
        }

    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    Log.d("DEBUG", "updateAppWidget")
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.join_chat_widget)


//    val intent = Intent(context, JoinChatWidget::class.java)
//    intent.action = "android.appwidget.action.JOIN_VOICE_BUTTON"
//
//    val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val prefs = context.getSharedPreferences("widget_settings", MODE_PRIVATE)

    val intent = Intent(context, MainActivity::class.java)
    intent.putExtra("count", prefs.getInt("count", 5))

    val pendingIntent = PendingIntent.getActivity(context, 1, intent, 0)


    views.setOnClickPendingIntent(R.id.textView3, pendingIntent)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}