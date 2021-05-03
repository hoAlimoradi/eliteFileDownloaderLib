package com.alimradi.elitefiledownloaderlib


object Constants {
    const val UPDATE = 0x01
    const val RANGE = "Range"
    const val ETAG = "ETag"
    const val MAX_DOWNLOAD_THREAD: Int = 0
    const val USER_AGENT = "User-Agent"
    const val DEFAULT_USER_AGENT = "EFDownloader"
    const val DEFAULT_READ_TIMEOUT_IN_MILLS = 20000
    const val DEFAULT_CONNECT_TIMEOUT_IN_MILLS = 20000
    const val HTTP_RANGE_NOT_SATISFIABLE = 416
    const val HTTP_TEMPORARY_REDIRECT = 307
    const val HTTP_PERMANENT_REDIRECT = 308
    const val CHANNEL_ID = "Elite File Downloader Channel Id"
    const val CHANNEL_NAME = "Elite File Downloader Channel Name"
    const val GROUP_KEY_DOWNLOAD = "com.alimradi.elitefiledownloaderlib.DOWNLOAD"
}