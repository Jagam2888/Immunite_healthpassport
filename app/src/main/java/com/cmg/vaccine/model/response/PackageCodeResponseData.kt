package com.cmg.vaccine.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PackageCodeResponseData(
    var packageAmount: Int?,
    var packageCode: String?,
    var packageCountryCode: String?,
    var packageCreatedBy: String?,
    var packageCreatedDate: String?,
    var packageCurrency: String?,
    var packageEndDate: String?,
    var packageName: String?,
    var packagePeriod: String?,
    var packageRemarks: String?,
    var packageStartDate: String?,
    var packageStatus: String?,
    var packageUpdatedBy: String?,
    var packageUpdatedDate: String?,
    var subsSeqNo: Int?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
}