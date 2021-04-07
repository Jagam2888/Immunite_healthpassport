package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ObservationStatus(
    var oscDisplayName: String,
    var oscSeqNo: Int,
    var oscSnomedCode: String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
