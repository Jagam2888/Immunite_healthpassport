package com.cmg.vaccine.services

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.api.client.http.FileContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import java.io.*
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class DriveServiceHelper(var mDriveService: Drive?) {
    private val mExecutor: Executor = Executors.newSingleThreadExecutor()
    private val APP_DATA_FOLDER_SPACE = "appDataFolder"
    private val DATABASE_FILENAME="immunitees"
      var backupFileID:String?=null

    fun backUpFile(filePath: String?): Task<String>? {
        return Tasks.call(mExecutor, {

            val fileMetaData = File()
            fileMetaData.name = DATABASE_FILENAME
            val file = java.io.File(filePath)
            val mediaContent = FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", file)
            var myFile: File? = null
            try {
                backupFileID=getFileIdBasedFilename(DATABASE_FILENAME)
                    //myFile = mDriveService?.files()?.create(fileMetaData, mediaContent)?.execute()
                if (backupFileID.isNullOrEmpty()) {
                    myFile = mDriveService?.files()?.create(fileMetaData, mediaContent)?.execute()
                }else{
                    myFile=mDriveService?.files()?.update(backupFileID,fileMetaData,mediaContent)?.execute()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (myFile == null) {
                throw IOException("null result when requested file creation")
            }
            return@call myFile.id
        })
    }


    fun downloadFile(fileSaveLocation: java.io.File?): Task<Void?>? {
        return Tasks.call(mExecutor, {
            // Retrieve the metadata as a File object.
            var id = getFileIdBasedFilename(DATABASE_FILENAME)
            Log.e("id", id.toString())
            Log.e("path", fileSaveLocation.toString())
            val outputStream: OutputStream = FileOutputStream(fileSaveLocation)
            mDriveService?.files()?.get(id)?.executeMediaAndDownloadTo(outputStream)
            null
        })
    }


    private fun getFileIdBasedFilename(name: String): String? {
        val result = mDriveService?.files()?.list()?.setQ("trashed = false")?.execute()
        result?.files?.forEach {
            Log.d("drive_file_name",it.name)
            if (it.name.equals(name)){
                Log.d("File exist", "Found with " + it.id)
                return it.id
            }
        }

        Log.d("File exist", "Not Found")
        return null
    }


}