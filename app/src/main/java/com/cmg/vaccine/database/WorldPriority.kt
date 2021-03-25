package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class WorldPriority(
        var prioRuleCountry: String?,
        var prioRuleCriteria: String?,
        var prioRuleNo: String?,
        var prioSeqNo: Int?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}