package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.request.AddFeedbackReq
import com.cmg.vaccine.model.response.*
import com.cmg.vaccine.network.MyApi
import com.cmg.vaccine.network.SafeAPIRequest
import com.cmg.vaccine.prefernces.PreferenceProvider
import okhttp3.MultipartBody
import retrofit2.Response

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

    suspend fun getFeedBackListApi():GetFeedbackStatusResponse{
        return apiRequest {
            api.getFeedBackListAPI(preferenceProvider.getSubId()!!)
        }
    }

    suspend fun updatedCaseStatus(caseNo: String,caseStatus:String):Int{
        return database.getDao().updateFeedBackData(caseNo,caseStatus)
    }

    fun getFeedBackDataCount(caseNo: String):Int{
        return database.getDao().getFeedBackDataCount(caseNo)
    }

    fun getFeedBackDataByCaseNo(caseNo: String):GetFeedbackStatusResponseData{
        return database.getDao().getFeedBackData(caseNo)
    }

    suspend fun insertFeedBackFiles(feedBackUploadedFiles: GetFeedbackStatusResponseAttachment){
        database.getDao().insertFeedBackUploadFiles(feedBackUploadedFiles)
    }

    fun getFeedBackFileCount(caseNo: String,fileName:String):Int{
        return database.getDao().getFeedBackFileCount(caseNo,fileName)
    }

    suspend fun insertFeedBackData(getFeedbackResponseData: GetFeedbackStatusResponseData){
        database.getDao().insertFeedBackData(getFeedbackResponseData)
    }


    fun getFeedBackFullList(status:String):List<GetFeedbackStatusResponseData>{
        return database.getDao().getFeedbackDataFullList(status)
    }

    fun getFeedBackUploadFiles(caseNo:String):List<GetFeedbackStatusResponseAttachment>{
        return database.getDao().getFeedBackUploadFiles(caseNo)
    }

    suspend fun insertFeedbackChronolgy(getFeedbackChronology: GetFeedbackChronology){
        database.getDao().insertFeedbackChronolgy(getFeedbackChronology)
    }

    fun getFeedBackChronolgy(caseNo: String):GetFeedbackChronology{
        return database.getDao().getFeedbackChronolgy(caseNo)
    }
}