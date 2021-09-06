package com.cmg.vaccine.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.repositary.FaceDetectRepositary
import com.cmg.vaccine.util.Couritnes
import com.cmg.vaccine.util.executeAsyncTask
import com.tzutalin.dlib.Constants
import com.tzutalin.dlib.FaceRec
import kotlinx.coroutines.launch
import java.io.*

/**
 * Created by jagad on 9/6/2021
 */
class FaceDetectViewModel(
    private val faceDetectViewModelRepositary: FaceDetectRepositary
):ViewModel() {

    var _userName:MutableLiveData<String> = MutableLiveData()
    val userName:LiveData<String>
    get() = _userName

    var filePath:MutableLiveData<String> = MutableLiveData()

    var mFaceRec:FaceRec?=null
    var simpleListener:SimpleListener?=null

    init {
        if (faceDetectViewModelRepositary.user() != null){
            _userName.postValue(faceDetectViewModelRepositary.user().fullName)
        }
    }

    fun detectFace(bitmap: Bitmap,destination:File) = viewModelScope.executeAsyncTask(
        onPreExecute = {
            simpleListener?.onStarted()
        },doInBackground = {
                mFaceRec = FaceRec(Constants.getDLibDirectoryPath())
                //val results = listOf<VisionDetRet>()
                val results = mFaceRec!!.detect(bitmap)
                if (results?.isEmpty() == true){
                    simpleListener?.onShowToast("No face was detected or face was too small. Please select a different image")
                }else if (results?.size!! > 1){
                    simpleListener?.onShowToast("More than one face was detected. Please select a different image")
                }else{
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes)
                    try {
                        destination.createNewFile()
                        val fo = FileOutputStream(destination)
                        fo.write(bytes.toByteArray())
                        fo.close()
                        //simpleListener?.onSuccess("Face Detected")
                    }catch (e:FileNotFoundException){
                        simpleListener?.onFailure(e.message!!)
                    }catch (e:IOException){
                        simpleListener?.onFailure(e.message!!)
                    }


            }
        },onPostExecute = {
            filePath.value = destination.absolutePath
            simpleListener?.onSuccess(destination.absolutePath)
        }
    )
}