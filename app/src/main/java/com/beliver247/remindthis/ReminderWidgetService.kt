package com.beliver247.remindthis

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class ReminderWidgetService {
    companion object {
        fun updateWidget(context: Context) {
            val widgetManager = AppWidgetManager.getInstance(context)
            val widgetComponent = ComponentName(context, ReminderWidgetProvider::class.java)
            val widgetIds = widgetManager.getAppWidgetIds(widgetComponent)
            val intent = Intent(context, ReminderWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
            }
            context.sendBroadcast(intent)
        }
    }
}
