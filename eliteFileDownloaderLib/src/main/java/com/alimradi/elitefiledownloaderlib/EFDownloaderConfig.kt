package com.alimradi.elitefiledownloaderlib

import com.alimradi.elitefiledownloaderlib.httpclient.DefaultHttpClient
import com.alimradi.elitefiledownloaderlib.httpclient.HttpClient


class EFDownloaderConfig private constructor(builder: Builder) {
    var readTimeout: Int
    var connectTimeout: Int
    var userAgent: String?
    var httpClient: HttpClient
    var isDatabaseEnabled: Boolean
    var maxSyncDownload :Int

    class Builder {
        var readTimeout = Constants.DEFAULT_READ_TIMEOUT_IN_MILLS
        var connectTimeout = Constants.DEFAULT_CONNECT_TIMEOUT_IN_MILLS
        var userAgent: String? = Constants.DEFAULT_USER_AGENT
        var httpClient: HttpClient = DefaultHttpClient()
        var databaseEnabled = false
        var maxSyncDownload = 0
        fun setReadTimeout(readTimeout: Int): Builder {
            this.readTimeout = readTimeout
            return this
        }

        fun setConnectTimeout(connectTimeout: Int): Builder {
            this.connectTimeout = connectTimeout
            return this
        }

        fun setUserAgent(userAgent: String?): Builder {
            this.userAgent = userAgent
            return this
        }

        fun setHttpClient(httpClient: HttpClient): Builder {
            this.httpClient = httpClient
            return this
        }

        fun setDatabaseEnabled(databaseEnabled: Boolean): Builder {
            this.databaseEnabled = databaseEnabled
            return this
        }

        fun setMaxSyncDownload(maxSyncDownload: Int): Builder {
            if (maxSyncDownload < 1)
                throw IllegalArgumentException("max Sync Download can not be less than 1")
            this.maxSyncDownload = maxSyncDownload
            return this
        }

        fun build(): EFDownloaderConfig {
            return EFDownloaderConfig(this)
        }
    }

    companion object {
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    init {
        readTimeout = builder.readTimeout
        connectTimeout = builder.connectTimeout
        userAgent = builder.userAgent
        httpClient = builder.httpClient
        isDatabaseEnabled = builder.databaseEnabled
        maxSyncDownload  = builder.maxSyncDownload
    }
}