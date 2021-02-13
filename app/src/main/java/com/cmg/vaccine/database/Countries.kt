package com.cmg.vaccine.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Countries (
        var countryCodeAlpha: String?,
        var countryMstrSeqno: Int?,
        var countryName: String?
        ){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0

    override fun toString(): String {
        return countryName!!
    }
}