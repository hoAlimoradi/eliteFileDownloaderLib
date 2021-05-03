
package com.alimradi.elitefiledownloaderlib.core

import java.util.concurrent.Executor

interface ExecutorSupplier {
    fun forDownloadTasks(): DownloadExecutor
    fun forBackgroundTasks(): Executor
    fun forMainThreadTasks(): Executor
}