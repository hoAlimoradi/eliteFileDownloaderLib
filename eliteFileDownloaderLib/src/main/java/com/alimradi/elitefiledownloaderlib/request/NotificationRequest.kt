package com.alimradi.elitefiledownloaderlib.request

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alimradi.elitefiledownloaderlib.Constants
import com.alimradi.elitefiledownloaderlib.Constants.CHANNEL_ID
import com.alimradi.elitefiledownloaderlib.Constants.CHANNEL_NAME
import com.alimradi.elitefiledownloaderlib.Error
import com.alimradi.elitefiledownloaderlib.Progress
import com.alimradi.elitefiledownloaderlib.R

class NotificationRequest internal constructor(context: Context, val id: Int) :
    RequestResponse {

    private val mNotifyManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
    private var mBuilder: NotificationCompat.Builder

    companion object {
        fun build(context: Context, id: Int) = NotificationRequest(context, id)
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                setSound(null, null)
            }
            mNotifyManager.createNotificationChannel(channel)
        }
        mBuilder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setContentTitle("File Download")
            setOnlyAlertOnce(true)
            setContentText("Download in progress")
            setSmallIcon(R.drawable.download)
            setOngoing(true)
            setGroup(Constants.GROUP_KEY_DOWNLOAD)
            mNotifyManager.notify(id, this.build())
        }
    }

    override fun onCancel() {
        mBuilder.apply {
            setOngoing(false)
            setProgress(0,0,false)
            setContentText("Download canceled")
            mNotifyManager.notify(id, this.build())
        }
    }

    override fun onDownloadComplete() {
        mBuilder.apply {
            setOngoing(false)
            setProgress(0,0,false)
            setContentText("Download completed")
            mNotifyManager.notify(id, this.build())
        }
    }

    override fun onError(error: Error?) {
        mBuilder.apply {
            setOngoing(false)
            setProgress(0,0,false)
            setContentText("Error in download")
            mNotifyManager.notify(id, this.build())
        }
    }

    override fun onPause() {
        mBuilder.apply {
            setOngoing(false)
            setProgress(0,0,false)
            setContentText("Download paused")
            mNotifyManager.notify(id, this.build())
        }
    }

    override fun onProgress(progress: Progress?) {
        progress?.let {
            mBuilder.setProgress(it.totalBytes.toInt(), it.currentBytes.toInt(), false)
                ?.apply {
                    mNotifyManager.notify(id, this.build())
                }
        }
    }

    override fun onStartOrResume() {

    }
}