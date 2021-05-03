package com.alimradi.elitefiledownloaderlib.request

import android.content.Context
import com.alimradi.elitefiledownloaderlib.*
import com.alimradi.elitefiledownloaderlib.core.Core
import com.alimradi.elitefiledownloaderlib.internal.ComponentHolder
import com.alimradi.elitefiledownloaderlib.internal.DownloadRequestQueue
import com.alimradi.elitefiledownloaderlib.internal.SynchronousCall
import com.alimradi.elitefiledownloaderlib.utils.Utils
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import java.io.File
import java.util.*
import java.util.concurrent.Future


class DownloadRequest internal constructor(builder: DownloadRequestBuilder) :
    OnProgressListener, OnDownloadListener, OnStartOrResumeListener, OnPauseListener,
    OnCancelListener {
    var priority: Priority? = builder.priority
    var tag: Any? = builder.tag
    var url: String? = builder.url
    var dirPath: String? = builder.dirPath
    var fileName: String? = builder.fileName
    var notificationRequest: NotificationRequest? = null
    var sequenceNumber = 0
    var future: Future<*>? = null
    var downloadedBytes: Long = 0
    var totalBytes: Long = 0
    var readTimeout: Int
    var connectTimeout: Int
    var onProgressListener: OnProgressListener? = null
    private var onDownloadListener: OnDownloadListener? = null
    private var onStartOrResumeListener: OnStartOrResumeListener? = null
    private var onPauseListener: OnPauseListener? = null
    private var onCancelListener: OnCancelListener? = null
    var downloadId = Utils.getUniqueId(url, dirPath, fileName)
    val headers: HashMap<String, MutableList<String>>? = builder.headerMap
    val maxSyncDownload = ComponentHolder.instance.getMaxSyncDownload()
    var status: Status? = null
    var userAgent: String?
        get() {
            if (field == null) {
                userAgent = ComponentHolder.instance.getUserAgent()
            }
            return field
        }
    var transferUtility: TransferUtility? = null
    val isAWSDownload: Boolean
        get() {
            return transferUtility != null
        }

    constructor(
        builder: DownloadRequestBuilder,
        s3Client: AmazonS3Client?,
        context: Context
    ) : this(builder) {
        transferUtility = TransferUtility.builder()
            .context(context)
            .awsConfiguration(AWSMobileClient.getInstance().configuration)
            .s3Client(s3Client)
            .build()

    }

    fun startAWS(key: String, file: File): Int {
        downloadId = transferUtility?.download(
            "jsaS3/$key",
            file,
            AWSDownloadRequest.build(this)
        )?.id ?: 0
        return downloadId
    }

    fun startAWS(bucket: String, key: String, file: File): Int {
        downloadId = transferUtility?.download(
            bucket,
            "jsaS3/$key",
            file,
            AWSDownloadRequest.build(this)
        )?.id ?: 0
        return downloadId
    }

    fun showNotification(context: Context): com.alimradi.elitefiledownloaderlib.request.DownloadRequest {
        notificationRequest = NotificationRequest.build(context, downloadId)
        return this
    }

    fun setOnProgressListener(onProgressListener: OnProgressListener?): com.alimradi.elitefiledownloaderlib.request.DownloadRequest {
        this.onProgressListener = onProgressListener
        return this
    }

    fun setOnStartOrResumeListener(onStartOrResumeListener: OnStartOrResumeListener?): com.alimradi.elitefiledownloaderlib.request.DownloadRequest {
        this.onStartOrResumeListener = onStartOrResumeListener
        return this
    }

    fun setOnPauseListener(onPauseListener: OnPauseListener?): com.alimradi.elitefiledownloaderlib.request.DownloadRequest {
        this.onPauseListener = onPauseListener
        return this
    }

    fun setOnCancelListener(onCancelListener: OnCancelListener?): com.alimradi.elitefiledownloaderlib.request.DownloadRequest {
        this.onCancelListener = onCancelListener
        return this
    }

    fun start(onDownloadListener: OnDownloadListener?): Int {
        this.onDownloadListener = onDownloadListener
        DownloadRequestQueue.instance?.addRequest(this)
        return downloadId
    }

    fun executeSync(): Response {
        return SynchronousCall(this).execute()
    }

    fun deliverError(error: Error?) {
        if (status != Status.CANCELLED) {
            status = Status.FAILED
            Core.getInstance()?.executorSupplier?.forMainThreadTasks()
                ?.execute {
                    onError(error)
                    finish()
                }
        }
    }

    fun deliverSuccess() {
        if (status != Status.CANCELLED) {
            status = Status.COMPLETED
            Core.getInstance()?.executorSupplier?.forMainThreadTasks()
                ?.execute {
                    onDownloadComplete()
                    finish()
                }
        }
    }

    fun deliverStartEvent() {
        if (status != Status.CANCELLED) {
            Core.getInstance()?.executorSupplier?.forMainThreadTasks()
                ?.execute { onStartOrResume() }
        }
    }

    fun deliverPauseEvent() {
        if (status != Status.CANCELLED) {
            Core.getInstance()?.executorSupplier?.forMainThreadTasks()
                ?.execute { onPause() }
        }
    }

    private fun deliverCancelEvent() {
        Core.getInstance()?.executorSupplier?.forMainThreadTasks()
            ?.execute {
                onCancel()
            }
    }

    fun cancel() {
        status = Status.CANCELLED
        future?.cancel(true)
        deliverCancelEvent()
        Utils.deleteTempFileAndDatabaseEntryInBackground(
            Utils.getTempPath(dirPath, fileName),
            downloadId
        )
    }

    private fun finish() {
        destroy()
        DownloadRequestQueue.instance?.finish(this)
    }

    private fun destroy() {
        onProgressListener = null
        onDownloadListener = null
        onStartOrResumeListener = null
        onPauseListener = null
        onCancelListener = null
        notificationRequest = null
    }

    private val readTimeoutFromConfig: Int
        get() = ComponentHolder.instance.getReadTimeout()
    private val connectTimeoutFromConfig: Int
        get() = ComponentHolder.instance.getConnectTimeout()

    init {
        readTimeout = if (builder.readTimeout != 0) builder.readTimeout else readTimeoutFromConfig
        connectTimeout =
            if (builder.connectTimeout != 0) builder.connectTimeout else connectTimeoutFromConfig
        userAgent = builder.userAgent
    }

    override fun onProgress(progress: Progress?) {
        onProgressListener?.onProgress(progress)
        notificationRequest?.onProgress(progress)
    }

    override fun onDownloadComplete() {
        onDownloadListener?.onDownloadComplete()
        notificationRequest?.onDownloadComplete()
    }

    override fun onError(error: Error?) {
        onDownloadListener?.onError(error)
        notificationRequest?.onError(error)
    }

    override fun onStartOrResume() {
        onStartOrResumeListener?.onStartOrResume()
        notificationRequest?.onStartOrResume()
    }

    override fun onPause() {
        onPauseListener?.onPause()
        notificationRequest?.onPause()
    }

    override fun onCancel() {
        onCancelListener?.onCancel()
        notificationRequest?.onCancel()
    }
}