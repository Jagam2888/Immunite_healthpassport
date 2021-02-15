package com.cmg.vaccine.util


object Passparams{
    const val LOGINPIN:String = "loginpin"
    const val ISCREATE:String = "isCreate"
    const val ISSETTINGS:String = "isSettings"
    const val PRIVATEKEY:String = "private_key"
    const val USER:String = "user"
    const val PARENT:String = "Principal"
    const val DEPENDENT:String = "Dependant"
    const val DEPENDENT_SUBID:String = "dependant_sub_id"

    //API
    const val URL:String = "http://47.254.238.145:7001/"
    const val PATIENT_REGISTRATION:String = "nhr-module-user/patientreg"
    const val DEPENDENT_REGISTRATION:String = "nhr-module-user/patientdependentreg"

    const val EDIT_PATIENT_PROFILE:String = "nhr-module-user/editpatientprofile"
    const val EDIT_DEPENDENT_PROFILE:String = "nhr-module-user/editdependentProfile"

    const val SEARCH_PRIVATE_KEY:String = "nhr-module-user/searchPrivateKey"
    const val SEARCH_VACCINE:String = "nhr-module-user/searchVaccine"
    const val VERIFY_TAC:String = "nhr-module-user/verifyTac"
    const val RESEND_TAC:String = "nhr-module-user/resendtac"
    const val COUNTRIES:String = "nhr-module-user/countries"
}