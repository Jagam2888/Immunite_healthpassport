package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorldEntryRulesByCountry(
        var woenSeqNo: Int?,
        var woen_country_code: String?,
        var woen_duration_hours: String?,
        var woen_end_date: String?,
        var woen_points: String?,
        var woen_rule_description: String?,
        var woen_rule_match_criteria: String?,
        var woen_rule_seq_no: String?,
        var woen_start_date: String?,
        var woen_status: String?,
        var woen_test_code: String?,
        var woen_vaccine_code: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}
