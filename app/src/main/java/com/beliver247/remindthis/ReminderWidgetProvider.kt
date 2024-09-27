package com.beliver247.remindthis

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class ReminderWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val widgetText = getRemindersText(context)
            val views = RemoteViews(context.packageName, R.layout.widget_reminders)

            // Set reminder text in widget
            views.setTextViewText(R.id.widget_reminder_1, widgetText[0])
            views.setTextViewText(R.id.widget_reminder_2, widgetText[1])
            views.setTextViewText(R.id.widget_reminder_3, widgetText[2])

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun formatTime(timeInMillis: Long): String {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            return String.format("%02d:%02d", hour, minute)
        }

        private fun getRemindersText(context: Context): List<String> {
            // Retrieve reminders from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("reminders", Context.MODE_PRIVATE)
            val gson = Gson()
            val json = sharedPreferences.getString("reminders", "[]")
            val type = object : TypeToken<MutableList<Reminder>>() {}.type
            val reminders: List<Reminder> = gson.fromJson(json, type)

            // Get the top 3 upcoming reminders sorted by time
            val upcomingReminders = reminders.sortedBy { it.time }.take(3)

            // Map reminders to title and formatted time, fill empty strings for fewer than 3 reminders
            return upcomingReminders.map { reminder ->
                "${reminder.title} at ${formatTime(reminder.time)}"
            } + List(3 - upcomingReminders.size) { "" }
        }
    }
}
