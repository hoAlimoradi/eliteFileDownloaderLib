package com.alimradi.elitefiledownloaderlib.request

import com.alimradi.elitefiledownloaderlib.Error
import com.alimradi.elitefiledownloaderlib.Progress
import com.alimradi.elitefiledownloaderlib.Status
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState

class AWSDownloadRequest constructor(private val request: DownloadRequest) : TransferListener {

    override fun onStateChanged(id: Int, state: TransferState?) {
        state?.let {
            when (it) {
                TransferState.PAUSED -> {
                    request.status = Status.PAUSED
                    request.onPause()
                }
                TransferState.CANCELED -> {
                    request.status = Status.CANCELLED
                    request.onCancel()
                }
                TransferState.COMPLETED -> {
                    request.status = Status.CANCELLED
                    request.onDownloadComplete()
                }
                else -> {
                }
            }
        }
    }

    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
        request.onProgress(Progress(bytesCurrent, bytesTotal))
        request.status = Status.RUNNING
    }

    override fun onError(id: Int, ex: Exception?) {
        request.onError(Error(connectionException = ex))
        request.status = Status.FAILED
    }

    companion object {
        fun build(request: DownloadRequest) = AWSDownloadRequest(request)
    }
}