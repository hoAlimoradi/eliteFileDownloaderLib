
package com.alimradi.elitefiledownloaderlib.handler

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.alimradi.elitefiledownloaderlib.Constants
import com.alimradi.elitefiledownloaderlib.OnProgressListener
import com.alimradi.elitefiledownloaderlib.Progress

class ProgressHandler(private val listener: OnProgressListener?) : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            Constants.UPDATE -> if (listener != null) {
                val progress = msg.obj as Progress
                listener.onProgress(progress)
            }
            else -> super.handleMessage(msg)
        }
    }
}