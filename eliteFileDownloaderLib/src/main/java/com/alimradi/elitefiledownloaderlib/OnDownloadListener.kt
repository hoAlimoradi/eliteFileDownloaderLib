
package com.alimradi.elitefiledownloaderlib


interface OnDownloadListener {
    fun onDownloadComplete()
    fun onError(error: Error?)
}