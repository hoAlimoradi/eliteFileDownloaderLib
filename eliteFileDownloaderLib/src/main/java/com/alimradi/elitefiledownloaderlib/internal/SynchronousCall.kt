package com.alimradi.elitefiledownloaderlib.internal

import com.alimradi.elitefiledownloaderlib.Response
import com.alimradi.elitefiledownloaderlib.request.DownloadRequest

class SynchronousCall(val request: DownloadRequest) {
    fun execute(): Response {
        val downloadTask: DownloadTask = DownloadTask.create(request)
        return downloadTask.run()
    }
}