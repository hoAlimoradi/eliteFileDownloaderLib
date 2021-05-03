package com.alimradi.elitefiledownloaderlib.request

import android.content.Context
import com.alimradi.elitefiledownloaderlib.Priority
import com.amazonaws.services.s3.AmazonS3Client
import java.util.*

class DownloadRequestBuilder(
    val url: String? = null,
    val dirPath: String? = null,
    val fileName: String? = null
) : RequestBuilder {
    lateinit var context: Context
    var priority = Priority.MEDIUM
    var tag: Any? = null
    var readTimeout = 0
    var connectTimeout = 0
    var userAgent: String? = null
    var headerMap: HashMap<String, MutableList<String>>? = null
    var s3Client: AmazonS3Client? = null

    constructor(s3Client: AmazonS3Client, context: Context) : this() {
        this.s3Client = s3Client
        this.context = context
    }

    override fun setHeader(name: String, value: String): DownloadRequestBuilder {
        if (headerMap == null) {
            headerMap = HashMap()
        }
        var list = headerMap!![name]
        if (list == null) {
            list = ArrayList()
            headerMap!![name] = list
        }
        if (!list.contains(value)) {
            list.add(value)
        }
        return this
    }

    override fun setPriority(priority: Priority): DownloadRequestBuilder {
        this.priority = priority
        return this
    }

    override fun setTag(tag: Any?): DownloadRequestBuilder {
        this.tag = tag
        return this
    }

    override fun setReadTimeout(readTimeout: Int): DownloadRequestBuilder {
        this.readTimeout = readTimeout
        return this
    }

    override fun setConnectTimeout(connectTimeout: Int): DownloadRequestBuilder {
        this.connectTimeout = connectTimeout
        return this
    }

    override fun setUserAgent(userAgent: String?): DownloadRequestBuilder {
        this.userAgent = userAgent
        return this
    }

    fun build(): DownloadRequest {
        return DownloadRequest(this)
    }

    fun buildAWS(): DownloadRequest {
        return DownloadRequest(this, s3Client, context)
    }
}