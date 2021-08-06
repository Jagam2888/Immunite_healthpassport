package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.request.AddFeedbackReq
import com.cmg.vaccine.model.response.AddFeedbackResponse
import com.cmg.vaccine.model.response.GetFeedbackStatusResponseAttachment
import com.cmg.vaccine.model.response.GetFeedbackStatusResponseData
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider
import okhttp3.MultipartBody

class FeedBackViewRepositary(
    private val api: MyApi,
    private val database: AppDatabase,
    private val preferenceProvider: PreferenceProvider
):SafeAPIRequest() {

    fun getPrinicipleData():User{
        return database.getDao().getUserData(preferenceProvider.getSubId()!!)
    }

    fun getPrincipleSubId():String{
        return preferenceProvider.getSubId()!!
    }

    fun getDependentList():List<Dependent>{
        return database.getDao().getDependentList(preferenceProvider.getSubId()!!)
    }

    suspend fun addFeedBackApi(file: Array<MultipartBody.Part?>, addFeedbackReq: AddFeedbackReq):AddFeedbackResponse{
        return apiRequest {
            api.addFeedback(file,addFeedbackReq)
        }
    }

    suspend fun insertFeedBackFiles(feedBackUploadedFiles: GetFeedbackStatusResponseAttachment){
        database.getDao().insertFeedBackUploadFiles(feedBackUploadedFiles)
    }

    suspend fun insertFeedBackData(getFeedbackResponseData: GetFeedbackStatusResponseData){
        database.getDao().insertFeedBackData(getFeedbackResponseData)
    }

    fun getFeedBackList(subId:String,status:String):List<GetFeedbackStatusResponseData>{
        return database.getDao().getFeedbackData(subId,status)
    }

    fun getFeedBackFullList(status:String):List<GetFeedbackStatusResponseData>{
        return database.getDao().getFeedbackDataFullList(status)
    }

    fun getFeedBackUploadFiles(caseNo:String):List<GetFeedbackStatusResponseAttachment>{
        return database.getDao().getFeedBackUploadFiles(caseNo)
    }
}