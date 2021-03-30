package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IdentifierType(
        var identifierCode: String?,
        var identifierDisplay: String?,
        var identifierSeqno: Int?,
        var identifierStatus: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

    override fun toString(): String {
        return identifierDisplay!!
    }
}