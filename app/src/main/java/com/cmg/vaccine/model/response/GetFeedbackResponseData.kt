package com.cmg.vaccine.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GetFeedbackResponseData(
    val caseCategory: String,
    val caseDOB: String,
    val caseDescription: String,
    val caseEncryptPK: String,
    val caseNo: String,
    val caseSeqNo: Int,
    val caseStatus: String,
    val caseSubId: String,
    val closeBy: String,
    val closeDate: String,
    val createdDate: String,
    val rating: Int,
    val updatedBy: String,
    val updatedDate: String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}