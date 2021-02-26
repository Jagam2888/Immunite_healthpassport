package com.cmg.vaccine.util


object Passparams{
    const val LOGINPIN = "loginpin"
    const val ISCREATE = "isCreate"
    const val ISSETTINGS = "isSettings"
    const val PRIVATEKEY = "private_key"
    const val USER_NAME = "user_name"
    const val SUBSID = "subsid"
    const val USER = "user"
    const val PARENT = "Principal"
    const val DEPENDENT = "Dependant"
    const val RELATIONSHIP = "relation_ship"
    const val DEPENDENT_SUBID = "dependant_sub_id"
    const val TEST_REPORT_ID = "test_report_id"
    const val FCM_TOKEN = "fcm_token"
    const val WORLD_ENTRY_SELECTED_COUNTRY_NAME = "world_entry_selected_country_name"
    const val WORLD_ENTRY_SELECTED_COUNTRY_CODE = "world_entry_selected_country_code"

    //API
    const val URL = "http://47.254.238.145:7001/"
    const val PATIENT_REGISTRATION = "nhr-module-user/patientreg"
    const val DEPENDENT_REGISTRATION = "nhr-module-user/patientdependentreg"

    const val EDIT_PATIENT_PROFILE = "nhr-module-user/editpatientprofile"
    const val EDIT_DEPENDENT_PROFILE = "nhr-module-user/editdependentProfile"

    const val SEARCH_PRIVATE_KEY = "nhr-module-user/searchPrivateKey"
    const val GET_PATIENT_PRIVATE_KEY = "nhr-module-user/privateKeyBySubsId"
    const val GET_DEPENDENT_PRIVATE_KEY = "nhr-module-user/privatekeybydepsubsid"
    const val SEARCH_VACCINE = "nhr-module-user/searchVaccine"
    const val SEARCH_TEST_REPORT = "nhr-module-user/searchtest"
    const val VERIFY_TAC = "nhr-module-user/verifyTac"
    const val RESEND_TAC = "nhr-module-user/resendtac"
    const val COUNTRIES = "nhr-module-user/countries"
    const val VACCINE = "nhr-module-user/vaccine"
    const val TESTTYPE = "nhr-module-user/testtype"
    const val VIRUS = "nhr-module-user/virus"
    const val GET_VACCINE_LIST = "nhr-module-user/getallvaccinerecord"
    const val GET_TEST_REPORT_LIST = "nhr-module-user/getalltestrecord"
    const val GET_WORLD_ENTRIES_LIST = "nhr-module-user/worldentry"
    const val GET_WORLD_ENTRIES_RULES = "nhr-module-user/worldentrybycountry"

    //Block Chain
    const val BLOCK_CHAIN_URL = "https://cashierbook.com.my:8081/blockchainapi/"

    const val GET_VACCINE_LIST_BLOCK_CHAIN = "getAllVaccineRecord"
    const val GET_TEST_REPORT_LIST_BLOCK_CHAIN = "getAllTestRecord"

}