package com.cmg.vaccine.viewmodel

import android.content.Context
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

    var userSubId:MutableLiveData<String> = MutableLiveData()

    var filePath:MutableLiveData<String> = MutableLiveData()

    var mFaceRec:FaceRec?=null
    var simpleListener:SimpleListener?=null

    var errorMsg:String?=null
    var failedMsg:String?=null
    var successMsg:String?=null

    init {
        try {
            if (faceDetectViewModelRepositary.user() != null){
                _userName.value = faceDetectViewModelRepositary.user().fullName
                userSubId.value = faceDetectViewModelRepositary.user().parentSubscriberId
            }
        }catch (e:NullPointerException){
            _userName.value = "User"
        }

    }

    fun detectFace(bitmap: Bitmap,destination:File,context: Context) = viewModelScope.executeAsyncTask(
        onPreExecute = {
            simpleListener?.onStarted()
        },doInBackground = {
                mFaceRec = FaceRec(Constants.getDLibDirectoryPath(context))
                //val results = listOf<VisionDetRet>()
                val results = mFaceRec!!.detect(bitmap)
                if (results?.isEmpty() == true){
                    failedMsg = "No face was detected or face was too small. Please select a different image"
                    //simpleListener?.onShowToast()
                }else if (results?.size!! > 1){
                    failedMsg = "More than one face was detected. Please select a different image"
                    //simpleListener?.onShowToast("")
                }else{
                    val bytes = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes)
                    try {
                        destination.createNewFile()
                        val fo = FileOutputStream(destination)
                        fo.write(bytes.toByteArray())
                        fo.close()
                        successMsg = "success"
                        //simpleListener?.onSuccess("Face Detected")
                    }catch (e:FileNotFoundException){
                        errorMsg = e.message
                        //simpleListener?.onFailure(e.message!!)
                    }catch (e:IOException){
                        errorMsg = e.message
                        //simpleListener?.onFailure(e.message!!)
                    }


            }
        },onPostExecute = {
            if (!successMsg.isNullOrEmpty()) {
                filePath.value = destination.absolutePath
                simpleListener?.onSuccess(destination.absolutePath)
            }else if (!failedMsg.isNullOrEmpty()){
                simpleListener?.onShowToast(failedMsg!!)
            }else if (!errorMsg.isNullOrEmpty()){
                simpleListener?.onFailure(errorMsg!!)
            }
        }
    )
}