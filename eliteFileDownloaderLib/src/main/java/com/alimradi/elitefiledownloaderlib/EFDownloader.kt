package com.alimradi.elitefiledownloaderlib

import android.content.Context
import com.alimradi.elitefiledownloaderlib.core.Core
import com.alimradi.elitefiledownloaderlib.internal.ComponentHolder
import com.alimradi.elitefiledownloaderlib.internal.DownloadRequestQueue
import com.alimradi.elitefiledownloaderlib.request.DownloadRequestBuilder
import com.alimradi.elitefiledownloaderlib.utils.Utils
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.services.s3.AmazonS3Client

/**
 * EFDownloader entry point.
 * You must initialize this class before use. The simplest way is to just do
 * {#code EFDownloader.initialize(context)}.
 */
object EFDownloader {

    fun initialize(
        context: Context?,
        config: EFDownloaderConfig = EFDownloaderConfig.newBuilder().build(),
        enableAWS: Boolean = false
    ) {
        ComponentHolder.instance.init(context, config)
        DownloadRequestQueue.initialize()
        if (enableAWS) AWSMobileClient.getInstance().initialize(context).execute()
    }

    /**
     * Method to make download request
     *
     * @param url      The url on which request is to be made
     * @param dirPath  The directory path on which file is to be saved
     * @param fileName The file name with which file is to be saved
     * @return the DownloadRequestBuilder
     */
    fun download(url: String, dirPath: String, fileName: String): DownloadRequestBuilder {
        return DownloadRequestBuilder(url, dirPath, fileName)
    }

    /**
     * Method to make download AWS request
     *
     * @param accessKey  The url on which request is to be made
     * @param secretKey  The directory path on which file is to be saved
     * @param context The file name with which file is to be saved
     * @return the DownloadRequestBuilder
     */
    fun prepareDownloadAWS(
        accessKey: String,
        secretKey: String,
        context: Context
    ): DownloadRequestBuilder {
        val credentials = BasicAWSCredentials(accessKey, secretKey)
        return DownloadRequestBuilder(AmazonS3Client(credentials), context)
    }

    /**
     * Method to pause request with the given downloadId
     *
     * @param downloadId The downloadId with which request is to be paused
     */
    fun pause(downloadId: Int) {
        DownloadRequestQueue.instance?.pause(downloadId)
    }

    /**
     * Method to resume request with the given downloadId
     *
     * @param downloadId The downloadId with which request is to be resumed
     */
    fun resume(downloadId: Int) {
        DownloadRequestQueue.instance?.resume(downloadId)
    }

    /**
     * Method to cancel request with the given downloadId
     *
     * @param downloadId The downloadId with which request is to be cancelled
     */
    fun cancel(downloadId: Int) {
        DownloadRequestQueue.instance?.cancel(downloadId)
    }

    /**
     * Method to cancel requests with the given tag
     *
     * @param tag The tag with which requests are to be cancelled
     */
    fun cancel(tag: Any) {
        DownloadRequestQueue.instance?.cancel(tag)
    }

    /**
     * Method to cancel all requests
     */
    fun cancelAll() {
        DownloadRequestQueue.instance?.cancelAll()
    }

    /**
     * Method to check the request with the given downloadId is running or not
     *
     * @param downloadId The downloadId with which request status is to be checked
     * @return the running status
     */
    fun getStatus(downloadId: Int): Status? {
        return DownloadRequestQueue.instance?.getStatus(downloadId)
    }

    /**
     * Method to clean up temporary resumed files which is older than the given day
     *
     * @param days the days
     */
    fun cleanUp(days: Int) {
        Utils.deleteUnwantedModelsAndTempFiles(days)
    }

    /**
     * Shuts PRDownloader down
     */
    fun shutDown() {
        Core.shutDown()
    }
}