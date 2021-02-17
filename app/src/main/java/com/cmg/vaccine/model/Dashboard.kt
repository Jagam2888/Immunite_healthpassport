package com.cmg.vaccine.model

import com.cmg.vaccine.model.response.TestReportListResponseData
import com.cmg.vaccine.model.response.VaccineListResponseData

class Dashboard {
    var fullName:String?=null
    var relationShip:String?=null
    var passportNo:String?=null
    var idNo:String?=null
    var nationality:String?=null
    var privateKey:String?=null
    var data:List<VaccineListResponseData>?=null
    var dataTest:List<TestReportListResponseData>?=null
}