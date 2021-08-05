package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FeedBackUploadedFiles(
    val caseNo:String,
    val caseSubId:String,
    val fileName:String,
    val file_path:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
