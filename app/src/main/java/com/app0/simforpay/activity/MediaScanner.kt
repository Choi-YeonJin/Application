package com.app0.simforpay.activity

import android.content.Context
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri

class MediaScanner(context: Context) {
    private val file_Path: String? = null
    private var mMediaScanner: MediaScannerConnection? = null
    private var mMediaScannerClient: MediaScannerConnectionClient? = null

    fun mediaScanning(path: String, context: Context) {
        if (mMediaScanner == null) {
            mMediaScannerClient = object : MediaScannerConnectionClient {
                override fun onMediaScannerConnected() {
                    mMediaScanner!!.scanFile(file_Path, null)
                }

                override fun onScanCompleted(path: String, uri: Uri) {
                    mMediaScanner!!.disconnect()
                }
            }
            mMediaScanner = MediaScannerConnection(context, mMediaScannerClient)
        }
        val mPath = path
        mMediaScanner!!.connect()
    }

    companion object {
        fun newInstance(context: Context): MediaScanner {
            return MediaScanner(context)
        }
    }
}