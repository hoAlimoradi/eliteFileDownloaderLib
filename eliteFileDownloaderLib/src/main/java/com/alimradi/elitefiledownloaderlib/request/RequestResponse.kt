package com.alimradi.elitefiledownloaderlib.request

import com.alimradi.elitefiledownloaderlib.Error
import com.alimradi.elitefiledownloaderlib.Progress

interface RequestResponse {
    fun onCancel()
    fun onDownloadComplete()
    fun onError(error: Error?)
    fun onPause()
    fun onProgress(progress: Progress?)
    fun onStartOrResume()
}