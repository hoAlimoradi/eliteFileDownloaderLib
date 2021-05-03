package com.alimradi.elitefiledownloaderlib.core

import android.os.Process
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

class DefaultExecutorSupplier internal constructor() : ExecutorSupplier {
    private val networkExecutor: DownloadExecutor
    private val backgroundExecutor: Executor
    private val mainThreadExecutor: Executor

    init {
        val backgroundPriorityThreadFactory: ThreadFactory =
            PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND)
        networkExecutor = DownloadExecutor(1, backgroundPriorityThreadFactory)
        backgroundExecutor = Executors.newSingleThreadExecutor()
        mainThreadExecutor = MainThreadExecutor()
    }

    override fun forDownloadTasks() = networkExecutor

    override fun forBackgroundTasks() = backgroundExecutor

    override fun forMainThreadTasks() = mainThreadExecutor

    companion object {
        private val DEFAULT_MAX_NUM_THREADS = 2 * Runtime.getRuntime().availableProcessors() + 1
    }


}