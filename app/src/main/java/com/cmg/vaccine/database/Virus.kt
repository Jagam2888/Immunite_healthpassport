package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Virus(
    var virus_code: String?,
    var virus_created_by: String?,
    var virus_created_date: String?,
    var virus_name: String?,
    var virus_seqno: Int?,
    var virus_status: String?,
    var virus_updated_by: String?,
    var virus_updated_date: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
