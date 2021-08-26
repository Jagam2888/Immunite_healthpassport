package com.cmg.vaccine.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by jagad on 8/26/2021
 */
@Entity
data class GetFeedbackChronology(
    val seqNo: Int,
    val caseNo: String,
    val comments: String,
    val createdBy: String,
    val createdDate: String,
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
