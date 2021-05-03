package com.alimradi.elitefiledownloaderlib.internal.stream

import java.io.*

class FileDownloadRandomAccessFile private constructor(file: File) : FileDownloadOutputStream {
    private val out: BufferedOutputStream
    private val fd: FileDescriptor
    private val randomAccess: RandomAccessFile = RandomAccessFile(file, "rw")

    @Throws(IOException::class)
    override fun write(b: ByteArray?, off: Int, len: Int) {
        out.write(b, off, len)
    }

    @Throws(IOException::class)
    override fun flushAndSync() {
        out.flush()
        fd.sync()
    }

    @Throws(IOException::class)
    override fun close() {
        out.close()
        randomAccess.close()
    }

    @Throws(IOException::class)
    override fun seek(offset: Long) {
        randomAccess.seek(offset)
    }

    @Throws(IOException::class)
    override fun setLength(totalBytes: Long) {
        randomAccess.setLength(totalBytes)
    }

    companion object {
        @Throws(IOException::class)
        fun create(file: File): FileDownloadOutputStream {
            return FileDownloadRandomAccessFile(file)
        }
    }

    init {
        fd = randomAccess.fd
        out = BufferedOutputStream(FileOutputStream(randomAccess.fd))
    }
}