package com.cmg.vaccine.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GetFeedbackStatusResponseAttachment(
    val attSeqNo: Int,
    val caseNo: String,
    val caseSubId: String,
    val fileName: String,
    val filePath: String,
    val fileStatus: String,
    val fileType: String,
    val uploadedDate: String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}