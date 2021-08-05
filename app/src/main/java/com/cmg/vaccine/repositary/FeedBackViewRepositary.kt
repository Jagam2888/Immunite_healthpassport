package com.cmg.vaccine.repositary

import com.cmg.vaccine.database.AppDatabase
import com.cmg.vaccine.database.Dependent
import com.cmg.vaccine.database.FeedBackUploadedFiles
import com.cmg.vaccine.database.User
import com.cmg.vaccine.model.request.AddFeedbackReq
import com.cmg.vaccine.model.response.AddFeedbackResponse
import com.cmg.vaccine.model.response.GetFeedbackResponseData
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

    fun getDependentList():List<Dependent>{
        return database.getDao().getDependentList(preferenceProvider.getSubId()!!)
    }

    suspend fun addFeedBackApi(file: Array<MultipartBody.Part?>, addFeedbackReq: AddFeedbackReq):AddFeedbackResponse{
        return apiRequest {
            api.addFeedback(file,addFeedbackReq)
        }
    }

    suspend fun insertFeedBackFiles(feedBackUploadedFiles: FeedBackUploadedFiles){
        database.getDao().insertFeedBackUploadFiles(feedBackUploadedFiles)
    }

    suspend fun insertFeedBackData(getFeedbackResponseData: GetFeedbackResponseData){
        database.getDao().insertFeedBackData(getFeedbackResponseData)
    }
}