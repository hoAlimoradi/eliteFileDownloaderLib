package com.alimradi.elitefiledownloaderlib.core


class Core private constructor(maxSyncDownload: Int = 0) {
    val executorSupplier: ExecutorSupplier

    companion object {
        private var instance: Core? = null
        fun getInstance(maxSyncDownload: Int = 0): Core? {
            if (instance == null) {
                synchronized(Core::class.java) {
                    if (instance == null) {
                        instance = Core(maxSyncDownload)
                    }
                }
            }
            return instance
        }

        fun shutDown() {
            if (instance != null) {
                instance = null
            }
        }
    }

    init {
        executorSupplier = DefaultExecutorSupplier()
    }
}