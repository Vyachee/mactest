package com.example.myapplication

import android.app.Service
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID
import android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class GridWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        return p0?.let { GridRemoteViewsFactory(this.applicationContext, it) }!!
    }
}

class GridRemoteViewsFactory(val context: Context, val intent: Intent) : RemoteViewsService.RemoteViewsFactory {

    var appWidgetId: Int = INVALID_APPWIDGET_ID
    var data: ArrayList<Int>

    init {
        appWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID)
        data = arrayListOf(
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7,
            R.drawable.a8,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7,
            R.drawable.a8,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7,
            R.drawable.a8,
        )
//        data += data
//        data += data
    }

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        Log.d("DEBUG", "count: ${data.count()}")
        return data.count()
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.listview_item)
        remoteViews.setImageViewResource(R.id.ivPreview, data.get(p0))

        return remoteViews
    }

    override fun getLoadingView(): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.listview_item)
        return remoteView
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun hasStableIds(): Boolean {
        return true
    }

}