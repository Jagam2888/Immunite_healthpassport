package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.data.UserProfileList
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.AddFeedbackData
import com.cmg.vaccine.model.request.AddFeedbackReq
import com.cmg.vaccine.repositary.FeedBackViewRepositary
import com.cmg.vaccine.util.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.SocketTimeoutException

class FeedBackViewModel(
    private val repositary: FeedBackViewRepositary
):ViewModel() {

    val feedbackTitle:MutableLiveData<String> = MutableLiveData()
    val feedbackDesc:MutableLiveData<String> = MutableLiveData()
    //var feedbackFile:MutableLiveData<String> = MutableLiveData()
    var feedbackFile = ObservableField<String>()

    val filePath:MutableLiveData<String> = MutableLiveData()
    val filePathList:MutableLiveData<ArrayList<String>> = MutableLiveData()

    val caseDob = ObservableField<String>()
    val caseSubId = ObservableField<String>()
    val caseEncryptPk = ObservableField<String>()
    val ratings = ObservableField<Int>()


    val _userProfileList:MutableLiveData<ArrayList<UserProfileList>> = MutableLiveData()
    val userProfileList:LiveData<ArrayList<UserProfileList>>
        get() = _userProfileList

    var listener:SimpleListener?=null

    init {
        val usersList = ArrayList<UserProfileList>()
        val priniciple = repositary.getPrinicipleData()
        if (priniciple != null){

            var privateKey:String?=null
            privateKey = if (!priniciple.privateKey.isNullOrEmpty()){
                priniciple.privateKey
            }else{
                ""
            }

            val users = UserProfileList(
                priniciple.fullName,
                Passparams.PARENT,
                priniciple.parentSubscriberId!!,
                privateKey!!,
                priniciple.dob!!
            )
            usersList.add(users)
        }

        val dependent = repositary.getDependentList()
        if (!dependent.isNullOrEmpty()){
            dependent.forEach {
                var privateKey:String?=null
                privateKey = if (!it.privateKey.isNullOrEmpty()){
                    it.privateKey
                }else{
                    ""
                }
                val users = UserProfileList(
                    it.firstName!!,
                    it.relationship!!,
                    it.subsId!!,
                    privateKey!!,
                    priniciple.dob!!
                )
                usersList.add(users)
            }
        }

        _userProfileList.value = usersList

    }
    fun addFeedback(){
        if (!feedbackFile.get().isNullOrEmpty()) {
            Couritnes.main {
                try {
                    val addFeedbackData = AddFeedbackData(
                        feedbackTitle.value!!,
                        caseDob.get()!!,
                        feedbackDesc.value!!,
                        caseEncryptPk.get()?.trim()!!,
                        "",
                        "New",
                        caseSubId.get()!!,
                        ratings.get().toString()
                    )
                    val addFeedbackReq = AddFeedbackReq(
                        addFeedbackData
                    )



                    val file = File(filePath.value)
                    //feedbackFile.set(file.name)

                    Log.d("file_name_path_list",filePathList.value?.size.toString())
                    Log.d("file_name_path", file.absolutePath!!)
                    Log.d("file_name", feedbackFile.get()!!)


                    /*val requestBody = CustomUploadRequest(file,"multipart/form-data",null)
                val body = MultipartBody.Part.createFormData("files", feedbackFile.get()?.trim(), requestBody)*/


                    val requestFile: RequestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val body = MultipartBody.Part.createFormData(
                        "files",
                        feedbackFile.get()?.trim(),
                        requestFile
                    )
                    val response = repositary.addFeedBackApi(body, addFeedbackReq)
                    Log.d("response", response.Message)
                    listener?.onSuccess(response.Message)
                } catch (e: APIException) {
                    listener?.onFailure("2" + e.message!!)
                } catch (e: NoInternetException) {
                    listener?.onFailure("3" + e.message!!)
                } catch (e: SocketTimeoutException) {
                    listener?.onShowToast(e.message!!)
                }
            }
        }else{
            listener?.onFailure("Please Choose File")
        }
    }
}