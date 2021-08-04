package com.cmg.vaccine.util

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import org.apache.http.entity.ContentType
import java.io.File
import java.io.FileInputStream
import java.lang.System.`in`
import kotlin.math.sin

class CustomUploadRequest(
    private val file:File,
    private val contentType: String,
    private val callback:uploadCallback
):RequestBody() {

    interface uploadCallback{
        fun onProgressUpdate(percentage:Int)
    }

    inner class progressUpdate(
        private val uploaded:Long,
        private val total:Long
    ):Runnable{
        override fun run() {
            callback.onProgressUpdate((100 * uploaded / total).toInt())
        }
    }

    override fun contentType() = MediaType.parse("$contentType/*")

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L

        fileInputStream.use {inputStream->
            var read:Int
            val handler = Handler(Looper.getMainLooper())

            do {
                read = `in`.read(buffer)

                if (read == -1) {
                    break
                }

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                handler.post(progressUpdate(uploaded, length!!))

            } while (true)

            /*while (inputStream.read(buffer).also { read = it }!=1){
                handler.post(progressUpdate(
                    uploaded,
                    length
                ))
                uploaded += read
                sink.write(buffer,0,read)
            }*/
        }
    }

    companion object{
        private const val DEFAULT_BUFFER_SIZE = 3072
    }
}