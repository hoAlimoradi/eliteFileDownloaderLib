package com.alimradi.android

import android.app.Application
import android.content.pm.PackageManager
import com.alimradi.elitefiledownloaderlib.EFDownloader
import com.alimradi.elitefiledownloaderlib.EFDownloaderConfig

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = EFDownloaderConfig.newBuilder()
            .setConnectTimeout(8000)
            .setDatabaseEnabled(true)
            .setMaxSyncDownload(1)
            .setReadTimeout(8000).build()
        EFDownloader.initialize(this, config)
    }
}