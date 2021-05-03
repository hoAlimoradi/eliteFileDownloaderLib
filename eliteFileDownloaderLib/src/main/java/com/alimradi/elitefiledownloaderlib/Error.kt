package com.alimradi.elitefiledownloaderlib

data class Error(
    var isServerError: Boolean = false,
    var isConnectionError: Boolean = false,
    var serverErrorMessage: String? = null,
    var headerFields: Map<String?, List<String?>?>? = null,
    var connectionException: Throwable? = null,
    var responseCode: Int = 0
)