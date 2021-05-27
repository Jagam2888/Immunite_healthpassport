package com.cmg.vaccine.model

import com.cmg.vaccine.database.TestReport
import com.cmg.vaccine.database.Vaccine
import com.cmg.vaccine.database.VaccineReport
import com.cmg.vaccine.model.response.TestReportListResponseData
import com.cmg.vaccine.model.response.VaccineListResponseData

class Dashboard {
    var fullName:String?=null
    var relationShip:String?=null
    var passportNo:String?=null
    var passportExpiry:String?=null
    var idNo:String?=null
    var idType:String?=null
    var nationality:String?=null
    var privateKey:String?=null
    var dob:String?=null
    var profileImg:String?=null
    var subId:String?=null
    var data:List<VaccineReport>?=null
    var dataTest:List<TestReport>?=null
}