package com.cmg.vaccine.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.FaceRecognitionRepositary
import com.cmg.vaccine.util.executeAsyncTask
import java.util.regex.Pattern

/**
 * Created by jagad on 9/6/2021
 */
class FaceRecognitionViewModel(
    private val faceRecognitionRepositary: FaceRecognitionRepositary
):ViewModel() {

    var simpleListener:SimpleListener?=null
    var msg = ""
    var names:ArrayList<String> = ArrayList()

    init {
        //addPeople()
    }

    fun addPeople() = viewModelScope.executeAsyncTask(
        onPreExecute = {
            simpleListener?.onStarted()
        },doInBackground = {
            msg = faceRecognitionRepositary.initializeFace()
        },onPostExecute = {
            simpleListener?.onSuccess(msg)
        }
    )

    fun recognizeFace(bitmap: Bitmap) = viewModelScope.executeAsyncTask(
        doInBackground = {
            names.addAll(faceRecognitionRepositary.recogizeFace(bitmap))
        },onPostExecute = {
            if (names.isNotEmpty()) {
                simpleListener?.onSuccess(getResultMessage(names)!!)
            }else{
                simpleListener?.onFailure("Face not found")
            }
        },onPreExecute = {

        }
    )

    private fun getResultMessage(names: ArrayList<String>): String? {
        var msg = String()
        if (names.isEmpty()) {
            msg = "No face detected or Unknown person"
        } else {
            for (i in names.indices) {
                msg += names[i].split(Pattern.quote(".")).toTypedArray()[0]
                if (i != names.size - 1) msg += ", "
            }
            msg += " found!"
        }
        return msg
    }

}