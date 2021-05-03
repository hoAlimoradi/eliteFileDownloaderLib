package com.alimradi.android

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alimradi.elitefiledownloaderlib.*
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val url1 = "https://cdna.p30download.ir/p30dl-audio/Naser.Cheshmazar.Music.Passion.of.Love.Mp3.128kbps_p30download.com.mp3"
    private val url2 = "https://cdna.p30download.ir/p30dl-ebook/Click.736-1398.10.01_p30download.com.zip";
    private val url3 = "https://cdna.p30download.ir/p30dl-ebook/Byte.473-1396.05.25_p30download.com.zip"
    private val url4 = "https://cdna.p30download.ir/p30dl-audio/Naser.Cheshmazar.Music.My.Mother.Mp3.128kbps_p30download.com.mp3"

    companion object {
        lateinit var dirPath: String
    }

    var downloadId1: Int = 0
    var downloadId2: Int = 0
    var downloadId3: Int = 0
    var downloadId4: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dirPath = Utils.getRootDirPath(applicationContext)
        onClickListener1()
        onClickListener2()
        onClickListener3()
        onClickListener4()
    }

    private fun onClickListener1() {
        buttonOne.setOnClickListener(View.OnClickListener {
            if (Status.RUNNING === EFDownloader.getStatus(downloadId1)) {
                EFDownloader.pause(downloadId1)
                return@OnClickListener
            }
            buttonOne.isEnabled = false
            progressBarOne.isIndeterminate = true
            progressBarOne.indeterminateDrawable.setColorFilter(
                Color.BLUE, PorterDuff.Mode.SRC_IN
            )
            if (Status.PAUSED === EFDownloader.getStatus(downloadId1)) {
                EFDownloader.resume(downloadId1)
                return@OnClickListener
            }
            downloadId1 = EFDownloader.download(url1, dirPath, "Passion")
                .build()
                .showNotification(this)
                .setOnStartOrResumeListener(object : OnStartOrResumeListener {
                    override fun onStartOrResume() {
                        progressBarOne.isIndeterminate = false
                        buttonOne.isEnabled = true
                        buttonOne.setText(R.string.pause)
                        buttonCancelOne.isEnabled = true
                    }
                })
                .setOnPauseListener(object : OnPauseListener {
                    override fun onPause() {
                        buttonOne.setText(R.string.resume)
                    }
                })
                .setOnCancelListener(object : OnCancelListener {
                    override fun onCancel() {
                        buttonOne.setText(R.string.start)
                        buttonCancelOne.isEnabled = false
                        progressBarOne.progress = 0
                        textViewProgressOne.text = ""
                        downloadId1 = 0
                        progressBarOne.isIndeterminate = false
                    }

                })
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress?) {
                        progress?.let {
                            val progressPercent: Long =
                                progress.currentBytes * 100 / progress.totalBytes
                            progressBarOne.progress = progressPercent.toInt()
                            textViewProgressOne.text = Utils.getProgressDisplayLine(
                                progress.currentBytes,
                                progress.totalBytes
                            )
                            progressBarOne.isIndeterminate = false
                        }
                    }

                })
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        buttonOne.isEnabled = false
                        buttonCancelOne.isEnabled = false
                        buttonOne.setText(R.string.completed)
                    }

                    override fun onError(error: Error?) {
                        buttonOne.setText(R.string.start)
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.some_error_occurred) + " " + "1",
                            Toast.LENGTH_SHORT
                        ).show()
                        textViewProgressOne.text = ""
                        progressBarOne.progress = 0
                        downloadId1 = 0
                        buttonCancelOne.isEnabled = false
                        progressBarOne.isIndeterminate = false
                        buttonOne.isEnabled = true
                    }
                })
        })
        buttonCancelOne.setOnClickListener { EFDownloader.cancel(downloadId1) }
    }

    private fun onClickListener2() {
        buttonTwo.setOnClickListener(View.OnClickListener {
            if (Status.RUNNING === EFDownloader.getStatus(downloadId2)) {
                EFDownloader.pause(downloadId2)
                return@OnClickListener
            }
            buttonTwo.isEnabled = false
            progressBarTwo.isIndeterminate = true
            progressBarTwo.indeterminateDrawable.setColorFilter(
                Color.BLUE, PorterDuff.Mode.SRC_IN
            )
            if (Status.PAUSED === EFDownloader.getStatus(downloadId2)) {
                EFDownloader.resume(downloadId2)
                return@OnClickListener
            }
            downloadId2 = EFDownloader.download(url2, dirPath, "Click")
                .build()
                .showNotification(this)
                .setOnStartOrResumeListener(object : OnStartOrResumeListener {
                    override fun onStartOrResume() {
                        progressBarTwo.isIndeterminate = false
                        buttonTwo.isEnabled = true
                        buttonTwo.setText(R.string.pause)
                        buttonCancelTwo.isEnabled = true
                        buttonCancelTwo.setText(R.string.cancel)
                    }
                })
                .setOnPauseListener(object : OnPauseListener {
                    override fun onPause() {
                        buttonTwo.setText(R.string.resume)
                    }
                })
                .setOnCancelListener(object : OnCancelListener {
                    override fun onCancel() {
                        downloadId2 = 0
                        buttonTwo.setText(R.string.start)
                        buttonCancelTwo.isEnabled = false
                        progressBarTwo.progress = 0
                        textViewProgressTwo.text = ""
                        progressBarTwo.isIndeterminate = false
                    }
                })
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress?) {
                        progress?.let {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarTwo.progress = progressPercent.toInt()
                            textViewProgressTwo.text =
                                Utils.getProgressDisplayLine(
                                    progress.currentBytes,
                                    progress.totalBytes
                                )
                        }
                    }
                })
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        buttonTwo.isEnabled = false
                        buttonCancelTwo.isEnabled = false
                        buttonTwo.setText(R.string.completed)
                    }

                    override fun onError(error: Error?) {
                        buttonTwo.setText(R.string.start)
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.some_error_occurred) + " " + "2",
                            Toast.LENGTH_SHORT
                        ).show()
                        textViewProgressTwo.text = ""
                        progressBarTwo.progress = 0
                        downloadId2 = 0
                        buttonCancelTwo.isEnabled = false
                        progressBarTwo.isIndeterminate = false
                        buttonTwo.isEnabled = true
                    }
                })
        })

        buttonCancelTwo.setOnClickListener { EFDownloader.cancel(downloadId2) }
    }

    private fun onClickListener3() {
        buttonThree.setOnClickListener(View.OnClickListener {
            if (Status.RUNNING === EFDownloader.getStatus(downloadId3)) {
                EFDownloader.pause(downloadId3)
                return@OnClickListener
            }
            buttonThree.isEnabled = false
            progressBarThree.isIndeterminate = true
            progressBarThree.indeterminateDrawable.setColorFilter(
                Color.BLUE, PorterDuff.Mode.SRC_IN
            )
            if (Status.PAUSED === EFDownloader.getStatus(downloadId3)) {
                EFDownloader.resume(downloadId3)
                return@OnClickListener
            }
            downloadId3 = EFDownloader.download(url3, dirPath, "Byte")
                .build()
                .showNotification(this)
                .setOnStartOrResumeListener(object : OnStartOrResumeListener {
                    override fun onStartOrResume() {
                        progressBarThree.isIndeterminate = false
                        buttonThree.isEnabled = true
                        buttonThree.setText(R.string.pause)
                        buttonCancelThree.isEnabled = true
                        buttonCancelThree.setText(R.string.cancel)
                    }
                })
                .setOnPauseListener(object : OnPauseListener {
                    override fun onPause() {
                        buttonThree.setText(R.string.resume)
                    }
                })
                .setOnCancelListener(object : OnCancelListener {
                    override fun onCancel() {
                        downloadId3 = 0
                        buttonThree.setText(R.string.start)
                        buttonCancelThree.isEnabled = false
                        progressBarThree.progress = 0
                        textViewProgressThree.text = ""
                        progressBarThree.isIndeterminate = false
                    }
                })
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress?) {
                        progress?.let {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarThree.progress = progressPercent.toInt()
                            textViewProgressThree.text =
                                Utils.getProgressDisplayLine(
                                    progress.currentBytes,
                                    progress.totalBytes
                                )
                        }
                    }
                })
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        buttonThree.isEnabled = false
                        buttonCancelThree.isEnabled = false
                        buttonThree.setText(R.string.completed)
                    }

                    override fun onError(error: Error?) {
                        buttonThree.setText(R.string.start)
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.some_error_occurred) + " " + "3",
                            Toast.LENGTH_SHORT
                        ).show()
                        textViewProgressThree.text = ""
                        progressBarThree.progress = 0
                        downloadId3 = 0
                        buttonCancelThree.isEnabled = false
                        progressBarThree.isIndeterminate = false
                        buttonThree.isEnabled = true
                    }
                })
        })

        buttonCancelThree.setOnClickListener { EFDownloader.cancel(downloadId3) }
    }

    private fun onClickListener4() {
        buttonFour.setOnClickListener(View.OnClickListener {
            if (Status.RUNNING === EFDownloader.getStatus(downloadId4)) {
                EFDownloader.pause(downloadId4)
                return@OnClickListener
            }
            buttonFour.isEnabled = false
            progressBarFour.isIndeterminate = true
            progressBarFour.indeterminateDrawable.setColorFilter(
                Color.BLUE, PorterDuff.Mode.SRC_IN
            )
            if (Status.PAUSED === EFDownloader.getStatus(downloadId4)) {
                EFDownloader.resume(downloadId4)
                return@OnClickListener
            }
            downloadId4 = EFDownloader.download(url4, dirPath, "Mother")
                .build()
                .showNotification(this)
                .setOnStartOrResumeListener(object : OnStartOrResumeListener {
                    override fun onStartOrResume() {
                        progressBarFour.isIndeterminate = false
                        buttonFour.isEnabled = true
                        buttonFour.setText(R.string.pause)
                        buttonCancelFour.isEnabled = true
                        buttonCancelFour.setText(R.string.cancel)
                    }
                })
                .setOnPauseListener(object : OnPauseListener {
                    override fun onPause() {
                        buttonFour.setText(R.string.resume)
                    }
                })
                .setOnCancelListener(object : OnCancelListener {
                    override fun onCancel() {
                        downloadId4 = 0
                        buttonFour.setText(R.string.start)
                        buttonCancelFour.isEnabled = false
                        progressBarFour.progress = 0
                        textViewProgressFour.text = ""
                        progressBarFour.isIndeterminate = false
                    }
                })
                .setOnProgressListener(object : OnProgressListener {
                    override fun onProgress(progress: Progress?) {
                        progress?.let {
                            val progressPercent = progress.currentBytes * 100 / progress.totalBytes
                            progressBarFour.progress = progressPercent.toInt()
                            textViewProgressFour.text =
                                Utils.getProgressDisplayLine(progress.currentBytes, progress.totalBytes)
                        }
                    }
                })
                .start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        buttonFour.isEnabled = false
                        buttonCancelFour.isEnabled = false
                        buttonFour.setText(R.string.completed)
                    }

                    override fun onError(error: Error?) {
                        buttonFour.setText(R.string.start)
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.some_error_occurred) + " " + "4",
                            Toast.LENGTH_SHORT
                        ).show()
                        textViewProgressFour.text = ""
                        progressBarFour.progress = 0
                        downloadId4 = 0
                        buttonCancelFour.isEnabled = false
                        progressBarFour.isIndeterminate = false
                        buttonFour.isEnabled = true
                    }
                })
        })

        buttonCancelFour.setOnClickListener { EFDownloader.cancel(downloadId4) }
    }
}