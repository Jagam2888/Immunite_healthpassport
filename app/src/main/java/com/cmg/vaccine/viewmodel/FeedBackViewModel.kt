package com.cmg.vaccine.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cmg.vaccine.data.MultipleFilesData
import com.cmg.vaccine.data.UserProfileList
import com.cmg.vaccine.listener.SimpleListener
import com.cmg.vaccine.model.request.AddFeedbackData
import com.cmg.vaccine.model.request.AddFeedbackReq
import com.cmg.vaccine.model.response.GetFeedbackChronology
import com.cmg.vaccine.model.response.GetFeedbackStatusResponseAttachment
import com.cmg.vaccine.model.response.GetFeedbackStatusResponseData
import com.cmg.vaccine.repositary.FeedBackViewRepositary
import com.cmg.vaccine.util.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import java.net.SocketTimeoutException

class FeedBackViewModel(
    private val repositary: FeedBackViewRepositary
):ViewModel() {

    val feedbackTitle = ObservableField<String>()
    val feedbackCategory = ObservableField<String>()
    val feedbackDesc:MutableLiveData<String> = MutableLiveData()
    val feedbackStatus:MutableLiveData<String> = MutableLiveData()
    //var feedbackFile:MutableLiveData<String> = MutableLiveData()
    var feedbackFile = ObservableField<String>()

    val filePath:MutableLiveData<String> = MutableLiveData()
    val _filePathList:MutableLiveData<ArrayList<MultipleFilesData>> = MutableLiveData()

    val filePathList:LiveData<ArrayList<MultipleFilesData>>
    get() = _filePathList

    val caseDob = ObservableField<String>()
    val caseSubId = ObservableField<String>()
    val caseEncryptPk = ObservableField<String>()
    val ratings = ObservableField<Int>()


    val _userProfileList:MutableLiveData<ArrayList<UserProfileList>> = MutableLiveData()
    val userProfileList:LiveData<ArrayList<UserProfileList>>
        get() = _userProfileList

    val _feedBackList:MutableLiveData<List<GetFeedbackStatusResponseData>> = MutableLiveData()
    val feedBackList:LiveData<List<GetFeedbackStatusResponseData>>
    get() = _feedBackList

    val _getFeedBackData:MutableLiveData<GetFeedbackStatusResponseData> = MutableLiveData()
    val getFeedBackData:LiveData<GetFeedbackStatusResponseData>
    get() = _getFeedBackData

    val _feedbackChronolgy:MutableLiveData<GetFeedbackChronology> = MutableLiveData()
    val feedbackChronology:LiveData<GetFeedbackChronology>
    get() = _feedbackChronolgy

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

    fun getFeedBackList(status:String){
        val list = repositary.getFeedBackFullList(status)
        _feedBackList.value = list
    }

    fun getAttachementList(caseNo:String):List<GetFeedbackStatusResponseAttachment>{
        return repositary.getFeedBackUploadFiles(caseNo)
    }

    fun getFeedBackDataByCaseNo(caseNo: String):GetFeedbackStatusResponseData{
        return repositary.getFeedBackDataByCaseNo(caseNo)
    }

    fun getFeedBackChronolgy(caseNo: String){
        val data = repositary.getFeedBackChronolgy(caseNo)
        if (data != null) {
            _feedbackChronolgy.value = data
        }
    }

    fun getFeedBackListFromAPI(){
        listener?.onStarted()
        Couritnes.main {
            try {
                val response = repositary.getFeedBackListApi()
                if (!response.data.isNullOrEmpty()){
                    response.data.forEach {
                        if (repositary.getFeedBackDataCount(it.caseNo) > 0){
                            repositary.updatedCaseStatus(it.caseNo,it.caseStatus)
                        }else{
                            repositary.insertFeedBackData(it)
                        }
                    }
                }
                if (!response.attachments.isNullOrEmpty()){
                    response.attachments.forEach {
                        if (repositary.getFeedBackFileCount(it.caseNo,it.fileName) == 0){
                            repositary.insertFeedBackFiles(it)
                        }
                    }
                }
                if (!response.chronology.isNullOrEmpty()){
                    response.chronology.forEach {
                        repositary.insertFeedbackChronolgy(it)
                    }
                }
                getFeedBackList(feedbackStatus.value!!)
                listener?.onSuccess("")
            }catch (e:APIException){
                listener?.onShowToast(e.message!!)
            }catch (e:NoInternetException){
                listener?.onShowToast(e.message!!)
            }catch (e:Exception){
                listener?.onShowToast(e.message!!)
            }
        }
    }

    fun addFeedback(){
        listener?.onStarted()
        if (!feedbackFile.get().isNullOrEmpty()) {
            Couritnes.main {
                try {
                    val addFeedbackData = AddFeedbackData(
                        feedbackCategory.get()!!,
                        caseDob.get()!!,
                        feedbackDesc.value!!,
                        caseEncryptPk.get()?.trim()!!,
                        "",
                        "New",
                        caseSubId.get()!!,
                        ratings.get().toString(),
                        repositary.getPrincipleSubId(),
                        feedbackTitle.get()!!
                    )
                    val addFeedbackReq = AddFeedbackReq(
                        addFeedbackData
                    )






                    Log.d("file_name_path_list",filePathList.value?.size.toString())
                    //Log.d("file_name_path", file.absolutePath!!)
                    Log.d("file_name", feedbackFile.get()!!)

                    val surveyImagesParts = arrayOfNulls<MultipartBody.Part>(
                        filePathList.value!!.size
                    )

                    for (i in filePathList.value!!.indices){
                        if (!filePathList.value!!.get(i)?.filePath.isNullOrEmpty()) {
                            val file = File(filePathList.value?.get(i)?.filePath)
                            val requestFile: RequestBody =
                                RequestBody.create(MediaType.parse("multipart/form-data"), file)
                            surveyImagesParts[i] = MultipartBody.Part.createFormData(
                                "files",
                                file.name,
                                requestFile
                            )
                        }
                    }

                    val response = repositary.addFeedBackApi(surveyImagesParts, addFeedbackReq)
                    Log.d("response", response.Message)
                    if (response.StatusCode == 1) {
                        val feedBackData = GetFeedbackStatusResponseData(
                        feedbackCategory.get()!!,
                        caseDob.get()!!,
                        feedbackDesc.value!!,
                        caseEncryptPk.get()?.trim()!!,
                        response.CaseNo,
                        0,
                        "NEW",
                        caseSubId.get()!!,
                        "",
                        "",
                        currentDateTime(),
                        ratings.get()!!.toInt(),
                        "",
                        currentDateTime(),
                            feedbackTitle.get()!!,
                            repositary.getPrincipleSubId()
                    )
                        repositary.insertFeedBackData(feedBackData)
                        filePathList.value?.forEach {
                            if (!it.filePath.isNullOrEmpty()){
                                val attachment = GetFeedbackStatusResponseAttachment(
                                    0,
                                    response.CaseNo,
                                    caseSubId.get()!!,
                                    it.fileName,
                                    it.filePath,
                                    "A",
                                    "",
                                    currentDateTime()
                                )
                                repositary.insertFeedBackFiles(attachment)
                            }
                        }
                        listener?.onSuccess(response.Message)
                    }

                    /*val requestBody = CustomUploadRequest(file,"multipart/form-data",null)
                val body = MultipartBody.Part.createFormData("files", feedbackFile.get()?.trim(), requestBody)*/


                    /*val requestFile: RequestBody =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file)
                    val body = MultipartBody.Part.createFormData(
                        "files",
                        feedbackFile.get()?.trim(),
                        requestFile
                    )
                    val response = repositary.addFeedBackApi(body, addFeedbackReq)
                    Log.d("response", response.Message)
                    listener?.onSuccess(response.Message)*/
                    //listener?.onSuccess(filePathList.value?.size.toString())
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