package com.cmg.vaccine.util


object Passparams{
    const val LOGINPIN = "loginpin"
    const val ISCREATE = "isCreate"
    const val ISSETTINGS = "isSettings"
    const val PRIVATEKEY = "private_key"
    const val USER_NAME = "user_name"
    const val USER_DOB = "user_dob"
    const val SUBSID = "subsid"
    const val USER = "user"
    const val PARENT = "Principal"
    const val DEPENDENT = "Dependent"
    const val RELATIONSHIP = "relation_ship"
    const val DEPENDENT_SUBID = "dependant_sub_id"
    const val TEST_REPORT_ID = "test_report_id"
    const val VACCINE_CODE = "vaccine_code"
    const val FCM_TOKEN = "fcm_token"
    const val WORLD_ENTRY_SELECTED_COUNTRY_NAME = "world_entry_selected_country_name"
    const val WORLD_ENTRY_SELECTED_COUNTRY_CODE = "world_entry_selected_country_code"
    const val QR_CODE_VALUE = "qr_code_value"
    const val DATE_FORMAT = "dd/mm/yyyy"
    const val CURRENT_USER_SUBSID = "current_user_subid"

    const val ADD_DEPENDENT_SUCCESS = "add_dependent_success"


    const val NAVIGATE_TO = "navigate_to"
    const val EXISTING_USER = "existing_user"
    const val DEPARTURE_VERIFICATION = "departure_verification"

    const val NAVIGATE_FROM = "navigate_from"
    const val SIGNUP = "signup"
    const val EDIT_PROFILE = "edit_profile"
    const val EDIT_DEPENDENT_PROFILE = "edit_dependent_profile"
    const val ADD_DEPENDENT_PROFILE = "add_dependent_profile"
    const val FORGOT_PIN = "forgot_pin"

    //API
    //const val URL = "http://47.254.238.145:7001/"

    //Production API
    const val URL = "http://47.254.236.183:7001/"

    const val PATIENT_REGISTRATION = "nhr-module-user/patientreg"
    const val DEPENDENT_REGISTRATION = "nhr-module-user/patientdependentreg"

    const val EDIT_PATIENT_PROFILE_API = "nhr-module-user/editpatientprofile"
    const val EDIT_DEPENDENT_PROFILE_API = "nhr-module-user/editdependentProfile"

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
    const val IMMUNIZATION_HISTORY = "nhr-module-user/immuhistory"
    //const val IMMUNIZATION_HISTORY = "nhr-module-user/upload"
    const val GET_EXISTING_USER = "nhr-module-user/gepatientprivatekey"
    const val GET_ALL_AIRPORT_CITIES = "nhr-module-user/searchairportcities"
    const val GET_ALL_WORLD_ENTRY_RULE = "nhr-module-user/searchworldentry"
    const val GET_IDENTIFIER_TYPE = "nhr-module-user/searchidentifiertype"
    const val GET_TEST_CODES = "nhr-module-user/searchworldtestcodes"
    const val GET_WORLD_PRIORITY_LIST = "nhr-module-user/searchworldprioritylist"

    //const val GET_VACCINE_TEST_REF = "blockchainapi/getAllTestRecord"
    const val GET_VACCINE_TEST_REF = "nhr-module-user/getalltestrecord"
    const val UPDATE_UUID = "nhr-module-user/updateuuid"
    const val UPDATE_PRIVATE_KEY_STATUS = "nhr-module-user/updatePrivateKeyStatus"
    const val UPDATE_FCM_TOKEN = "nhr-module-user/updatetoken"
    const val DOWNLOAD_TEST_REPORT_FILE = "gp-module-lab/ext/labpdf"

    //Block Chain
    const val BLOCK_CHAIN_URL = "https://cashierbook.com.my:8081/blockchainapi/"

    //Download file
    const val ICARE_URL = "http://10.1.1.150:6001/"

    const val GET_VACCINE_LIST_BLOCK_CHAIN = "getAllVaccineRecord"
    const val GET_TEST_REPORT_LIST_BLOCK_CHAIN = "getAllTestRecord"

}