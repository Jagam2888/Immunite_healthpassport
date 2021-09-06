package com.cmg.vaccine.util

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.RawRes
import java.io.*

/**
 * Created by jagad on 9/6/2021
 */
fun copyFile(srcPath:String,targetPath:String){
    var inputStream:InputStream?=null
    var outputStream:OutputStream?=null
    try {
        inputStream = FileInputStream(srcPath)
        outputStream = FileOutputStream(targetPath)
        val buf = ByteArray(1024)
        var len = 0
        while (inputStream.read(buf).also { len = it } > 0){
            outputStream.write(buf, 0, len)
        }
    }catch (e:Exception){

    }finally {
        try {
            inputStream?.close()
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
@NonNull
fun copyFileFromRawToOthers(context: Context,@RawRes id:Int,targetPath: String){
    val inputStream = context.resources.openRawResource(id)
    var outputStream:OutputStream?=null
    try {
        outputStream = FileOutputStream(targetPath)
        val buf = ByteArray(1024)
        var read = 0
        while (inputStream.read(buf).also { read = it } > 0){
            outputStream.write(buf, 0, read)
        }
    }catch (e:Exception){
        e.printStackTrace()
    }finally {
        try {
            inputStream?.close()
            outputStream?.close()
        }catch (io:IOException){
            io.printStackTrace()
        }
    }
}
