
# EFDownloader - A file downloader library for Android with pause and resume support

### Overview of EFDownloader library
* EFDownloader can be used to download any type of files like image, video, pdf, apk and etc.
* This file downloader library supports pause and resume while downloading a file.
* Supports large file download.
* This downloader library has a simple interface to make download request.
* We can check if the status of downloading with the given download Id.
* EFDownloader gives callbacks for everything like onProgress, onCancel, onStart, onError and etc while downloading a file.
* Supports proper request canceling.
* Many requests can be made in parallel.
* All types of customization are possible.

## Using EFDownloader Library in your Android application

Initialize it in onCreate() Method of application class :
```kotlin
EFDownloader.initialize(this)
```
Initializing it with some customization
```kotlin
// Enabling database for resume support even after the application is killed:
val config = EFDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)           
EFDownloader.initialize(this, config)

// Setting timeout globally for the download network requests and set multi download thread:
val config = EFDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .setMaxSyncDownload(1)
                .build()
EFDownloader.initialize(this, config)
```

### Make a download request
```kotlin
// Enable display download information in notification bar by call showNotification function
val downloadId = EFDownloader.download(url, dirPath, fileName)
                        .build()
                        .showNotification(context)
                        .setOnStartOrResumeListener(new OnStartOrResumeListener {
                            @Override
                            public void onStartOrResume() {
                               
                            }
                        })
                        .setOnPauseListener(new OnPauseListener {
                            @Override
                            public void onPause() {
                               
                            }
                        })
                        .setOnCancelListener(new OnCancelListener {
                            @Override
                            public void onCancel() {
                                
                            }
                        })
                        .setOnProgressListener(new OnProgressListener {
                            @Override
                            public void onProgress(progress: Progress?) {
                               
                            }
                        })
                        .start(new OnDownloadListener {
                            @Override
                            public void onDownloadComplete() {
                               
                            }

                            @Override
                            public void onError(error: Error?) {
                               
                            }
                        });            
```

### Pause a download request
```kotlin
PRDownloader.pause(downloadId)
```

### Resume a download request
```kotlin
PRDownloader.resume(downloadId)
```

### Cancel a download request
```kotlin
// Cancel with the download id
EFDownloader.cancel(downloadId);
// The tag can be set to any request and then can be used to cancel the request
EFDownloader.cancel(TAG);
// Cancel all the requests
EFDownloader.cancelAll();
```

### Status of a download request
```kotlin
val status :Status = EFDownloader.getStatus(downloadId);
```

### Clean up resumed files if database enabled
```kotlin
// Method to clean up temporary resumed files which is older than the given day
EFDownloader.cleanUp(days);
```



