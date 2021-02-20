package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VaccineDetail(
    var loinc: String?,
    var snomed: String?,
    var vaccineSeqno: Int?,
    var vaccine_code: String?,
    var vaccine_created_by: String?,
    var vaccine_created_date: String?,
    var vaccine_duration_between_dosses: String?,
    var vaccine_manufacterer: String?,
    var vaccine_manufacturing_year: String?,
    var vaccine_name: String?,
    var vaccine_no_of_days_for_maximum_efficacy: String?,
    var vaccine_no_of_doses: String?,
    var vaccine_status: String?,
    var vaccine_updated_by: String?,
    var vaccine_updated_date: String?,
    var vaccine_virus_code: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
