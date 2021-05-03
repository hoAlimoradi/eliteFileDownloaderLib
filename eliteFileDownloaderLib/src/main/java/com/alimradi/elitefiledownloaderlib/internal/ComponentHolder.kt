package com.alimradi.elitefiledownloaderlib.internal

import android.content.Context
import com.alimradi.elitefiledownloaderlib.Constants
import com.alimradi.elitefiledownloaderlib.EFDownloader
import com.alimradi.elitefiledownloaderlib.EFDownloaderConfig
import com.alimradi.elitefiledownloaderlib.database.AppDbHelper
import com.alimradi.elitefiledownloaderlib.database.DbHelper
import com.alimradi.elitefiledownloaderlib.database.NoOpsDbHelper
import com.alimradi.elitefiledownloaderlib.httpclient.DefaultHttpClient
import com.alimradi.elitefiledownloaderlib.httpclient.HttpClient

class ComponentHolder {
    private var readTimeout = 0
    private var connectTimeout = 0
    private var userAgent: String? = null
    private var httpClient: HttpClient? = null
    private var dbHelper: DbHelper? = null
    private var maxSyncDownload = 0
    fun init(context: Context?, config: EFDownloaderConfig) {
        readTimeout = config.readTimeout
        connectTimeout = config.connectTimeout
        userAgent = config.userAgent
        httpClient = config.httpClient
        maxSyncDownload = config.maxSyncDownload
        dbHelper = if (config.isDatabaseEnabled) AppDbHelper(context) else NoOpsDbHelper()
        if (config.isDatabaseEnabled) {
            EFDownloader.cleanUp(30)
        }
    }

    fun getReadTimeout(): Int {
        if (readTimeout == 0) {
            synchronized(ComponentHolder::class.java) {
                if (readTimeout == 0) {
                    readTimeout = Constants.DEFAULT_READ_TIMEOUT_IN_MILLS
                }
            }
        }
        return readTimeout
    }

    fun getConnectTimeout(): Int {
        if (connectTimeout == 0) {
            synchronized(ComponentHolder::class.java) {
                if (connectTimeout == 0) {
                    connectTimeout = Constants.DEFAULT_CONNECT_TIMEOUT_IN_MILLS
                }
            }
        }
        return connectTimeout
    }

    fun getUserAgent(): String? {
        if (userAgent == null) {
            synchronized(ComponentHolder::class.java) {
                if (userAgent == null) {
                    userAgent = Constants.DEFAULT_USER_AGENT
                }
            }
        }
        return userAgent
    }

    fun getMaxSyncDownload(): Int {
        synchronized(ComponentHolder::class.java) {
            return maxSyncDownload
        }
    }

    fun getDbHelper(): DbHelper? {
        if (dbHelper == null) {
            synchronized(ComponentHolder::class.java) {
                if (dbHelper == null) {
                    dbHelper = NoOpsDbHelper()
                }
            }
        }
        return dbHelper
    }

    fun getHttpClient(): HttpClient {
        if (httpClient == null) {
            synchronized(ComponentHolder::class.java) {
                if (httpClient == null) {
                    httpClient = DefaultHttpClient()
                }
            }
        }
        return httpClient!!.clone()
    }

    companion object {
        val instance = ComponentHolder()
    }
}