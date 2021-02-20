package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestType(
    var loinc: String?,
    var snomed: String?,
    var testTypeSeqno: Int?,
    var test_code: String?,
    var test_created_by: String?,
    var test_created_date: String?,
    var test_name: String?,
    var test_result_validaty: String?,
    var test_specimen: String?,
    var test_status: String?,
    var test_update_date: String?,
    var test_updated_by: String?,
    var test_virus_code: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}