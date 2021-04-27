package com.cmg.vaccine.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SystemConfigResponseData(
    var configSeqno: Int?,
    var sysCreatedBy: String?,
    var sysCreatedDate: String?,
    var sysDescription: String?,
    var sysMappingKeyName: String?,
    var sysMappingValue: String?,
    var sysReferredBy: String?,
    var sysStatus: String?,
    var sysUpdatedBy: String?,
    var sysUpdatedDate: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}